import router from '@/router';
import axios from 'axios'

// 创建一个新的axiox对象
const request = axios.create({
    baseURL: 'http://localhost:9090',  //后端的接口地址
    timeout: 30000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token,对请求参数统一加密
request.interceptors.request.use(
    config => {
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        let consumer = JSON.parse(localStorage.getItem("consumer") || '{}')
        config.headers['token'] = consumer.token    //设置请求头
        return config
    },
    error => {
        console.error('request error: ' + error)
        return Promise.reject(error)
    }
);

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        } 

        if (res.code === '401') {
            router.push('/login')
        }
        return res;
    },
    error => {
        console.error('response error: ' + error)
        return Promise.reject(error)
    }
)


export default request