<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							商品名称：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.shangpinmingcheng" placeholder="商品名称"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							商品品牌：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.shangpinpinpai" placeholder="商品品牌"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('shangpinxinxi','新增')">
						新增
					</el-button>
				</div>
			</div>
			<div class="dataList"
				 v-if="btnAuth('shangpinxinxi','查看')"
			>
				<div class="item" v-for="(item,index) in list" :key="item.id">
                    <template v-if="item.shangpintupian">
                        <el-image v-if="item.shangpintupian.substring(0,4)=='http'" preview-teleported
                                  :preview-src-list="[item.shangpintupian.split(',')[0]]"
                                  :src="item.shangpintupian.split(',')[0]"></el-image>
                        <el-image v-else preview-teleported
                                  :preview-src-list="[$config.url+item.shangpintupian.split(',')[0]]"
                                  :src="$config.url+item.shangpintupian.split(',')[0]">
                        </el-image>
                    </template>
					<div class="title">商品名称：{{item.shangpinmingcheng}}</div>
                    <div class="title status-row">
                    </div>
					<div class="btns">
						<el-button class="view_btn" type="info" v-if=" btnAuth('shangpinxinxi','查看')" @click="infoClick(item.id)">
							详情
						</el-button>
						<el-button class="edit_btn" type="primary" @click="editClick(item.id)" v-if=" btnAuth('shangpinxinxi','修改')">
							修改
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(item.id)"  v-if="btnAuth('shangpinxinxi','删除')">
							删除
						</el-button>
                        <el-button class="operate_btn" v-if="btnAuth('shangpinxinxi','私信')" type="success" @click="chatClick(item)">
                          私信
                        </el-button>
					</div>
				</div>
			</div>
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
        <el-dialog v-model="chatVisible" @close="clearChat" :title="nowname" :append-to-body="true">
            <div class="chat-content" id="chat-content">
                <div v-bind:key="item.id" v-for="(item,index) in chatList">
                    <div v-if="index>1&&moment(chatList[index-1].addtime).date()!=moment(item.addtime).date()||index==0"
                         style="font-size: 12px;text-align: center;margin: 4px 0;">
                        {{moment(item.addtime).format("MM-DD HH:mm")}}
                    </div>
                    <div v-if="item.uid==user.id" class="right-content">
                        <el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false"
                                  type="warning"></el-alert>
                        <video v-else-if="item.content.endsWith('.mp4')" controls style="width: 200px;height: 160px">
                            <source  :src="$config.url + item.content">
                        </video>
                        <el-image v-else fit="cover" :src="item.content?$config.url + item.content:''"
                                  style="width: 100px;height: 100px;"
                                  :preview-src-list="[item.content?$config.url + item.content:'']"></el-image>
                        <img :src="avatar?$config.url + avatar:require('@/assets/img/avatar.png')" alt=""
                             style="width: 30px;border-radius: 50%;height: 30px;margin: 0 0 0 10px;flex-shrink: 0;object-fit: cover;" />
                    </div>
                    <div v-else class="left-content">
                        <img :src="nowfpic?$config.url + nowfpic:require('@/assets/img/avatar.png')" alt=""
                             style="width: 30px;border-radius: 50%;height: 30px;margin: 0 10px 0 0;flex-shrink: 0;object-fit: cover;" />
                        <el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false"
                                  type="success"></el-alert>
                        <video v-else-if="item.content.endsWith('.mp4')" controls style="width: 200px;height: 160px">
                            <source  :src="$config.url + item.content">
                        </video>
                        <el-image v-else fit="cover" :src="item.content?$config.url + item.content:''"
                                  style="width: 100px;height: 100px;"
                                  :preview-src-list="[item.content?$config.url + item.content:'']"></el-image>
                    </div>
                    <div class="clear-float"></div>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-input @keydown.enter.native="addChat" v-model="chatForm.content" placeholder="请输入内容"
                          style="width: calc(100% - 180px);float: left;">
                </el-input>
                <el-button :disabled="chatForm.content?false:true" type="primary" @click="addChat">发送</el-button>
                <el-upload style="display: inline-block;margin: 0 0 0 6px;" class="upload-demo" :action="uploadUrl"
                           :on-success="uploadSuccess" :show-file-list="false" accept="image/*,.mp4">
                    <el-button type="success">上传文件</el-button>
                </el-upload>
            </div>
        </el-dialog>
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

	const tableName = 'shangpinxinxi'
	const formName = '商品信息'
	const route = useRoute()
    const router = useRouter()
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
		if(searchQuery.value.shangpinmingcheng&&searchQuery.value.shangpinmingcheng!=''){
			params['shangpinmingcheng'] = '%' + searchQuery.value.shangpinmingcheng + '%'
		}
		if(searchQuery.value.shangpinpinpai&&searchQuery.value.shangpinpinpai!=''){
			params['shangpinpinpai'] = '%' + searchQuery.value.shangpinpinpai + '%'
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
    const chatVisible = ref(false)
    const nowfid = ref(0)
    const nowfpic = ref('')
    const nowname = ref('')
    const chatList = ref([])
    const chatForm = ref({
        content: ''
    })
    const chatTimer = ref(null)
    const uploadUrl = context?.$config.url + 'file/upload'
    const chatClick = (row) => {
        if (row.shangjiazhanghao == user.value.shangjiazhanghao) {
            context.$toolUtil.message('不能给自己发消息！','error')
            return false
        }
        context.$http.get('shangjia/query', {
			params: {
                shangjiazhanghao: row.shangjiazhanghao
			}
		}).then(res => {
			if (res.data && res.data.code == 0) {
                nowfid.value = res.data.data.id
                nowname.value = res.data.data.shangjiazhanghao
				if (res.data.data.touxiang) {
                    nowfpic.value = res.data.data.touxiang.split(',')[0]
				} else if (res.data.data.headportrait) {
                    nowfpic.value = res.data.data.headportrait.split(',')[0]
				}
				getChatList()
                scrollFlag.value = true
				chatVisible.value = true
			}
		})
    }
    const scrollFlag = ref(true)
    const getChatList = () => {
        context.$http.get('chatmessage/mlist', {
            params: {
                page: 1,
                limit: 1000,
                uid: user.value.id,
                fid: nowfid.value
            }
        }).then(res => {
            if (res.data && res.data.code == 0) {
                chatList.value = res.data.data.list
                let div = document.getElementsByClassName('chat-content')[0]
                setTimeout(() => {
                    if (div){
                        if(div.scrollTop+div.clientHeight==div.scrollHeight || scrollFlag.value){
                            div.scrollTop = div.scrollHeight
                            scrollFlag.value = false
                        }
                    }
                }, 0)
                chatTimer.value = setTimeout(() => {
                    getChatList()
                }, 5000)
            }
        })
    }
    const clearChat = () => {
        clearTimeout(chatTimer.value)
        chatList.value = []
        getList()
    }
    const uploadSuccess = (res) => {
        if (res.code == 0) {
            clearTimeout(chatTimer.value)
            context.$http.get('chatfriend/page', {
                params: {
                    uid: user.value.id,
                    fid: nowfid.value,
                }
            }).then(obj => {
                if (obj.data && obj.data.code == 0) {
                    if (!obj.data.data.list.length) {
                        context.$http.post('chatfriend/add', {
                            uid: user.value.id,
                            fid: nowfid.value,
                            name: nowname.value,
                            picture: nowfpic.value,
                            type: 2,
                            tablename: 'shangpinxinxi',
                        }).then(res => {
                            context.$http.post('chatfriend/add', {
                                uid: nowfid.value,
                                fid: user.value.id,
                                type: 2,
                                tablename: 'shangpinxinxi',
                                name: context.$toolUtil.storageGet('adminName'),
                                picture: avatar.value,
                            }).then(res1 => {

                            })
                        })
                    }
                    context.$http.post('chatmessage/add', {
                        uid: user.value.id,
                        fid: nowfid.value,
                        content: 'file/' + res.file,
                        format: 2
                    }).then(res2 => {
                        chatForm.value = {
                            content: ''
                        }
                        scrollFlag.value = true
                        getChatList()
                    })
                }
            })
        }
    }
    const addChat = () => {
        if(!chatForm.value.content.trim())return context.$message.error("消息内容不能为空")
        clearTimeout(chatTimer.value)
        context.$http.get('chatfriend/page', {
            params: {
                uid: user.value.id,
                fid: nowfid.value,
            }
        }).then(obj => {
            if (obj.data && obj.data.code == 0) {
                if (!obj.data.data.list.length) {
                    context.$http.post('chatfriend/add', {
                        uid: user.value.id,
                        fid: nowfid.value,
                        name: nowname.value,
                        picture: nowfpic.value,
                        type: 2,
                        tablename:  'shangjia' ,
                    }).then(res => {
                        context.$http.post('chatfriend/add', {
                            uid: nowfid.value,
                            fid: user.value.id,
                            type: 2,
                            tablename: localStorage.getItem('sessionTable'),
                            name: context.$toolUtil.storageGet('adminName'),
                            picture: avatar.value,
                        }).then(res1 => {

                        })
                    })
                }
                context.$http.post('chatmessage/add', {
                    uid: user.value.id,
                    fid: nowfid.value,
                    content: chatForm.value.content,
                    format: 1
                }).then(res2 => {
                    chatForm.value = {
                        content: ''
                    }
                    scrollFlag.value = true
                    getChatList()
                })
            }
        })
    }
	//初始化
	const init = () => {
		getList()
	}
	init()
</script>
<style lang="scss" scoped>

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
	.section-content {
		cursor: pointer;
		padding: 20px;
		box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
		margin: 0 0 20px;
		color: #333;
		background: #fff;
		display: flex;
		width: 100%;
		border-color: #efefef;
		border-width: 0;
		align-items: center;
		border-style: solid;
		position: relative;
	}

	.section-content:hover {
		color: #fff;
		background: #DF847F10;
	}
	.chat-content {
		padding-bottom: 20px;
		width: 100%;
		margin-bottom: 10px;
		max-height: 300px;
		height: 300px;
		overflow-y: scroll;
		border: 1px solid #eeeeee;
		background: #fff;

		.left-content {
			float: left;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
			display: flex;
			align-items: center;
		}

		.right-content {
			float: right;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
			display: flex;
			align-items: center;
		}
	}

	.clear-float {
		clear: both;
	}
	.noList {
		color: #9e9e9e;
		width: 100%;
		text-align: center;
		padding: 60px 0;
	}
    .status-row{
        display:flex;
        gap: 6px;
    }
</style>
