<!-- 回到顶部功能小组件 -->
<template>
    <div>
        <!-- 回到顶部 -->
        <div class="scroll-top">
            <a href="#">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-huidaodingbu"></use>
                </svg>
                <span>顶部</span>
            </a>
        </div>
    </div>
</template>

<script>
export default {
  name: "ScrollTop",
};
</script>

<style scoped>

    /* #region回到顶部 */

    .scroll-top {
        position: fixed;
        right: 45px;
        bottom: 100px;
        border: 1px solid black;
        border-radius: 10%;
        background-color: #ffff;
        z-index: 10;
        cursor: pointer;
        transition: .4s;
    }

    .scroll-top:hover {
        background-color: #3333;
    }

    .scroll-top a {
        padding: 5px;
        text-decoration: none;
        color: #000;
        font-size: 12px;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .scroll-top svg {
        width: 12px;
        height: 12px;
    }

    /* #endregion回到顶部 */
    
</style>