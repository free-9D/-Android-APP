package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.YijianfankuiDao;
import com.cl.entity.YijianfankuiEntity;
import com.cl.service.YijianfankuiService;
import com.cl.entity.view.YijianfankuiView;

@Service("yijianfankuiService")
public class YijianfankuiServiceImpl extends ServiceImpl<YijianfankuiDao, YijianfankuiEntity> implements YijianfankuiService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YijianfankuiEntity> page = this.selectPage(
                new Query<YijianfankuiEntity>(params).getPage(),
                new EntityWrapper<YijianfankuiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<YijianfankuiEntity> wrapper) {
		  Page<YijianfankuiView> page =new Query<YijianfankuiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<YijianfankuiView> selectListView(Wrapper<YijianfankuiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YijianfankuiView selectView(Wrapper<YijianfankuiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
