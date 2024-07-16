<!-- 收藏管理页面 -->
<template>
    <div>
        <!-- 搜索模块 -->
        <div>
            <el-input v-model="keywords" style="width: 200px;" placeholder="模糊查询"></el-input>
            <el-button type="primary" @click="getData">查询</el-button>
            <el-button type="info" @click="reset">重置</el-button>
        </div>
        <!-- 功能模块 -->
        <div  style="margin: 10px 0;">
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <!-- 表格数据 -->
        <el-table ref="table" :data="tempData" @selection-change="handleSelectionChange" :row-key="getRowKey" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}">
            <el-table-column type="selection" width="55" :reserve-selection="true" align="center"></el-table-column>
            <el-table-column label="序号" type="index" :index="indexMethod" width="55" align="center"></el-table-column>
            <el-table-column label="图片"  align="center" width="150">
                <template v-slot="scope">
                    <template v-if="!scope.row.pic">
                        <img src="@/assets/img/admin_avatar.png" alt="歌曲图片" width="90px" height="90px">
                    </template>
                    <template v-else>
                        <img :src="scope.row.pic" alt="歌曲图片" width="90px" height="90px">
                    </template>
                </template>
            </el-table-column>
            <el-table-column label="歌曲-歌手" prop="name" align="center"></el-table-column>
            <el-table-column label="专辑" prop="album" align="center"></el-table-column>
            <el-table-column label="歌曲" align="center" width="300" prop="url">
                <template v-slot="scope">
                    <audio controls :src="scope.row.url"></audio>
                </template>
            </el-table-column>
            <el-table-column label="歌词" align="center" width="300">
                <template v-slot="scope">
                    <div v-if="!scope.row.lyricLocal">
                        暂无歌词
                    </div>
                    <div v-else>
                        <el-button size="mini" type="primary" style="margin-bottom: 5px;" @click="parseLyric(scope.row.lyricLocal,scope.row.name)">查看歌词</el-button>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template v-slot="scope">
                    <el-button size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页功能的插件 -->
        <div style="margin: 10px 0;">
            <el-pagination
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="tableData.length">
            </el-pagination>
        </div>
        <!-- 歌词查看模态框 -->
        <el-dialog :title="title" :visible.sync="lrcVisible" center width="50%">
            <div>
                <ul style="height: 500px; overflow: auto; list-style: none; text-align: center;">
                    <li v-for="(item, index) in lyric" :key="index">
                        {{ item }}
                    </li>
                </ul>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "Collect",
    data() {
        return {
            consumerId: '',     //传递的用户id
            keywords: '',       //关键字
            pageNum: 1,         //当前页码
            pageSize: 4,        //每页显示的条数
            tableData: [],      //过滤后的所有的数据
            lrcVisible: false,  //歌词模态框状态
            title:'',           //模态框名字
            lyric:[],
            ids: [],            //批量删除的id数组
            // 收藏的歌曲列表
            collectList: [],
            // 将收藏列表转为具体的歌曲列表
            songList: []
        }   
    },
    computed: {
        // 分页后的数据,这里是前端分页实现
        tempData() {
            return this.tableData.slice((this.pageNum - 1)*this.pageSize, this.pageNum*this.pageSize)
        }
    },
    created() {
        this.consumerId = this.$route.query.id        //consumer页面传递的用户id
        //页面挂载表格数据
        this.getCollect()
    },
    methods: {
        // 获取用户对应收藏
        getCollect() {
            // 重置页数为1
            this.pageNum = 1
            this.collectList = []
            this.songList = []
            this.$request.get('/collect/selectByConsumerId', {
                params: {
                    consumerId: this.consumerId
                }
            }).then(res => {
                if (res.code === '200') {
                    this.collectList = res.data
                    // 转化收藏列表为歌曲列表
                    for(let i=0; i<this.collectList.length; i++) {
                        this.getSongList(this.collectList[i].songId)
                    }
                    this.tableData = this.songList
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 转化收藏列表为歌曲列表
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
        // 过滤数据
        getData() {
            if (this.keywords === '') {
                this.tableData = this.songList
            } else {
                this.tableData = []
                for (let item of this.songList) {
                    if (item.name.includes(this.keywords)) {
                        this.tableData.push(item)
                    }
                }
                this.pageNum = 1
            }
        },
        // 重置表格
        reset() {
            this.keywords = '',
            this.getCollect()
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum
        },
        // 自定义索引
        indexMethod(index) {
            return (this.pageNum - 1)*this.pageSize+index+1
        },
        // 解析歌词文件
        parse (text) {
            let lines = text
            // let pattern = /\[\d{2}:\d{2}(.\d{2,3})*\])/g
            let pattern = /(\[\d{2}:\d{2}.(\d{3}|\d{2})\])|(\[\d{2}:\d{2}])/g
            let result = []
            // 对于歌词格式不对的特殊处理
            if (!(/\[.+\]/.test(text))) {
                return [text]
            }
            for (let item of lines) {
                if (pattern.test(item)) {
                    let value = item.replace(pattern, '') // 存歌词
                    if(value){
                         result.push(value)
                    }
                }
            }
            return result
        },
        //解析歌词
        parseLyric(url,name) {
            this.title = name
            this.$request.get('/song/lrc', {
                params: {
                    url: url,
                }
            }).then(res => {
                if (res.code === '200') {
                    this.lrcVisible = true
                    this.lyric = this.parse(res.data)
                } else {
                    this.$message.error("找不到歌词文件，请重新上传！");
                    this.lrcVisible = false
                }
            })
        },
        // 选中行
        handleSelectionChange(rows) {
            this.ids = rows.map(v => v.id)
        },
        // 取消收藏
        del(id) {
            this.$confirm('您确认取消此收藏吗?','确认取消',{type:"warning"}).then(response => {
                this.$request.delete('/collect/deleteSong',{
                    params: {
                        consumerId: this.consumerId,
                        songId: id
                    }
                }).then(res => {
                    if (res.code === '200') {
                        this.$message.success('歌曲已取消收藏')
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                        this.$router.go(0)
                    } else {
                        this.$message.error(res.msg)
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                        this.$router.go(0)
                    }
                })
            }).catch(() => {})
        },
        // 批量取消收藏
        delBatch() {
            if (!this.ids.length) {
                this.$message.warning('请选择要取消的收藏')
                return
            }
            this.$confirm('您确认取消收藏这些数据吗?','确认取消',{type:"warning"}).then(response => {
                for(let i=0; i<this.ids.length; i++) {
                    this.$request.delete('/collect/deleteSong',{
                        params: {
                            consumerId: this.consumerId,
                            songId: this.ids[i]
                        }
                    }).then(res => {
                        if (res.code === '200') {
                            
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
                this.$message.success('歌曲已取消收藏')
                this.$refs.table.clearSelection();      //清除上次记录的删除选项
                this.$router.go(0)
            }).catch(() => {
                // this.$refs.table.clearSelection();      //清除上次记录的删除选项
                // this.$message.error(res.msg)
                // this.$router.go(0)
            })
        },
        //用于翻页多选记忆
        getRowKey(row) {
            return row.id;
        },
    }
}
</script>