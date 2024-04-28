<!-- 用户登录页面 -->
<template>
    <div class="login-container">
        <div class="login-wrapper">
            <div class="login-img">
                <img src="@/assets/img/login.jpg" alt="登录背景图片" width="100%">
            </div>
            <div class="login-form">
                <el-form :model="consumer" :rules="rules" ref="loginRef" style="width: 70%;">
                    <div class="login-title">欢迎登录音乐网站</div>
                    <el-form-item prop="username">
                        <el-input prefix-icon="el-icon-user"  v-model="consumer.username" placeholder="请输入用户名" clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input prefix-icon="el-icon-lock" v-model="consumer.password" placeholder="请输入密码" show-password clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                        <div style="display: flex; line-height: 40px">
                            <el-input prefix-icon="el-icon-circle-check" v-model="consumer.code"  placeholder="请输入验证码" style="flex: 1; margin-right: 5px;" ></el-input>
                            <div style="flex: 1; height: 40px; background: #bbb; border-radius: 4px;">
                                <valid-code @update:value="getCode"/>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" style="width: 100%;" @click="login">登 录</el-button>
                    </el-form-item>
                    <div style="display: flex;">
                        <div style="flex: 1;">还没有账号？请 <span style="color: #0f9876;cursor: pointer;" @click="$router.push('/register')">注册</span></div>
                        <!-- <div style="flex: 1;text-align: right;">忘记密码</div> -->
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import ValidCode from "@/components/ValidCode.vue";

export default {
    name: "Login",
    components: {
        ValidCode
    },
    data(){
        // 验证码校验
        const validateCode = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入验证码'));
            } else if (value.toLowerCase() !== this.code) {
                callback(new Error('验证码错误'));
            } else {
                callback();
            }
        };
        return{
            code: '',       //验证码组件传递过来的code
            consumer:{
                code: '',   //用户输入的验证码code
                username: '',
                password: '',
                role: '用户'
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
                code: [
                    { validator: validateCode, trigger: 'blur' }
                ],
            }
        }
    },
    methods: {
        getCode(code) {
            this.code = code.toLowerCase()
        },
        login() {
            this.$refs['loginRef'].validate((valid) => {
                if (valid) {
                    //验证通过
                    this.$request.post('/consumer/login' , this.consumer).then(res =>{
                        if (res.code === '200'){
                            this.$message.success('登录成功')
                            localStorage.setItem("consumer",JSON.stringify(res.data))  //存储用户信息
                            this.$router.push('/home')
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


<style scoped>
    .login-container {
        height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background-image: linear-gradient(to right, #fbc2eb, #a6c1ee);
    }
    .login-wrapper {
        display: flex;
        width: 70%;
        border-radius: 15px;
        overflow: hidden;
        background-color: #ffcfdf; 
    }
    .login-img {
        flex: 1;
    }
    .login-form {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .login-title {
        font-size: 20px;
        font-weight: bolder;
        text-align: center;
        margin-bottom: 20px;
    }
</style>