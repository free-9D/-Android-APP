<template>
	<div>
		<div class="forget_view">
			<el-form :model="forgetForm" class="forget_box">
				<div class="forget_title">基于Android的外卖点餐App设计与实现{{pageType==1?'输入账号':pageType==2?'输入密保':'重置密码'}}</div>
				<div class="tab_view">
					<div class="tab" :class="pageType==1?'tab_active':''">输入账号</div>
					<div class="tab" :class="pageType==2?'tab_active tab_active1':''"> 输入密保</div>
					<div class="tab" :class="pageType==3?'tab_active tab_active1 tab_active2':''">重置密码</div>
				</div>
				<div class="list_item" v-if="pageType==1">
					<div class="item_label">账号：</div>
					<el-input class="list_inp" v-model="forgetForm.username" placeholder="请输入账号" />
				</div>

				<div class="list_item" v-if="pageType==2">
					<div class="item_label">密保问题：</div>
					<el-input class="list_inp" v-model="userForm.pquestion" placeholder="请输入密保问题" />
				</div>
				<div class="list_item" v-if="pageType==2">
					<div class="item_label">密保答案：</div>
					<el-input class="list_inp" v-model="forgetForm.myanswer" placeholder="请输入密保答案" />
				</div>
				<div class="list_item" v-if="pageType==3">
					<div class="item_label">密码：</div>
					<el-input class="list_inp" v-model="forgetForm.mima" type="password" placeholder="请输入密码" autocomplete="new-password"/>
				</div>
				<div class="list_item" v-if="pageType==3">
					<div class="item_label">确认密码：</div>
					<el-input class="list_inp" v-model="forgetForm.mima2" type="password" placeholder="请输入确认密码" autocomplete="new-password"/>
				</div>
				<div class="list_btn">
                    <el-button v-if="pageType==1" class="get_btn" type="success" @click="getSecurity">获取密保</el-button>
                    <el-button v-if="pageType==2" class="valid_btn" type="primary" @click="validateSecurity">确认密保</el-button>
					<el-button v-if="pageType==3" class="update_btn" type="warning" @click="updatePassword">重置密码</el-button>
					<div class="r-login" @click="close">已有账号，直接登录</div>
				</div>
			</el-form>
		</div>
	</div>
</template>
<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
        onMounted,
	} from 'vue';
    import { useRoute } from 'vue-router'
    const route = useRoute()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
    onMounted(()=>{
    })
	const pageType = ref(1)
	const forgetForm = ref({})
	const userForm = ref({})
    //获取用户信息
    const getSecurity=()=>{
        forgetForm.value.role = 'shangjia'
		if(!forgetForm.value.role) {
		    context?.$toolUtil.message('请选择角色','error');
		    return false
		}
		if(!forgetForm.value.username){
			context?.$toolUtil.message('请输入账号','error');
		    return false
		}
		context?.$http({
			url:forgetForm.value.role + `/security?username=${forgetForm.value.username}`,
			method:'get',
		}).then(res=>{
			if(res.data.data){
				userForm.value = res.data.data
				pageType.value = 2
			}else{
				context?.$toolUtil.message('未获取到用户信息','error');
			}
		})
	}
	//验证
	const validateSecurity=()=>{
		if(userForm.value.panswer != forgetForm.value.myanswer){
			context?.$toolUtil.message('答案输入不正确','error');
			return false
		}
		context.$message.success('答案正确')
        pageType.value = 3
	}
	const updatePassword=()=>{
		if(forgetForm.value.mima!=forgetForm.value.mima2){
			context?.$toolUtil.message('两次密码输入不一致','error')
			return false
		}
		if(forgetForm.value.role == 'shangjia'){
			userForm.value.mima = forgetForm.value.mima
		}
		context?.$http({
			url:forgetForm.value.role + `/update`,
			method:'post',
			data:userForm.value
		}).then(res=>{
			context?.$toolUtil.message('修改密码成功','success',obj=>{
				close()
			});
		})
	}
	//返回登录
	const close = () => {
		context?.$router.push({
			path: "/login"
		});
	}
</script>

