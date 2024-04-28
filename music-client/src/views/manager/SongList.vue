<!-- 歌单页面 -->
<template>
    <div>
        <!-- 歌单列表 -->
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
                        </ul>
                    </div>  
                </div>
                <!-- 歌单列表显示 -->
                <div class="songList-show">
                    <ul>
                        <li v-for="item in tempData" :key="item.id">
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
                <!-- 分页插件 -->
                <div class="pageNation">
                    <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page="pageNum"
                        :page-size="pageSize"
                        layout="total, prev, pager, next"
                        :total="songList.length">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default{
    name: 'SongList',
    data(){
        return{
            // 歌单导航栏信息
            AllStyle: [],
            // 活跃状态的导航栏
            activeStyle: '全部',
            // 歌单列表
            songList: [],
            // 歌单对应歌曲
            songs:[],
            songsList:[],
            // 用于翻页
            pageNum: 1,         //当前页码
            pageSize: 15,        //每页显示的条数
        }
    },
    computed: {
        // 分页后的数据,这里是前端分页实现
        tempData() {
            return this.songList.slice((this.pageNum - 1)*this.pageSize, this.pageNum*this.pageSize)
        }
    },
    created() {
        // 获取数据库中歌单的所有风格
        this.getAllStyle()
        // 获取对应风格的歌单列表
        this.getSongList()
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
            // 重置页数为1
            this.pageNum = 1
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
                    this.songList = res.data
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 跳转到歌单详情页面
        toAlbumPage(item) {
            this.$store.commit("setTempItem",item)
            this.$router.push({path:`song-list-album/${item.id}`})
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum
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
            for(let i=0; i<this.songs.length; i++) {
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
        },
    }
}
</script>

<style scoped>

    /* #region首页歌单列表区域 */

    .songList {
        min-height: calc(100vh - 182px);
        margin-top: 40px;
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
        margin-bottom: 62px;
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

</style>