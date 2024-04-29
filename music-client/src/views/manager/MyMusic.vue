<!-- 我的音乐页面 -->
<template>
    <div>
        <!-- 用户详情 -->
        <div class="my-music">
            <!-- 版心 -->
            <div class="container">
                <!-- 上边 -->
                <div class="top">
                    <!-- 用户图片 -->
                    <template v-if="!consumer.avatar">
                        <img src="@/assets/img/consumer_avatar.png" alt="用户头像">
                    </template>
                    <template v-else>
                        <img :src="consumer.avatar" alt="用户头像">
                    </template>
                    <!-- 用户信息 -->
                    <div class="consumer-info">
                        <div class="name"><span>呢称：</span>{{ consumer.name }}</div>
                        <div class="gender"><span>性别：</span>{{ consumer.gender }}</div>
                        <div class="phone"><span>电话：</span>{{ consumer.phone }}</div>
                        <div class="email"><span>邮箱：</span>{{ consumer.email }}</div>
                        <div class="address"><span>地址：</span>{{ consumer.address }}</div>
                        <div class="birth"><span>出生日期：</span>{{ consumer.birth }}</div>
                        <div class="introduction">
                            <span>个性签名：</span>
                            <div>{{ consumer.introduction }}</div>
                        </div>
                    </div>
                </div>
                <!-- 下边 -->
                <div class="bottom">
                    <div class="title">收藏列表</div>
                    <!-- 收藏歌曲展示 -->
                    <template v-if="!songList.length">
                        <h1>用户暂无歌曲收藏！</h1>
                    </template>
                    <template v-else>
                        <div class="total">共：{{ songList.length }}首</div>
                        <div class="songs-show">
                            <ul>
                                <li>
                                    <div class="song-item">
                                        <span class="item-index">序号</span>
                                        <span class="item-title">歌曲</span>
                                        <span class="item-name">歌手</span>
                                        <span class="item-album">专辑</span>
                                        <div class="item-check"><el-button type="primary" @click="delBatch">取消收藏</el-button></div>
                                    </div>
                                </li>
                                <el-checkbox-group v-model="checkList" class="checkbox">
                                    <li v-for="(item,index) in songList" :key="item.id" :index="index" :class="{ select: item.id === $store.getters.id }">
                                        <div class="song-item" @click="toplay(item.id,item.url,item.name,item.pic,item.lyricLocal,index)">
                                            <span class="item-index">{{index+1}}</span>
                                            <span class="item-title">{{item.name.split('-')[0]}}</span>
                                            <span class="item-name">{{item.name.split('-')[1]}}</span>
                                            <span class="item-album">{{item.album}}</span>
                                            <div class="item-check"><el-checkbox @click.native.stop :label="item.id">取消收藏</el-checkbox></div>
                                        </div>
                                    </li>
                                </el-checkbox-group>
                            </ul>
                        </div>
                    </template>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MyMuisc',
    data() {
        return {
            checkList: [],
            consumer: JSON.parse(localStorage.getItem('consumer') || '{}'),
            // 收藏的歌曲列表
            collectList: [],
            // 将收藏列表转为具体的歌曲列表
            songList: []
        }
    },
    created() {
        // 获取用户对应收藏
        this.getCollect()
    },
    methods: {
        // 获取用户对应收藏
        getCollect() {
            this.$request.get('/collect/selectByConsumerId', {
                params: {
                    consumerId: this.consumer.id
                }
            }).then(res => {
                if (res.code === '200') {
                    this.collectList = res.data
                    // 转化收藏列表为歌曲列表
                    for(let i=0; i<this.collectList.length; i++) {
                        this.getSongList(this.collectList[i].songId)
                    }
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        getSongList(songId) {
            this.$request.get('/song/selectBySongId', {
                params: {
                    songId: songId
                }
            }).then(res => {
                if (res.code === '200') {
                    this.songList.push(res.data)
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 播放歌曲
        toplay(id,url,name,pic,lyric,index) {
            this.$store.commit('setId',id)
            this.$store.commit('setUrl',url)
            this.$store.commit('setName',name)
            this.$store.commit('setPic',pic)
            this.$store.commit('setLyric',lyric)
            this.$store.commit('setIndexOfSong',index)
            this.$store.commit('setIsPlay',true)
            this.$store.commit('setSongList',this.songList)
        },
        // 批量取消收藏
        delBatch() {
            if (!this.checkList.length) {
                this.$message.warning('请选择要取消的收藏歌曲')
                return
            }
            this.$confirm('您确认取消收藏这些数据吗?','确认取消',{type:"warning"}).then(response => {
                for(let i=0; i<this.checkList.length; i++) {
                    this.$request.delete('/collect/deleteSong',{
                        params: {
                            consumerId: this.consumer.id,
                            songId: this.checkList[i]
                        }
                    }).then(res => {
                        if (res.code === '200') {
                            
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
                this.$message.success('歌曲已取消收藏')
                this.$router.go(0)
            }).catch(() => {
            })
        },
    }
}
</script>

<style scoped>

    /* #region我的详情区域 */

    .my-music {
        min-height: calc(100vh - 182px);
        margin-top: 40px;
        margin-bottom: 62px;
        padding: 10px 0;
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
    }

    .my-music .container .top {
        display: flex;
        align-items: center;
    }

    .my-music .container .top img {
        width: 250px;
        height: 250px;
        margin-right: 40px;
        border-radius: 50%;
    }

    .my-music .container .top .consumer-info span {
        font-weight: bolder;
    }

    .my-music .container .top .consumer-info .name{
        padding: 10px 0;
        font-size: 31px;
        color: skyblue;
    }

    .my-music .container .top .consumer-info .gender,
    .my-music .container .top .consumer-info .phone,
    .my-music .container .top .consumer-info .email,
    .my-music .container .top .consumer-info .address,
    .my-music .container .top .consumer-info .birth {
        font-size: 14px;
        padding-bottom: 10px;
        color: skyblue;
    }

    .my-music .container .top .consumer-info .introduction {
        font-size: 14px;
        display: flex;
        flex-wrap: wrap;
        color: skyblue;
    }

    .my-music .container .bottom {
        padding: 40px 0;
    }

    .my-music .container .bottom div.title {
        padding: 20px 0;
        text-align: center;
        font-size: 30px;
        letter-spacing: 10px;
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

    .bottom .songs-show ul .checkbox li:nth-child(2n+1) {
        background-color: #f7f7f7;
    }

    .bottom .songs-show ul .checkbox li.select,
    .bottom .songs-show ul .checkbox li:hover {
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
        width: 30%;
        overflow: hidden;
    }

    .bottom .songs-show .song-item .item-check {
        width: 10%;
        overflow: hidden;
    }

    .total {
        font-size: 12px;
        padding: 20px;
    }

    /* #endregion我的详情区域 */

</style>