<style lang="scss" scoped>
	.forget_view {
        background-image: url("http://clfile.zggen.cn/20240921/fb6feddfbb544df593bbf3240e3437db.webp")!important;
		// 表单盒子
		.forget_box {
			// 标题
			.forget_title {
			}
			// tab
			// 盒子
			.tab_view {
				padding: 0 0 50px;
				display: flex;
				width: 100%;
				align-items: center;
				// 默认样式
				.tab {
					padding: 16px 0;
					width: calc(100% / 3);
					font-size: 16px;
					box-sizing: border-box;
					text-align: center;
				}
				// 选中样式一
				.tab_active {
					clip-path: polygon(00% 0%, 92% 00%, 100% 50%, 92% 100%, 0 100%, 8% 50%);
					color: #fff;
					background: var(--theme);
				}
				// 选中样式二
				.tab_active1 {
					color: #fff;
					background: var(--theme);
				}
				// 选中样式三
				.tab_active2 {
					color: #fff;
					background: var(--theme);
				}
			}
			// item
			.list_item {
				// label
				.item_label {
				}
				// 输入框
				:deep(.list_inp) {
				}
			}
			// 按钮盒子
			.list_btn {
				// 获取密保
				:deep(.el-button--success) {
				}
				// 获取密保悬浮
				:deep(.el-button--success:hover) {
				}
				// 确认密保
				:deep(.el-button--primary) {
				}
				// 确认密保悬浮
				:deep(.el-button--primary:hover) {
				}
				// 重置密码
				:deep(.el-button--warning) {
				}
				// 重置密码悬浮
				:deep(.el-button--warning:hover) {
				}
				.r-login {
				}
			}
		}
	}


</style>
<style>
.forget_view {
    min-height: 100vh;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-position: center center;
    background-size: 100% 100% !important;
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-origin: initial;
    background-clip: initial;
    background-color: initial;
}


.forget_view .forget_box{
    margin: 15vh 0;
    background: #FFFFFF;
    border-radius: 0px 0px 0px 0px;
    border: 5px solid var(--theme);
    position: relative;
    padding: 50px 80px 30px;
    width: 630px;
}


.forget_view .forget_title{
   width: 100%;
   float: left;
   line-height: 46px;
   font-size:24px;
   font-weight: 600;
   letter-spacing: 2px;
   color:var(--theme);
   position: relative;
   margin-bottom:20px;
}


.forget_view .list_item{  display: flex; align-items: center;  width: 100%; justify-content: flex-start; margin: 10px 0px;  }
.forget_view .list_item .item_label { margin-right:10px; white-space: nowrap;  font-size: 16px; }
.forget_view .list_item .item_label i {  font-size:24px; color:#999;  }
.forget_view .list_item .list_inp{ width:100%; border:none; border-bottom:1px solid #ddd; height:40px; line-height:40px; font-size: 16px;  }
.forget_view .list_item .el-select{  width:100%; border-bottom:1px solid #ddd; height:40px; line-height:40px; font-size: 16px;  color:#999;  }
.forget_view .list_item .el-select .el-input__inner{ font-size: 16px;  color:#999;  }

.forget_view .list_type{ margin-bottom:20px;  }


.forget_view .list_btn{  text-align:center;  }

.forget_view .list_btn .get_btn{ width: 100%; height: 50px; line-height: 46px; background: #fff; border: 3px solid #ccc; font-weight: 600; font-size: 20px; color: #999; margin-bottom:20px; padding:0;  margin-top:20px; color: var(--theme); border-color: var(--theme);}

.forget_view .list_btn .valid_btn{ width: 100%; height: 50px; line-height: 46px; background: #fff; border: 3px solid #ccc; font-weight: 600; font-size: 20px; color: #999; margin-bottom:20px; padding:0;  margin-top:20px; }
.forget_view .list_btn .valid_btn:hover { color: var(--theme); border-color: var(--theme); }

.forget_view .list_btn .update_btn{ width: 100%; height: 50px; line-height: 46px; background: #fff; border: 3px solid #ccc; font-weight: 600; font-size: 20px; color: #999; margin-bottom:20px; padding:0;  margin-top:20px; }
.forget_view .list_btn .update_btn:hover { color: var(--theme); border-color: var(--theme); }

.forget_view .list_btn .r-login{  font-size: 16px; color: #999;  }
.forget_view .list_btn .r-login:hover{ cursor:pointer; color: var(--theme);  }
.forget_view .el-select,.forget_view .el-input {
  border: none;
}

</style>
