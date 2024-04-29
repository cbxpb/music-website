<!-- 修改管理员密码 -->
<template>
  <div>
    <div style="margin: 100px auto; width: 700px">
      <el-card style="width: 100%;" >
          <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" style="padding-right: 20px;">
            <el-form-item label="原始密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入原密码" show-password clearable></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="form.newPassword" placeholder="请输入新密码" show-password clearable></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="form.confirmPassword" placeholder="请确认新密码" show-password clearable></el-input>
            </el-form-item>
            <div style="text-align: center; margin-bottom: 20px">
              <el-button type="primary" @click="update">确认修改</el-button>
            </div>
          </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "Password",
  data(){
    // 密码校验
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
          callback(new Error('请确认密码'));
      } else if (value !== this.form.newPassword) {
          callback(new Error('两次输入的密码不一致'));
      } else {
          callback();
      }
    };
    return{
      admin: JSON.parse(localStorage.getItem('admin') || '{}'),
      form: {
        password: '',
        newPassword: '',
        confirmPassword: '',
      },
      rules: {
        password: [
            { required: true, message: '请输入原密码', trigger: 'blur' },
        ],
        newPassword: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
            { validator: validatePassword, required: true, trigger: 'blur' }
        ],
      }
    }
  },
  methods:{
    update() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          //表单校验通过
          this.$request.put('/admin/updatePassword',null, {
            params: {
              id: this.admin.id,
              password: this.form.password,
              newPassword: this.form.newPassword,
            }
          }).then(res => {
              if (res.code === '200') {               //表示修改成功
                this.$message.success('密码修改成功')
                this.$router.push('/login')
              } else {
                this.$message.error(res.msg)    //弹出错误信息
              }
          })
        }
      })
    }
  }
}


</script>


<style scoped>
  .el-form-item__label {
    font-weight: bold;
  }
</style>