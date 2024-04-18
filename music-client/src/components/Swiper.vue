<template>
  <div class="swiper">
    <div class="swiper-container">
      <el-carousel :interval="4000" type="card" height="300px">
        <el-carousel-item v-for="(item, index) in swiperList" :key="index">
          <img :src="item.picImg">
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>


<script>
export default {
  name: "Swiper",
  data() {
    return {
      swiperList: [
        { picImg: require("@/assets/img/swiper/1.jpg") },
        { picImg: require("@/assets/img/swiper/2.jpg") },
        { picImg: require("@/assets/img/swiper/3.jpg") },
        { picImg: require("@/assets/img/swiper/4.jpg") },
        { picImg: require("@/assets/img/swiper/5.jpg") },
        { picImg: require("@/assets/img/swiper/6.jpg") },
        { picImg: require("@/assets/img/swiper/7.jpg") },
        { picImg: require("@/assets/img/swiper/8.jpg") },
      ],
    };
  },
};
</script>

<style scoped>
.swiper {
  width: 1200px;
  margin:0 auto;
  margin-top: 40px;
}
 img {
    width: 100%;
  }
</style>
