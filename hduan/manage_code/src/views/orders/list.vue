<template>
	<div>
		<div class="center_view">
			<el-tabs
			    v-model="orderStatus"
			    type="card"
			    class="demo-tabs"
				@tab-change="orderTabClick"
				>
				<el-tab-pane label="全部" :name="''"></el-tab-pane>
				<el-tab-pane label="未支付" name="未支付" v-if="btnAuth(`orders/未支付`,'查看')"></el-tab-pane>
				<el-tab-pane label="拼团中" name="拼团中" v-if="btnAuth(`orders/拼团中`,'查看')"></el-tab-pane>
				<el-tab-pane label="已支付" name="已支付" v-if="btnAuth(`orders/已支付`,'查看')"></el-tab-pane>
				<el-tab-pane label="已发货" name="已发货" v-if="btnAuth(`orders/已发货`,'查看')"></el-tab-pane>
				<el-tab-pane label="已完成" name="已完成" v-if="btnAuth(`orders/已完成`,'查看')"></el-tab-pane>
				<el-tab-pane label="已退款" name="已退款" v-if="btnAuth(`orders/已退款`,'查看')"></el-tab-pane>
				<el-tab-pane label="已取消" name="已取消" v-if="btnAuth(`orders/已取消`,'查看')"></el-tab-pane>
			</el-tabs>
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							订单编号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.orderid" placeholder="订单编号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('orders','新增')">
						新增
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'删除')">
						删除
					</el-button>
					<el-button class="other_btn" type="default" :disabled="selRows.length?false:true" @click="exportClick" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'导出')">
						导出
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="orderid"
					label="订单编号">
					<template #default="scope">
						{{scope.row.orderid}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="goodname"
					label="商品名称">
					<template #default="scope">
						{{scope.row.goodname}}
					</template>
				</el-table-column>
				<el-table-column label="图片" min-width="140" width="120" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<div v-if="scope.row.picture">
							<el-image v-if="scope.row.picture.substring(0,4)=='http'" preview-teleported
								:preview-src-list="[scope.row.picture.split(',')[0]]"
								:src="scope.row.picture.split(',')[0]" style="width:100px;height:100px"></el-image>
							<el-image v-else preview-teleported
								:preview-src-list="[$config.url+scope.row.picture.split(',')[0]]"
								:src="$config.url+scope.row.picture.split(',')[0]" style="width:100px;height:100px">
							</el-image>
						</div>
						<div v-else>无图片</div>
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="buynumber"
					label="购买数量">
					<template #default="scope">
						{{scope.row.buynumber}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="price"
					label="单价">
					<template #default="scope">
						{{scope.row.price}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="discountprice"
					label="折扣价">
					<template #default="scope">
						{{scope.row.discountprice}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="total"
					label="总价">
					<template #default="scope">
						{{scope.row.total}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="discounttotal"
					label="折扣总价格">
					<template #default="scope">
						{{scope.row.discounttotal}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="type"
					:formatter="orderStatusFormatter"
					label="支付类型">
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="status"
					label="订单状态">
					<template #default="scope">
						{{scope.row.status}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="address"
					label="地址">
					<template #default="scope">
						{{scope.row.address}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tel"
					label="电话">
					<template #default="scope">
						{{scope.row.tel}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="consignee"
					label="收货人">
					<template #default="scope">
						{{scope.row.consignee}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="remark"
					label="备注">
					<template #default="scope">
						{{scope.row.remark}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="addtime"
					label="创建时间">
					<template #default="scope">
						{{scope.row.addtime}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="shangjiazhanghao"
					label="商户名称">
					<template #default="scope">
						{{scope.row.shangjiazhanghao}}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="300" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'查看')" @click="infoClick(scope.row.id)">
							详情
						</el-button>
						<el-button class="edit_btn" type="primary" @click="editClick(scope.row.id)" v-if=" btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'修改')">
							修改						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'删除')">
							删除						</el-button>
						<el-button class="operate_btn" v-if="btnAuth('orders'+(scope.row.status?'/'+scope.row.status:''),'物流')" type="warning" @click="logisticsClick(scope.row)">
							物流
						</el-button>
						<el-button class="operate_btn" v-if="btnAuth('orders'+(scope.row.status?'/'+scope.row.status:''),'发货')" type="warning" @click="sendGoodClick(scope.row)">
							发货
						</el-button>
						<el-button class="operate_btn" v-if="btnAuth('orders'+(scope.row.status?'/'+scope.row.status:''),'确认收货')&&scope.row.userid==user.id" type="warning" @click="confirmGoodClick(scope.row)">
							确认收货
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="listQuery.limit"
                v-model:current-page="listQuery.page"
				prev-text="➨"
				next-text="➨"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
	</div>
</template>
<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		watch,
		computed,
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	import formModel from './formModel.vue'
	//基础信息

	const tableName = 'orders'
	const formName = '商品订单'
	const route = useRoute()
    const router = useRouter()
	watch(() => route.query,() => {
		init()
	})
	//基础信息
	onMounted(()=>{
	})
	//列表数据
	const list = ref(null)
	const table = ref(null)
	const listQuery = ref({
		page: 1,
		limit: 20,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})
	const selRows = ref([])
	const listLoading = ref(false)
	const listChange = (row) =>{
		nextTick(()=>{
			//table.value.clearSelection()
			table.value.toggleRowSelection(row)
		})
	}
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(orderStatus.value){
			params['status'] = orderStatus.value
		}
		if(searchQuery.value.orderid&&searchQuery.value.orderid!=''){
			params['orderid'] = '%' + searchQuery.value.orderid + '%'
		}
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
		})
	}
	//删
	const delClick = (id) => {
		let ids = []
		if (id) {
			ids = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//列表数据
	//分页
	const total = ref(0)
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	//表单
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	const addClick = ()=>{
		formRef.value.init()
	}
	const editClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'edit')
			return
		}
		if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'edit')
		}
	}

	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	// 表单
	// 预览文件
	const preClick = (file) =>{
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		window.open(context?.$config.url + file)
	}
	// 下载文件
	const download = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
			headers: {
				token: context?.$toolUtil.storageGet('Token')
			},
			responseType: "blob"
		}).then(({
			data
		}) => {
			const binaryData = [];
			binaryData.push(data);
			const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
				type: 'application/pdf;chartset=UTF-8'
			}))
			const a = document.createElement('a')
			a.href = objectUrl
			a.download = arr
			// a.click()
			// 下面这个写法兼容火狐
			a.dispatchEvent(new MouseEvent('click', {
				bubbles: true,
				cancelable: true,
				view: window
			}))
			window.URL.revokeObjectURL(data)
		})
	}
	//导出选中数据
	const exportClick = () => {
		import('@/utils/Export2Excel').then(excel => {
			const tHeader = [
				"订单编号",
				"商品表名",
				"商品id",
				"商品名称",
				"图片",
				"购买数量",
				"单价",
				"折扣价",
				"总价",
				"折扣总价格",
				"支付类型",
				"订单状态",
				"地址",
				"电话",
				"收货人",
				"备注",
				"物流",
				"用户角色",
				"创建时间",
				"用户id",
				"商户名称",
			]
			const filterVal = [
				"orderid",
				"tablename",
				"goodid",
				"goodname",
				"picture",
				"buynumber",
				"price",
				"discountprice",
				"total",
				"discounttotal",
				"type",
				"status",
				"address",
				"tel",
				"consignee",
				"remark",
				"logistics",
				"role",
				"addtime",
				"userid",
				"shangjiazhanghao",
			]
			excel.export_json_to_excel2(tHeader, selRows.value, filterVal, formName)
		})
	}
	const orderTabClick = () => {
        let query = {}
        if(orderStatus.value){
            query.menuJump = orderStatus.value
        }
        router.replace({query})
	}
	const orderStatus = ref('')
	const orderStatusFormatter =(row, column)=> {
      var temp = ''
      switch (row.type-0) {
          case 1:
              temp = '现金'
              break
          case 2:
              temp = '积分'
              break
      }
      return temp
    }
	//修改物流
	const logisticsClick = (row)=>{
		formRef.value.init(row.id,'logistics','',row)
	}
	//发货
	const sendGoodClick=(row)=>{
		ElMessageBox.confirm(`确定该订单商品发货？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(()=>{
			row.status = '已发货'
			context.$http({
				url: 'orders/update',
				method: 'post',
				data: row
			}).then(res=>{
				context?.$toolUtil.message('已发货','success',()=>{
					searchClick()
				})
			})
		}).catch(_ => {})
	}
	//确认收货
	const confirmGoodClick=(row)=>{
		ElMessageBox.confirm(`确定该订单商品已收货？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(()=>{
			row.status = '已完成'
			context.$http({
				url: 'orders/update',
				method: 'post',
				data: row
			}).then(res=>{
				context?.$toolUtil.message('订单完成','success',()=>{
					searchClick()
				})
			})
		}).catch(_ => {})
	}
	//初始化
	const init = () => {
        if(['未支付','拼团中','已支付','已发货','已完成','已退款','已取消'].includes(route.query.menuJump)){
            orderStatus.value = route.query.menuJump
        }else{
            orderStatus.value = ''
        }
		getList()
	}
	init()
</script>
<style lang="scss" scoped>
	// 切换栏
	.demo-tabs {
		// 头部
		:deep(.el-tabs__header) {
			// 滑动区
			.el-tabs__nav-scroll {
				// list
				.el-tabs__nav {
					// item
					.el-tabs__item {
					}
					// item active
					.is-active {
					}
					// item hover
					.el-tabs__item:hover {
					}
				}
			}
		}
	}

	// 操作盒子
	.list_search_view {
		// 搜索盒子
		.search_form {
			// 子盒子
			.search_view {
				// 搜索label
				.search_label {
				}
				// 搜索item
				.search_box {
					// 输入框
					:deep(.search_inp) {
					}
				}
			}
			// 搜索按钮盒子
			.search_btn_view {
				// 搜索按钮
				.search_btn {
				}
				// 搜索按钮-悬浮
				.search_btn:hover {
				}
			}
		}
		//头部按钮盒子
		.btn_view {
			// 其他
			:deep(.el-button--default){
			}
			// 其他-悬浮
			:deep(.el-button--default:hover){
			}
			// 新增
			:deep(.el-button--success){
			}
			// 新增-悬浮
			:deep(.el-button--success:hover){
			}
			// 删除
			:deep(.el-button--danger){
			}
			// 删除-悬浮
			:deep(.el-button--danger:hover){
			}
			// 统计
			:deep(.el-button--warning){
			}
			// 统计-悬浮
			:deep(.el-button--warning:hover){
			}
		}
	}
	// 表格样式
	.el-table {
		:deep(.el-table__header-wrapper) {
			thead {
				tr {
					th {
						.cell {
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				tr {
					td {
						.cell {
							// 编辑
							.el-button--primary {
							}
							// 编辑-悬浮
							.el-button--primary:hover {
							}
							// 详情
							.el-button--info {
							}
							// 详情-悬浮
							.el-button--info:hover {
							}
							// 删除
							.el-button--danger {
							}
							// 删除-悬浮
							.el-button--danger:hover {
							}
							// 跨表
							.el-button--success {
							}
							// 跨表-悬浮
							.el-button--success:hover {
							}
							// 操作
							.el-button--warning {
							}
							// 操作-悬浮
							.el-button--warning:hover {
							}
						}
					}
				}
				tr:hover {
					td {
					}
				}
			}
		}
	}
	// 分页器
	.el-pagination {
		// 总页码
		:deep(.el-pagination__total) {
		}
		// 上一页
		:deep(.btn-prev) {
		}
		// 下一页
		:deep(.btn-next) {
		}
		// 上一页禁用
		:deep(.btn-prev:disabled) {
		}
		// 下一页禁用
		:deep(.btn-next:disabled) {
		}
		// 页码
		:deep(.el-pager) {
			// 数字
			.number {
			}
			// 数字悬浮
			.number:hover {
			}
			// 选中
			.number.is-active {
			}
		}
		// sizes
		:deep(.el-pagination__sizes) {
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
			.el-select {
			}
		}
		// 跳页
		:deep(.el-pagination__jump) {
			// 输入框
			.el-input {
			}
		}
	}
</style>
