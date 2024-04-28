<!-- 搜索页面 -->
<template>
    <div>
        <!-- search页面 -->
        <div  class="search">
            <!-- 版心 -->
            <div class="container">
                <!-- 导航区 -->
                <nav class="search-nav">
                    <span :class="{ activeNav: activeNav === 'SearchSong' }" @click="handleChangeView('SearchSong')">歌曲</span>
                    <span :class="{ activeNav: activeNav === 'SearchSongList' }" @click="handleChangeView('SearchSongList')">歌单</span>
                    <span :class="{ activeNav: activeNav === 'SearchSinger' }" @click="handleChangeView('SearchSinger')">歌手</span>
                </nav>
                <div class="content">
                    <component :is="currentView" :keywords="keywords"></component>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SearchSong from '@/components/search/SearchSong.vue';
import SearchSongList from '@/components/search/SearchSongList.vue';
import SearchSinger from '@/components/search/SearchSinger.vue';

export default {
    name: "Search",
    components: {
        SearchSong,
        SearchSongList,
        SearchSinger
    },
    data(){
        return{
            activeNav: 'SearchSong',
            currentView: 'SearchSong'
        }
    },
    props:['getKeywords'],
    computed:{
        keywords() {
            // 将子组件的keywords传给父组件
            this.getKeywords(this.$route.query.keywords)
            return this.$route.query.keywords;
        }
    },
    methods:{
        // 切换组件
        handleChangeView(component) {
            this.currentView = component
            this.activeNav = component
        }
    },
};
</script>

<style scoped>

    .search {
        margin-top: 40px;
        background-color: #fafafa;
        box-shadow: 2px 10px 6px rgba(0, 21, 41, .35);
        min-height: calc(100vh - 182px);
        margin-bottom: 62px;
    }

    .search .search-nav {
        padding-top: 10px;
        padding-bottom: 40px;
    }
    
    .search .search-nav span{
        margin-right: 55px;
        font-size: 20px;
        cursor: pointer;
    }

    .search .search-nav .activeNav {
        color: #31c27c;
    }

    .search .search-nav span:hover {
        color: #31c27c;
    }

    .search .content {
        background-color: white;
    }

</style>