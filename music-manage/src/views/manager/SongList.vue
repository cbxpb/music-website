<!-- 歌单管理页面 -->
<template>
    <div>
        <!-- 搜索模块 -->
        <div>
            <el-input v-model="title" style="width: 200px;" placeholder="歌单名模糊查询"></el-input>
            <el-input v-model="style" style="width: 200px; margin: 0 5px;" placeholder="风格模糊查询"></el-input>
            <el-button type="primary" @click="load(1)">查询</el-button>
            <el-button type="info" @click="reset">重置</el-button>
        </div>
        <!-- 功能模块 -->
        <div  style="margin: 10px 0;">
            <el-button type="primary" @click="handleAdd">添加歌单</el-button>
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <!-- 表格数据 -->
        <el-table ref="table" :data="tableData" @selection-change="handleSelectionChange" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}" :row-key="getRowKey">
            <el-table-column type="selection" :reserve-selection="true" width="55" align="center"></el-table-column>
            <el-table-column label="序号" type="index" :index="indexMethod" width="55" align="center"></el-table-column>
            <el-table-column label="歌单图片"  align="center" width="150">
                <template v-slot="scope">
                    <template v-if="!scope.row.pic">
                        <img src="@/assets/img/songList_pic.png" alt="歌单图片" width="90px" height="90px">
                    </template>
                    <template v-else>
                        <img :src="scope.row.pic" alt="歌单图片" width="90px" height="90px">
                    </template>
                    <el-upload action="http://localhost:9090/file/upload"
                        :headers="{token: admin.token}"
                        :data="{type:5}"
                        :show-file-list="false"
                        :on-success=" (res,file)=> {handlePicSuccess (scope.row.id,res,file)}"
                        :before-upload="beforePicUpload"
                    >
                        <el-button size="mini" type="primary">歌单图片上传</el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="歌单名" prop="title" align="center"></el-table-column>
            <el-table-column label="风格" prop="style" align="center"></el-table-column>
            <el-table-column label="简介" align="center" width="300">
                <template slot-scope="scope">
                    <p style="height: 100px; overflow: auto">{{ scope.row.introduction }}</p>
                </template>
            </el-table-column>
            <el-table-column label="歌曲管理" align="center" width="110">
                <template v-slot="scope">
                    <el-button size="mini" type="primary" @click="listSongEdit(scope.row.id)">歌曲管理</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="300" align="center">
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
        <!-- 新增歌单模态框 -->
        <el-dialog :title="fromVisibleTitle" :visible.sync="fromVisible" center width="30%">
            <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
              <el-form-item label="歌单名" prop="title">
                <el-input v-model="form.title" placeholder="歌单名" clearable></el-input>
              </el-form-item>
              <el-form-item label="风格">
                <el-input v-model="form.style" placeholder="风格" clearable></el-input>
              </el-form-item>
              <el-form-item label="简介">
                <el-input v-model="form.introduction" placeholder="简介" clearable></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="fromVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveForm">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "SongList",
    data() {
        return {
            title: '',              //根据歌单名模糊查询
            style: '',              //根据风格模糊查询
            pageNum: 1,             //当前页码
            pageSize: 4,            //每页显示的条数
            total: 0,               //查询总条数
            tableData: [],          //所有的数据
            fromVisible: false,     //模态框状态
            fromVisibleTitle:'',    //模态框名字
            form:{},                //歌单的信息
            rules:{
                title: [
                    { required: true, message: '请输入歌单名', trigger: 'blur' },
                ],
            },
            admin: JSON.parse(localStorage.getItem('admin')||{}),   //获取本地token
            Url:{           
                downUrl: '',    //下载预览地址
                uploadUrl: ''   //本地存储地址
            },
            ids: []
        }   
    },
    created() {
        //页面挂载表格数据
        this.load()
    },
    methods: {
         //渲染歌曲表格
         load(pageNum) {
            if (pageNum){
                this.pageNum = pageNum
            }
            this.$request.get('/songList/selectByPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    title: this.title,
                    style: this.style,
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
        // 添加歌单按钮
        handleAdd(){  
            this.fromVisibleTitle = '添加歌单'       
            this.resetRuleForm(),           //清除表单校验
            this.form = {}                  //新增数据的时候清空数据
            this.fromVisible = true         //打开弹窗
        },
        //修改歌单按钮
        handleEdit(row){   
            this.fromVisibleTitle = '编辑歌单'    
            this.resetRuleForm()
            this.form = JSON.parse(JSON.stringify(row))
            this.fromVisible = true
        },
        //存储表单信息
        saveForm() { 
            this.$refs['formRef'].validate((valid) => {
                if (valid) {
                    this.$request({
                        url: this.form.id ? '/songList/update' : '/songList/add',
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
            this.title = '',
            this.style = '',
            this.load(1)
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.load(pageNum)
        },
        // 自定义索引
        indexMethod(index) {
            return (this.pageNum - 1)*this.pageSize + index + 1
        },
        // 重置表单验证
        resetRuleForm() {
            this.$nextTick(()=>{
                this.$refs['formRef'].clearValidate();
            })
        },
        //歌单图片上传成功后的事件
        handlePicSuccess(id, res, file ){
            if (res.code === '401') {
                this.$router.push('/login')
            }else {
                this.Url.downUrl = res.data.downUrl
                this.Url.uploadUrl = res.data.uploadUrl
                this.$request.put('/songList/updatePic',null, {
                    params: {
                        downUrl: this.Url.downUrl,
                        uploadUrl: this.Url.uploadUrl,
                        id: id,
                    }
                }).then(res => {
                    if (res.code === '200') {           //表示保存成功
                        this.$message.success('歌单图片上传成功')
                        this.load(1)
                    } else {
                        this.$message.error('歌单图片上传失败' + res.msg)    //弹出错误信息
                        this.load(this.page)
                    }
                })
            }
        },
        // 上传歌单图片设置
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
        // 选中行
        handleSelectionChange(rows) {
            this.ids = rows.map(v => v.id)
        },
        // 删除
        del(id) {
            this.$confirm('您确认删除吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/songList/delete/' + id).then(res => {
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
                this.$request.delete('/songList/delete/batch', {data:this.ids}).then(res => {
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
        // 转向歌单歌曲管理页面
        listSongEdit(id) {
            this.$router.push({path:'/listSong',query:{id}})
        }
    }
}
</script>