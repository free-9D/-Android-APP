<template>
	<div>
		<div class="center_view edit_form">
			<el-form class="userinfo_form" ref="userinfoFormRef" :model="form">
				<el-row>
					<el-col :span="12">
						<el-form-item label="商家账号" prop="shangjiazhanghao">
							<el-input class="list_inp" v-model="user.shangjiazhanghao" readonly placeholder="商家账号" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="商家姓名" prop="shangjiaxingming">
							<el-input class="list_inp" v-model="user.shangjiaxingming"  placeholder="商家姓名" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="头像" prop="touxiang">
							<uploads
								action="file/upload" 
								tip="请上传头像"
								style="width: 100%;text-align: left;"
								:fileUrls="user.touxiang?user.touxiang:''" 
								@change="shangjiatouxiangUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="性别" prop="xingbie">
							<el-select 
								class="list_sel" 
								v-model="user.xingbie" 
								placeholder="请选择性别"
								>
								<el-option v-for="item in shangjiaxingbieLists" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="lianxidianhua">
							<el-input class="list_inp" v-model="user.lianxidianhua"  placeholder="联系电话" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="资质类型" prop="zizhileixing">
							<el-select 
								class="list_sel" 
								v-model="user.zizhileixing" 
								placeholder="请选择资质类型"
								>
								<el-option v-for="item in shangjiazizhileixingLists" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="资质证明" prop="zizhizhengming">
							<uploads
								action="file/upload" 
								tip="请上传资质证明"
								style="width: 100%;text-align: left;"
								:fileUrls="user.zizhizhengming?user.zizhizhengming:''" 
								@change="shangjiazizhizhengmingUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="密保问题" prop="pquestion">
							<el-input class="list_inp" v-model="user.pquestion"  placeholder="密保问题" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="密保答案" prop="panswer">
							<el-input class="list_inp" v-model="user.panswer"  placeholder="密保答案" clearable />
						</el-form-item>
					</el-col>
					<span class="formModel_btn_box">
						<el-button class='confirm_btn' type="primary" @click="onSubmit">保存</el-button>
					</span>
				</el-row>
			</el-form>
		</div>
	</div>
</template>

<script setup>
	import { isNumber,isIntNumer,isEmail,isMobile,isPhone,isURL,checkIdCard } from "@/utils/toolUtil";
	import {
		reactive,
		ref,
		getCurrentInstance,
        computed
	} from 'vue'
	import { useStore } from 'vuex'
	const store = useStore()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const tableName = ref('shangjia')
	const user = ref({})
	const shangjiaxingbieLists = ref([])
	const shangjiazizhileixingLists = ref([])

	const init = () => {
		shangjiaxingbieLists.value = "男,女".split(',')
		shangjiazizhileixingLists.value = "实人认证,企业认证,采购认证,实力认证".split(',')
	}
	const shangjiatouxiangUploadSuccess=(fileUrls)=> {
	    user.value.touxiang = fileUrls;
	}
	const shangjiazizhizhengmingUploadSuccess=(fileUrls)=> {
	    user.value.zizhizhengming = fileUrls;
	}
	const onSubmit = () => {
		if((!user.value.shangjiazhanghao)){
			context?.$toolUtil.message(`商家账号不能为空`,'error')
			return false
		}
		if((!user.value.mima)){
			context?.$toolUtil.message(`密码不能为空`,'error')
			return false
		}
		if((!user.value.shangjiaxingming)){
			context?.$toolUtil.message(`商家姓名不能为空`,'error')
			return false
		}
		if((!user.value.touxiang)){
			context?.$toolUtil.message(`头像不能为空`,'error')
			return false
		}
		if(user.value.touxiang!=null){
			user.value.touxiang = user.value.touxiang.replace(new RegExp(context?.$config.url,"g"),"");
		}
		if((user.value.lianxidianhua)&&(!context?.$toolUtil.isMobile(user.value.lianxidianhua))){
			context?.$toolUtil.message(`联系电话应输入手机格式`,'error')
			return false
		}
		if(user.value.zizhizhengming!=null){
			user.value.zizhizhengming = user.value.zizhizhengming.replace(new RegExp(context?.$config.url,"g"),"");
		}
		store.dispatch('user/update',user.value).then(res=>{
			if(res?.data&&res.data.code==0)context?.$toolUtil.message('修改成功','success')
		})

	}
	const getInfo = () => {
		context?.$http({
			url: `${tableName.value}/session`,
			method: 'get'
		}).then(res => {
			user.value = res.data.data
			init()
		})
	}
	getInfo()
</script>

<style lang="scss" scoped>
	// 表单
	.userinfo_form {
		// form item
		:deep(.el-form-item) {
			// 内容盒子
			.el-form-item__content{
			}
			// 输入框
			.list_inp {
			}
			//下拉框样式
			.list_sel {
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;
					}
				}
			}
			//图片上传样式
			.el-upload-list  {
				//提示语
				.el-upload__tip {
				}
				//外部盒子
				.el-upload--picture-card {
					//图标
					.el-icon{
					}
				}
				.el-upload-list__item {
				}
			}

		}
		// 按钮盒子
		.formModel_btn_box {
			// 确定按钮
			.confirm_btn {
			}
			// 确定按钮-悬浮
			.confirm_btn:hover {
			}
		}
	}
</style>
