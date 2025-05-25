package com.cl.dao;

import com.cl.entity.DuanshipinEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DuanshipinView;


/**
 * 短视频
 * 
 * @author 
 * @email 
 * @date 2025-05-16 15:40:02
 */
public interface DuanshipinDao extends BaseMapper<DuanshipinEntity> {
	
	List<DuanshipinView> selectListView(@Param("ew") Wrapper<DuanshipinEntity> wrapper);

	List<DuanshipinView> selectListView(Pagination page,@Param("ew") Wrapper<DuanshipinEntity> wrapper);
	
	DuanshipinView selectView(@Param("ew") Wrapper<DuanshipinEntity> wrapper);


}
