<!-- 页面主体框架部分 -->
<template>
    <div>
        <!-- 头部区域 -->
        <header class="header">
            <!-- 版心 -->
            <div class="container">
                <!-- 左侧的logo区 -->
                <div class="logo">
                    <svg class="icon" aria-hidden="true">
                        <use xlink:href="#icon-erji"></use>
                    </svg>
                    <span>music</span>
                </div>
                <!-- 中间的导航区 -->
                <div class="header-nav">
                    <ul>
                        <li :class="{ activeName: item.name === $route.meta.name }" v-for="item in navMsg" :key="item.name" @click="toPage(item.path)">{{ item.name }}</li>
                    </ul>
                </div>
                <!-- 中间的搜索区 -->
                <div class="search">
                    <el-input v-model="keywords" placeholder="搜索音乐、歌单、歌手" clearable @keyup.enter.native="toSearch()"></el-input>
                    <el-button type="primary" icon="el-icon-search" @click="toSearch()">搜索</el-button>
                </div>
                <!-- 右侧的登录区 -->
                <div class="header-login">
                    <el-dropdown placement="bottom">
                        <div class="consumer-avatar">
                            <template v-if="!consumer.avatar">
                                <el-upload action="http://localhost:9090/file/upload"
                                    :headers="{token: consumer.token}"
                                    :show-file-list="false"
                                    :data="{type:2}"
                                    :on-success=" (res,file) => {handleAvatarSuccess (res,file)}"
                                    :before-upload="beforeAvatarUpload"
                                >
                                <img class="avatar" src="@/assets/img/consumer_avatar.png" alt="头像">
                                </el-upload>
                            </template>
                            <template v-else>
                                <el-upload action="http://localhost:9090/file/upload"
                                    :headers="{token: consumer.token}"
                                    :show-file-list="false"
                                    :data="{type:2}"
                                    :on-success=" (res,file) => {handleAvatarSuccess (res,file)}"
                                    :before-upload="beforeAvatarUpload"
                                >
                                <img class="avatar" :src="consumer.avatar" alt="头像">
                                </el-upload>
                            </template>
                            <span>{{consumer.name}}</span>
                        </div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
                            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
                            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>
        </header>

        <!-- 主体内容 -->
        <main>
            <router-view :getKeywords="getKeywords" @update:consumer="updateConsumer"/>
        </main>

        <!-- 底部区域 -->
        <footer>
            <!-- 播放器 -->
            <SongAudio></SongAudio>
            <!-- 回到顶部 -->
            <ScrollTop></ScrollTop>
        </footer>
    </div>
</template>

<script>
import ScrollTop from '@/components/ScrollTop.vue'
import SongAudio from '@/components/SongAudio.vue'

export default {
    name: "Manager",
    components: {
        ScrollTop,
        SongAudio
    },
    data(){
        return{
            // 导航栏信息
            navMsg: [
                { name: '首页', path: '/'},
                { name: '歌单', path: '/song-list'},
                { name: '歌手', path: '/singer'},
                { name: '我的音乐', path: '/my-music'},
            ],
            // 搜索关键字
            keywords: '',
            consumer: JSON.parse(localStorage.getItem('consumer') || '{}'),
            Url:{           
                downUrl: '',    //下载预览地址
                uploadUrl: ''   //本地存储地址
            },
        }
    },
    methods:{
        // 更新用户数据
        updateConsumer(consumer) {    //获取子组件传过来的数据，更新当前页面的数据
            this.consumer = JSON.parse(JSON.stringify(consumer))  //让父级的对象跟子级的对象毫无关联
        },
        // 退出登录
        logout() {
            localStorage.removeItem('consumer')
            this.$router.push('/login')
        },
        // 接收子组件的keywords
        getKeywords(keywords) {
            this.keywords = keywords
        },
        // 点击导航栏跳转到指定页面
        toPage(path) {
            this.keywords = ''
            this.$router.push({path: path})
        },
        // 跳转到搜索页面
        toSearch() {
            this.$router.push({path:'/search',query:{keywords: this.keywords}})
        },
         // 用户头像上传成功后的事件
         handleAvatarSuccess(res, file ) {
            if (res.code === '401') {
                this.$router.push('/login')
            }else {
                this.Url.downUrl = res.data.downUrl
                this.Url.uploadUrl = res.data.uploadUrl
                this.$request.put('/consumer/updateAvatar',null, {
                    params: {
                        downUrl: this.Url.downUrl,
                        uploadUrl: this.Url.uploadUrl,
                        id: this.consumer.id,
                    }
                }).then(res => {
                    if (res.code === '200') {           //表示保存成功
                        this.$message.success('头像上传成功')
                        this.consumer.avatar = this.Url.downUrl,
                        this.consumer.avatarLocal = this.Url.uploadUrl,
                        localStorage.setItem('consumer',JSON.stringify(this.consumer))
                    } else {
                        this.$message.error('头像上传失败' + res.msg)    //弹出错误信息
                    }
                })
            }
        },
        // 用户头像设置
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
    },
};
</script>

<style scoped>

    /* #region页面头部区域 */

    .header {
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
    }

    .header .container {
        height: 80px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .header .logo {
        display: flex;
        align-items: center;
    }

    .header .logo svg {
        width: 60px;
        height: 60px
    }

    .header .logo span {
        font-size: 30px;
        margin-left: 20px;
    }

    .header .header-nav ul {
        display: flex;
        width: 370px;
        justify-content: space-between;
        align-items: center;
    }

    .header .header-nav ul li {
        font-size: 25px;
        cursor: pointer;
    }

    .header .header-nav ul .activeName {
        font-size: 27px;
        color: skyblue;
    }

    .header .header-nav ul li:hover {
        font-size: 27px;
        color: skyblue;
        transition:.1s
    }

    .header .search {
        display: flex;
        align-items: center;
    }

    .header .search .el-input--small {
        margin-right: 5px;
    }

    .header .header-login {
        cursor: pointer;
    }

    .header .header-login .consumer-avatar {
        display: flex;
        align-items: center;
    }

    .header .header-login .consumer-avatar img.avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin-right: 5px;
        transition: .3s;
    }

    .header .header-login .consumer-avatar img.avatar:hover {
        box-shadow: 0px 0px 10px #3333;
    }
 
    /* #endregion页面头部区域 */
    
</style>