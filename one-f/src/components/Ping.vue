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
          <el-button @click="openScoreDialog(row)" type="success">评分</el-button>
          <el-button @click="openCommentDialog(row)" type="danger">评语</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 评分弹窗 -->
    <el-dialog
        v-model="scoreDialogVisible"
        title="日报评分"
        width="30%"
    >
      <el-input
          v-model="scoreValue"
          label="请输入分数"
      />
      <template #footer>
        <el-button @click="scoreDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitScore">确定</el-button>
      </template>
    </el-dialog>

    <!-- 评语弹窗 -->
    <el-dialog
        v-model="commentDialogVisible"
        title="日报评语"
        width="40%"
    >
      <el-input
          v-model="commentValue"
          type="textarea"
          :rows="4"
          placeholder="请输入评语"
      />
      <template #footer>
        <el-button @click="commentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComment">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElLoading } from 'element-plus';
import axios from 'axios';
import router from '@/router.js';
import { Search, SwitchButton } from "@element-plus/icons-vue";

// 响应式数据
const dailyList = ref([]);
const loading = ref(false);
const scoreDialogVisible = ref(false);
const commentDialogVisible = ref(false);
const currentDailyId = ref(null);
const scoreValue = ref('');
const commentValue = ref('');

// 方法定义
const openScoreDialog = (row) => {
  currentDailyId.value = row.id;
  scoreValue.value = row.dailyscore ?row.dailyscore.toString():'';
  scoreDialogVisible.value = true;
};

const openCommentDialog = (row) => {
  currentDailyId.value = row.id;
  commentValue.value = row.statereason || '';
  commentDialogVisible.value = true;
};

const getDailyList = async () => {
  try {
    loading.value = true;
    const res = await axios.get('http://localhost:8080/rbao/x', {
      withCredentials: true
    });

    // 添加过滤逻辑
    const rawData = res.data?.list || res.data || [];
    dailyList.value = rawData.filter(item => item.state === "通过");

  } catch (error) {
    ElMessage.error('数据加载失败');
  } finally {
    loading.value = false;
  }
};

const submitScore = async () => {
  try {
    await axios.put(`http://localhost:8080/updateScore`, {
      id: currentDailyId.value,
      dailyscore: scoreValue.value.toString()
    });
    ElMessage.success('评分成功');
    await getDailyList();
    scoreDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('评分失败');
  }
};

const submitComment = async () => {
  try {
    await axios.put(`http://localhost:8080/updateComment`, {
      id: currentDailyId.value,
      statereason: commentValue.value
    });
    ElMessage.success('评语提交成功');
    await getDailyList();
    commentDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('评语提交失败');
  }
};

// 生命周期钩子
onMounted(() => {
  getDailyList();
});
</script>

<style scoped>
.container {
  padding: 20px;
}

.button-group {
  margin-bottom: 20px;
}

.button-group .el-button {
  margin-right: 10px;
}

.el-table {
  margin-top: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
</style>