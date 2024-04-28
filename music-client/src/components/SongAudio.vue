<!-- 播放器组件 -->
<template>
    <div>
        <!-- audio播放器，隐藏 -->
        <div class="container" style="display: none;">
            <audio ref="player"
                :src="url"
                controls = "controls"
               @canplay="startPlay"
               @ended="ended"
               @timeupdate="timeUpdate"
               :loop="isLoop"
            ></audio>
        </div>
        <!-- 自己的播放器 -->
        <template v-if="url">
            <div class="wrapper">
                <!-- 版心 -->
                <div class="audio-wrapper container">
                    <!-- 左边播放器按钮 -->
                    <div class="left-control">
                        <!-- 上一首 -->
                        <i class="iconfont icon-Rew" @click="prep"></i>
                        <!-- 播放暂停 -->
                        <i :class="iconPlay" @click="playMusic"></i>
                        <!-- 下一首 -->
                        <i class="iconfont icon-ffw" @click="next"></i>
                    </div>
                    <!-- 中间主体区 -->
                    <div class="middle-control">
                        <div class="middle-img" @click="toLrc">
                            <template v-if="!pic">
                                <img src="@/assets/img/consumer_avatar.png" alt="歌曲图片">
                            </template>
                            <template v-else>
                                <img :src="pic" alt="歌曲图片">
                            </template>
                            <div class="mark">
                                <i :class="iconLrc"></i>
                            </div>
                        </div>
                        <div class="middle-speed" @mouseup="mouseUp" @mousemove="mouseMove">
                            <div class="music-info">
                                <div class="music-name">{{ name }}</div>
                                <div class="music-time">{{ nowTime }}/{{ songTime }}</div>
                            </div>
                            <div ref="speed" class="music-speed" @click="updateMove">
                                <div ref="drag" class="music-drag" :style="{ width: curLength + '%' }" >
                                    <i @mousedown="mouseDown"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 右边播放器按钮 -->
                    <div class="right-control">
                        <i :class="iconModel" @click="switchModel"></i>
                        <i :class="iconCollect" @click="collection"></i>
                        <i class="iconfont icon-xiazai" @click="downMusic"></i>
                        <i class="iconfont icon-bofangliebiao" @click="isShowList = !isShowList"></i>
                        <i :class="iconVolume" @click="changeVolume"></i>
                        <el-slider v-model="curVolume" @input="changeCurVolume"></el-slider>
                    </div>
                </div>
            </div>
            <!-- 播放列表显示 -->
            <div class="showList container" :style="{height: (isShowList?400:0) + 'px' }">
                <div class="total">共{{ songList.length }}首</div>
                <!-- 歌曲展示 -->
                <template v-if="!songList.length">
                    <h1>列表暂无歌曲</h1>
                </template>
                <template v-else>
                    <div class="songs-show">
                        <ul>
                            <li>
                                <div class="song-item">
                                    <span class="item-index">序号</span>
                                    <span class="item-title">歌曲</span>
                                    <span class="item-name">歌手</span>
                                    <span class="item-album">专辑</span>
                                </div>
                            </li>
                            <li v-for="(item,index) in songList" :key="item.id" :index="index" :class="{ select: item.id === $store.getters.id }">
                                <div class="song-item" @click="toplay(index)">
                                    <span class="item-index">{{index+1}}</span>
                                    <span class="item-title">{{item.name.split('-')[0]}}</span>
                                    <span class="item-name">{{item.name.split('-')[1]}}</span>
                                    <span class="item-album">{{item.album}}</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </template>
            </div>
        </template>
    </div>
</template>

