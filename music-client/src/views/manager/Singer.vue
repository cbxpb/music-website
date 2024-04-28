<!-- 歌手页面 -->
<template>
    <div>
        <!-- 歌手列表 -->
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
                        </ul>
                    </div>
                    <div class="gender">
                        <span>性别:</span>
                        <ul>
                            <li :class="{ activeStyle: '全部' === activeGender }" @click="getGender('全部')">全部</li>
                            <li :class="{ activeStyle: item == activeGender }" v-for="item in AllGender" :key="item" @click="getGender(item)">{{ item }}</li>
                        </ul>
                    </div>
                </div>
                <!-- 歌手列表显示 -->
                <div class="singerList-show">
                    <ul>
                        <li v-for="item in tempData" :key="item.id">
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
                <!-- 分页插件 -->
                <div class="pageNation">
                    <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page="pageNum"
                        :page-size="pageSize"
                        layout="total, prev, pager, next"
                        :total="singerList.length">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default{
    name: 'Singer',
    data(){
        return{
            // 歌手导航栏信息
            AllLocation: [],
            AllGender: [],
            // 活跃状态的导航栏
            activeLocation: '全部',
            activeGender: '全部',
            // 歌手列表
            singerList: [],
            // 用于翻页
            pageNum: 1,         //当前页码
            pageSize: 15,        //每页显示的条数
        }
    },
    computed: {
        // 分页后的数据,这里是前端分页实现
        tempData() {
            return this.singerList.slice((this.pageNum - 1)*this.pageSize, this.pageNum*this.pageSize)
        }
    },
    created() {
        // 获取数据库中歌手的所有地址
        this.getAllLocation()
        // 获取数据库中歌手的所有性别
        this.getAllGender()
        // 获取对应地址和性别的歌手列表
        this.getSingerList()
    },
    methods:{
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
            // 重置页数为1
            this.pageNum = 1
            this.$request.get('/singer/selectAll', {
                params: {
                    location: this.activeLocation === '全部'? '':this.activeLocation,
                    gender: this.activeGender === '全部'? '':this.activeGender
                }
            }).then(res => {
                if (res.code === '200') {
                    this.singerList = res.data
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
        // 跳转到歌手详情页面
        toAlbumPage(item) {
            this.$store.commit("setTempItem",item)
            this.$router.push({path:`singer-album/${item.id}`})
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum
        },
    }
}
</script>

<style scoped>

    /* #region首页歌手列表区域 */

    .singerList {
        min-height: calc(100vh - 182px);
        margin-top: 40px;
        background-color: #f4f4f4;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
        margin-bottom: 62px;
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