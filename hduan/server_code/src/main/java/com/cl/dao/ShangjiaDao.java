package com.cl.dao;

import com.cl.entity.ShangjiaEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.ShangjiaView;


/**
 * 商家
 * 
 * @author 
 * @email 
 * @date 2025-05-16 15:40:02
 */
public interface ShangjiaDao extends BaseMapper<ShangjiaEntity> {
	
	List<ShangjiaView> selectListView(@Param("ew") Wrapper<ShangjiaEntity> wrapper);

	List<ShangjiaView> selectListView(Pagination page,@Param("ew") Wrapper<ShangjiaEntity> wrapper);
	
	ShangjiaView selectView(@Param("ew") Wrapper<ShangjiaEntity> wrapper);


}
