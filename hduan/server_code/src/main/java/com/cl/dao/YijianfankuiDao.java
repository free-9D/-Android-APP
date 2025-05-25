package com.cl.dao;

import com.cl.entity.YijianfankuiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.YijianfankuiView;


/**
 * 意见反馈
 * 
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
public interface YijianfankuiDao extends BaseMapper<YijianfankuiEntity> {
	
	List<YijianfankuiView> selectListView(@Param("ew") Wrapper<YijianfankuiEntity> wrapper);

	List<YijianfankuiView> selectListView(Pagination page,@Param("ew") Wrapper<YijianfankuiEntity> wrapper);
	
	YijianfankuiView selectView(@Param("ew") Wrapper<YijianfankuiEntity> wrapper);


}
