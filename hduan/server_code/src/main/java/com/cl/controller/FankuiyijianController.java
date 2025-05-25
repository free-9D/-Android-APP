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

import com.cl.entity.FankuiyijianEntity;
import com.cl.entity.view.FankuiyijianView;

import com.cl.service.FankuiyijianService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 反馈意见
 * 后端接口
 * @author 
 * @email 
 * @date 2025-05-16 15:40:03
 */
@RestController
@RequestMapping("/fankuiyijian")
public class FankuiyijianController {
    @Autowired
    private FankuiyijianService fankuiyijianService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,FankuiyijianEntity fankuiyijian,
                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                if(tableName.equals("shangjia")) {
                    fankuiyijian.setShangjiazhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                            EntityWrapper<FankuiyijianEntity> ew = new EntityWrapper<FankuiyijianEntity>();
                                                                                                                                        
        
        
        PageUtils page = fankuiyijianService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fankuiyijian), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,FankuiyijianEntity fankuiyijian,
		HttpServletRequest request){
        EntityWrapper<FankuiyijianEntity> ew = new EntityWrapper<FankuiyijianEntity>();

		PageUtils page = fankuiyijianService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fankuiyijian), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( FankuiyijianEntity fankuiyijian){
       	EntityWrapper<FankuiyijianEntity> ew = new EntityWrapper<FankuiyijianEntity>();
      	ew.allEq(MPUtil.allEQMapPre( fankuiyijian, MPUtil.camelToSnake("fankuiyijian")));
        return R.ok().put("data", fankuiyijianService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(FankuiyijianEntity fankuiyijian){
        EntityWrapper< FankuiyijianEntity> ew = new EntityWrapper< FankuiyijianEntity>();
 		ew.allEq(MPUtil.allEQMapPre( fankuiyijian, MPUtil.camelToSnake("fankuiyijian")));
		FankuiyijianView fankuiyijianView =  fankuiyijianService.selectView(ew);
		return R.ok("查询反馈意见成功").put("data", fankuiyijianView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FankuiyijianEntity fankuiyijian = fankuiyijianService.selectById(id);
		fankuiyijian = fankuiyijianService.selectView(new EntityWrapper<FankuiyijianEntity>().eq("id", id));
        return R.ok().put("data", fankuiyijian);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FankuiyijianEntity fankuiyijian = fankuiyijianService.selectById(id);
		fankuiyijian = fankuiyijianService.selectView(new EntityWrapper<FankuiyijianEntity>().eq("id", id));
        return R.ok().put("data", fankuiyijian);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FankuiyijianEntity fankuiyijian, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(fankuiyijian);
        fankuiyijianService.insert(fankuiyijian);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody FankuiyijianEntity fankuiyijian, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(fankuiyijian);
        fankuiyijianService.insert(fankuiyijian);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody FankuiyijianEntity fankuiyijian, HttpServletRequest request){
        //ValidatorUtils.validateEntity(fankuiyijian);
        fankuiyijianService.updateById(fankuiyijian);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        fankuiyijianService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
