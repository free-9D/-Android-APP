package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.FankuiyijianEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.FankuiyijianView;


/**
 * 反馈意见
 *
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
public interface FankuiyijianService extends IService<FankuiyijianEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<FankuiyijianView> selectListView(Wrapper<FankuiyijianEntity> wrapper);
   	
   	FankuiyijianView selectView(@Param("ew") Wrapper<FankuiyijianEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<FankuiyijianEntity> wrapper);
   	
   
}