<script>
import {mapState} from 'vuex'
import {mapGetters} from 'vuex'
import axios from 'axios'
export default {
    name: "SongAudio",
    computed:{
        ...mapState(['isPlay','duration','curTime','changeTime','autoNext','volume','modelflag']),
        ...mapGetters(['songList','id','url','name','pic','lyric','indexOfSong'])
    },
    watch:{
        // 切换播放图标
        isPlay: {
            handler() {
                if (this.isPlay) {
                    this.iconPlay = 'iconfont icon-Pause'
                }else {
                    this.iconPlay = 'iconfont icon-Play'
                }
                this.togglePlay()
            }
        },
        // 进度条变化
        curTime: {
            handler() {
                this.nowTime = this.formatTime(this.curTime)
                this.songTime = this.formatTime(this.duration)
                this.curLength = (this.curTime/this.duration)*100
            }
        },
        // 跳转到指定时间
        changeTime: {
            handler() {
                this.$refs.player.currentTime = this.changeTime
            }
        },
        // 改变音量
        volume: {
            handler(newVolume,oldVolume) {
                // 当点击音量器时，记住上次的音量
                this.oldVolume = oldVolume
            }
        },
        // 监控自动播放，只要值改变就播放下一首
        autoNext: {
            handler() {
                this.next()
            }
        },
        // 切换播放模式图标
        modelflag: {
            handler() {
                if ( this.modelflag === 0 ) {
                    this.iconModel = 'iconfont icon-liebiaoxunhuan'
                    this.isLoop = false
                }else if(this.modelflag === 1){
                    this.iconModel = 'iconfont icon-suijibofang'
                    this.isLoop = false
                }else if(this.modelflag === 2) {
                    this.iconModel = 'iconfont icon-danquxunhuan'
                    this.isLoop = true
                }
            }
        },
        // 切换收藏图标
        isCollect: {
            handler() {
                if (this.isCollect) {
                    this.iconCollect = 'iconfont icon-xz'
                }else {
                    this.iconCollect = 'iconfont icon-shoucang'
                }  
            }
        },
        // 歌曲id变化则重新判断歌曲是否收藏
        id: {
            handler() {
                this.existCollet()
            }
        },
        // 切换上拉，下拉图标
        isLrc: {
            handler() {
                if (this.isLrc) {
                    this.iconLrc = 'iconfont icon-xiala'
                }else {
                    this.iconLrc = 'iconfont icon-shangla'
                }  
            }
        },
    },
    data() {
        return {
            consumer: JSON.parse(localStorage.getItem('consumer') || '{}'),
            // 播放器图标(icon-Play icon-Pause)
            iconPlay: 'iconfont icon-Play',
            // 播放模式图标(icon-liebiaoxunhuan icon-suijibofang icon-danquxunhuan)
            iconModel: 'iconfont icon-liebiaoxunhuan',
            // 音量器图标(icon-guanbiyinliang iconfont icon-yinliang)
            iconVolume: 'iconfont icon-yinliang',
            // 收藏图标(icon-shoucang icon-xz)
            iconCollect: 'iconfont icon-shoucang',
            // 下拉上拉图标(icon-shangla icon-xiala)
            iconLrc: 'iconfont icon-shangla',
            nowTime: '00:00',           //播放进度
            songTime: '00:00',          //歌曲总时长
            curLength: 0,               //进度条位置
            mouseTag: false,            //拖拽开始为true,结束为false
            curVolume: 50,              //默认音量50
            oldVolume: 0,               //旧的音量
            isShowList: false,          //是否展示歌曲列表
            isLoop: false,              //是否循环播放
            isCollect:false,            //是否收藏
            isLrc:false                 //是否是歌词页面
        }
    },
    mounted(){
        // 判断是否收藏
        this.existCollet()
        // 判断是否为歌词页面
        this.ShowLrc()
    },
    methods:{
        // 控制音乐播放、暂停
        playMusic() {
            if (this.isPlay) {
                this.$store.commit('setIsPlay',false)
            }else {
                this.$store.commit('setIsPlay',true)
            }
            this.togglePlay()
        },
        // 点击进度条切换
        updateMove(e) {
            if(!this.mouseTag) {
                let curLength = this.$refs.speed.offsetLeft
                let newPercent = ((e.clientX - curLength) / 600 ) * 100
                if(newPercent > 100) {
                    newPercent = 100
                }
                this.curLength = newPercent
                this.changeCurTime(newPercent)
            }
        },
        // 拖拽进度条,按下
        mouseDown() {
            this.mouseTag = true
        },
        // 拖拽进度条,松开
        mouseUp() {
            this.mouseTag = false
        },
        // 拖拽进度条,移动
        mouseMove(e) {
            if(this.mouseTag) {
                let curLength = this.$refs.speed.getBoundingClientRect().left
                let moveMentX = e.clientX - curLength
                let newPercent = ((moveMentX)/600)*100
                if(newPercent > 100) {
                    newPercent = 100
                }
                if(newPercent < 0) {
                    newPercent = 0
                }
                this.curLength = newPercent
                this.nowTime = this.formatTime((newPercent*0.01) * this.duration)
                this.changeCurTime(this.curLength)
            }
        },
        // 更改歌曲进度
        changeCurTime(percent) {
            if(!this.mouseTag) {
                let newCurTime = (percent*0.01) * this.duration
                this.$store.commit('setChangeTime',newCurTime)
            }
        },
        // 更改音量
        changeCurVolume(volume) {
            if (volume === 0) {
                this.iconVolume = 'iconfont icon-guanbiyinliang'
            } else {
                this.iconVolume = 'iconfont icon-yinliang'
            }
            this.$store.commit('setVolume',volume)
            this.$refs.player.volume = volume / 100
        },
        // 点击音量按钮
        changeVolume() {
            if (!this.curVolume) {
                this.curVolume = this.oldVolume
            }else {
                this.curVolume = 0
            }
        },
        // 上一首
        prep() {
            // modelflag等于1，随机播放歌曲
            if(this.modelflag === 1) {
                for(;;) {
                    let index = parseInt(Math.random()*this.songList.length)
                    console.log("index",index)
                    if(index !== this.indexOfSong) {
                        this.$store.commit('setIndexOfSong',index)
                        break
                    }
                }
            }else {
                if (this.indexOfSong <= 0) {
                    this.$store.commit('setIndexOfSong',this.songList.length - 1)
                }else {
                    this.$store.commit('setIndexOfSong',this.indexOfSong - 1)
                } 
            }
            this.toplay(this.indexOfSong)
        },
        // 下一首
        next() {
            // modelflag等于1，随机播放歌曲
            if(this.modelflag === 1) {
                for(;;) {
                    let index = parseInt(Math.random()*this.songList.length)
                    console.log("index",index)
                    if(index !== this.indexOfSong) {
                        this.$store.commit('setIndexOfSong',index)
                        break
                    }
                }
            }else {
                if (this.indexOfSong >= (this.songList.length-1)) {
                    this.$store.commit('setIndexOfSong',0)
                } else {
                    this.$store.commit('setIndexOfSong',this.indexOfSong + 1)
                } 
            }
            this.toplay(this.indexOfSong)
        },
        // 选择播放模式
        switchModel() {
            if(this.modelflag === 0){
                this.$store.commit('setModelflag',1)
                return
            }else if(this.modelflag === 1) {
                this.$store.commit('setModelflag',2)
                return
            }else if(this.modelflag === 2) {
                this.$store.commit('setModelflag',0)
                return
            }
        },
        //播放歌曲
        toplay(index) {
            this.$store.commit('setId',this.songList[index].id)
            this.$store.commit('setUrl',this.songList[index].url)
            this.$store.commit('setName',this.songList[index].name)
            this.$store.commit('setPic',this.songList[index].pic)
            this.$store.commit('setLyric',this.songList[index].lyricLocal)
            this.$store.commit('setIndexOfSong',index)
            this.$store.commit('setIsPlay',true)
        },
        // audio的事件
        startPlay() {
            let player = this.$refs.player
            // 获取歌曲时长
            this.$store.commit('setDuration',player.duration)
            this.songTime = this.formatTime(this.duration)
            // 开始播放歌曲
            this.togglePlay()
        },
        // 播放完毕
        ended() {
            this.$store.commit('setIsPlay',false)
            this.$store.commit('setCurTime',0)
            // 如果时循环播放则不跳到下一首
            if(!this.isLoop) {
                this.$store.commit('setAutoNext',!this.autoNext)
            }
        },
        // 开始播放、暂停播放
        togglePlay() {
            let player = this.$refs.player
            if (this.isPlay) {
                // 需要使用定时器，不然切换组件会报错The play() request was interrupted by a new load request.
                this.timer = setTimeout(() => {
                    player.play()
                    clearTimeout(this.timer)
                },500);
            } else {
                player.pause()
            }
        },
        // 记录音乐当前时间
        timeUpdate() {
            if(!this.mouseTag) {
                this.$store.commit('setCurTime',this.$refs.player.currentTime)
            }
        },
        // 处理时间格式
        formatTime(value) {
            // 时间取整
            let time = parseInt(value)
            // 获取小时
            let hour = parseInt(time / 3600)
            let minute = parseInt((time / 60) % 60)
            let second = parseInt(time % 60)
            hour = (hour >= 10) ? hour : ('0' + hour)           //不足10前面补0
            minute = (minute >= 10) ? minute : ('0' + minute)   //不足10前面补0
            second = (second >= 10) ? second : ('0' + second)   //不足10前面补0
            if(hour === '00') {
                return minute + ':'+ second                     //分和秒拼接
            }else {
                return hour + ':' + minute + ':'+ second
            }
        },
        // 下载音乐
        downMusic () {
            axios({
                method: 'get',
                url: this.url,
                responseType: 'blob'
            }).then(res => {
                let content = res.data
                let eleLink = document.createElement('a')
                eleLink.download = `${this.name}.mp3`
                eleLink.style.display = 'none'
                // 字符内容转变成blob地址
                let blob = new Blob([content])
                eleLink.href = URL.createObjectURL(blob)
                // 触发点击
                document.body.appendChild(eleLink)
                eleLink.click()
                // 然后移除
                document.body.removeChild(eleLink)
            }).catch(err => {
                console.log(err)
            })
        },
        // 判断是否已经收藏
        existCollet() {
            this.$request.get('/collect/existSong', {
                params: {
                    consumerId: this.consumer.id,
                    songId: this.id
                }
            }).then(res => {
                if (res.code === '200') {
                    if(res.data === 'false') {
                        this.isCollect = false
                    }else {
                        this.isCollect = true
                    }
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 收藏按钮
        collection() {
            // isCollect = false,表示还未收藏,此时执行收藏功能
            if(!this.isCollect) {
                this.add()
            }else {         // isCollect = true,表示已收藏,此时执行取消收藏功能
                this.delete()
            }
        },
        // 收藏功能
        add() {
            this.$request.post('/collect/add',null, {
                params: {
                    consumerId: this.consumer.id,
                    songId: this.id
                }
            }).then(res => {
                if (res.code === '200') {
                    this.isCollect = true
                    this.$message.success('歌曲已成功收藏')
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 取消收藏
        delete() {
            this.$request.delete('/collect/deleteSong',{
                params: {
                    consumerId: this.consumer.id,
                    songId: this.id
                }
            }).then(res => {
                if (res.code === '200') {
                    this.isCollect = false
                    this.$message.success('歌曲已取消收藏')
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // 判断此页面是否是歌词页面
        ShowLrc() {
            if (this.$route.meta.name === "歌词") {
                this.isLrc = true
            }else {
                this.isLrc = false
            }
        },
        // 跳转页面
        toLrc() {
            if (this.$route.meta.name === "歌词") {
                this.$router.go(-1)
                this.isLrc = false
            }else {
                this.$router.push('/lrc')
                this.isLrc = true
            }
        }
    }
}
</script>


<style scoped>

    .wrapper {
        position: fixed;
        bottom: 1px;
        left: 0;
        right: 0;
        height: 62px;
        background-color: #fff;
        border: 1px #3333 solid;
    }

    .audio-wrapper {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
    /* #region左边区域 */

    .audio-wrapper .left-control i {
        font-size: 25px;
        margin-right: 10px;
        opacity: 0.5;
        cursor: pointer;
        transition: .3s;
    }

    .audio-wrapper .left-control i:hover {
        opacity: 1;
    }

    .audio-wrapper .left-control i:last-child{
        margin-right: 0;
    }

    /* #endregion左边区域 */

    /* #region中间区域 */

    .audio-wrapper .middle-control {
        display: flex;
        background-color: #f2f2f2;
        align-items: center;
    }

    .audio-wrapper .middle-control .middle-img {
        position: relative;
        cursor: pointer;
        height: 60px;
    }

    .audio-wrapper .middle-control .middle-img img {
        border: 1px solid #3333;
        width: 60px;
        height: 60px;
    }

    .audio-wrapper .middle-control .middle-img:hover .mark {
        opacity: 0.6;
    }

    .audio-wrapper .middle-control .middle-img .mark {
        position: absolute;
        width: 60px;
        height: 60px;
        background: #333;
        font-size: 20px;
        color: #ffff;
        text-align: center;
        line-height: 60px;
        top: 0;
        opacity: 0;
        transition: .3s;
    }
    
    .audio-wrapper .middle-control .middle-speed {
        height: 60px;
        padding: 10px 20px;
    }

    .middle-control .middle-speed .music-info {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-bottom: 8px;
        cursor: default;
    }

    .middle-control .middle-speed .music-speed {
        height: 5px;
        width: 600px;
        background-color: #e0e0e0;
        border-radius: 3px;
        cursor: pointer;
    }

    .middle-control .middle-speed .music-speed .music-drag {
        position: relative;
        width: 0%;
        height: 5px;
        border-radius: 3px;
        background-color: #666666;
    }

    .middle-control .middle-speed .music-speed .music-drag i {
        position: absolute;
        width: 11px;
        height: 11px;
        border-radius: 50%;
        background-color: white;
        top: -3px;
        left: 100%;
    }

    /* #endregion中间区域 */

    /* #region右边区域 */

    .audio-wrapper .right-control {
        display: flex;
        align-items: center;
    }

    .audio-wrapper .right-control i {
        font-size: 25px;
        margin-right: 10px;
        opacity: 0.5;
        cursor: pointer;
        transition: .3s;
    }

    .audio-wrapper .right-control i.icon-xz {
        color: red;
    }

    .audio-wrapper .right-control i:hover {
        opacity: 1;
    }

    .audio-wrapper .right-control .el-slider {
        width: 100px;
    }

    /* #endregion右边区域 */

    /* #region列表区域 */

    .showList {
        position: fixed;
        bottom: 62px;
        left: 0;
        right: 0;
        background-color: #fff;
        border: 1px #3333 solid;
        overflow: auto;
        transition: 1s;
        z-index: 10;
    }

    .showList .total {
        font-size: 12px;
        color: #afafaf;
        padding: 20px;
    }

    .songs-show .song-item {
        padding:20px;
        display: flex;
        font-size: 16px;
        cursor: pointer;
        align-items: center;
    }

    .songs-show ul>li:nth-child(2n+1) {
        background-color: #f7f7f7;
    }

    .songs-show ul>li.select,
    .songs-show ul>li:not(:first-child):hover {
        background-color: #31c27c88;;
    }

    .songs-show .song-item .item-index {
        width: 5%;
        margin-right: 10px;
        font-weight: lighter;
        opacity: 0.8;
    }

    .songs-show .song-item .item-title {
        width: 30%;
        margin-right: 30px;
        overflow: hidden;
    }

    .songs-show .song-item .item-name {
        width: 25%;
        margin-right: 30px;
        overflow: hidden;
    }

    .songs-show .song-item .item-album {
        width: 40%;
        overflow: hidden;
    }

    /* #endregion列表区域 */

    /* #region歌词区域 */

    .showLrc {
        position: fixed;
        height:500px;
        bottom: 62px;
        left: 0;
        right: 0;
        background-color: red;
        border: 1px #3333 solid;
        overflow: auto;
        transition: 1s;
    }

    /* #endregion歌词区域 */

</style>