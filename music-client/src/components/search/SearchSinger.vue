<!-- 搜索歌手页面 -->
<template>
    <div>
        <!-- 歌手展示 -->
        <template v-if="!singerList.length">
            <h1>系统暂无符合条件的歌手</h1>
        </template>
        <template v-else>
            <div class="singers-show">
                <ul>
                    <li>
                        <div class="singer-item">
                            <span class="item-index">序号</span>
                            <span class="item-name">歌手</span>
                            <span class="item-gender">性别</span>
                            <span class="item-location">地区</span>
                        </div>
                    </li>
                    <li v-for="(item,index) in singerList" :key="item.id" :index="index">
                        <div class="singer-item" @click="toAlbumPage(item)">
                            <span class="item-index">{{index+1}}</span>
                            <!-- 歌手图片和名字 -->
                            <div class="item-name">
                                <template v-if="!item.pic">
                                    <img src="@/assets/img/consumer_avatar.png" alt="歌手图片">
                                </template>
                                <template v-else>
                                    <img :src="item.pic" alt="歌手图片">
                                </template>
                                <span>{{item.name}}</span>
                            </div>
                            <span class="item-gender">{{item.gender}}</span>
                            <span class="item-location">{{item.location}}</span>
                        </div>
                    </li>
                </ul>
            </div>
        </template>
    </div>
</template>

<script>
export default {
    name: 'SearchSinger',
    data () {
        return {
           singerList:[],
        }
    },
    props:['keywords'],
    watch:{
        keywords: {
            handler() {
                this.getSinger()
            }
        }
    },
    mounted () {
        this.getSinger()
    },
    methods: {
        //搜索歌手数据
        getSinger() {
            if (!this.keywords) {
                this.$message.error("您输入的内容为空！")
            }else {
                this.$request.get('/singer/selectByName', {
                    params: {
                        name: this.keywords,
                    }
                }).then(res => {
                    if (res.code === '200') {
                        if (!res.data.length) {
                            this.$message.error("系统暂无符合条件的歌手")
                            this.singerList = res.data
                        }else {
                            this.singerList = res.data
                        }
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            }
        },
        // 跳转到歌手详情页面
        toAlbumPage(item) {
            this.$store.commit("setTempItem",item)
            this.$router.push({path:`singer-album/${item.id}`})
        }
    }
}
</script>

<style scoped>

    .singers-show .singer-item {
        padding:20px;
        display: flex;
        font-size: 16px;
        cursor: pointer;
        align-items: center;
    }

    .singers-show ul>li:nth-child(2n+1) {
        background-color: #f7f7f7;
    }

    .singers-show ul>li:not(:first-child):hover {
        background-color: #31c27c88;;
    }

    .singers-show .singer-item .item-index {
        width: 5%;
        margin-right: 10px;
        font-weight: lighter;
        opacity: 0.8;
    }

    .singers-show .singer-item .item-name {
        margin-right: 30px;
        width: 30%;
        overflow: hidden;
    }

    .singers-show ul>li:not(:first-child) .item-name {
        display: flex;
        align-items: center;
    }

    .singers-show .singer-item .item-name img {
        width: 60px;
        height: 60px;
        margin-right: 20px;
        border-radius: 50%;
    }

    .singers-show .singer-item .item-gender {
        width: 25%;
        margin-right: 30px;
        overflow: hidden;
    }

    .singers-show .singer-item .item-location {
        width: 40%;
        overflow: hidden;
    }

</style>