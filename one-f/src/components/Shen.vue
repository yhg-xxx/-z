<template>
  <div>
    <!-- 退出登录按钮 -->
    <el-button @click="router.push('/')" type="danger" :icon="SwitchButton">退出登录</el-button>
    <!-- 查询日报按钮 -->
    <el-button @click="router.push('/chaxun')" type="primary" :icon="Search" >查询</el-button>
    <!-- 日报列表 -->
    <el-table :data="dailyList" stripe style="width: 100%">
      <el-table-column prop="id" label="日报编号" />
      <el-table-column prop="publishdate" label="发表时间" />
      <el-table-column prop="stuno" label="学号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="spenttime" label="当天花费时间" />
      <el-table-column prop="codeamount" label="代码量" />
      <el-table-column prop="blogsnumber" label="发表博客数量" />
      <el-table-column prop="knowlegepoints" label="所学知识点" />
      <el-table-column prop="state" label="日报状态" />
      <el-table-column prop="dailyscore" label="评分" />
      <el-table-column prop="statereason" label="评语" />
      <el-table-column fixed="right" label="审核日报" min-width="150">

        <template #default="{ row }">
          <!-- 传递row对象 -->
          <el-button @click="approveDaily(row)" type="success">通过</el-button>
          <el-button @click="rejectDaily(row)" type="danger">退回</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import router from '@/router.js';
import { Search, SwitchButton} from "@element-plus/icons-vue";

const dailyList = ref([])
onMounted(async () => {
  await getDailyList()
})

const getDailyList = async () => {
  const res = await axios.get("http://localhost:8080/rbao/x",{
    withCredentials: true
  });
  dailyList.value = res.data.list || res.data;

};



// 通过按钮处理
const approveDaily = async (row) => {
  try {
    const response = await axios.post(
        `http://localhost:8080/updateState/${row.id}`, // 使用反引号和正确路径
        { state: '通过' } // 只传state字段
    );
    if (response.data.success) {
      ElMessage.success(response.data.message);
      await getDailyList()
    } else {
      ElMessage.error(response.data.message);
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message);
  }
};

// 退回按钮处理
const rejectDaily = async (row) => {
  try {
    const response = await axios.post(
        `http://localhost:8080/updateState/${row.id}`, // 使用反引号和正确路径
        { state: '退回' } // 只传state字段
    );
    if (response.data.success) {
      ElMessage.success(response.data.message);
      getDailyList();
    } else {
      ElMessage.error(response.data.message);
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message);
  }
};
</script>

<style scoped>

</style>