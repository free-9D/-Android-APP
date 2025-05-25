package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;

import com.cl.entity.DiscussduanshipinEntity;
import com.cl.entity.view.DiscussduanshipinView;

import com.cl.service.DiscussduanshipinService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 短视频评论表
 * 后端接口
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
@RestController
@RequestMapping("/discussduanshipin")
public class DiscussduanshipinController {
    @Autowired
    private DiscussduanshipinService discussduanshipinService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DiscussduanshipinEntity discussduanshipin,
                                                                                                                            HttpServletRequest request){
                                    EntityWrapper<DiscussduanshipinEntity> ew = new EntityWrapper<DiscussduanshipinEntity>();
                                                                                                                                                                                        
        
        
        PageUtils page = discussduanshipinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussduanshipin), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DiscussduanshipinEntity discussduanshipin,
		HttpServletRequest request){
        EntityWrapper<DiscussduanshipinEntity> ew = new EntityWrapper<DiscussduanshipinEntity>();

		PageUtils page = discussduanshipinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussduanshipin), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DiscussduanshipinEntity discussduanshipin){
       	EntityWrapper<DiscussduanshipinEntity> ew = new EntityWrapper<DiscussduanshipinEntity>();
      	ew.allEq(MPUtil.allEQMapPre( discussduanshipin, MPUtil.camelToSnake("discussduanshipin")));
        return R.ok().put("data", discussduanshipinService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscussduanshipinEntity discussduanshipin){
        EntityWrapper< DiscussduanshipinEntity> ew = new EntityWrapper< DiscussduanshipinEntity>();
 		ew.allEq(MPUtil.allEQMapPre( discussduanshipin, MPUtil.camelToSnake("discussduanshipin")));
		DiscussduanshipinView discussduanshipinView =  discussduanshipinService.selectView(ew);
		return R.ok("查询短视频评论表成功").put("data", discussduanshipinView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DiscussduanshipinEntity discussduanshipin = discussduanshipinService.selectById(id);
		discussduanshipin = discussduanshipinService.selectView(new EntityWrapper<DiscussduanshipinEntity>().eq("id", id));
        return R.ok().put("data", discussduanshipin);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DiscussduanshipinEntity discussduanshipin = discussduanshipinService.selectById(id);
		discussduanshipin = discussduanshipinService.selectView(new EntityWrapper<DiscussduanshipinEntity>().eq("id", id));
        return R.ok().put("data", discussduanshipin);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DiscussduanshipinEntity discussduanshipin, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(discussduanshipin);
        discussduanshipinService.insert(discussduanshipin);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DiscussduanshipinEntity discussduanshipin, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(discussduanshipin);
        discussduanshipinService.insert(discussduanshipin);
        return R.ok();
    }

     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        DiscussduanshipinEntity discussduanshipin = discussduanshipinService.selectOne(new EntityWrapper<DiscussduanshipinEntity>().eq("", username));
        return R.ok().put("data", discussduanshipin);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody DiscussduanshipinEntity discussduanshipin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discussduanshipin);
        discussduanshipinService.updateById(discussduanshipin);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        discussduanshipinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,DiscussduanshipinEntity discussduanshipin, HttpServletRequest request,String pre){
        EntityWrapper<DiscussduanshipinEntity> ew = new EntityWrapper<DiscussduanshipinEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicktime");
        params.put("order", "desc");
		PageUtils page = discussduanshipinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussduanshipin), params), params));
        return R.ok().put("data", page);
    }









}
