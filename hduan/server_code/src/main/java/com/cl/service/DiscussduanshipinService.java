package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.DiscussduanshipinEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DiscussduanshipinView;


/**
 * 短视频评论表
 *
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
public interface DiscussduanshipinService extends IService<DiscussduanshipinEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DiscussduanshipinView> selectListView(Wrapper<DiscussduanshipinEntity> wrapper);
   	
   	DiscussduanshipinView selectView(@Param("ew") Wrapper<DiscussduanshipinEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DiscussduanshipinEntity> wrapper);
   	
   
}

