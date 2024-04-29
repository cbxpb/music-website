<!-- 歌单歌曲管理页面 -->
<template>
    <div>
        <!-- 搜索模块 -->
        <div>
            <el-input v-model="name" style="width: 200px;" placeholder="歌曲模糊查询"></el-input>
            <el-button type="primary" @click="load(1)">查询</el-button>
            <el-button type="info" @click="reset">重置</el-button>
        </div>
        <!-- 功能模块 -->
        <div  style="margin: 10px 0;">
            <el-button type="primary" @click="addSong()">添加歌曲</el-button>
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <!-- 表格数据 -->
        <el-table ref="tableOne" :data="tableData" @selection-change="handleSelectionChange" :row-key="getRowKey" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}">
            <el-table-column type="selection" width="55" :reserve-selection="true" align="center"></el-table-column>
            <el-table-column label="序号" type="index" :index="indexMethod" width="55" align="center"></el-table-column>
            <el-table-column label="图片"  align="center" width="150">
                <template v-slot="scope">
                    <template v-if="!scope.row.song.pic">
                        <img src="@/assets/img/admin_avatar.png" alt="歌曲图片" width="90px" height="90px">
                    </template>
                    <template v-else>
                        <img :src="scope.row.song.pic" alt="歌曲图片" width="90px" height="90px">
                    </template>
                </template>
            </el-table-column>
            <el-table-column label="歌曲-歌手" prop="song.name" align="center"></el-table-column>
            <el-table-column label="专辑" prop="song.album" align="center"></el-table-column>
            <el-table-column label="歌曲" align="center" width="300" prop="url">
                <template v-slot="scope">
                    <audio controls :src="scope.row.song.url"></audio>
                </template>
            </el-table-column>
            <el-table-column label="歌词" align="center" width="300">
                <template v-slot="scope">
                    <div v-if="!scope.row.song.lyricLocal">
                        暂无歌词
                    </div>
                    <div v-else>
                        <el-button size="mini" type="primary" style="margin-bottom: 5px;" @click="parseLyric(scope.row.song.lyricLocal,scope.row.song.name)">查看歌词</el-button>
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
            :total="total">
            </el-pagination>
        </div>
        <!-- 添加歌曲模态框 -->
        <el-dialog title="添加歌曲" :visible.sync="tableVisible" center width="70%">
            <div>
                <el-input v-model="table.name" style="width: 200px;" placeholder="歌曲模糊查询"></el-input>
                <el-input v-model="table.album" style="width: 200px; margin: 0 5px;" placeholder="专辑模糊查询"></el-input>
                <el-button type="primary" @click="addSong(1)">查询</el-button>
                <el-button type="info" @click="resetTable">重置</el-button>
            </div>
            <div style="margin: 10px 0;">
                <el-button type="primary" @click="addAll">批量添加</el-button>
            </div>
            <el-table :data="table.songData" @selection-change="handleSelectionChangeTable" :row-key="getRowKey" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}" ref="tableTwo">
                <el-table-column type="selection" width="55" :reserve-selection="true" align="center"></el-table-column>
                <el-table-column label="序号" type="index" :index="indexMethodTable" width="55" align="center"></el-table-column>
                <el-table-column label="歌曲-歌手" prop="name" align="center"></el-table-column>
                <el-table-column label="专辑" prop="album" align="center"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template v-slot="scope">
                        <el-button size="mini" type="primary" @click="addOne(scope.row.id)">单个添加</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页功能的插件 -->
            <div style="margin: 10px 0;">
                <el-pagination
                    @current-change="handleCurrentChangeTable"
                    :current-page="table.pageNum"
                    :page-size="table.pageSize"
                    layout="total, prev, pager, next"
                    :total="table.total">
                </el-pagination>
            </div>
        </el-dialog>

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
    name: "ListSong",
    data() {
        return {
            songListId: '',      //歌单页面传递的歌单id
            name: '',            //根据歌名歌手模糊查询
            table:{
                pageNum: 1,     //当前页码
                pageSize: 4,    //每页显示的条数
                total: 0,       //查询总条数
                album: '',
                name: '',
                singerId:'',
                songData:[]        //数据库中歌曲的信息
            },
            title:'',
            tableVisible: false, //模态框状态
            tableData: [],  //所有的数据
            pageNum: 1,     //当前页码
            pageSize: 4,    //每页显示的条数
            total: 0,       //查询总条数
            admin: JSON.parse(localStorage.getItem('admin')||{}),   //获取本地token
            lrcVisible: false,
            lyric:[],
            ids: [],
            songIds:[]
        }   
    },
    created() {
        this.songListId = this.$route.query.id        
        //页面挂载表格数据
        this.load()
    },
    methods: {
         //渲染歌曲表格
        load(pageNum) {
            this.tableData = []
            if (pageNum){
                this.pageNum = pageNum
            }
            this.$request.get('/listSong/selectByPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                    songListId: this.songListId
                }
            }).then(res => {
                if (res.code === '200') {
                    this.tableData = res.data.records
                    this.total = res.data.total
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        //批量添加歌曲
        addAll() {
            if (!this.songIds.length) {
                this.$message.warning('请选择数据')
                return
            }
            var songIdsStr = this.songIds.join();
            console.log(songIdsStr)
            this.$confirm('您确认批量添加这些数据吗?','确认添加',{type:"warning"}).then(response => {
                this.$request.post('/listSong/addAll', null, {
                    params: {
                        songIdsStr: songIdsStr,
                        songListId: this.songListId
                    }
                }).then(res => {
                    if (res.code === '200') {
                        this.$message.success('添加成功')
                        this.tableVisible = false
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)
                        this.tableVisible = false
                        this.load(1)
                    }
                })
            }).catch(() => {})
        },
        //添加单个歌曲
        addOne(songId){
            this.$confirm('您确认添加吗?','确认添加',{type:"warning"}).then(response => {
                this.$request.post('/listSong/addOne', null, {
                    params: {
                        songId: songId,
                        songListId: this.songListId
                    }
                }).then(res => {
                    if (res.code === '200') {           //表示保存成功
                        this.$message.success('保存成功')
                        this.tableVisible = false
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)    //弹出错误信息
                        this.tableVisible = false
                        this.load(1)
                    }
                })
            }).catch(() => {})
        },
        // 添加歌曲按钮
        addSong(pageNum){   
            if (pageNum){
                this.table.pageNum = pageNum
            }else{
                this.songIds = []
                this.table.album = '' 
                this.table.name = ''
                this.$nextTick(()=>{
                    this.$refs.tableTwo.clearSelection();
                })
            }
            this.table.songData = []      //新增数据的时候清空数据
            this.tableVisible = true      //打开弹窗
            this.$request.get('/listSong/selectByAllSongsPage', {
                params: {
                    pageNum: this.table.pageNum,
                    pageSize: this.table.pageSize,
                    name: this.table.name,
                    songListId: this.songListId,
                    album:this.table.album,
                }
            }).then(res => {
                if (res.code === '200') {
                    this.table.songData = res.data.records
                    this.table.total = res.data.total
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 重置表格
        reset() {
            this.name = '',
            this.load(1)
        },
        //重置模态框里的表格
        resetTable() {
            this.table.name = '',
            this.table.album = '',
            this.addSong(1)
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.load(pageNum)
        },
        //改变模态框里的页数
        handleCurrentChangeTable(pageNum) {
            this.addSong(pageNum)
        },
        //自定义模态框里的索引
        indexMethodTable(index) {
            return (this.table.pageNum - 1)*this.table.pageSize+index+1
        },
        //自定义索引
        indexMethod(index) {
            return (this.pageNum - 1) * this.pageSize + index + 1
        },
        // 解析歌词文件
        parse (text) {
            let lines = text
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
        // 批量删除
        delBatch() {
            if (!this.ids.length) {
                this.$message.warning('请选择数据')
                return
            }
            this.$confirm('您确认批量删除这些数据吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/listSong/delete/batch', {data:this.ids}).then(res => {
                    if (res.code === '200') {
                        this.$message.success('删除成功')
                        this.$refs.tableOne.clearSelection();      //清除上次记录的删除选项
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)
                        this.$refs.tableOne.clearSelection();      //清除上次记录的删除选项
                    }
                })
            }).catch(() => {})
        },
        // 选中行
        handleSelectionChange(rows) {
            this.ids = rows.map(v => v.id)
        },
        // 选中模态框里的行
        handleSelectionChangeTable(rows) {
            this.songIds = rows.map(v => v.id)
        },
        // 删除
        del(id) {
            this.$confirm('您确认删除吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/listSong/delete/' + id).then(res => {
                    if (res.code === '200') {
                        this.$message.success('删除成功')
                        this.$refs.tableOne.clearSelection();      //清除上次记录的删除选项
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)
                        this.$refs.tableOne.clearSelection();      //清除上次记录的删除选项
                    }
                })
            }).catch(() => {})
        },
        //用于翻页多选记忆
        getRowKey(row) {
            return row.id;
        },
    }
}
</script>