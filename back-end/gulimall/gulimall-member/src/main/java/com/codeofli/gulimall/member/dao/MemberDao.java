package com.codeofli.gulimall.member.dao;

import com.codeofli.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 19:53:22
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
