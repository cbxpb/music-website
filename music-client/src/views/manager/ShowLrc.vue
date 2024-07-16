<!-- 歌词页面 -->
<template>
    <div>
        <div class="showLrc">
            <!-- 版心 -->
            <div class="container">
                <div class="showLrc-left">
                    <template v-if="!pic">
                        <img src="@/assets/img/consumer_avatar.png" alt="歌曲图片">
                    </template>
                    <template v-else>
                        <img :src="pic" alt="歌曲图片">
                    </template>
                </div>
                <div class="showLrc-right">
                    <!-- 歌名 -->
                    <div class="name">{{name.split('-')[0]}}</div>
                    <!-- 歌手、专辑 -->
                    <div class="info">
                        <div class="singer">歌手：{{name.split('-')[1]}}</div>
                        <div class="album">专辑：{{songList[indexOfSong].album}}</div>
                    </div>
                    <!-- 歌词 -->
                    <div class="lrc">
                        <!--有歌词-->
                        <ul class="has-lyric" v-if="Lyric.length">
                            <li v-for="(item, index) in Lyric" :key="index" :class="{ isHighlight:(curTime >= item[0])&&(curTime<= ((index+1)===Lyric.length?duration:Lyric[index+1][0]))}">
                                {{ item[1] }}
                            </li>
                        </ul>
                        <!--没歌词-->
                        <div v-else class="no-lyric" key="no-lyric">
                            <span>暂无歌词</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {mapState} from 'vuex'
import {mapGetters} from 'vuex'
export default {
    name: 'ShowLrc',
    computed:{
        ...mapState(['curTime','duration']),
        ...mapGetters(['songList','url','name','pic','lyric','indexOfSong'])
    },
    watch:{
        url: {
            handler() {
                this.parseLyric(this.lyric)
                this.LyricIndex = 0
            }
        },
        curTime: {
            handler() {
                if (this.Lyric.length) {
                    if(this.LyricIndex<this.Lyric.length&&this.curTime >= this.Lyric[this.LyricIndex][0]) {
                        this.LyricIndex += 1
                    } else if(this.LyricIndex>0&&this.curTime < this.Lyric[this.LyricIndex-1][0]){
                        this.LyricIndex -= 1
                    }
                }
            }
        },
        LyricIndex: {
            handler() {
                let myUl = document.querySelector('.lrc');
                myUl.scrollTop = 40*((this.LyricIndex-1)>3?this.LyricIndex-4:0)
            }
        }
    },
    data () {
        return {
            Lyric: [],
            LyricIndex: 0,  //歌词数组下标
        }
    },
    mounted () {
        this.parseLyric(this.lyric)
    },
    methods: {
        // 解析歌词
        parse (text) {
            let lines = text
            let pattern = /(\[\d{2}:\d{2}.(\d{3}|\d{2})\])|(\[\d{2}:\d{2}\])/g
            let result = []
            // 对于歌词格式不对的特殊处理
            if (!(/\[.+\]/.test(text))) {
                return [[0, text]]
            }
            // 去掉前面格式不正确的行
            while (!pattern.test(lines[0])) {
                lines = lines.slice(1)
            }
            for (let item of lines) {
                let time = item.match(pattern) // 存前面的时间段
                let value = item.replace(pattern, '') // 存后面的歌词
                for (let item1 of time) {
                    let t = item1.slice(1, -1).split(':')
                    if (value !== '') {
                        result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value])
                    }
                }
            }
            result.sort(function (a, b) {
                return a[0] - b[0]
            })
            return result
        },
        // 解析歌词文件
        parseLyric(url) {
            console.log("url",url)
            this.$request.get('/song/lrc', {
                params: {
                    url: url,
                }
            }).then(res => {
                if (res.code === '200') {
                    this.Lyric = this.parse(res.data)
                    console.log("length",this.Lyric.length)
                } else {
                    this.$message.error("暂无歌词资源！");
                    this.Lyric = []
                    console.log("333",this.Lyric)
                }
            })
        },
    }
}
</script>


<style scoped>
    .showLrc {
        margin-top: 40px;
        height: calc(100vh - 182px);
        background: #ffffff;
    }
    .showLrc .container {
        display: flex;
        justify-content: center;
        align-items: flex-start;
    }
    .showLrc .container .showLrc-left img {
        width: 250px;
        height: 250px;
        margin-right: 40px;
    }
    .showLrc .container .showLrc-right {
        width: 420px;

    }
    .showLrc .container .showLrc-right .name {
        padding: 10px;
        font-size: 24px;
        color: #111111;
    }
    .showLrc .container .showLrc-right .info {
        display: flex;
        padding: 10px;
        font-size: 13px;
        color: #111111;
        justify-content: space-between;
        border-bottom: 1px solid #e5e5e5;
    }
    .showLrc .container .showLrc-right .lrc {
        height: 380px;
        overflow-y: scroll;
        padding-top: 10px;
        font-size: 15px;
        color: #666666;
        scrollbar-width: thin;
        transition: .5s;
        /* padding-top: 80px; */
    }
    .showLrc .container .showLrc-right .lrc li {
        margin-top: 20px;
    }
    .showLrc .container .showLrc-right .lrc li.isHighlight {
        color: #ff4444;
    }

</style>