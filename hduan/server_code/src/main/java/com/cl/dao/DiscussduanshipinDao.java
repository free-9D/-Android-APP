package com.cl.dao;

import com.cl.entity.DiscussduanshipinEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DiscussduanshipinView;


/**
 * 短视频评论表
 * 
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
public interface DiscussduanshipinDao extends BaseMapper<DiscussduanshipinEntity> {
	
	List<DiscussduanshipinView> selectListView(@Param("ew") Wrapper<DiscussduanshipinEntity> wrapper);

	List<DiscussduanshipinView> selectListView(Pagination page,@Param("ew") Wrapper<DiscussduanshipinEntity> wrapper);
	
	DiscussduanshipinView selectView(@Param("ew") Wrapper<DiscussduanshipinEntity> wrapper);


}
