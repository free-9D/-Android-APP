package com.cl.dao;

import com.cl.entity.FankuiyijianEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.FankuiyijianView;


/**
 * 反馈意见
 * 
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
public interface FankuiyijianDao extends BaseMapper<FankuiyijianEntity> {
	
	List<FankuiyijianView> selectListView(@Param("ew") Wrapper<FankuiyijianEntity> wrapper);

	List<FankuiyijianView> selectListView(Pagination page,@Param("ew") Wrapper<FankuiyijianEntity> wrapper);
	
	FankuiyijianView selectView(@Param("ew") Wrapper<FankuiyijianEntity> wrapper);


}
