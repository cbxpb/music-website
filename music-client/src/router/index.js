import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏中的vue-router在3.0版本以上频繁点击菜单报错的问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

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
      { path: 'home', name: 'Home', meta:{ name: '首页' },  component: () => import('../views/manager/Home.vue')},
      { path: 'song-list', name: 'SongList', meta:{ name: '歌单' },  component: () => import('../views/manager/SongList.vue')},
      { path: 'singer', name: 'Singer', meta:{ name: '歌手' },  component: () => import('../views/manager/Singer.vue')},
      { path: 'my-music', name: 'MyMusic', meta:{ name: '我的音乐' },  component: () => import('../views/manager/MyMusic.vue')},
      { path: 'singer-album/:id', name: 'SingerAlbum', meta:{ name: '歌手详情' },  component: () => import('../views/manager/SingerAlbum.vue')},
      { path: 'song-list-album/:id', name: 'SongListAlbum', meta:{ name: '歌单详情' },  component: () => import('../views/manager/SongListAlbum.vue')},
      { path: 'search', name: 'Search', meta:{ name: '搜索' },  component: () => import('../views/manager/Search.vue')},
      { path: 'lrc', name: 'ShowLrc', meta:{ name: '歌词' },  component: () => import('../views/manager/ShowLrc.vue')},
      { path: 'person', name: 'Person', meta:{ name: '个人信息' }, component: () => import('../views/manager/Person.vue')},
      { path: 'password', name: 'Password', meta:{ name: '修改密码' }, component: () => import('../views/manager/Password.vue')},
    ]
  },
  {
    // 无权限页面
    path: '/403',
    name: 'Auth',
    component: () => import('../views/Auth.vue')
  },
  { path: '/*', name: '404', component: () => import('../views/404.vue')},
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to , from, next) => {
  // to 是到达的路由信息
  // from 是来源的路由信息
  // next 是帮助我们跳转路由的函数
  let consumerPaths = ['/home','/song-list','/singer','/my-music','/singer-album/:id','/song-list-album/:id','/search','/lrc','/person','/password']
  let consumer = JSON.parse(localStorage.getItem("consumer") || '{}')
  if (consumer.role !== '用户' && consumerPaths.includes(to.path)) {  //如果当前登录的不是用户，然后当前的到达的路径是用户才有权限访问的路径，那这个时候就让它去一个无权限页面
    next('/403')
  } else{ 
    next()
  }
})

export default router
