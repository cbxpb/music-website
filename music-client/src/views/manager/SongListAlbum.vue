<!-- 歌单详情页面 -->
<template>
    <div>
        <!-- 歌单详情 -->
        <div class="song-list-album">
            <!-- 版心 -->
            <div class="container">
                <!-- 上边 -->
                <div class="top">
                    <!-- 歌单图片 -->
                    <template v-if="!tempItem.pic">
                        <img src="@/assets/img/songList_pic.png" alt="歌单图片">
                    </template>
                    <template v-else>
                        <img :src="tempItem.pic" alt="歌单图片">
                    </template>
                    <!-- 歌单信息 -->
                    <div class="song-list-info">
                        <div class="title">{{ tempItem.title }}</div>
                        <div class="style"><span>风格：</span>{{ tempItem.style }}</div>
                        <div class="introduction">
                            <span>简介：</span>
                            <div>{{ tempItem.introduction }}</div>
                        </div>
                    </div>
                </div>
                <!-- 下边 -->
                <div class="bottom">
                    <!-- 歌曲展示 -->
                    <template v-if="!songs.length">
                        <h1>歌单暂无歌曲！</h1>
                    </template>
                    <template v-else>
                        <div class="total">共：{{ songs.length }}首</div>
                        <div class="songs-show">
                            <ul>
                                <li>
                                    <div class="song-item">
                                        <span class="item-index">序号</span>
                                        <span class="item-title">歌曲</span>
                                        <span class="item-name">歌手</span>
                                        <span class="item-album">专辑</span>
                                    </div>
                                </li>
                                <li v-for="(item,index) in songList" :key="item.id" :index="index" :class="{ select: item.id === $store.getters.id }">
                                    <div class="song-item" @click="toplay(item.id,item.url,item.name,item.pic,item.lyricLocal,index)">
                                        <span class="item-index">{{index+1}}</span>
                                        <span class="item-title">{{item.name.split('-')[0]}}</span>
                                        <span class="item-name">{{item.name.split('-')[1]}}</span>
                                        <span class="item-album">{{item.album}}</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </template>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
    name: 'SongListAlbum',
    computed:{
        ...mapGetters([
            'tempItem',      // 用vueX获取当前歌单的信息，减少一次查询
            'id'             //歌曲id
        ]),
    },
    data() {
        return {
            // 歌单id
            songListId: '',
            // 歌单对应的所有歌曲信息
            songs:[],
            // 将songs转化为歌曲列表songList
            songList: []
        }
    },
    created() {
        // 获取歌单id
        this.songListId = this.$route.params.id
        // 获取歌单歌曲
        this.getSongs()
    },
    methods: {
        // 获取歌单对应歌曲
        getSongs() {
            this.$request.get('/listSong/selectBySongListId', {
                params: {
                    songListId: this.songListId
                }
            }).then(res => {
                if (res.code === '200') {
                    this.songs = res.data
                    this.getSongList()
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 将songs转化为歌单列表songList,以便其他组件使用
        getSongList() {
            for(let i= 0; i<this.songs.length; i++) {
                this.songList.push(this.songs[i].song)
            }
        },
        //播放歌曲
        toplay(id,url,name,pic,lyric,index) {
            this.$store.commit('setId',id)
            this.$store.commit('setUrl',url)
            this.$store.commit('setName',name)
            this.$store.commit('setPic',pic)
            this.$store.commit('setLyric',lyric)
            this.$store.commit('setIndexOfSong',index)
            this.$store.commit('setIsPlay',true)
            this.$store.commit('setSongList',this.songList)
        }
    }
}
</script>

<style scoped>
    /* #region歌单详情区域 */

    .song-list-album {
        min-height: calc(100vh - 182px);
        margin-top: 40px;
        margin-bottom: 62px;
        padding: 10px 0;
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
    }

    .song-list-album .container .top{
        display: flex;
        align-items: center;
    }

    .song-list-album .container .top img {
        width: 250px;
        height: 250px;
        margin-right: 40px;
    }

    .song-list-album .container .top .song-list-info span {
        font-weight: bolder;
    }

    .song-list-album .container .top .song-list-info .title{
        padding: 10px 0;
        font-size: 31px;
    }

    .song-list-album .container .top .song-list-info .style {
        font-size: 14px;
        padding-bottom: 10px;
    }

    .song-list-album .container .top .song-list-info .introduction {
        font-size: 14px;
        display: flex;
        flex-wrap: wrap;
    }

    .song-list-album .container .bottom {
        padding: 40px 0;
    }

    .bottom .songs-show {
        background-color: #fff;
    }

    .bottom .songs-show .song-item {
        padding:20px;
        display: flex;
        font-size: 16px;
        cursor: pointer;
        align-items: center;
    }

    .bottom .songs-show ul>li:nth-child(2n) {
        background-color: #f7f7f7;
    }

    .bottom .songs-show ul>li.select,
    .bottom .songs-show ul>li:not(:first-child):hover {
        background-color: #31c27c88;;
    }

    .bottom .songs-show .song-item .item-index {
        width: 5%;
        margin-right: 10px;
        font-weight: lighter;
        opacity: 0.8;
    }

    .bottom .songs-show .song-item .item-title {
        width: 30%;
        margin-right: 30px;
        overflow: hidden;
    }

    .bottom .songs-show .song-item .item-name {
        width: 25%;
        margin-right: 30px;
        overflow: hidden;
    }

    .bottom .songs-show .song-item .item-album {
        width: 40%;
        overflow: hidden;
    }

    .total {
        font-size: 12px;
        padding: 20px;
    }

    /* #endregion歌单详情区域 */

</style>