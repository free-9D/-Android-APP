package com.cl.entity.view;

import com.cl.entity.DiscussduanshipinEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.cl.utils.EncryptUtil;
 

/**
 * 短视频评论表
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
@TableName("discussduanshipin")
public class DiscussduanshipinView  extends DiscussduanshipinEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DiscussduanshipinView(){
	}
 
 	public DiscussduanshipinView(DiscussduanshipinEntity discussduanshipinEntity){
 	try {
			BeanUtils.copyProperties(this, discussduanshipinEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}



}
