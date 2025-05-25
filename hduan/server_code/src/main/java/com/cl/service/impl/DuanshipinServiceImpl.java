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


import com.cl.dao.DuanshipinDao;
import com.cl.entity.DuanshipinEntity;
import com.cl.service.DuanshipinService;
import com.cl.entity.view.DuanshipinView;

@Service("duanshipinService")
public class DuanshipinServiceImpl extends ServiceImpl<DuanshipinDao, DuanshipinEntity> implements DuanshipinService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DuanshipinEntity> page = this.selectPage(
                new Query<DuanshipinEntity>(params).getPage(),
                new EntityWrapper<DuanshipinEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DuanshipinEntity> wrapper) {
		  Page<DuanshipinView> page =new Query<DuanshipinView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<DuanshipinView> selectListView(Wrapper<DuanshipinEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DuanshipinView selectView(Wrapper<DuanshipinEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
