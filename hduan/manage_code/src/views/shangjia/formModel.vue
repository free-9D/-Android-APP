<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="80%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="12">
						<el-form-item label="商家账号" prop="shangjiazhanghao">
							<el-input class="list_inp" v-model="form.shangjiazhanghao" placeholder="商家账号"
								type="text" :readonly="!isAdd||disabledForm.shangjiazhanghao?true:false" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="密码" prop="mima">
							<el-input class="list_inp" v-model="form.mima" placeholder="密码"
								type="password" :readonly="!isAdd||disabledForm.mima?true:false" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="商家姓名" prop="shangjiaxingming">
							<el-input class="list_inp" v-model="form.shangjiaxingming" placeholder="商家姓名"
                                type="text"
								:readonly="!isAdd||disabledForm.shangjiaxingming?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item prop="touxiang"
									  label="头像"
						>
							<uploads
								:disabled="!isAdd||disabledForm.touxiang?true:false"
								action="file/upload"
								tip="请上传头像"
								style="width: 100%;text-align: left;"
								:fileUrls="form.touxiang?form.touxiang:''" 
								@change="touxiangUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="性别" prop="xingbie">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.xingbie?true:false"
								v-model="form.xingbie" 
								placeholder="请选择性别"
								>
								<el-option v-for="(item,index) in xingbieLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话" prop="lianxidianhua">
							<el-input class="list_inp" v-model="form.lianxidianhua" placeholder="联系电话"
                                type="text"
								:readonly="!isAdd||disabledForm.lianxidianhua?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="资质类型" prop="zizhileixing">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.zizhileixing?true:false"
								v-model="form.zizhileixing" 
								placeholder="请选择资质类型"
								>
								<el-option v-for="(item,index) in zizhileixingLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item prop="zizhizhengming"
									  label="资质证明"
						>
							<uploads
								:disabled="!isAdd||disabledForm.zizhizhengming?true:false"
								action="file/upload"
								tip="请上传资质证明"
								style="width: 100%;text-align: left;"
								:fileUrls="form.zizhizhengming?form.zizhizhengming:''" 
								@change="zizhizhengmingUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="密保问题" prop="pquestion">
							<el-input class="list_inp" v-model="form.pquestion" placeholder="密保问题"
                                type="text"
								:readonly="!isAdd||disabledForm.pquestion?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="密保答案" prop="panswer">
							<el-input class="list_inp" v-model="form.panswer" placeholder="密保答案"
                                type="text"
								:readonly="!isAdd||disabledForm.panswer?true:false" />
						</el-form-item>
					</el-col>

				</el-row>
			</el-form>
			<template #footer v-if="isAdd||type=='logistics'||type=='reply'">
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="closeClick">取消</el-button>
					<el-button class="confirm_btn" type="primary" @click="save"
						>
						提交
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		computed,
		defineEmits
	} from 'vue'
    import {
        useStore
    } from 'vuex';
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;	
	const emit = defineEmits(['formModelChange'])
	//基础信息
	const tableName = 'shangjia'
	const formName = '商家'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        shangjiazhanghao : false,
        mima : false,
        shangjiaxingming : false,
        touxiang : false,
        xingbie : false,
        lianxidianhua : false,
        zizhileixing : false,
        zizhizhengming : false,
        sfsh : false,
        shhf : false,
        pquestion : false,
        panswer : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		shangjiazhanghao: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		mima: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		shangjiaxingming: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		touxiang: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		xingbie: [
		],
		lianxidianhua: [
			{ validator: context.$toolUtil.validator.mobile, trigger: 'blur' },
		],
		zizhileixing: [
		],
		zizhizhengming: [
		],
		sfsh: [
		],
		shhf: [
		],
		pquestion: [
		],
		panswer: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//头像上传回调
	const touxiangUploadSuccess=(e)=>{
		form.value.touxiang = e
	}
	//性别列表
	const xingbieLists = ref([])
	//资质类型列表
	const zizhileixingLists = ref([])
	//资质证明上传回调
	const zizhizhengmingUploadSuccess=(e)=>{
		form.value.zizhizhengming = e
	}

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			shangjiazhanghao: '',
			mima: '',
			shangjiaxingming: '',
			touxiang: '',
			xingbie: '',
			lianxidianhua: '',
			zizhileixing: '',
			zizhizhengming: '',
			shhf: '',
			pquestion: '',
			panswer: '',
		}
	}
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			form.value = res.data.data
			formVisible.value = true
		})
	}
	const crossRow = ref('')
	const crossTable = ref('')
	const crossTips = ref('')
	const crossColumnName = ref('')
	const crossColumnValue = ref('')
	//初始化
	const init=(formId=null,formType='add',formNames='',row=null,table=null,statusColumnName=null,tips=null,statusColumnValue=null)=>{
		resetForm()
		if(formId){
			id.value = formId
			type.value = formType
		}
		if(formType == 'add'){
			isAdd.value = true
			formTitle.value = '新增' + formName
			formVisible.value = true
		}else if(formType == 'info'){
			isAdd.value = false
			formTitle.value = '查看' + formName
			getInfo()
		}else if(formType == 'edit'){
			isAdd.value = true
			formTitle.value = '修改' + formName
			getInfo()
		}
		else if(formType == 'cross'){
			isAdd.value = true
			formTitle.value = formNames
			// getInfo()
			for(let x in row){
				if(x=='shangjiazhanghao'){
					form.value.shangjiazhanghao = row[x];
					disabledForm.value.shangjiazhanghao = true;
					continue;
				}
				if(x=='mima'){
					form.value.mima = row[x];
					disabledForm.value.mima = true;
					continue;
				}
				if(x=='shangjiaxingming'){
					form.value.shangjiaxingming = row[x];
					disabledForm.value.shangjiaxingming = true;
					continue;
				}
				if(x=='touxiang'){
					form.value.touxiang = row[x];
					disabledForm.value.touxiang = true;
					continue;
				}
				if(x=='xingbie'){
					form.value.xingbie = row[x];
					disabledForm.value.xingbie = true;
					continue;
				}
				if(x=='lianxidianhua'){
					form.value.lianxidianhua = row[x];
					disabledForm.value.lianxidianhua = true;
					continue;
				}
				if(x=='zizhileixing'){
					form.value.zizhileixing = row[x];
					disabledForm.value.zizhileixing = true;
					continue;
				}
				if(x=='zizhizhengming'){
					form.value.zizhizhengming = row[x];
					disabledForm.value.zizhizhengming = true;
					continue;
				}
				if(x=='pquestion'){
					form.value.pquestion = row[x];
					disabledForm.value.pquestion = true;
					continue;
				}
				if(x=='panswer'){
					form.value.panswer = row[x];
					disabledForm.value.panswer = true;
					continue;
				}
			}
			if(row){
				crossRow.value = row
			}
			if(table){
				crossTable.value = table
			}
			if(tips){
				crossTips.value = tips
			}
			if(statusColumnName){
				crossColumnName.value = statusColumnName
			}
			if(statusColumnValue){
				crossColumnValue.value = statusColumnValue
			}
			formVisible.value = true
		}

		context?.$http({
			url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
		})
		xingbieLists.value = "男,女".split(',')
		zizhileixingLists.value = "实人认证,企业认证,采购认证,实力认证".split(',')
	}
	//初始化
	//声明父级调用
	defineExpose({
		init
	})
	//关闭
	const closeClick = () => {
		formVisible.value = false
	}
	//富文本
	const editorChange = (e,name) =>{
		form.value[name] = e
	}
	//提交
	const save= async ()=>{
		if(form.value.touxiang!=null) {
			form.value.touxiang = form.value.touxiang.replace(new RegExp(context?.$config.url,"g"),"");
		}
		if(form.value.zizhizhengming!=null) {
			form.value.zizhizhengming = form.value.zizhizhengming.replace(new RegExp(context?.$config.url,"g"),"");
		}
		var table = crossTable.value
		var objcross = JSON.parse(JSON.stringify(crossRow.value))
		let crossUserId = ''
		let crossRefId = ''
		let crossOptNum = ''
		if(type.value == 'cross'){
			if(crossColumnName.value!=''){
				if(!crossColumnName.value.startsWith('[')){
					for(let o in objcross){
						if(o == crossColumnName.value){
							objcross[o] = crossColumnValue.value
						}
					}
					//修改跨表数据
					changeCrossData(objcross)
				}else{
					crossUserId = user.value.id
					crossRefId = objcross['id']
					crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
				}
			}
		}
		formRef.value.validate((valid)=>{
			if(valid){
				if(crossUserId&&crossRefId){
					form.value.crossuserid = crossUserId
					form.value.crossrefid = crossRefId
					let params = {
						page: 1,
						limit: 1000, 
						crossuserid:form.value.crossuserid,
						crossrefid:form.value.crossrefid,
					}
					context?.$http({
						url: `${tableName}/page`,
						method: 'get', 
						params: params 
					}).then(res=>{
						if(res.data.data.total>=crossOptNum){
							context?.$toolUtil.message(`${crossTips.value}`,'error')
							return false
						}else{
							context?.$http({
								url: `${tableName}/${!form.value.id ? "save" : "update"}`,
								method: 'post', 
								data: form.value 
							}).then(async res=>{
								emit('formModelChange')
								context?.$toolUtil.message(`操作成功`,'success')
                                formVisible.value = false
							})
						}
					})
				}else{
					context?.$http({
						url: `${tableName}/${!form.value.id ? "save" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(async (res)=>{
						emit('formModelChange')
						context?.$toolUtil.message(`操作成功`,'success')
                        formVisible.value = false
					})
				}
			}else{
                context.$message.error('请完善信息')
            }
		})
	}
	//修改跨表数据
	const changeCrossData = async (row)=>{
        if(type.value == 'cross'){
            await context?.$http({
                url: `${crossTable.value}/update`,
                method: 'post',
                data: row
            }).then(res=>{})
        }
	}
</script>
<style lang="scss" scoped>
	// 表单
	.formModel_form{
		// form item
		:deep(.el-form-item) {
			//label
			.el-form-item__label {
			}
			// 内容盒子
			.el-form-item__content {
				// 输入框
				.list_inp {
				}
				// 下拉框
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
		}
	}
	// 按钮盒子
	.formModel_btn_box {
		.cancel_btn {
		}
		.cancel_btn:hover {
		}
		
		.confirm_btn {
		}
		.confirm_btn:hover {
		}
	}
</style>
