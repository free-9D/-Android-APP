package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.DuanshipinEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DuanshipinView;


/**
 * 短视频
 *
 * @author 
 * @email 
 * @date 2025-05-16 15:40:02
 */
public interface DuanshipinService extends IService<DuanshipinEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DuanshipinView> selectListView(Wrapper<DuanshipinEntity> wrapper);
   	
   	DuanshipinView selectView(@Param("ew") Wrapper<DuanshipinEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DuanshipinEntity> wrapper);
   	
   
}

