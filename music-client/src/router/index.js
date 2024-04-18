import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  // 用户登录页面
  { path: '/login', name: 'Login', meta:{ name: '登录' }, component: () => import('../views/Login.vue')},
  // 用户注册页面
  { path: '/register', name: 'Register', meta:{ name: '注册' },  component: () => import('../views/Register.vue')},
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    //重定向到首页
    redirect: '/home',
    children:[
      { path: '/home', name: 'Home', meta:{ name: '首页' },  component: () => import('../views/manager/Home.vue')},
      { path: '/song-list', name: 'SongList', meta:{ name: '歌单' },  component: () => import('../views/manager/SongList.vue')},
     
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router