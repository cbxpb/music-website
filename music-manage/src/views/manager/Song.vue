<!-- 歌手对应的歌曲管理页面 -->
<template>
    <div>
        <!-- 搜索模块 -->
        <div>
            <el-input v-model="name" style="width: 200px;" placeholder="歌曲模糊查询"></el-input>
            <el-input v-model="album" style="width: 200px; margin: 0 5px;" placeholder="专辑模糊查询"></el-input>
            <el-button type="primary" @click="load(1)">查询</el-button>
            <el-button type="info" @click="reset">重置</el-button>
        </div>
        <!-- 功能模块 -->
        <div  style="margin: 10px 0;">
            <el-button type="primary" @click="handleAdd">添加歌曲</el-button>
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <!-- 表格数据 -->
        <el-table ref="table" :data="tableData" @selection-change="handleSelectionChange" :row-key="getRowKey" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}">
            <el-table-column type="selection" :reserve-selection="true" width="55" align="center"></el-table-column>
            <el-table-column label="序号" type="index" :index="indexMethod" width="55" align="center"></el-table-column>
            <el-table-column label="图片"  align="center" width="150">
                <template v-slot="scope">
                    <template v-if="!scope.row.pic">
                        <img src="@/assets/img/admin_avatar.png" alt="歌曲图片" width="90px" height="90px">
                    </template>
                    <template v-else>
                        <img :src="scope.row.pic" alt="歌曲图片" width="90px" height="90px">
                    </template>
                    <el-upload action="http://localhost:9090/file/upload"
                        :headers="{token: admin.token}"
                        :data="{type:4}"
                        :show-file-list="false"
                        :on-success=" (res,file)=> {handleSuccess (scope.row.id,type=4,res,file)}"
                        :before-upload="beforePicUpload"
                    >
                        <el-button size="mini" type="primary">歌曲图片上传</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="歌曲-歌手" prop="name" align="center"></el-table-column>
            <el-table-column label="专辑" prop="album" align="center"></el-table-column>
            <el-table-column label="歌曲上传" align="center" width="300" prop="url">
                <template v-slot="scope">
                    <div>
                        <audio controls :src="scope.row.url"></audio>
                    </div>
                    <el-upload action="http://localhost:9090/file/upload"
                        :headers="{token: admin.token}"
                        :data="{type:6}"
                        :show-file-list="false"
                        :on-success=" (res,file)=> {handleSuccess (scope.row.id,type=6,res,file)}"
                        :before-upload="beforeUrlUpload"
                    >
                        <el-button size="mini" type="primary">歌曲上传</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="歌词" align="center" width="300">
                <template v-slot="scope">
                    <div v-if="!scope.row.lyric" style="margin-bottom: 5px">
                        还没有上传歌词,请上传歌词
                    </div>
                    <div v-else style="margin-bottom: 5px">
                        <el-button size="mini" type="primary" @click="parseLyric(scope.row.lyricLocal,scope.row.name)">查看歌词</el-button>
                    </div>
                    <el-upload action="http://localhost:9090/file/upload"
                        :headers="{token: admin.token}"
                        :data="{type:7}"
                        :show-file-list="false"
                        :on-success=" (res,file)=> {handleSuccess (scope.row.id,type = 7,res,file)}"
                        :before-upload="beforeLrcUpload"
                    >
                        <el-button size="mini" type="primary">歌词上传</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template v-slot="scope">
                    <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
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
        <!-- 新增歌手模态框 -->
        <el-dialog :title="title" :visible.sync="fromVisible" center width="30%">
            <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
              <el-form-item label="歌曲名" prop="name">
                <el-input v-model="form.name" placeholder="歌曲名" clearable></el-input>
              </el-form-item>
              <el-form-item label="专辑名">
                <el-input v-model="form.album" placeholder="专辑名" clearable></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="fromVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveForm">确 定</el-button>
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
    name: "Song",
    data() {
        return {
            singerId: '',       //singer页面传递的歌手id
            singerName: '',     //singer页面传递的歌手姓名
            name: '',           //根据姓名模糊查询
            album: '',          //根据专辑模糊查询
            pageNum: 1,         //当前页码
            pageSize: 4,        //每页显示的条数
            total: 0,           //查询总条数
            tableData: [],      //所有的数据
            fromVisible: false, //歌曲模态框状态
            lrcVisible: false,  //歌词模态框状态
            title:'',           //模态框名字
            form:{},            //歌曲的信息
            rules:{
                name: [
                    { required: true, message: '请输入歌曲名', trigger: 'blur' },
                ],
            },
            admin: JSON.parse(localStorage.getItem('admin')||{}),   //获取本地token
            Url:{           
                downUrl: '',    //下载预览地址
                uploadUrl: ''   //本地存储地址
            },
            lyric:[],
            ids: []
        }   
    },
    created() {
        this.singerId = this.$route.query.id        //singer页面传递的歌手id
        this.singerName = this.$route.query.name    //singer页面传递的歌手姓名
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
            this.$request.get('/song/selectByPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                    album: this.album,
                    singerId: this.singerId
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
        //添加歌曲按钮
        handleAdd() {  
            this.title = '添加歌曲'       
            this.resetRuleForm(),        //清除表单校验
            this.form = {}               //新增数据的时候清空数据
            this.fromVisible = true      //打开弹窗
        },
        //编辑歌曲按钮
        handleEdit(row) {   
            this.title = '编辑歌曲'    
            this.resetRuleForm()
            this.form = JSON.parse(JSON.stringify(row))
            this.form.name = this.form.name.split('-')[0]
            this.fromVisible = true
        },
        //存储表单信息
        saveForm() { 
            this.$refs['formRef'].validate((valid) => {
                if (valid) {
                    this.form.singerId = this.singerId
                    this.form.name = this.form.name + '-' + this.singerName
                    this.$request({
                        url: this.form.id ? '/song/update' : '/song/add',
                        method: this.form.id ? 'PUT' : 'POST',
                        data: this.form
                    }).then(res => {
                        if (res.code === '200') {           //表示保存成功
                            this.$message.success('保存成功')
                            this.fromVisible = false
                            this.load(1)
                        } else {
                            this.$message.error(res.msg)    //弹出错误信息
                            this.fromVisible = false
                            this.load(this.page)
                        }
                    })
                }
            })
        },
        // 重置表格
        reset() {
            this.name = '',
            this.album = '',
            this.load(1)
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.load(pageNum)
        },
        // 自定义索引
        indexMethod(index) {
            return (this.pageNum - 1)*this.pageSize+index+1
        },
        // 重置表单验证
        resetRuleForm() {
            this.$nextTick(()=>{
                this.$refs['formRef'].clearValidate();
            })
        },
        // 文件上传成功后的事件
        handleSuccess(id, type, res, file ){
            if (res.code === '401') {
                this.$router.push('/login')
            }else {
                this.Url.downUrl = res.data.downUrl
                this.Url.uploadUrl = res.data.uploadUrl
                this.$request.put('/song/updateFile',null, {
                    params: {
                        downUrl: this.Url.downUrl,
                        uploadUrl: this.Url.uploadUrl,
                        id: id,
                        type:type
                    }
                }).then(res => {
                    if (res.code === '200') {           //表示保存成功
                        this.$message.success('文件上传成功')
                        this.load(this.page)
                    } else {
                        this.$message.error('文件上传失败' + res.msg)    //弹出错误信息
                        this.load(this.page)
                    }
                })
            }
        },
        // 上传歌曲文件设置
        beforeUrlUpload(file) {
            const isMP3 = file.type === 'audio/mpeg';
            if (!isMP3) {
                this.$message.error('歌曲只能是 MP3 格式!');
            }
            return isMP3;
        },
        // 上传歌词文件设置
        beforeLrcUpload(file) {
            const suffix = file.name.substring(file.name.lastIndexOf(".")+1)
            const isLrc = suffix === 'lrc';
            if (!isLrc) {
                this.$message.error('歌曲只能是 lrc 格式!');
            }
            return isLrc;
        },
        // 上传歌曲头像文件设置
        beforePicUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const size = file.size / 1024 / 1024 < 10;
            if (!isJPG) {
            this.$message.error('上传图片只能是 JPG 格式!');
            }
            if (!size) {
            this.$message.error('上传图片大小不能超过 10MB!');
            }
            return isJPG && size;
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
        // 删除
        del(id) {
            this.$confirm('您确认删除吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/song/delete/' + id).then(res => {
                    if (res.code === '200') {
                        this.$message.success('删除成功')
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                    }
                })
            }).catch(() => {})
        },
        // 批量删除
        delBatch() {
            if (!this.ids.length) {
                this.$message.warning('请选择数据')
                return
            }
            this.$confirm('您确认批量删除这些数据吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/song/delete/batch', {data:this.ids}).then(res => {
                    if (res.code === '200') {
                        this.$message.success('删除成功')
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
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