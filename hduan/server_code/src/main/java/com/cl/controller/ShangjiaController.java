package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.entity.TokenEntity;
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

import com.cl.entity.ShangjiaEntity;
import com.cl.entity.view.ShangjiaView;

import com.cl.service.ShangjiaService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 商家
 * 后端接口
 * @author 
 * @email 
 * @date 2025-05-16 15:40:02
 */
@RestController
@RequestMapping("/shangjia")
public class ShangjiaController {
    @Autowired
    private ShangjiaService shangjiaService;



	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", username));
        if(u==null || !u.getMima().equals(password)) {
            return R.error("账号或密码不正确");
        }
        if(!"是".equals(u.getSfsh())) return R.error("账号已锁定，请联系管理员审核。");
		String token = tokenService.generateToken(u.getId(), username,"shangjia",  "商家" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody ShangjiaEntity shangjia){
    	//ValidatorUtils.validateEntity(shangjia);
                            ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()));
                                                                                                                                                		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		shangjia.setId(uId);
        shangjiaService.insert(shangjia);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        return R.ok().put("data", shangjiaService.selectView(new EntityWrapper<ShangjiaEntity>().eq("id", id)));
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima("123456");
        shangjiaService.updateById(u);
        return R.ok("密码已重置为：123456");
    }






    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ShangjiaEntity shangjia,
                                                                                                                                                                                HttpServletRequest request){
                                    EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
                                                                                                                                                                                                                                                                                    
        
        
        PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShangjiaEntity shangjia,
		HttpServletRequest request){
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();

		PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ShangjiaEntity shangjia){
       	EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shangjia, MPUtil.camelToSnake("shangjia")));
        return R.ok().put("data", shangjiaService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShangjiaEntity shangjia){
        EntityWrapper< ShangjiaEntity> ew = new EntityWrapper< ShangjiaEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shangjia, MPUtil.camelToSnake("shangjia")));
		ShangjiaView shangjiaView =  shangjiaService.selectView(ew);
		return R.ok("查询商家成功").put("data", shangjiaView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShangjiaEntity shangjia = shangjiaService.selectById(id);
		shangjia = shangjiaService.selectView(new EntityWrapper<ShangjiaEntity>().eq("id", id));
        return R.ok().put("data", shangjia);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShangjiaEntity shangjia = shangjiaService.selectById(id);
		shangjia = shangjiaService.selectView(new EntityWrapper<ShangjiaEntity>().eq("id", id));
        return R.ok().put("data", shangjia);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request){
        if(shangjiaService.selectCount(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()))>0) {
            return R.error("商家账号已存在");
        }
    	shangjia.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangjia);
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		shangjia.setId(new Date().getTime());
        shangjiaService.insert(shangjia);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request){
        if(shangjiaService.selectCount(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()))>0) {
            return R.error("商家账号已存在");
        }
    	shangjia.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangjia);
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", shangjia.getShangjiazhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		shangjia.setId(new Date().getTime());
        shangjiaService.insert(shangjia);
        return R.ok();
    }

     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        ShangjiaEntity shangjia = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("shangjiazhanghao", username));
        return R.ok().put("data", shangjia);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shangjia);
        shangjiaService.updateById(shangjia);//全部更新
        if(null!=shangjia.getShangjiazhanghao())
        {
            // 修改token
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setUsername(shangjia.getShangjiazhanghao());
            tokenService.update(tokenEntity, new EntityWrapper<TokenEntity>().eq("userid", shangjia.getId()));
        }
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<ShangjiaEntity> list = new ArrayList<ShangjiaEntity>();
        for(Long id : ids) {
            ShangjiaEntity shangjia = shangjiaService.selectById(id);
            shangjia.setSfsh(sfsh);
            shangjia.setShhf(shhf);
            list.add(shangjia);
        }
        shangjiaService.updateBatchById(list);
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        shangjiaService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
