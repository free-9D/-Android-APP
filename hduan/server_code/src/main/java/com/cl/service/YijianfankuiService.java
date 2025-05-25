package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.YijianfankuiEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.YijianfankuiView;


/**
 * 意见反馈
 *
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
public interface YijianfankuiService extends IService<YijianfankuiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YijianfankuiView> selectListView(Wrapper<YijianfankuiEntity> wrapper);
   	
   	YijianfankuiView selectView(@Param("ew") Wrapper<YijianfankuiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<YijianfankuiEntity> wrapper);
   	
   
}

