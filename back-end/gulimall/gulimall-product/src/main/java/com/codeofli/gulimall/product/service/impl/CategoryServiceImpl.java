package com.codeofli.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.common.utils.Query;
import com.codeofli.gulimall.product.dao.CategoryDao;
import com.codeofli.gulimall.product.entity.CategoryEntity;
import com.codeofli.gulimall.product.service.CategoryBrandRelationService;
import com.codeofli.gulimall.product.service.CategoryService;
import com.codeofli.gulimall.product.vo.Catalog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    @Autowired
//    CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     *
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }
    //每一个需要缓存的数据我们都来指定要放到那个名字的缓存。【缓存的分区(按照业务类型分)】
    @Cacheable("category")
    @Override
    public List<CategoryEntity> getLevel1Catagories() {
//        long start = System.currentTimeMillis();
        List<CategoryEntity> parent_cid = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
//        System.out.println("查询一级菜单时间:"+(System.currentTimeMillis()-start));
        return parent_cid;
    }

    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        //给缓存中放json字符串，拿出的json字符串，还用逆转为能用的对象类型，【序列化与反序列化】
        // 1、加入缓存逻辑,缓存中存的数据是json字符串。
        // 3、JSON跨语言，跨平台兼容。
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("缓存不命中，准备查询数据库。。。");
            // 2、缓存中没有,查询数据库
            Map<String, List<Catalog2Vo>> categoriesDb = getCatalogJsonFromDbWithRedisLock();
            return categoriesDb;
        }
        System.out.println("缓存命中。。。。");
        //转为我们指定的对象。
        return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
        });
    }

    //从数据库中查出三级分类
    public synchronized Map<String, List<Catalog2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        // 锁的名字。锁的粒度，越细越好越快。
        RLock lock = redissonClient.getLock("CatalogJson-lock");
        lock.lock();
        Map<String, List<Catalog2Vo>> categoriesDb;
        try {
            System.out.println("获取分布式锁失成功。");
            categoriesDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return categoriesDb;
    }

    //从数据库中查出三级分类
    public synchronized Map<String, List<Catalog2Vo>> getCatalogJsonFromDbWithRedisLock() {
        // 1、占分布式锁。去redis占坑
        String uuid = UUID.randomUUID().toString();
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Boolean lock = ops.setIfAbsent("lock", uuid, 5, TimeUnit.SECONDS);
        if (lock) {
            //加锁成功...执行业务
            // 2、设置过期时间，必须和加锁是同步的，原子的
            //  redisTemplate.expire( "lock", 30, TimeUnit.SECONDS ) ;
            Map<String, List<Catalog2Vo>> categoriesDb;
            try {
                System.out.println("获取分布式锁失成功。");
                categoriesDb = getDataFromDb();
            } finally {
                //获取值对比+对比成功删除=原子操作     Lua脚本解锁
                //String lockValue = redisTempLate.opsForVaLue( ).get("Lock");
                // if(uuid.equals(LockValue)){
                //删除我自己的锁
                //            redisTempLate.delete( "Lock");//删除锁
                //        }

                String lockValue = ops.get("lock");
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                //删除锁
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), lockValue);
            }
            return categoriesDb;
        } else {
            //加锁失败...重试。synchronized ()
            //休眠100ms重试
            System.out.println("获取分布式锁失败...等待重试");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock(); //自旋的方式
        }
    }

    private Map<String, List<Catalog2Vo>> getDataFromDb() {
        /**
         * 1、空结果缓存:解决缓存穿透
         * 2、设置过期时间（加随机值）:解决缓存雪崩
         * 3、加锁:解决缓存击穿
         */
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        // 1、synchronized (this ): SpringBoot所有的组件在容器中都是单例的。
        //TODo 本地锁: synchronized，uc (Lock)，在分布式情况下，想要锁住所有，必须使用分布式锁

        //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");


        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("查询了数据库");
            /**
             * 1、将数据库的多次查询变为一次
             */
            List<CategoryEntity> selectList = baseMapper.selectList(null);
            // 1、查询所有一级分类
            List<CategoryEntity> level1Categories = getCategoriesByParentCid(selectList, 0L);
            //2、封装数据
            Map<String, List<Catalog2Vo>> categoriesDb = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                // 1、拿到每一个一级分类 然后查询他们的二级分类
                List<CategoryEntity> entities = getCategoriesByParentCid(selectList, v.getCatId());
                // 2、封装上面的结果
                List<Catalog2Vo> Catalog2Vos = null;
                if (entities != null) {
                    Catalog2Vos = entities.stream().map(l2 -> {
                        Catalog2Vo Catalog2Vo = new Catalog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                        // 1、找当前二级分类的三级分类
                        List<CategoryEntity> level3 = getCategoriesByParentCid(selectList, l2.getCatId());
                        // 三级分类有数据的情况下
                        if (level3 != null) {
                            // 封装成指定格式
                            List<Catalog2Vo.Catalog3Vo> catalog3Vos = level3.stream().map(l3 -> new Catalog2Vo.Catalog3Vo(l3.getCatId().toString(), l3.getName(), l2.getCatId().toString())).collect(Collectors.toList());
                            Catalog2Vo.setCatalog3List(catalog3Vos);
                        }
                        return Catalog2Vo;
                    }).collect(Collectors.toList());
                }
                return Catalog2Vos;
            }));

            //3、查到的数据再放入缓存，将对象转为json放在缓存中
            String toJSONString = JSON.toJSONString(categoriesDb);
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set("catalogJson", toJSONString);
            return categoriesDb;
        } else {
            Map<String, List<Catalog2Vo>> listMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
            });
            return listMap;
        }
    }

    //从数据库中查出三级分类
    public synchronized Map<String, List<Catalog2Vo>> getCatalogJsonFromDbWithLocalLock() {
        /**
         * 1、空结果缓存:解决缓存穿透
         * 2、设置过期时间（加随机值）:解决缓存雪崩
         * 3、加锁:解决缓存击穿
         */
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        // 1、synchronized (this ): SpringBoot所有的组件在容器中都是单例的。
        //TODo 本地锁: synchronized，uc (Lock)，在分布式情况下，想要锁住所有，必须使用分布式锁

        //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
        return getDataFromDb();
    }

    private List<CategoryEntity> getCategoriesByParentCid(List<CategoryEntity> selectList, Long parentCid) {
        return selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }


    //225,25,2
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;

    }


    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2、菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }


}