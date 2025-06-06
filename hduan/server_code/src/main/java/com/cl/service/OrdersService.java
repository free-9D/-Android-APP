package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.OrdersEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.OrdersView;


/**
 * 商品订单
 *
 * @author 
 * @email 
 * @date 2025-05-16 15:40:02
 */
public interface OrdersService extends IService<OrdersEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<OrdersView> selectListView(Wrapper<OrdersEntity> wrapper);
   	
   	OrdersView selectView(@Param("ew") Wrapper<OrdersEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<OrdersEntity> wrapper);
   	
   
}

