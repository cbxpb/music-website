<template>
    <div>
        <!-- 首页轮播图 -->
        <swiper></swiper>
        <!-- 首页歌单列表 -->
        <div class="songList">
            <!-- 版心 -->
            <div class="container">
                <!-- 歌单列表头部 -->
                <div class="songList-header">
                    <span>歌单列表</span>
                </div>
                <!-- 歌单列表分类导航栏 -->
                <div class="songList-nav">
                    <ul>
                        <li :class="{ activeName: item.name == activeName }" v-for="item in navMsg" :key="item.name" @click="getSongList(item.name)">{{ item.name }}</li>
                        <li @click="toSongListPage()">更多...</li>
                    </ul>
                </div>
                <!-- 歌单列表显示 -->
                <div class="songList-show">
                    <ul>
                        <li v-for="(item, index) in list[0].songList" :key="index">
                            <div class="songList-show-info">
                                <!-- 歌单图片和标题 -->
                                <template v-if="!item.pic">
                                    <img src="@/assets/img/consumer_avatar.jpg" alt="歌单图片">
                                </template>
                                <template v-else>
                                    <img :src="item.pic" alt="歌单图片">
                                </template>
                                <span>{{ item.title }}</span>
                                <!-- 歌单上的播放按钮 -->
                                <svg class="icon" aria-hidden="true">
                                    <use xlink:href="#icon-bofang"></use>
                                </svg>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Swiper from '@/components/Swiper.vue'

export default {
    name: 'Home',
    components: {
        Swiper
    },
    data() {
        return{
            // 导航栏信息
            navMsg: [
                { name: '华语' },
                { name: '华语1' },
                { name: '歌手' },
                { name: '我的音乐' },
            ],
            activeName: '华语',
            list: [
                {name: "歌单", songList: [] },
                {name: "歌手", singerList: [] },
            ]
        }
    },
    created() {
        this.getSongList()
    },
    methods:{
        // 获取对应风格的歌单列表
        getSongList(name) {
            if (name){
                this.activeName = name
            }
            // 获取歌单
            this.$request.get('/songList/selectByStyle', {
                params: {
                    style: this.activeName,
                }
            }).then(res => {
                if (res.code === '200') {
                    this.list[0].songList = res.data.slice(0,10)
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 跳转到歌单页面
        toSongListPage() {
            this.$router.push('/song-list')
        }

    },
}
</script>

<style scoped>
    /* #region首页歌单列表区域 */
    .songList {
        background-color: #3333;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
    }

    .songList .songList-header {
        padding: 40px 0;
        text-align: center;
        font-size: 30px;
        letter-spacing: 10px;
    }

    .songList .songList-nav ul {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .songList .songList-nav ul li {
        margin: 0 10px;
        font-size: 14px;
        cursor: pointer;
    }

    .songList .songList-nav ul .activeName {
        color: #31c27c;
        font-size: 16px;
    }

    .songList .songList-nav ul li:hover {
        color: #31c27c;
        font-size: 16px;
        transition:.1s
    }

    .songList .songList-show {
        padding-top: 40px;
    }

    .songList .songList-show ul {
        display: flex;
        flex-wrap: wrap;
    }

    .songList .songList-show ul li{
        margin-right: 50px;
        margin-bottom: 50px;
    }

    .songList .songList-show ul li:nth-child(5n) {
        margin-right: 0;
    }

    .songList-show-info {
        width: 200px;
        height: 250px;
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        overflow: hidden;
        cursor: pointer;
        transition: .2s;
    }
    
    .songList-show-info:hover {
        box-shadow: 0px 0px 20px black;
    }

    .songList-show-info img {
        width: 200px;
        height: 200px;
    }

    .songList-show-info span {
        font-size: 16px;
    }

    .songList .songList-show svg{
        width: 60px;
        height: 60px;
        position: absolute;
        bottom: 65px;
        right: 10px;
        cursor: pointer;
        opacity: 0;
        transition: opacity .2s linear;
    }

    .songList-show-info:hover svg {
        opacity: 0.5;
    }
    
    .songList-show-info svg:hover {
        opacity: 1;
    }



    /* #endregion首页歌单列表区域 */
</style>