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


import com.cl.dao.DiscussduanshipinDao;
import com.cl.entity.DiscussduanshipinEntity;
import com.cl.service.DiscussduanshipinService;
import com.cl.entity.view.DiscussduanshipinView;

@Service("discussduanshipinService")
public class DiscussduanshipinServiceImpl extends ServiceImpl<DiscussduanshipinDao, DiscussduanshipinEntity> implements DiscussduanshipinService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscussduanshipinEntity> page = this.selectPage(
                new Query<DiscussduanshipinEntity>(params).getPage(),
                new EntityWrapper<DiscussduanshipinEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscussduanshipinEntity> wrapper) {
		  Page<DiscussduanshipinView> page =new Query<DiscussduanshipinView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<DiscussduanshipinView> selectListView(Wrapper<DiscussduanshipinEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DiscussduanshipinView selectView(Wrapper<DiscussduanshipinEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
