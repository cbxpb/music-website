<!-- 用户个人信息页 -->
<template>
    <div>
      <div style="margin: 30px auto; width: 700px">
        <el-card style="width: 100%;" >
            <el-form :model="consumer" label-width="80px" style="padding-right: 20px;">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="consumer.username" disabled></el-input>
              </el-form-item>
              <el-form-item label="姓名" prop="name">
                <el-input v-model="consumer.name"></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="consumer.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                    <el-radio label="未知">未知</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="consumer.phone"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="consumer.email"></el-input>
              </el-form-item>
              <el-form-item label="出生日期">
                <el-date-picker v-model="consumer.birth" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"  placeholder="选择日期" style="width: 50%;" clearable></el-date-picker>
              </el-form-item>
              <el-form-item label="个性签名" prop="introduction">
                <el-input type="textarea" v-model="consumer.introduction" placeholder="个性签名" clearable></el-input>
              </el-form-item>
              <el-form-item label="地址" prop="address">
                <el-input type="textarea" v-model="consumer.address"></el-input>
              </el-form-item>
              <el-form-item label="角色" prop="role">
                <el-input v-model="consumer.role" disabled></el-input>
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
        consumer: JSON.parse(localStorage.getItem('consumer') || '{}'),
      }
  },
  methods:{
    update() {
      //保存当前的用户信息到数据库
      this.$request.put('/consumer/update',this.consumer).then(res => {
        if (res.code === '200') {
          localStorage.setItem('consumer',JSON.stringify(this.consumer))
          this.$emit('update:consumer',this.consumer)
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