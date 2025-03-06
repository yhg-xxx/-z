<template>
  <div>
    <!-- 退出登录按钮 -->
    <el-button @click="router.push('/')" type="danger" :icon="SwitchButton">退出登录</el-button>
    <!-- 新增日报按钮 -->
    <el-button @click="openAddDailyDialog" type="primary" :icon="Plus">新增日报</el-button>
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

      <el-table-column fixed="right" label="操作" min-width="150">

        <template #default="{ row, $index }">
          <el-button @click="openEditDailyDialog(row)" type="warning">修改日报</el-button>
          <el-button @click="deleteDaily(row.id)" type="danger">删除日报</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增日报对话框 -->
    <el-dialog v-model="addDailyDialogVisible" title="新增日报" @close="clearAddDailyForm">
      <el-form :model="addDailyForm" ref="addDailyFormRef" label-width="100px">
        <el-form-item label="发表时间" prop="publishdate">
          <el-input v-model="addDailyForm.publishdate" placeholder="请输入发表时间"></el-input>
        </el-form-item>
        <el-form-item label="当天花费时间" prop="spenttime">
          <el-input v-model="addDailyForm.spenttime" placeholder="请输入当天花费时间"></el-input>
        </el-form-item>
        <el-form-item label="代码量" prop="codeamount">
          <el-input v-model="addDailyForm.codeamount" placeholder="请输入代码量"></el-input>
        </el-form-item>
        <el-form-item label="发表博客数量" prop="blogsnumber">
          <el-input v-model="addDailyForm.blogsnumber" placeholder="请输入博客数量"></el-input>
        </el-form-item>
        <el-form-item label="所学知识点" prop="knowlegepoints">
          <el-input v-model="addDailyForm.knowlegepoints" placeholder="所学知识点"></el-input>
        </el-form-item>
      </el-form>
      <!--foot-->
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDailyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddDaily">确定</el-button>
      </div>
    </el-dialog>

    <!-- 修改日报对话框 -->
    <el-dialog v-model="editDailyDialogVisible" title="修改日报" @close="clearEditDailyForm">
      <el-form :model="editDailyForm" ref="editDailyFormRef" label-width="100px">
        <el-form-item label="发表时间" prop="publishdate">
          <el-input v-model="editDailyForm.publishdate" placeholder="请输入发表时间"></el-input>
        </el-form-item>
        <el-form-item label="当天花费时间" prop="spenttime">
          <el-input v-model="editDailyForm.spenttime" placeholder="当天花费时间"></el-input>
        </el-form-item>
        <el-form-item label="代码量" prop="codeamount">
          <el-input v-model="editDailyForm.codeamount" placeholder="请输入代码量"></el-input>
        </el-form-item>
        <el-form-item label="发表博客数量" prop="blogsnumber">
          <el-input v-model="editDailyForm.blogsnumber" placeholder="请输入博客数量"></el-input>
        </el-form-item>
        <el-form-item label="所学知识点" prop="knowlegepoints">
          <el-input v-model="editDailyForm.knowlegepoints" placeholder="请输入所学知识点"></el-input>
        </el-form-item>
        <!--foot-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDailyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditDaily">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {ElMessage} from "element-plus";
import axios from "axios";
import router from "@/router.js";
import {Plus, Search, SwitchButton} from "@element-plus/icons-vue";

const currentUser = ref({})
const dailyList = ref([]); // 日报数据列表
const addDailyDialogVisible = ref(false); // 新增日报对话框
const editDailyDialogVisible = ref(false); // 修改日报对话框

onMounted(async () => {
  const user = JSON.parse(sessionStorage.getItem('user'))
  if (!user) {
    ElMessage.error('请先登录')
    router.push('/login')
    return
  }
    currentUser.value = user
    await getDailyList()
    await setCurrentDate();
  })

// 设置当前日期，格式为 YYYYMMDD
function setCurrentDate() {
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始，因此要加1
  const day = String(currentDate.getDate()).padStart(2, '0');
  addDailyForm.value.publishdate = `${year}${month}${day}`; // 生成 "YYYYMMDD" 格式的日期
}
// 新增日报表单
const addDailyForm = ref({
  publishdate: '',
  spenttime: '',
  codeamount: '',
  blogsnumber: '',
  knowlegepoints: '',
  state: '待审核',      // 状态（不需要用户输入，直接设为初始值）
  dailyscore: '未评分', // 日报评分（不需要用户输入，直接设为初始值）
  statereason: '无'     // 状态原因（不需要用户输入，直接设为初始值）
});

// 修改日报表单
const editDailyForm = ref({
  publishdate: '',
  spenttime: '',
  codeamount: '',
  blogsnumber: '',
  knowlegepoints: '',
  state: '待审核',      // 状态（不需要用户输入，直接设为初始值）
  dailyscore: '未评分', // 日报评分（不需要用户输入，直接设为初始值）
  statereason: '无'     // 状态原因（不需要用户输入，直接设为初始值）
});

// 打开新增日报对话框
const openAddDailyDialog = () => {
  addDailyDialogVisible.value = true;
};
// 打开修改日报对话框
const openEditDailyDialog = (row) => {
  editDailyForm.value = {...row};
  editDailyDialogVisible.value = true;
};

// 清空新增日报表单
const clearAddDailyForm = () => {
  addDailyForm.value = {
    publishdate: '',
    spenttime: '',
    codeamount: '',
    blogsnumber: '',
    knowlegepoints: '',
  };
};

// 清空修改日报表单
const clearEditDailyForm = () => {
  editDailyForm.value = {
    publishdate: '',
    spenttime: '',
    codeamount: '',
    blogsnumber: '',
    knowlegepoints: '',
  };
};
//显示数据
const getDailyList = async () => {
  try {
  const res = await axios.get("http://localhost:8080/rbao/x", {
    withCredentials: true
  });
  const rawData = res.data.list || res.data;
  dailyList.value = rawData.filter(rbao =>
      String(rbao.stuno) === String(currentUser.value.userid)
  );
  } catch (error) {
    ElMessage.error('获取学生列表失败: ' + error.message)
  }


};


//  提交新增表单
const submitAddDaily = async () => {
  try {
    const formData = {
      ...addDailyForm.value,
      stuno: currentUser.value.userid,
      name: currentUser.value.username,
    }

   await axios.post('http://localhost:8080/rbao',  formData, {
     withCredentials: true
   })
  console.log(addDailyForm.value)
  await getDailyList()
  ElMessage.success('日报新增成功！');
  addDailyDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('发送失败: ' + error.message)
  }
}

// 删除日报
const deleteDaily = async (id) => {
  try {
    const response = await axios.delete(`http://localhost:8080/rbao/${id}`);
    await getDailyList()
    if (response.data === 1)
      ElMessage.success('删除成功！');
    else {
      ElMessage.error("删除失败！");
    }
    // 这里可以处理删除后的逻辑，比如重新获取部门列表
  } catch (error) {
    console.error('删除失败:', error);
    // 这里可以处理错误，比如显示错误提示
  }
};

// 提交修改表单
const submitEditDaily = async () => {
  const response = await axios.put(`http://localhost:8080/rbao/${editDailyForm.value.id}`, editDailyForm.value);
  if (response.data === 1)
    ElMessage.success('修改成功！');
  else {
    ElMessage.error("修改失败");
  }
  await getDailyList()
  editDailyDialogVisible.value = false;
};

</script>
<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
