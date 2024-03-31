<template>
    <div>
        <!-- 搜索模块 -->
        <div>
            <el-input v-model="username" style="width: 200px;" placeholder="用户名模糊查询"></el-input>
            <el-select v-model="gender" placeholder="请选择性别" clearable style="width: 200px; margin: 0 5px;">
                <el-option value="男"></el-option>
                <el-option value="女"></el-option>
            </el-select>
            <el-button type="primary" @click="load(1)">查询</el-button>
            <el-button type="info" @click="reset">重置</el-button>
        </div>
        <!-- 功能模块 -->
        <div  style="margin: 10px 0;">
            <el-button type="primary" @click="addConsumer">添加用户</el-button>
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <!-- 表格数据 -->
        <el-table ref="table" :data="tableData" @selection-change="handleSelectionChange" :row-key="getRowKey" border :header-cell-style="{ backgroundColor:'aliceblue', color: '#666'}">
            <el-table-column type="selection" :reserve-selection="true" width="55" align="center"></el-table-column>
            <el-table-column label="序号" type="index" :index="indexMethod" width="55" align="center"></el-table-column>
            <el-table-column label="图片"  align="center" width="150">
                <template v-slot="scope">
                    <template v-if="!scope.row.avatar">
                        <img src="@/assets/img/consumer_avatar.jpg" alt="用户头像" width="100%" height="90px">
                    </template>
                    <template v-else>
                        <img :src="scope.row.avatar" alt="用户头像" width="100%" height="90px">
                    </template>
                    <el-upload action="http://localhost:9090/file/upload"
                        :headers="{token: admin.token}"
                        :data="{type:2}"
                        :show-file-list="false"
                        :on-success=" (res,file)=> {handleAvatarSuccess (scope.row.id,res,file)}"
                        :before-upload="beforeAvatarUpload"
                    >
                        <el-button size="mini" type="primary">图片上传<i class="el-icon-upload el-icon--right"></i></el-button>
                    </el-upload>
                </template>
            </el-table-column>
            <el-table-column label="用户名" prop="username" align="center"></el-table-column>
            <el-table-column label="姓名" prop="name" align="center"></el-table-column>
            <el-table-column label="性别" prop="gender" align="center"></el-table-column>
            <el-table-column label="电话" prop="phone" align="center"></el-table-column>
            <el-table-column label="邮件" prop="email" align="center"></el-table-column>
            <el-table-column label="生日" prop="birth" align="center"></el-table-column>
            <el-table-column label="个性签名" prop="introduction" align="center"></el-table-column>
            <el-table-column label="地址" prop="address" align="center"></el-table-column>
            <el-table-column label="角色" prop="role" align="center"></el-table-column>
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
        <!-- 新增修改模态框 -->
        <el-dialog :title="title" :visible.sync="fromVisible" center width="30%">
            <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="用户名" clearable></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" placeholder="密码" show-password clearable></el-input>
              </el-form-item>
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="姓名" clearable></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="form.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="form.phone" placeholder="电话" clearable></el-input>
              </el-form-item>
              <el-form-item label="邮件" prop="email">
                <el-input v-model="form.email" placeholder="邮件" clearable></el-input>
              </el-form-item>
              <el-form-item label="出生日期">
                <el-date-picker v-model="form.birth" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"  placeholder="选择日期" style="width: 50%;" clearable></el-date-picker>
              </el-form-item>
              <el-form-item label="个性签名" prop="introduction">
                <el-input type="textarea" v-model="form.introduction" placeholder="个性签名" clearable></el-input>
              </el-form-item>
              <el-form-item label="地址" prop="address">
                <el-input type="textarea" v-model="form.address" placeholder="地址" clearable></el-input>
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
    name: "Consumer",
    data() {
        return {
            username: '',       //根据用户名模糊查询
            gender: '',         //根据性别查询
            fromVisible: false, //模态框状态
            title:'',           //模态框名字
            form:{},            //表单信息
            rules:{
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
                gender: [
                    { required: true, message: '请选择用户性别', trigger: 'blur' },
                ],
                email: [
                    { type:'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
                ],
            },
            tableData: [],      //所有的数据
            pageNum: 1,         //当前页码
            pageSize: 4,        //每页显示的条数
            total: 0,           //查询总条数
            admin: JSON.parse(localStorage.getItem('admin')||{}),   //获取本地token
            Url:{           
                downUrl: '',    //下载预览地址
                uploadUrl: ''   //本地存储地址
            },
            ids: []
        }   
    },
    created() {
        // 页面挂载表格数据
        this.load()
    },

    methods: {
         //渲染用户列表
         load(pageNum) {
            if (pageNum){
                this.pageNum = pageNum
            }
            this.$request.get('/consumer/selectByPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    username: this.username,
                    gender: this.gender
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
        // 添加用户按钮
        addConsumer(){  
            this.title = '添加用户'       
            this.resetRuleForm(),
            this.form = {}   //新增数据的时候清空数据
            this.fromVisible = true      //打开弹窗
        },
        //修改用户按钮
        handleEdit(row){   
            this.title = '编辑用户'    
            this.resetRuleForm()
            this.form = JSON.parse(JSON.stringify(row))
            this.form.password = ''
            this.fromVisible = true      //打开弹窗
        },
        //存储表单信息
        saveForm() {
            this.$refs['formRef'].validate((valid) => {
                if (valid) {
                    this.$request({
                        url: this.form.id ? '/consumer/update' : '/consumer/add',
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
            this.username = '',
            this.gender = '',
            this.load(1)
        },
        //改变当前页数
        handleCurrentChange(pageNum) {
            this.load(pageNum)
        },
        // 自定义索引
        indexMethod(index) {
            return (this.pageNum - 1) * this.pageSize + index + 1
        },
        // 重置表单验证
        resetRuleForm() {
            this.$nextTick(()=>{
                this.$refs['formRef'].clearValidate();
            })
        },
        // 头像上传成功后的事件
        handleAvatarSuccess(id, res, file ){
            if (res.code === '401') {
                this.$router.push('/login')
            }else {
                this.Url.downUrl = res.data.downUrl
                this.Url.uploadUrl = res.data.uploadUrl
                this.$request.put('/consumer/updateAvatar',null, {
                    params: {
                        downUrl: this.Url.downUrl,
                        uploadUrl: this.Url.uploadUrl,
                        id: id,
                    }
                }).then(res => {
                    if (res.code === '200') {                           //表示保存成功
                        this.$message.success('头像上传成功')
                        this.load(this.page)
                    } else {
                        this.$message.error('头像上传失败' + res.msg)    //弹出错误信息
                        this.load(this.page)
                    }
                })
            }
        },
        // 上传歌曲头像文件设置
        beforeAvatarUpload(file) {
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
    
        // 批量删除
        delBatch() {
            if (!this.ids.length) {
                this.$message.warning('请选择要删除的用户')
                return
            }
            console.log(this.ids)
            this.$confirm('您确认批量删除这些用户吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/consumer/delete/batch', {data:this.ids}).then(res => {
                    if (res.code === '200') {
                        this.$message.success('批量删除成功')
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)
                        this.$refs.table.clearSelection();      //清除上次记录的删除选项
                    }
                })
            }).catch(() => {})
        },
        // 选中行
        handleSelectionChange(rows) {
            this.ids = rows.map(v => v.id)
        },
        // 删除
        del(id) {
            console.log(id)
            this.$confirm('您确认删除此用户吗?','确认删除',{type:"warning"}).then(response => {
                this.$request.delete('/consumer/delete/' + id).then(res => {
                    if (res.code === '200') {
                        this.$message.success('删除用户成功')
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