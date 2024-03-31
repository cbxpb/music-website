<template>
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-1">
              <div class="grid-cont-right">
                <div class="grid-num">{{ consumerCount }}</div>
                <div>用户总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-2">
              <div class="grid-cont-right">
                <div class="grid-num">{{ songCount }}</div>
                <div>歌曲总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-3">
              <div class="grid-cont-right">
                <div class="grid-num">{{ singerCount }}</div>
                <div>歌手数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-4">
              <div class="grid-cont-right">
                <div class="grid-num">{{ songListCount }}</div>
                <div>歌单数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <div id="pie" style="width: 100%;height: 400px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div id="bar" style="width: 100%;height: 400px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <div id="pie1" style="width: 100%;height: 400px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div id="bar1" style="width: 100%;height: 400px;"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
</template>
  
<script>
    import * as echarts from 'echarts';
    const consumerGender = {
        title: {
            text: '用户性别',
            subtext: '扇形图',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                name: '性别',
                type: 'pie',
                center:['50%','60%'],
                radius: '50%',
                data: [],
                label: {
                    show: true,
                    formatter(param) {
                        return param.name + ' (' + param.percent + '%)'
                    }
                },
                emphasis: {
                    itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    const singerGender = {
        title: {
            text: '歌手性别',
            subtext: '扇形图',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                name: '性别',
                type: 'pie',
                center:['50%','60%'],
                radius: '50%',
                data: [],
                label: {
                    show: true,
                    formatter(param) {
                        return param.name + ' (' + param.percent + '%)'
                    }
                },
                emphasis: {
                    itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    const songListStyle = {
        title: {
            text: '歌单风格的统计图',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: [],
                type: 'bar',
            }
        ]
    };
    const singerAddress = {
        title: {
            text: '歌手国籍的统计图',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: [],
                type: 'bar',
            }
        ]
    };

export default {
    name: "Home",
    data(){
        return{
            
            consumerCount: 0,
            songCount: 0,
            singerCount: 0,
            songListCount: 0

        }
    },
    mounted(){
      var pieDom = document.getElementById('pie');
      var pieChart = echarts.init(pieDom);
      var barDom = document.getElementById('bar');
      var barChart = echarts.init(barDom);
      var pie1Dom = document.getElementById('pie1');
      var pie1Chart = echarts.init(pie1Dom);
      var bar1Dom = document.getElementById('bar1');
      var bar1Chart = echarts.init(bar1Dom);

      this.$request.get('/countData/charts').then(res => {
        if (res.code === '200') {
          this.consumerCount = res.data.Num.consumerCount
          this.singerCount = res.data.Num.singerCount
          this.songCount = res.data.Num.songCount
          this.songListCount = res.data.Num.songListCount
          //饼图
          //用户性别
          consumerGender.series[0].data = res.data?.ConsumerGender || []
          pieChart.setOption(consumerGender)
          //歌手性别
          singerGender.series[0].data = res.data?.SingerGender || []
          pie1Chart.setOption(singerGender)

          //柱状图
          //歌单风格
          songListStyle.xAxis.data = res.data?.Style?.map(v => v.category) || []
          songListStyle.series[0].data = res.data?.Style?.map(v => v.value) || []
          barChart.setOption(songListStyle)
          //歌手国籍
          singerAddress.xAxis.data = res.data?.Address?.map(v => v.category) || []
          singerAddress.series[0].data = res.data?.Address?.map(v => v.value) || []
          bar1Chart.setOption(singerAddress)
        } else {
            this.$message.error(res.msg)
        }
      })
    },
}
</script>


<style>
  .grid-content {
    display: flex;
    align-items: center;
    height: 100px;
  }

  .grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
  }

  .grid-num {
    font-size: 30px;
    font-weight: bold;
  }

  .el-row {
    margin-bottom: 15px;
  }
</style>
  