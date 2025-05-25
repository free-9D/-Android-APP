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

import com.cl.entity.YijianfankuiEntity;
import com.cl.entity.view.YijianfankuiView;

import com.cl.service.YijianfankuiService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 意见反馈
 * 后端接口
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
@RestController
@RequestMapping("/yijianfankui")
public class YijianfankuiController {
    @Autowired
    private YijianfankuiService yijianfankuiService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YijianfankuiEntity yijianfankui,
                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                if(tableName.equals("yonghu")) {
                    yijianfankui.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                            EntityWrapper<YijianfankuiEntity> ew = new EntityWrapper<YijianfankuiEntity>();
                                                                                                                                        
        
        
        PageUtils page = yijianfankuiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yijianfankui), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YijianfankuiEntity yijianfankui,
		HttpServletRequest request){
        EntityWrapper<YijianfankuiEntity> ew = new EntityWrapper<YijianfankuiEntity>();

		PageUtils page = yijianfankuiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yijianfankui), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YijianfankuiEntity yijianfankui){
       	EntityWrapper<YijianfankuiEntity> ew = new EntityWrapper<YijianfankuiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yijianfankui, MPUtil.camelToSnake("yijianfankui")));
        return R.ok().put("data", yijianfankuiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YijianfankuiEntity yijianfankui){
        EntityWrapper< YijianfankuiEntity> ew = new EntityWrapper< YijianfankuiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yijianfankui, MPUtil.camelToSnake("yijianfankui")));
		YijianfankuiView yijianfankuiView =  yijianfankuiService.selectView(ew);
		return R.ok("查询意见反馈成功").put("data", yijianfankuiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YijianfankuiEntity yijianfankui = yijianfankuiService.selectById(id);
		yijianfankui = yijianfankuiService.selectView(new EntityWrapper<YijianfankuiEntity>().eq("id", id));
        return R.ok().put("data", yijianfankui);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YijianfankuiEntity yijianfankui = yijianfankuiService.selectById(id);
		yijianfankui = yijianfankuiService.selectView(new EntityWrapper<YijianfankuiEntity>().eq("id", id));
        return R.ok().put("data", yijianfankui);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody YijianfankuiEntity yijianfankui, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(yijianfankui);
        yijianfankuiService.insert(yijianfankui);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody YijianfankuiEntity yijianfankui, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(yijianfankui);
        yijianfankuiService.insert(yijianfankui);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody YijianfankuiEntity yijianfankui, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yijianfankui);
        yijianfankuiService.updateById(yijianfankui);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        yijianfankuiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
