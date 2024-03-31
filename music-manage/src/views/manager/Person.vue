<!-- 管理员个人信息页 -->
<template>
    <div>
      <div style="margin: 100px auto; width: 700px">
        <el-card style="width: 100%;" >
            <el-form :model="admin" label-width="80px" style="padding-right: 20px;">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="admin.username" disabled></el-input>
              </el-form-item>
              <el-form-item label="姓名" prop="name">
                <el-input v-model="admin.name"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="admin.phone"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="admin.email"></el-input>
              </el-form-item>
              <el-form-item label="地址" prop="address">
                <el-input type="textarea" v-model="admin.address"></el-input>
              </el-form-item>
              <el-form-item label="角色" prop="role">
                <el-input v-model="admin.role" disabled></el-input>
              </el-form-item>
              <div style="text-align: center; margin-bottom: 20px">
                <el-button type="primary" @click="update">保存</el-button>
              </div>
            </el-form>
        </el-card>
      </div>
    </div>
</template>
  
<script>

export default {
  name: "Person",
  data(){
      return{
          admin: JSON.parse(localStorage.getItem('admin') || '{}'),
      }
  },
  methods:{
    update() {
      //保存当前的管理员信息到数据库
      this.$request.put('/admin/update',this.admin).then(res => {
        if (res.code === '200') {
          localStorage.setItem('admin',JSON.stringify(this.admin))
          this.$emit('update:admin',this.admin)
          location.replace('/home');
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>

<style scoped>
  .el-form-item__label {
    font-weight: bold;
  }
</style>