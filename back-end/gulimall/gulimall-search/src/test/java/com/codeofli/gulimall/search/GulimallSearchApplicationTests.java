//package com.codeofli.gulimall.search;
//
//import com.alibaba.fastjson.JSON;
//import com.codeofli.gulimall.search.config.GulimallElasticSearchConfig;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.aggregations.Aggregation;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class GulimallSearchApplicationTests {
//    @Autowired
//    private RestHighLevelClient client;
//
//    @Test
//    void contextLoads() {
//        System.out.println(client);
//    }
//
//    /**
//     * 测试存储数据到es
//     * 更新也可以
//     */
//    @Test
//    public void indexData() throws IOException {
//
//        // 设置索引
//        IndexRequest indexRequest = new IndexRequest("users");
//        indexRequest.id("1");
//
//        User user = new User();
//        user.setUserName("张三");
//        user.setAge(20);
//        user.setGender("男");
//        String jsonString = JSON.toJSONString(user);
//
//        //设置要保存的内容，指定数据和类型
//        indexRequest.source(jsonString, XContentType.JSON);
//
//        //执行创建索引和保存数据
//        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
//
//        System.out.println(index);
//
//    }
//
//    @Test
//    public void searchData() throws IOException {
//        // 1 创建检索请求
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("bank");
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        // 构造检索条件
//        //        sourceBuilder.query();
//        //        sourceBuilder.from();
//        //        sourceBuilder.size();
//        //        sourceBuilder.aggregation();
//        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
//        System.out.println(sourceBuilder.toString());
//
//        searchRequest.source(sourceBuilder);
//
//        // 2 执行检索
//        SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
//        // 3 分析响应结果
//        System.out.println(response.toString());
//    }
//
//    @Test
//    public void find() throws IOException {
//        // 1 创建检索请求
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("bank");
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        // 构造检索条件
//        //        sourceBuilder.query();
//        //        sourceBuilder.from();
//        //        sourceBuilder.size();
//        //        sourceBuilder.aggregation();
//        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
//        //AggregationBuilders工具类构建AggregationBuilder
//        // 构建第一个聚合条件:按照年龄的值分布
//        TermsAggregationBuilder agg1 = AggregationBuilders.terms("ageAgg").field("age").size(10);// 聚合名称
//        // 参数为AggregationBuilder
//        sourceBuilder.aggregation(agg1);
//        // 构建第二个聚合条件:平均薪资
//        AvgAggregationBuilder agg2 = AggregationBuilders.avg("agg2").field("balance");
//        sourceBuilder.aggregation(agg2);
//
//        System.out.println("检索条件" + sourceBuilder.toString());
//
//        searchRequest.source(sourceBuilder);
//
//        // 2 执行检索
//        SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
//        // 3 分析响应结果
//        System.out.println(response.toString());
//
//        // 3.1 获取java bean
//        //把检索结果封装为java bean
//        SearchHits hits = response.getHits();
//        SearchHit[] hits1 = hits.getHits();
//        for (SearchHit hit : hits1) {
//            hit.getId();
//            hit.getIndex();
//            String sourceAsString = hit.getSourceAsString();
//            Account account = JSON.parseObject(sourceAsString, Account.class);
//            System.out.println(account);
//        }
//        // 3.2 获取检索到的分析信息
//        Aggregations aggregations = response.getAggregations();
//        Terms agg21 = aggregations.get("ageAgg");
//        for (Terms.Bucket bucket : agg21.getBuckets()) {
//            String keyAsString = bucket.getKeyAsString();
//            System.out.println("年龄：" + keyAsString);
//        }
//    }
//
//
//    @Setter
//    @Getter
//    class User {
//        private int age;
//        private String userName;
//        private String gender;
//    }
//
//    @ToString
//    @Data
//    static class Account {
//        private int account_number;
//        private int balance;
//        private String firstname;
//        private String lastname;
//        private int age;
//        private String gender;
//        private String address;
//        private String employer;
//        private String email;
//        private String city;
//        private String state;
//    }
//}
