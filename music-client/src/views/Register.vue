<!-- 用户注册页面 -->
<template>
    <div class="register-container">
        <div class="register-wrapper">
            <div class="register-img">
                <img src="@/assets/img/regin.jpg" alt="注册背景图片" width="100%">
            </div>
            <div class="register-form">
                <el-form :model="consumer" :rules="rules" ref="registerRef" style="width: 70%;">
                    <div class="register-title">欢迎注册音乐网站</div>
                    <el-form-item prop="username">
                        <el-input prefix-icon="el-icon-user"  v-model="consumer.username" placeholder="请输入用户名" clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input prefix-icon="el-icon-lock" v-model="consumer.password" placeholder="请输入密码" show-password clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input prefix-icon="el-icon-lock" v-model="consumer.confirmPassword" placeholder="请确认密码" show-password clearable></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" style="width: 100%;" @click="register">注 册</el-button>
                    </el-form-item>
                    <div style="display: flex;">
                        <div style="flex: 1;">已有账号？请 <span style="color: #0f9876;cursor: pointer;" @click="$router.push('/login')">登录</span></div>
                        <div style="flex: 1;text-align: right;">忘记密码</div>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Register",
    data(){
        // 密码校验
        const validatePassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请确认密码'));
            } else if (value !== this.consumer.password) {
                callback(new Error('两次输入的密码不一致'));
            } else {
                callback();
            }
        };
        return{
            consumer:{
                username: '',
                password: '',
                confirmPassword: '',
                role: '用户'
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
                confirmPassword: [
                    { validator: validatePassword, trigger: 'blur' }
                ],
            }
        }
    },
    methods: {
        register() {
            console.log(this.consumer)
            this.$refs['registerRef'].validate((valid) => {
                if (valid) {
                    //验证通过
                    this.$request.post('/consumer/register' , this.consumer).then(res =>{
                        if (res.code === '200'){
                            this.$router.push('/login')
                            this.$message.success('注册成功')
                        } else{
                            this.$message.error(res.msg)
                        }
                    })
                }
            })
        }
    }
}
</script>


<style>
    .register-container {
        height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background-image: linear-gradient(to right, #fbc2eb, #a6c1ee);
    }
    .register-wrapper {
        /* height: 276.5px; */
        display: flex;
        width: 70%;
        border-radius: 15px;
        overflow: hidden;
        background-color: #53ccd5;
        
        
    }
    .register-img {
        flex: 1;
    }
    .register-form {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .register-title {
        font-size: 20px;
        font-weight: bolder;
        text-align: center;
        margin-bottom: 20px;
    }
</style>