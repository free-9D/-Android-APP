	import {
		createRouter,
		createWebHashHistory
	} from 'vue-router'
	import duanshipin from '@/views/duanshipin/list'
	import address from '@/views/address/list'
	import shangpinxinxi from '@/views/shangpinxinxi/list'
	import fankuiyijian from '@/views/fankuiyijian/list'
	import storeup from '@/views/storeup/list'
	import users from '@/views/users/list'
	import shangjia from '@/views/shangjia/list'
	import cart from '@/views/cart/list'
	import discussduanshipin from '@/views/discussduanshipin/list'
	import shangpinfenlei from '@/views/shangpinfenlei/list'
	import chat from '@/views/chat/list'
	import yonghu from '@/views/yonghu/list'
	import orders from '@/views/orders/list'
	import config from '@/views/config/list'
	import yijianfankui from '@/views/yijianfankui/list'
	import usersCenter from '@/views/users/center'
	import shangjiaRegister from '@/views/shangjia/register'
	import shangjiaCenter from '@/views/shangjia/center'

export const routes = [{
		path: '/login',
		name: 'login',
        meta: { title: '登录' },
		component: () => import('../views/login.vue')
	},{
		path: '/',
		name: '首页',
        meta: { title: '首页' },
		component: () => import('../views/index'),
		children: [{
			path: '/',
			name: 'home',
			component: () => import('../views/HomeView.vue'),
			meta: {
				affix: true,
                title: '首页'
			}
		}, {
			path: '/updatepassword',
			name: 'updatepassword',
            meta: { title: '修改密码' },
			component: () => import('../views/updatepassword.vue')
		}

		,{
			path: '/usersCenter',
			name: 'usersCenter',
            meta: { title: '管理员个人中心' },
			component: usersCenter
		}
		,{
			path: '/shangjiaCenter',
			name: 'shangjiaCenter',
            meta: { title: '商家个人中心' },
			component: shangjiaCenter
		}
		,{
			path: '/duanshipin',
			name: 'duanshipin',
            meta: { title: '短视频' },
			component: duanshipin
		}
		,{
			path: '/address',
			name: 'address',
            meta: { title: '地址' },
			component: address
		}
		,{
			path: '/shangpinxinxi',
			name: 'shangpinxinxi',
            meta: { title: '商品信息' },
			component: shangpinxinxi
		}
		,{
			path: '/fankuiyijian',
			name: 'fankuiyijian',
            meta: { title: '反馈意见' },
			component: fankuiyijian
		}
		,{
			path: '/storeup',
			name: 'storeup',
            meta: { title: '我的关注' },
			component: storeup
		}
		,{
			path: '/users',
			name: 'users',
            meta: { title: '管理员' },
			component: users
		}
		,{
			path: '/shangjia',
			name: 'shangjia',
            meta: { title: '商家' },
			component: shangjia
		}
		,{
			path: '/cart',
			name: 'cart',
            meta: { title: '购物车' },
			component: cart
		}
		,{
			path: '/discussduanshipin',
			name: 'discussduanshipin',
            meta: { title: '短视频评论-评论' },
			component: discussduanshipin
		}
		,{
			path: '/shangpinfenlei',
			name: 'shangpinfenlei',
            meta: { title: '商品分类' },
			component: shangpinfenlei
		}
		,{
			path: '/chat',
			name: 'chat',
            meta: { title: '在线客服' },
			component: chat
		}
		,{
			path: '/yonghu',
			name: 'yonghu',
            meta: { title: '用户' },
			component: yonghu
		}
		,{
			path: '/orders',
			name: 'orders',
            meta: { title: '订单管理' },
			component: orders
		}
		,{
			path: '/config',
			name: 'config',
            meta: { title: '轮播图' },
			component: config
		}
		,{
			path: '/yijianfankui',
			name: 'yijianfankui',
            meta: { title: '意见反馈' },
			component: yijianfankui
		}
		]
	},
	{
		path: '/shangjiaRegister',
		name: 'shangjiaRegister',
        meta: { title: '商家注册' },
		component: shangjiaRegister
	},
	{
		path: '/forget',
		name: 'forget',
        meta: { title: '忘记密码' },
		component: () => import('../views/forget.vue')
	}
]

const router = createRouter({
	history: createWebHashHistory(process.env.BASE_URL),
	routes
})

export default router
