import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    songList: [],           //歌曲列表
    id: '',                 //歌曲id
    url: '',                //歌曲的播放地址
    name: '',               //歌曲名
    pic: '',                //歌曲图片
    lyric: '',              //歌词地址
    
    tempItem:'',            //歌单或歌手详情信息

    isPlay: false,          //是否处于播放状态
    duration: 0,            //歌曲时长
    curTime: 0,             //当前播放位置
    changeTime: 0,          //指定播放位置
    autoNext: true,         //只要值变化就会自动播放下一首
    indexOfSong: null,      //歌曲在列表中的下标
    volume: 50,             //音量
    modelflag: 0,           //播放模式(0列表循环，1随机播放，2单曲循环)

  },
  getters: {
    songList(state) {
      let songList = state.songList
      if (!songList.length) {
        songList = JSON.parse(localStorage.getItem('songList') || '[]')
      }
      return songList
    },
    id(state) {
      let id = state.id
      if (!id) {
        id = JSON.parse(localStorage.getItem('id') || null)
      }
      return id
    },
    url(state) {
      let url = state.url
      if (!url) {
        url = JSON.parse(localStorage.getItem('url') || null)
      }
      return url
    },
    name(state) {
      let name = state.name
      if (!name) {
        name = JSON.parse(localStorage.getItem('name') || null)
      }
      return name
    },
    pic(state) {
      let pic = state.pic
      if (!pic) {
        pic = JSON.parse(localStorage.getItem('pic') || null)
      }
      return pic
    },
    lyric(state) {
      let lyric = state.lyric
      if (!lyric) {
        lyric = JSON.parse(localStorage.getItem('lyric') || null)
      }
      return lyric
    },
    indexOfSong(state) {
      let indexOfSong = state.indexOfSong
      if (!indexOfSong) {
        indexOfSong = JSON.parse(localStorage.getItem('indexOfSong') || null)
      }
      return indexOfSong
    },
    tempItem(state) {
      let tempItem = state.tempItem
      if (!tempItem) {
        tempItem = JSON.parse(localStorage.getItem('tempItem') || null)
      }
      return tempItem
    },


  },
  mutations: {
    setSongList(state,songList) {
      state.songList = songList
      localStorage.setItem("songList",JSON.stringify(songList))
    },
    setId(state,id) {
      state.id = id
      localStorage.setItem("id",JSON.stringify(id))
    },
    setUrl(state,url) {
      state.url = url
      localStorage.setItem("url",JSON.stringify(url))
    },
    setName(state,name) {
      state.name = name
      localStorage.setItem("name",JSON.stringify(name))
    },
    setPic(state,pic) {
      state.pic = pic
      localStorage.setItem("pic",JSON.stringify(pic))
    },
    setLyric(state,lyric) {
      state.lyric = lyric
      localStorage.setItem("lyric",JSON.stringify(lyric))
    },


    setIsPlay(state,isPlay) {
      state.isPlay = isPlay
    },
    setDuration(state,duration) {
      state.duration = duration
    },
    setCurTime(state,curTime) {
      state.curTime = curTime
    },
    setChangeTime(state,changeTime) {
      state.changeTime = changeTime
    },
    setAutoNext(state,autoNext) {
      state.autoNext = autoNext
    },
    setIndexOfSong(state,indexOfSong) {
      state.indexOfSong = indexOfSong
      localStorage.setItem("indexOfSong",JSON.stringify(indexOfSong))
    },
    setVolume(state,volume) {
      state.volume = volume
    },
    setModelflag(state,modelflag) {
      state.modelflag = modelflag
    },
    setTempItem(state,tempItem) {
      state.tempItem = tempItem
      localStorage.setItem("tempItem",JSON.stringify(tempItem))
    }
  },
  actions: {
  },
  modules: {
  }
})
