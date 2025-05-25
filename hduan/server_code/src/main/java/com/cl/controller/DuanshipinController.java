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

import com.cl.entity.DuanshipinEntity;
import com.cl.entity.view.DuanshipinView;

import com.cl.service.DuanshipinService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.service.StoreupService;
import com.cl.entity.StoreupEntity;

/**
 * 短视频
 * 后端接口
 * @author 
 * @email 
 * @date 2025-05-16 15:40:02
 */
@RestController
@RequestMapping("/duanshipin")
public class DuanshipinController {
    @Autowired
    private DuanshipinService duanshipinService;

    @Autowired
    private StoreupService storeupService;








    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DuanshipinEntity duanshipin,
                                                                                                                                                        HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                                                                        if(tableName.equals("shangjia")) {
                    duanshipin.setShangjiazhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                                                                                                                EntityWrapper<DuanshipinEntity> ew = new EntityWrapper<DuanshipinEntity>();
                                                                                                                                                                                                                                            
        
        
        PageUtils page = duanshipinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, duanshipin), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DuanshipinEntity duanshipin,
		HttpServletRequest request){
        EntityWrapper<DuanshipinEntity> ew = new EntityWrapper<DuanshipinEntity>();

		PageUtils page = duanshipinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, duanshipin), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DuanshipinEntity duanshipin){
       	EntityWrapper<DuanshipinEntity> ew = new EntityWrapper<DuanshipinEntity>();
      	ew.allEq(MPUtil.allEQMapPre( duanshipin, MPUtil.camelToSnake("duanshipin")));
        return R.ok().put("data", duanshipinService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DuanshipinEntity duanshipin){
        EntityWrapper< DuanshipinEntity> ew = new EntityWrapper< DuanshipinEntity>();
 		ew.allEq(MPUtil.allEQMapPre( duanshipin, MPUtil.camelToSnake("duanshipin")));
		DuanshipinView duanshipinView =  duanshipinService.selectView(ew);
		return R.ok("查询短视频成功").put("data", duanshipinView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DuanshipinEntity duanshipin = duanshipinService.selectById(id);
		duanshipin = duanshipinService.selectView(new EntityWrapper<DuanshipinEntity>().eq("id", id));
        return R.ok().put("data", duanshipin);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DuanshipinEntity duanshipin = duanshipinService.selectById(id);
		duanshipin = duanshipinService.selectView(new EntityWrapper<DuanshipinEntity>().eq("id", id));
        return R.ok().put("data", duanshipin);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        DuanshipinEntity duanshipin = duanshipinService.selectById(id);
        if(type.equals("1")) {
        	duanshipin.setThumbsupNumber(duanshipin.getThumbsupNumber()+1);
        } else {
        	duanshipin.setCrazilyNumber(duanshipin.getCrazilyNumber()+1);
        }
        duanshipinService.updateById(duanshipin);
        return R.ok("投票成功");
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DuanshipinEntity duanshipin, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(duanshipin);
        duanshipinService.insert(duanshipin);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DuanshipinEntity duanshipin, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(duanshipin);
        duanshipinService.insert(duanshipin);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DuanshipinEntity duanshipin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(duanshipin);
        duanshipinService.updateById(duanshipin);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        duanshipinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
