import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏中的vue-router在3.0版本以上频繁点击菜单报错的问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  // 管理员登录页面
  { path: '/login', name: 'Login', meta:{ name: '登录' }, component: () => import('../views/Login.vue')},


  {
    // 管理员注册页面
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },

  
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  //重定向到主页
    children:[
      { path: 'home', name: 'Home', meta:{ name: '系统首页' }, component: () => import('../views/manager/Home.vue')},
      { path: 'consumer', name: 'Consumer', meta:{ name: '用户管理' }, component: () => import('../views/manager/Consumer.vue')},
      { path: 'singer', name: 'Singer', meta:{ name: '歌手管理' }, component: () => import('../views/manager/Singer.vue')},
      { path: 'song', name: 'Song', meta:{ name: '歌曲管理' },  component: () => import('../views/manager/Song.vue')},
      { path: 'songList', name: 'SongList', meta:{ name: '歌单管理' }, component: () => import('../views/manager/SongList.vue')},
      { path: 'listSong', name: 'ListSong', meta:{ name: '歌单歌曲管理' }, component: () => import('../views/manager/ListSong.vue')},
      
      { path: 'person', name: 'Person', meta:{ name: '个人信息' }, component: () => import('../views/manager/Person.vue')},
      
      {
        // 无权限页面
        path: '/403',
        name: 'Auth',
        component: () => import('../views/Auth.vue')
      },
    ]
  },
  { path: '/*', name: '404', component: () => import('../views/404.vue')},
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to , from, next) => {
  console.log(to,from,next)
  // to 是到达的路由信息
  // from 是来源的路由信息
  // next 是帮助我们跳转路由的函数
  let adminPaths = ['/home','/consumer','/singer','/song','/songList','/listSong','/']
  let admin = JSON.parse(localStorage.getItem("admin") || '{}')
  if (admin.role !== '管理员' && adminPaths.includes(to.path)) {  //如果当前登录的用户不是管理员，然后当前的到达的路径是管理员才有权限访问的路径，那这个时候就让用户去一个无权限页面
    next('/403')
  } else{ 
    next()
  }
})

export default router
