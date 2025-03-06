<template>
  <div>
    <div class="menu-container">
      <!-- 查询条件输入 -->
      <el-input
          v-model="queryParams.publishdate"
          placeholder="发表日期（格式：YYYY-MM-DD）"
          style="width: 220px; margin-right: 10px"
      ></el-input>

      <el-input
          v-model="queryParams.knowlegepoints"
          placeholder="知识点关键词"
          style="width: 180px; margin-right: 10px"
      ></el-input>

      <!-- 查询按钮 -->
      <el-button type="primary" @click="queryPersonal">查询我的日报</el-button>

      <!-- 组长/老师可见 -->
      <template v-if="['1', '2'].includes(user?.usercategory)">
        <el-button type="success" @click="queryAllStocks">查询全部日报</el-button>
      </template>
    </div>

    <!-- 日报表格 -->
    <el-table
        :data="stockList"
        style="width: 100%"
        v-loading="loading"
        empty-text="暂无数据"
    >
      <!-- 表格列定义 -->
      <el-table-column prop="id" label="日报编号" width="100" />
      <el-table-column prop="publishdate" label="发表时间" width="150" />
      <el-table-column prop="stuno" label="学号" width="120" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="spenttime" label="耗时(小时)" width="120" />
      <el-table-column prop="codeamount" label="代码量" width="120" />
      <el-table-column prop="blogsnumber" label="博客数" width="120" />
      <el-table-column prop="knowlegepoints" label="知识点" min-width="200" />
      <el-table-column prop="state" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.state === '已通过' ? 'success' : 'warning'">
            {{ row.state || '待审核' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dailyscore" label="评分" width="120" />
      <el-table-column prop="statereason" label="评语" min-width="200" />
    </el-table>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';
import {ElMessage} from 'element-plus'; // 必须导入消息组件

// 用户信息
const user = JSON.parse(sessionStorage.getItem('user')) || {};
// 日报列表
const stockList = ref([]);
// 加载状态
const loading = ref(false);
// 查询参数
const queryParams = ref({
  publishdate: '',
  knowlegepoints: ''
});

// 查询个人日报
const queryPersonal = async () => {
  try {
    loading.value = true;
    const {data} = await axios.get('http://localhost:8080/rbao/personal', {
      params: {
        ...queryParams.value,

        userId: user.userid
      },
      withCredentials: true
    });

    stockList.value = data || [];
    ElMessage.success(`找到 ${data.length} 条记录`);
  } catch (error) {
    console.error('查询失败', error);
    ElMessage.error(`查询失败: ${error.response?.data?.message || error.message}`);
  } finally {
    loading.value = false;
  }
};

// 查询全部日报（组长/老师）
const queryAllStocks = async () => {
  try {
    loading.value = true;
    const {data} = await axios.get('http://localhost:8080/rbao/x');
    stockList.value = data || [];
    ElMessage.success(`共 ${data.length} 条记录`);
  } catch (error) {
    console.error('查询失败', error);
    ElMessage.error(`查询失败: ${error.message}`);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.menu-container {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>