<!-- 歌手管理页面 -->
<template>
    <div>
        <!-- 搜索模块 -->
        <div>
            <el-input v-model="name" style="width: 200px;" placeholder="姓名模糊查询"></el-input>
            <el-select v-model="gender" placeholder="请选择性别" clearable style="width: 200px; margin: 0 5px;">
                <el-option value="男"></el-option>
                <el-option value="女"></el-option>
                <el-option value="组合"></el-option>
                <el-option value="不明"></el-option>
            </el-select>
            <el-button type="primary" @click="load(1)">查询</el-button>
            <el-button type="info" @click="reset">重置</el-button>
        </div>
        <!-- 功能模块 -->
        <div  style="margin: 10px 0;">
            <el-button type="primary" @click="handleAdd">添加歌手</el-button>
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <!-- 表格数据 -->
        <el-table ref="table" :data="tableData" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}"  @selection-change="handleSelectionChange" :row-key="getRowKey">
            <el-table-column type="selection" :reserve-selection="true" width="55" align="center"></el-table-column>
            <el-table-column label="序号" type="index" :index="indexMethod" width="55" align="center"></el-table-column>
            <el-table-column label="歌手图片"  align="center" width="150">
                <template v-slot="scope">
                    <template v-if="!scope.row.pic">
                        <img src="@/assets/img/consumer_avatar.jpg" alt="歌手图片" width="100%" height="90px">
                    </template>
                    <template v-else>
                        <img :src="scope.row.pic" alt="歌手图片" width="100%" height="90px">
                    </template>
                    <el-upload action="http://localhost:9090/file/upload"
                        :headers="{token: admin.token}"
                        :data="{type:3}"
                        :show-file-list="false"
                        :on-success=" (res,file)=> {handlePicSuccess (scope.row.id,res,file)}"
                        :before-upload="beforePicUpload"
                    >
                        <el-button size="mini" type="primary">歌手图片上传<i class="el-icon-upload el-icon--right"></i></el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="姓名" prop="name" align="center"></el-table-column>
            <el-table-column label="性别" prop="gender" align="center"></el-table-column>
            <el-table-column label="出生" prop="birth" align="center"></el-table-column>
            <el-table-column label="地区" prop="location" align="center"></el-table-column>
            <el-table-column label="简介" align="center" width="300">
                <template slot-scope="scope">
                    <p style="height: 100px; overflow: auto">{{ scope.row.introduction }}</p>
                </template>
            </el-table-column>
            <el-table-column label="歌曲管理" align="center" width="110">
                <template v-slot="scope">
                    <el-button size="mini" type="primary" @click="songEdit(scope.row.id,scope.row.name)">歌曲管理</el-button>
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
              <el-form-item label="歌手名" prop="name">
                <el-input v-model="form.name" placeholder="歌手名" clearable></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="form.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                    <el-radio label="组合">组合</el-radio>
                    <el-radio label="不明">不明</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="出生日期">
                <el-date-picker v-model="form.birth" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"  placeholder="选择日期" style="width: 50%;" clearable></el-date-picker>
              </el-form-item>
              <el-form-item label="所属地区">
                <el-input v-model="form.location" placeholder="所属地区" clearable></el-input>
              </el-form-item>
              <el-form-item label="歌手简介">
                <el-input v-model="form.introduction" type="textarea" :rows="2" placeholder="请输入内容" clearable></el-input>
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
    name: "Singer",
    data() {
        return {
            name: '',               //根据姓名模糊查询
            gender: '',             //根据性别查询
            pageNum: 1,             //当前页码
            pageSize: 4,            //每页显示的条数
            total: 0,               //总数
            tableData: [],          //所有的数据
            title: '',              //模态框标题
            fromVisible: false,     //模态框标题
            form:{},                //歌手的信息
            admin: JSON.parse(localStorage.getItem('admin')||{}),
            rules:{
                name: [
                    { required: true, message: '请输入歌手姓名', trigger: 'blur' },
                ],
                gender: [
                    { required: true, message: '请选择歌手性别', trigger: 'blur' },
                ],
            },
            Url:{           
                downUrl: '',
                uploadUrl: ''
            },
            ids: []
        }   
    },
    created() {
        //页面挂载表格数据
        this.load()
    },
    methods: {
        //渲染歌手表格
        load(pageNum) {
            if (pageNum){
                this.pageNum = pageNum
            }
            this.$request.get('/singer/selectByPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                    gender:this.gender
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
        //添加歌手按钮
        handleAdd() {
            this.title = '添加歌手'
            this.resetRuleForm()         //清除表单校验
            this.form = {}               //新增数据的时候清空数据
            this.fromVisible = true      //打开弹窗
        },
        //编辑歌手按钮
        handleEdit(row) {
            this.title = '编辑歌手'
            this.resetRuleForm(),
            this.form = JSON.parse(JSON.stringify(row))
            this.fromVisible = true
        },
        //存储歌手信息
        saveForm() {
            this.$refs['formRef'].validate((valid) => {
                if (valid) {
                    this.$request({
                        url: this.form.id ? '/singer/update' : '/singer/add',
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
            this.gender= '',
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
        // 歌手图片上传成功后的事件
        handlePicSuccess(id,res, file ) {
            if (res.code === '401') {
                this.$router.push('/login')
            }else {
                this.Url.downUrl = res.data.downUrl
                this.Url.uploadUrl = res.data.uploadUrl
                this.$request.put('/singer/updatePic',null, {
                    params: {
                        downUrl: this.Url.downUrl,
                        uploadUrl: this.Url.uploadUrl,
                        id: id,
                    }
                }).then(res => {
                    if (res.code === '200') {           //表示保存成功
                        this.$message.success('歌手图片上传成功')
                        this.load(this.page)
                    } else {
                        this.$message.error('歌手图片上传失败' + res.msg)    //弹出错误信息
                        this.load(this.page)
                    }
                })
            }
        },
        // 上传歌曲图片设置
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
            console.log(id)
            this.$confirm('您确认删除吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/singer/delete/' + id).then(res => {
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
            console.log(this.ids)
            this.$confirm('您确认批量删除这些数据吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/singer/delete/batch', {data:this.ids}).then(res => {
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
        // 转向歌曲管理页面
        songEdit(id,name) {
            this.$router.push({path:'/song',query:{id,name}})
        }
    }
}
</script>


<style scoped>
</style>