<!-- 首页 -->
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
                    <div class="style">
                        <span>风格:</span>
                        <ul>
                            <li :class="{ activeStyle: '全部' === activeStyle }" @click="getSongList('全部')">全部</li>
                            <li :class="{ activeStyle: item === activeStyle }" v-for="item in AllStyle" :key="item" @click="getSongList(item)">{{ item }}</li>
                            <li @click="toSongListPage()">更多...</li>
                        </ul>
                    </div>  
                </div>
                <!-- 歌单列表显示 -->
                <div class="songList-show">
                    <ul>
                        <li v-for="item in songList" :key="item.id">
                            <div class="songList-show-info" @click="toAlbumPage(item)">
                                <!-- 歌单图片和标题 -->
                                <template v-if="!item.pic">
                                    <img src="@/assets/img/songList_pic.png" alt="歌单图片">
                                </template>
                                <template v-else>
                                    <img :src="item.pic" alt="歌单图片">
                                </template>
                                <span>{{ item.title }}</span>
                                <!-- 歌单上的播放按钮 -->
                                <svg class="icon" aria-hidden="true" @click.stop="toPlay(item.id)">
                                    <use xlink:href="#icon-bofang"></use>
                                </svg>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- 首页歌手列表 -->
        <div class="singerList">
            <!-- 版心 -->
            <div class="container">
                <!-- 歌手列表头部 -->
                <div class="singerList-header">
                    <span>歌手列表</span>
                </div>
                <!-- 歌手列表分类导航栏 -->
                <div class="singerList-nav">
                    <div class="location">
                        <span>地区:</span>
                        <ul>
                            <li :class="{ activeStyle: '全部' === activeLocation }" @click="getLocation('全部')">全部</li>
                            <li :class="{ activeStyle: item == activeLocation }" v-for="item in AllLocation" :key="item" @click="getLocation(item)">{{ item }}</li>
                            <li @click="toSingerListPage()">更多...</li>
                        </ul>
                    </div>
                    <div class="gender">
                        <span>性别:</span>
                        <ul>
                            <li :class="{ activeStyle: '全部' === activeGender }" @click="getGender('全部')">全部</li>
                            <li :class="{ activeStyle: item == activeGender }" v-for="item in AllGender" :key="item" @click="getGender(item)">{{ item }}</li>
                            <li @click="toSingerListPage()">更多...</li>
                        </ul>
                    </div>
                </div>
                <!-- 歌手列表显示 -->
                <div class="singerList-show">
                    <ul>
                        <li v-for="item in singerList" :key="item.id">
                            <div class="singerList-show-info" @click="toAlbumPage(item)">
                                <!-- 歌手图片和标题 -->
                                <template v-if="!item.pic">
                                    <img src="@/assets/img/consumer_avatar.png" alt="歌手图片">
                                </template>
                                <template v-else>
                                    <img :src="item.pic" alt="歌手图片">
                                </template>
                                <span>{{ item.name }}</span>
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
        Swiper,
    },
    data() {
        return{
            // 歌单列表导航栏信息
            AllStyle: [],
            // 歌手列表导航栏信息
            AllLocation: [],
            AllGender: [],
            // 活跃状态的歌单列表导航栏
            activeStyle: '全部',
            // 活跃状态的歌手列表导航栏
            activeLocation: '全部',
            activeGender: '全部',
            // 歌单列表
            songList: [],
            // 歌手列表
            singerList: [],
            // 歌单对应歌曲
            songs:[],
            songsList:[]
        }
    },
    created() {
        // 获取数据库中歌单的所有风格
        this.getAllStyle()
        // 获取数据库中歌手的所有地址
        this.getAllLocation()
        // 获取数据库中歌手的所有性别
        this.getAllGender()
        // 获取对应风格的歌单列表
        this.getSongList()
        // 获取对应地址和性别的歌手列表
        this.getSingerList()
    },
    methods:{
        // 获取数据库中歌单的所有风格
        getAllStyle() {
            this.$request.get('/songList/getAllStyle').then(res => {
                if (res.code === '200') {
                    this.AllStyle = res.data
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 获取对应风格的歌单列表
        getSongList(style) {
            if(style) {
                this.activeStyle = style
            }
            // 获取歌单
            this.$request.get('/songList/selectByStyle', {
                params: {
                    style: this.activeStyle === '全部'? '':this.activeStyle,
                }
            }).then(res => {
                if (res.code === '200') {
                    // 最多显示10条数据
                    this.songList = res.data.slice(0,10)
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 跳转到歌单页面
        toSongListPage() {
            this.$router.push('/song-list')
        },
        // 获取数据库中歌手的所有地址
        getAllLocation() {
            this.$request.get('/singer/getAllLocation').then(res => {
                if (res.code === '200') {
                    this.AllLocation = res.data
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 获取数据库中歌手的所有性别
        getAllGender() {
            this.$request.get('/singer/getAllGender').then(res => {
                if (res.code === '200') {
                    this.AllGender = res.data
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 获取对应地址和性别的歌手列表
        getSingerList() {
            this.$request.get('/singer/selectAll', {
                params: {
                    location: this.activeLocation === '全部'? '':this.activeLocation,
                    gender: this.activeGender === '全部'? '':this.activeGender
                }
            }).then(res => {
                if (res.code === '200') {
                    // 最多显示10条数据
                    this.singerList = res.data.slice(0,10)
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 获取对应的歌手地址
        getLocation(location) {
            this.activeLocation = location
            this.getSingerList()
        },
        // 获取对应的歌手性别
        getGender(gender) {
            this.activeGender = gender
            this.getSingerList()
        },
        // 跳转到歌手页面
        toSingerListPage() {
            this.$router.push('/singer')
        },
        // 跳转到歌手，歌单详情页面
        toAlbumPage(item) {
            this.$store.commit("setTempItem",item)
            if(item.name) {
                this.$router.push({path:`singer-album/${item.id}`})
            }else {
                this.$router.push({path:`song-list-album/${item.id}`})
            }
        },
        // 点击歌单的播放图标播放歌曲
        toPlay(id) {
            this.songs = []
            this.songsList = []
            this.getSongs(id)
        },
        // 获取歌单对应歌曲
        getSongs(id) {
            this.$request.get('/listSong/selectBySongListId', {
                params: {
                    songListId: id
                }
            }).then(res => {
                if (res.code === '200') {
                    this.songs = res.data
                    this.getSongsList()
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 将songs转化为歌单列表songsList,以便其他组件使用
        getSongsList() {
            for(let i= 0; i<this.songs.length; i++) {
                this.songsList.push(this.songs[i].song)
            }
            this.play()
        },
        // 播放
        play(){
            if(this.songs.length){
                this.$store.commit('setId',this.songsList[0].id)
                this.$store.commit('setUrl',this.songsList[0].url)
                this.$store.commit('setName',this.songsList[0].name)
                this.$store.commit('setPic',this.songsList[0].pic)
                this.$store.commit('setLyric',this.songsList[0].lyricLocal)
                this.$store.commit('setIndexOfSong',0)
                this.$store.commit('setIsPlay',true)
                this.$store.commit('setSongList',this.songsList)
            }
        }
    },
}
</script>

<style scoped>

    /* #region首页歌单列表区域 */

    .songList {
        margin-top: 40px;
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
    }

    .songList .songList-header {
        padding: 40px 0;
        text-align: center;
        font-size: 30px;
        letter-spacing: 10px;
    }

    .songList .songList-nav span {
        color: red;
        font-size: 20px;
    }

    .songList .songList-nav .style {
        display: flex;
        align-items: center;
    }

    .songList-nav .style ul {
        display: flex;
        align-items: center;
    }

    .songList-nav .style ul li {
        margin: 0 20px;
        font-size: 14px;
        cursor: pointer;
    }

    .songList .songList-nav ul li.activeStyle {
        color: #31c27c;
        font-size: 16px;
    }

    .songList .songList-nav ul li:hover {
        color: #31c27c;
        font-size: 16px;
        transition:.2s
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
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        position: relative;
    }

    .songList-show-info img {
        width: 200px;
        height: 200px;
        cursor: pointer;
        transition: .2s;
    }

    .songList-show-info img:has(~ svg:hover),
    .songList-show-info img:hover {
        transform: scale(1.1);
        box-shadow: 0px 0px 20px black;
    }

    .songList-show-info span {
        font-size: 16px;
        height: 40px;
        width: 200px;
        text-align: center;
        cursor: pointer;
        overflow: hidden;
    }

    .songList-show-info span:hover {
        color: #31c27c;
    }

    .songList-show-info svg{
        width: 60px;
        height: 60px;
        position: absolute;
        bottom: 65px;
        right: 10px;
        cursor: pointer;
        opacity: 0;
        transition: opacity .2s linear;
    }

    .songList-show-info img:hover~svg {
        opacity: 0.5;
    }
    
    .songList-show-info svg:hover {
        opacity: 1;
    }
    
    /* #endregion首页歌单列表区域 */

    /* #region首页歌手列表区域 */

    .singerList {
        margin-top: 40px;
        margin-bottom: 62px;
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
    }

    .singerList .singerList-header {
        padding: 40px 0;
        text-align: center;
        font-size: 30px;
        letter-spacing: 10px;
    }


    .singerList .singerList-nav span {
        color: red;
        font-size: 20px;
    }

    .singerList .singerList-nav .location {
        padding-bottom: 10px;
    }

    .singerList .singerList-nav .gender,
    .singerList .singerList-nav .location {
        display: flex;
        align-items: center;
    }

    .singerList-nav .gender ul,
    .singerList-nav .location ul {
        display: flex;
        align-items: center;
    }

    .singerList-nav .gender ul li,
    .singerList-nav .location ul li {
        margin: 0 20px;
        font-size: 14px;
        cursor: pointer;
    }

    .singerList .singerList-nav ul li.activeStyle {
        color: #31c27c;
        font-size: 16px;
    }

    .singerList .singerList-nav ul li:hover {
        color: #31c27c;
        font-size: 16px;
        transition:.2s
    }

    .singerList .singerList-show {
        padding-top: 40px;
    }

    .singerList .singerList-show ul {
        display: flex;
        flex-wrap: wrap;
    }

    .singerList .singerList-show ul li{
        margin-right: 50px;
        margin-bottom: 50px;
    }

    .singerList .singerList-show ul li:nth-child(5n) {
        margin-right: 0;
    }

    .singerList-show-info {
        width: 200px;
        height: 250px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
    }
    
    .singerList-show-info img {
        width: 200px;
        height: 200px;
        border-radius: 50%;
        cursor: pointer;
        transition: .2s;
    }

    .singerList-show-info img:hover {
        transform: scale(1.1);
        box-shadow: 0px 0px 20px black;
    }

    .singerList-show-info span {
        font-size: 17px;
        cursor: pointer;
    }

    .singerList-show-info span:hover {
        color: #31c27c;
    }

    /* #endregion首页歌手列表区域 */

</style>