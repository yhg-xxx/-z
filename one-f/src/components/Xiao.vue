<template>
  <div>
    <div class="header-container">
      <el-button @click="logout" type="danger" :icon="SwitchButton">退出登录</el-button>
      <div class="user-info">
        当前用户：{{ currentUser.username }}（{{ userRoleText }}）
      </div>
    </div>

    <!-- 组员表格 -->
    <div class="table-container">
      <h3>学生列表</h3>
      <el-table :data="dailyList" stripe style="width: 100%">
        <el-table-column prop="userid" label="学号" width="120" />
        <el-table-column prop="username" label="姓名" width="120" />
        <el-table-column prop="groupno" label="组号" width="100" />
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="{ row }">
            <el-button
                @click="openEditDailyDialog(row)"
                type="warning"
                :disabled="currentUser.usercategory === '0'"
            >
              发送消息
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 消息表格 -->
    <div class="table-container">
      <h3>消息列表</h3>
      <el-table :data="currentPageData" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="类别" width="100">
          <template #default="{ row }">
            {{ messageCategory[row.category] }}
          </template>
        </el-table-column>
        <el-table-column prop="sendername" label="发送人" width="120" />
        <el-table-column prop="receivername" label="接收人" width="120" />
        <el-table-column prop="content" label="内容" />
        <el-table-column prop="state" label="状态" width="100" />
        <el-table-column prop="createTime" label="时间" width="180" />
      </el-table>
      <!-- 分页组件 -->
      <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :total="dailyList1.length"
          layout="total, sizes, prev, pager, next, jumper"
          style="margin-top: 20px"
      />
    </div>

    <!-- 发送消息对话框 -->
    <el-dialog v-model="editDailyDialogVisible" title="发送消息" width="40%">
      <el-form
          :model="editDailyForm"
          :rules="formRules"
          ref="editDailyFormRef"
          label-width="80px"
      >
        <el-form-item label="类别" prop="category">
          <el-select
              v-model="editDailyForm.category"
              placeholder="请选择消息类型"
              clearable
          >
            <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
              v-model="editDailyForm.content"
              type="textarea"
              :rows="4"
              placeholder="请输入消息内容"
              maxlength="200"
              show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDailyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEditDaily">发送</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import router from '@/router'
import { SwitchButton } from '@element-plus/icons-vue'

// 响应式数据
const dailyList = ref([])
const dailyList1 = ref([])
const editDailyDialogVisible = ref(false)
const selectedReceiver = ref({})
const currentUser = ref({})
const editDailyForm = ref({
  category: '',
  content: ''
})

// 表单验证规则
const formRules = {
  category: [
    { required: true, message: '请选择消息类型', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入消息内容', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在5到200个字符', trigger: 'blur' }
  ]
}

// 计算属性
const userRoleText = computed(() => {
  return {
    '0': '学生',
    '1': '组长',
    '2': '老师'
  }[currentUser.value.usercategory]
})

const messageCategory = computed(() => ({
  '0': '催交',
  '1': '形式不合格',
  '2': '内容不合格'
}))

const categoryOptions = computed(() => [
  { value: '0', label: '催交' },
  { value: '1', label: '形式不合格' },
  { value: '2', label: '内容不合格' }
])

// 生命周期钩子
onMounted(async () => {
  const user = JSON.parse(sessionStorage.getItem('user'))
  if (!user) {
    ElMessage.error('请先登录')
    router.push('/login')
    return
  }

  currentUser.value = user

  if (user.usercategory === '0') {
    ElMessage.error('学生无权访问此页面')
    router.push('/')
    return
  }

  await getDailyList()
  await getDailyList1()
})

// 方法
const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    sessionStorage.removeItem('user')
    router.push('/login')
  })
}

const getDailyList = async () => {
  try {
    const res = await axios.get('http://localhost:8080/users', {
      withCredentials: true
    })
    const rawData = res.data.list || res.data

    dailyList.value = rawData.filter(user => {
      // 老师：所有学生
      if (currentUser.value.usercategory === '2') {
        return user.usercategory === '0'
      }
      // 组长：本组学生
      if (currentUser.value.usercategory === '1') {
        return user.groupno === currentUser.value.groupno &&
            user.usercategory === '0'
      }
      return false
    })
  } catch (error) {
    ElMessage.error('获取学生列表失败: ' + error.message)
  }
}



const openEditDailyDialog = (row) => {
  selectedReceiver.value = row
  editDailyForm.value = {
    category: '',
    content: ''
  }
  editDailyDialogVisible.value = true
}

const submitEditDaily = async () => {
  try {
    const formData = {
      ...editDailyForm.value,
      sendername: currentUser.value.username,
      senderno: currentUser.value.userid,
      receivername: selectedReceiver.value.username,
      receiverno: selectedReceiver.value.userid,
      state: '未读',
      createTime: new Date().toISOString()
    }

    await axios.post('http://localhost:8080/xiaoxi', formData, {
      withCredentials: true
    })

    ElMessage.success('消息发送成功')
    editDailyDialogVisible.value = false
    await getDailyList1()
  } catch (error) {
    ElMessage.error('发送失败: ' + error.message)
  }
}

// 分页相关变量
const currentPage = ref(1);
const pageSize = ref(5);

// 计算当前页数据
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return dailyList1.value.slice(start, end);
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
const getDailyList1 = async () => {
  try {
    const res = await axios.get('http://localhost:8080/xiaoxi/x', {
      withCredentials: true
    })
    const rawData = res.data.list || res.data

    // 老师：所有消息
    if (currentUser.value.usercategory === '2') {
      dailyList1.value = rawData
    }
    // 组长：本组学生的消息
    else if (currentUser.value.usercategory === '1') {
      const groupStudents = dailyList.value.map(s => s.userid)
      dailyList1.value = rawData.filter(msg =>
          groupStudents.includes(msg.receiverno))
    }
  } catch (error) {
    ElMessage.error('获取消息列表失败: ' + error.message)
  }currentPage.value = 1; // 数据更新后回到第一页
}

</script>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.user-info {
  font-size: 16px;
  color: #666;
}

.table-container {
  margin: 20px 0;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

h3 {
  margin-bottom: 15px;
  color: #333;
}
</style>