<template>
  <div>
    <div>
      <el-button @click="router.push('/')" type="danger" :icon="SwitchButton">退出登录</el-button>
      <el-button @click="router.push('/chaxun')" type="primary" :icon="Search">查询</el-button>
    </div>
    <div>
      <el-table :data="summaryList" stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column prop="stuno" label="学号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="submitCount" label="提交数量"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import router from "@/router.js";
import { Search, SwitchButton } from "@element-plus/icons-vue";
import { onMounted, ref } from 'vue';
import axios from 'axios';

const summaryList = ref([]); // 修改变量名以明确这是汇总后的数据

onMounted(async () => {
  await getDailyList();
});

const getDailyList = async () => {
  try {
    const res = await axios.get("http://localhost:8080/rbao/x", {
      withCredentials: true
    });
    const rawData = res.data.list || res.data;

    // 使用对象存储分组结果（键为stuno）
    const groupedData = {};
    rawData.forEach(item => {
      const key = item.stuno;
      if (!groupedData[key]) {
        // 如果学号不存在，创建新分组
        groupedData[key] = {
          stuno: item.stuno,
          name: item.name,
          submitCount: 1
        };
      } else {
        // 如果学号已存在，增加计数
        groupedData[key].submitCount++;
      }
    });

    // 将对象转换为数组
    summaryList.value = Object.values(groupedData);
  } catch (error) {
    console.error('获取数据失败:', error);
  }
};
</script>

<style scoped>
</style>