<!-- 搜索歌单页面 -->
<template>
    <div>
        <!-- 歌单展示 -->
        <template v-if="!playLists.length">
            <h1>系统暂无符合条件的歌单</h1>
        </template>
        <template v-else>
            <div class="songLists-show">
                <ul>
                    <li>
                        <div class="songList-item">
                            <span class="item-index">序号</span>
                            <span class="item-title">歌单</span>
                            <span class="item-style">风格</span>
                            <span class="item-introduction">介绍</span>
                        </div>
                    </li>
                    <li v-for="(item,index) in playLists" :key="item.id" :index="index">
                        <div class="songList-item" @click="toAlbumPage(item)">
                            <span class="item-index">{{index+1}}</span>
                            <!-- 歌单图片和名字 -->
                            <div class="item-title">
                                <template v-if="!item.pic">
                                    <img src="@/assets/img/songList_pic.png" alt="歌单图片">
                                </template>
                                <template v-else>
                                    <img :src="item.pic" alt="歌单图片">
                                </template>
                                <span>{{item.title}}</span>
                            </div>
                            <span class="item-style">{{item.style}}</span>
                            <span class="item-introduction">{{item.introduction}}</span>
                        </div>
                    </li>
                </ul>
            </div>
        </template>
    </div>
</template>

<script>
export default {
    name: 'SearchSongList',
    data () {
        return {
            playLists:[],
        }
    },
    props:['keywords'],
    watch:{
        keywords: {
            handler() {
                this.getSongList()
            }
        }
    },
    mounted () {
        this.getSongList()
    },
    methods: {
        //搜索歌单数据
        getSongList() {
            if (!this.keywords) {
                this.$message.error("您输入的内容为空！")
            }else {
                this.$request.get('/songList/selectByName', {
                    params: {
                        name: this.keywords,
                    }
                }).then(res => {
                    if (res.code === '200') {
                        if (!res.data.length) {
                            this.playLists = res.data
                            this.$message.error("系统暂无符合条件的歌单")
                        }else {
                            this.playLists = res.data
                        }
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            }
        },
        // 跳转到歌单详情页面
        toAlbumPage(item) {
            this.$store.commit("setTempItem",item)
            this.$router.push({path:`song-list-album/${item.id}`})
        }
    }
}
</script>

<style scoped>

    .songLists-show .songList-item {
        padding:20px;
        display: flex;
        font-size: 16px;
        cursor: pointer;
        align-items: center;
    }

    .songLists-show ul>li:nth-child(2n+1) {
        background-color: #f7f7f7;
    }

    .songLists-show ul>li:not(:first-child):hover {
        background-color: #31c27c88;;
    }

    .songLists-show .songList-item .item-index {
        width: 5%;
        margin-right: 10px;
        font-weight: lighter;
        opacity: 0.8;
    }

    .songLists-show .songList-item .item-title {
        margin-right: 30px;
        width: 30%;
        overflow: hidden;
    }

    .songLists-show ul>li:not(:first-child) .item-title {
        display: flex;
        align-items: center;
    }

    .songLists-show .songList-item .item-title img {
        width: 60px;
        height: 60px;
        margin-right: 20px;
        /* border-radius: 50%; */
    }

    .songLists-show .songList-item .item-style {
        width: 25%;
        margin-right: 30px;
        overflow: hidden;
    }

    .songLists-show .songList-item .item-introduction {
        width: 40%;
        overflow: hidden;
    }

</style>
