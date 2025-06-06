package com.cl.entity.view;

import com.cl.entity.YijianfankuiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.cl.utils.EncryptUtil;
 

/**
 * 意见反馈
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
@TableName("yijianfankui")
public class YijianfankuiView  extends YijianfankuiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public YijianfankuiView(){
	}
 
 	public YijianfankuiView(YijianfankuiEntity yijianfankuiEntity){
 	try {
			BeanUtils.copyProperties(this, yijianfankuiEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}



}
