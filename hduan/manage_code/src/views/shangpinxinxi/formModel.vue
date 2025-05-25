<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="80%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="12">
						<el-form-item label="商品名称" prop="shangpinmingcheng">
							<el-input class="list_inp" v-model="form.shangpinmingcheng" placeholder="商品名称"
                                type="text"
								:readonly="!isAdd||disabledForm.shangpinmingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="商品分类" prop="shangpinfenlei">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.shangpinfenlei?true:false"
								v-model="form.shangpinfenlei" 
								placeholder="请选择商品分类"
								>
								<el-option v-for="(item,index) in shangpinfenleiLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="商品品牌" prop="shangpinpinpai">
							<el-input class="list_inp" v-model="form.shangpinpinpai" placeholder="商品品牌"
                                type="text"
								:readonly="!isAdd||disabledForm.shangpinpinpai?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="商品规格" prop="shangpinguige">
							<el-input class="list_inp" v-model="form.shangpinguige" placeholder="商品规格"
                                type="text"
								:readonly="!isAdd||disabledForm.shangpinguige?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item prop="shangpintupian"
									  label="商品图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.shangpintupian?true:false"
								action="file/upload"
								tip="请上传商品图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.shangpintupian?form.shangpintupian:''" 
								@change="shangpintupianUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="商品简介" prop="shangpinjianjie">
							<el-input class="list_inp" v-model="form.shangpinjianjie" placeholder="商品简介"
                                type="text"
								:readonly="!isAdd||disabledForm.shangpinjianjie?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="价格" prop="price">
							<el-input class="list_inp" v-model.number="form.price" placeholder="价格"
                                type="number"
                                @mousewheel.native.prevent
								:readonly="!isAdd||disabledForm.price?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="商家账号" prop="shangjiazhanghao">
							<el-input class="list_inp" v-model="form.shangjiazhanghao" placeholder="商家账号"
                                type="text"
								:readonly="!isAdd||disabledForm.shangjiazhanghao?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="12">
						<el-form-item label="商家姓名" prop="shangjiaxingming">
							<el-input class="list_inp" v-model="form.shangjiaxingming" placeholder="商家姓名"
                                type="text"
								:readonly="!isAdd||disabledForm.shangjiaxingming?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="商品详情" prop="shangpinxiangqing">
							<el-input v-model="form.shangpinxiangqing" placeholder="商品详情" type="textarea"
							:readonly="!isAdd||disabledForm.shangpinxiangqing?true:false"
							/>
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
	const tableName = 'shangpinxinxi'
	const formName = '商品信息'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        shangpinmingcheng : false,
        shangpinfenlei : false,
        shangpinpinpai : false,
        shangpinguige : false,
        shangpintupian : false,
        shangpinjianjie : false,
        shangpinxiangqing : false,
        price : false,
        shangjiazhanghao : false,
        shangjiaxingming : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		shangpinmingcheng: [
		],
		shangpinfenlei: [
		],
		shangpinpinpai: [
		],
		shangpinguige: [
		],
		shangpintupian: [
		],
		shangpinjianjie: [
		],
		shangpinxiangqing: [
		],
		price: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		shangjiazhanghao: [
		],
		shangjiaxingming: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//商品分类列表
	const shangpinfenleiLists = ref([])
	//商品图片上传回调
	const shangpintupianUploadSuccess=(e)=>{
		form.value.shangpintupian = e
	}

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			shangpinmingcheng: '',
			shangpinfenlei: '',
			shangpinpinpai: '',
			shangpinguige: '',
			shangpintupian: '',
			shangpinjianjie: '',
			shangpinxiangqing: '',
			price: '',
			shangjiazhanghao: '',
			shangjiaxingming: '',
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
				if(x=='shangpinmingcheng'){
					form.value.shangpinmingcheng = row[x];
					disabledForm.value.shangpinmingcheng = true;
					continue;
				}
				if(x=='shangpinfenlei'){
					form.value.shangpinfenlei = row[x];
					disabledForm.value.shangpinfenlei = true;
					continue;
				}
				if(x=='shangpinpinpai'){
					form.value.shangpinpinpai = row[x];
					disabledForm.value.shangpinpinpai = true;
					continue;
				}
				if(x=='shangpinguige'){
					form.value.shangpinguige = row[x];
					disabledForm.value.shangpinguige = true;
					continue;
				}
				if(x=='shangpintupian'){
					form.value.shangpintupian = row[x];
					disabledForm.value.shangpintupian = true;
					continue;
				}
				if(x=='shangpinjianjie'){
					form.value.shangpinjianjie = row[x];
					disabledForm.value.shangpinjianjie = true;
					continue;
				}
				if(x=='shangpinxiangqing'){
					form.value.shangpinxiangqing = row[x];
					disabledForm.value.shangpinxiangqing = true;
					continue;
				}
				if(x=='price'){
					form.value.price = row[x];
					disabledForm.value.price = true;
					continue;
				}
				if(x=='shangjiazhanghao'){
					form.value.shangjiazhanghao = row[x];
					disabledForm.value.shangjiazhanghao = true;
					continue;
				}
				if(x=='shangjiaxingming'){
					form.value.shangjiaxingming = row[x];
					disabledForm.value.shangjiaxingming = true;
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
			if(json.hasOwnProperty('shangjiazhanghao')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.shangjiazhanghao = json.shangjiazhanghao
				disabledForm.value.shangjiazhanghao = true;
			}
			if(json.hasOwnProperty('shangjiaxingming')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.shangjiaxingming = json.shangjiaxingming
				disabledForm.value.shangjiaxingming = true;
			}
		})
		context?.$http({
			url: `option/shangpinfenlei/shangpinfenlei`,
			method: 'get'
		}).then(res=>{
			shangpinfenleiLists.value = res.data.data
		})
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
		if(form.value.shangpintupian!=null) {
			form.value.shangpintupian = form.value.shangpintupian.replace(new RegExp(context?.$config.url,"g"),"");
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
				// 长文本
				.el-textarea__inner {
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
