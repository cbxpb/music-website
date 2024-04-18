<!-- 页面主体框架部分 -->
<template>
    <div>
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
                        <li v-for="item in navMsg" :key="item.name" @click="toPage(item.path, item.name)">{{ item.name }}</li>
                    </ul>
                </div>
                <!-- 中间的搜索区 -->
                <div class="search">
                    <el-input v-model="ser" placeholder="搜索"></el-input>
                    <el-input style="width: 200px" v-model="name" placeholder="歌曲模糊查询"></el-input>
                    <el-button type="primary" @click="load(1)">查询</el-button>
                </div>
                <!-- 右侧的登录区 -->
                <div class="header-login">
                    头像
                </div>
            </div>
        </header>
        <main>
            <router-view/>
        </main>
    </div>
</template>

<script>
    export default {
        name: "Manager",
        data(){
            return{
                navMsg: [
                    { name: '首页', path: '/'},
                    { name: '歌单', path: '/song-list'},
                    { name: '歌手', path: '/singer'},
                    { name: '我的音乐', path: '/my-music'},
                ]
            }
        },
        methods:{
            toPage(path,name) {
                this.$router.push({path: path})
            },
        },
    };
</script>

<style>
    /* #region页面头部区域 */
    .header {
        background-color: skyblue;
    }

    .header .container {
        height: 120px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .header .logo {
        display: flex;
        align-items: center;
    }

    .header .logo svg {
        width: 80px;
        height: 80px
    }

    .header .logo span {
        font-size: 30px;
        margin-left: 20px;
    }

    .header .header-nav ul {
        display: flex;
    }
    .header .header-nav ul li {
        margin: 0 10px;
        font-size: 25px;
    }
    /* #endregion页面头部区域 */
</style>