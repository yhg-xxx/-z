<template>
  <!-- 退出登录按钮 -->
  <el-button @click="router.push('/')" type="danger" :icon="SwitchButton">退出登录</el-button>
  <!-- 消息列表 -->
  <el-table :data="currentPageData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号" />
    <el-table-column prop="category" label="消息类别" />
    <el-table-column prop="sendername" label="发送人姓名" />
    <el-table-column prop="senderno" label="发送人学号" />
    <el-table-column prop="receivername" label="接收人姓名" />
    <el-table-column prop="receiverno" label="接受人学号" />
    <el-table-column prop="content" label="内容" />
    <el-table-column prop="state" label="状态" />
    <el-table-column fixed="right" label="操作" min-width="150">

      <template #default="{row}">
        <el-button
            @click="approveDaily(row)"
            :type="row.state === '已读' ? 'success' : 'danger'"
            :disabled="row.state === '已读'">
          {{ row.state === '已读' ? '已读' : '标记为已读' }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页组件 -->
  <el-pagination
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
      :current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      :total="dailyList.length"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px"
  />
</template>

<script setup>
import { onMounted, ref, computed } from 'vue';
import {ElMessage} from "element-plus";
import axios from "axios";
import router from "@/router.js";
import {SwitchButton} from "@element-plus/icons-vue";

//解析登录者信息
const user = JSON.parse(sessionStorage.getItem('user'));
const dailyList = ref([]); // 消息数据列表
//定义方法
onMounted(() => {
  getDailyList();
});

//已读按钮处理
const approveDaily = async (row) => {
  try {
    const response = await axios.post(
        `http://localhost:8080/updateState1/${row.id}`, // 使用反引号和正确路径
        { state: '已读' } // 只传state字段
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
// 分页相关变量
const currentPage = ref(1);
const pageSize = ref(5);

// 计算当前页数据
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return dailyList.value.slice(start, end);
});

// 分页事件处理
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1; // 每页条数改变时重置到第一页
};
//获取消息数据，并过滤。接受者为登录者
// 修改获取数据方法，重置页码
const getDailyList = async () => {
  const res = await axios.get("http://localhost:8080/xiaoxi/x", {
    withCredentials: true
  });

  const rawData = res.data.list || res.data;
  dailyList.value = rawData.filter(xiaoxi =>
      String(xiaoxi.receiverno) === String(user.userid)
  );
  currentPage.value = 1; // 数据更新后回到第一页
};
</script>
<style scoped>

</style>