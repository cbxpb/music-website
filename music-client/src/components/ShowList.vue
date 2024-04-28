<!-- 歌曲列表(暂未用到) -->
<template>
    <div>
        <div class="showList container">
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
                        <li v-for="(item,index) in songList" :key="item.id" :index="index" :class="{ select: index === $store.state.indexOfSong }">
                            <div class="song-item" @click="toplay(item.id,item.url,item.name,item.pic,item.lyricLocal,index)">
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
    </div>
</template>

<script>
export default {
    name: 'ShowList',
    data () {
        return {
            
        }
    },
    props:['songList'],
    mounted () {
    },
    methods: {
         //播放歌曲
         toplay(id,url,name,pic,lyric,index) {
            this.$store.commit('setId',id)
            this.$store.commit('setUrl',url)
            this.$store.commit('setName',name)
            this.$store.commit('setPic',pic)
            this.$store.commit('setLyric',lyric)
            this.$store.commit('setIndexOfSong',index)
        }
    }
}
</script>

<style scoped>
    .showList {
        position: fixed;
        height: 400px;
        bottom: 62px;
        left: 0;
        right: 0;
        background-color: #fff;
        border: 1px #3333 solid;
        overflow: auto;
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
</style>