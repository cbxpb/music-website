<!-- 页面框架部分 -->
<template>
    <div>
        <el-container>
            <!-- 侧边栏 -->
            <el-aside :width="asideWith">
               <div class="aside-logo">
                    <img src="@/assets/img/logo.png" alt="logo">
                    <span v-show="!isCollapse">music</span>
               </div>
               <!-- 侧边栏菜单 -->
               <el-menu :collapse="isCollapse" :collapse-transition="false" router background-color="#334256" text-color="#ffffff" active-text-color="#20a0ff" style="border: none" :default-active="$route.path">
                    <el-menu-item v-for="item in items" :key="item.index" :index="item.index" v-if="admin.role === '管理员'">
                        <i :class="item.icon"></i>
                        <span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <el-container>
                <!-- 头部区域 -->
                <el-header>
                    <i :class="collapseIcon" style="font-size: 26px;" @click="handleCollapse"></i>
                    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px;">
                        <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
                        <el-breadcrumb-item >{{ $route.meta.name }}</el-breadcrumb-item>
                    </el-breadcrumb>

                    <div style="flex: 1;width: 0; display: flex; align-items: center; justify-content: end; cursor: pointer;">
                        <el-tooltip :content="fullScreen?'取消全屏':'全屏'" placement="bottom">
                            <i class="el-icon-quanping" style="font-size: 26px;" @click="handleFull"></i>
                        </el-tooltip>
                        <el-dropdown placement="bottom">
                            <div style="display: flex; align-items: center;">
                                <template v-if="!admin.avatar">
                                    <el-upload action="http://localhost:9090/file/upload"
                                    :headers="{token: admin.token}"
                                    :show-file-list="false"
                                    :data="{type:1}"
                                    :on-success=" (res,file)=> {handleAvatarSuccess (res,file)}"
                                    :before-upload="beforeAvatarUpload"
                                    >
                                    <img class="avatar" src="@/assets/img/logo.png" alt="头像">
                                    </el-upload>
                                </template>
                                <template v-else>
                                    <el-upload action="http://localhost:9090/file/upload"
                                    :headers="{token: admin.token}"
                                    :show-file-list="false"
                                    :data="{type:1}"
                                    :on-success=" (res,file)=> {handleAvatarSuccess (res,file)}"
                                    :before-upload="beforeAvatarUpload"
                                    >
                                    <img class="avatar" :src="admin.avatar" alt="头像">
                                    </el-upload>
                                </template>
                                <span>{{admin.name}}</span>
                            </div>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
                                <el-dropdown-item>修改密码</el-dropdown-item>
                                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </el-header>
                <!-- 主体 -->
                <el-main>
                    <router-view @update:admin="updateAdmin"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
export default {
    data(){
        return{
            isCollapse: false,      //不收缩
            asideWith: '200px',
            collapseIcon: 'el-icon-s-fold',
            fullScreen: false,
            admin: JSON.parse(localStorage.getItem('admin') || '{}'),
            items:[
                {
                    icon: 'el-icon-house',
                    index: '/home',
                    title: '系统首页'
                },
                {
                    icon: 'el-icon-user',
                    index: '/consumer',
                    title: '用户管理'
                },
                {
                    icon: 'el-icon-house',
                    index: '/singer',
                    title: '歌手管理'
                },
                {
                    icon: 'el-icon-house',
                    index: '/songList',
                    title: '歌单管理'
                },
            ],
            Url:{           
                downUrl: '',    //下载预览地址
                uploadUrl: ''   //本地存储地址
            },
        }
    },
    methods: {
        updateAdmin(admin) {    //获取子组件传过来的数据，更新当前页面的数据
            this.admin = JSON.parse(JSON.stringify(admin))  //让父级的对象跟子级的对象毫无关联
        },
        logout() {
            localStorage.removeItem('admin')
            this.$router.push('/login')
        },
        // 全屏事件 还有bug,Esc取消后就会出现
        handleFull() {
            let element = document.documentElement
            if(this.fullScreen) {
                if(document.exitFullscreen){
                    document.exitFullscreen()
                }else if(document.webkitCancelFullScreen){      //sofair,chrome
                    document.webkitCancelFullScreen()
                }else if (document.mozCancelFullScreen) {       //firefox
                    document.mozCancelFullScreen()
                } else if (document.msExitFullscreen) {         //ie
                    document.msExitFullscreen()
                }
            } else {
                if (element.requestFullscreen){
                    element.requestFullscreen()
                }else if (element.webkitRequestFullScreen){     //sofair,chrome
                    element.webkitRequestFullScreen()
                }else if (element.mozRequestFullScreen){        //firefox
                    element.mozRequestFullScreen()
                }else if (element.msRequestFullscreen){         //ie
                    element.msRequestFullscreen()
                }
            }
            this.fullScreen = !this.fullScreen
            // document.documentElement.requestFullscreen()
        },
        // 侧边栏收缩事件
        handleCollapse() {
            this.isCollapse = !this.isCollapse
            this.asideWith = this.isCollapse ? '64px' : '200px'
            this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
        },
         // 管理员头像上传成功后的事件
         handleAvatarSuccess(res, file ) {
            if (res.code === '401') {
                this.$router.push('/login')
            }else {
                this.Url.downUrl = res.data.downUrl
                this.Url.uploadUrl = res.data.uploadUrl
                this.$request.put('/admin/updateAvatar',null, {
                    params: {
                        downUrl: this.Url.downUrl,
                        uploadUrl: this.Url.uploadUrl,
                        id: this.admin.id,
                    }
                }).then(res => {
                    if (res.code === '200') {           //表示保存成功
                        this.$message.success('文件上传成功')
                        this.admin.avatar = this.Url.downUrl,
                        this.admin.avatarLocal = this.Url.uploadUrl,
                        localStorage.setItem('admin',JSON.stringify(this.admin))
                    } else {
                        this.$message.error('文件上传失败' + res.msg)    //弹出错误信息
                    }
                })
            }
        },
        // 上传歌曲头像文件设置
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const size = file.size / 1024 / 1024 < 10;
            if (!isJPG) {
                this.$message.error('上传图片只能是 JPG 格式!');
            }
            if (!size) {
                this.$message.error('上传图片大小不能超过 10MB!');
            }
            return isJPG && size;
        },
    }
  }
</script>

<style scoped>
    .el-aside {
        min-height: 100vh;
        background-color: #001529;
        transition: .3s;
        box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
    }
    .aside-logo {
        height: 60px;
        color: blue;
        background-color: #f6f6f6;
        display: flex; 
        align-items:center;
        justify-content:center
    }
    /* style="width: 40px;height: 40px; */
    .aside-logo>img {
        width: 40px;
        height: 40px;
    }
    .el-header {
        box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
        display: flex;
        align-items: center;
    }
    .avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin: 0 5px;
    }
</style>
