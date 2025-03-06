<template>

  <div>
    <div class="top-container">
      <h2 class="welcome-message">欢迎 {{ user.usercategory }}!</h2>
      <div v-if="user.usercategory === '0'" class="menu-container">
        <div class="menu-item">
          <router-link to="/rbao" class="menu-link">
            <el-icon><ChromeFilled /></el-icon>
            学生管理
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/zhang" class="menu-link">
            <el-icon><Message/></el-icon>
            查看站内消息
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/" class="menu-link">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </router-link>
        </div>
      </div>

      <div v-else-if="user.usercategory === '1'" class="menu-container">
        <div class="menu-item">
          <router-link to="/shen" class="menu-link">
            <el-icon><SuitcaseLine /></el-icon>
            审核日报
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/tong" class="menu-link">
            <el-icon><DocumentChecked /></el-icon>
            发表情况统计
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/xiao" class="menu-link">
            <el-icon><Edit /></el-icon>
            通知学生
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/" class="menu-link">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </router-link>
        </div>
      </div>

      <div v-else-if="user.usercategory === '2'" class="menu-container">
        <div class="menu-item">
          <router-link to="/ping" class="menu-link">
            <el-icon><SuitcaseLine/></el-icon>
            日报评分
          </router-link>
        </div>
        <div class="menu-item">
          <router-link to="/tong" class="menu-link">
            <el-icon><DocumentChecked /></el-icon>
            发表情况统计
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/xiao" class="menu-link">
            <el-icon><Edit /></el-icon>
            通知学生
          </router-link>
        </div>

        <div class="menu-item">
          <router-link to="/" class="menu-link">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
//导入icon
import {
  ChromeFilled,
  DocumentChecked,
  Edit,
  Message,
  SuitcaseLine,
  SwitchButton
} from "@element-plus/icons-vue";

//解析user
const user = JSON.parse(sessionStorage.getItem('user'));

</script>

<style scoped>
.top-container {
  position: sticky;
  top: 0;
  background-color: #f4f6f9;
  z-index: 1000;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-bottom: 2px solid #ddd; /* 增加底部边框 */
}

.welcome-message {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #2c3e50; /* 深色文字 */
  background: linear-gradient(to right, #3498db, #2980b9); /* 渐变色背景 */
}

.menu-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 15px;
}

.menu-item {
  padding: 12px 20px;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  width: 220px; /* 固定宽度 */

}

.menu-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.menu-link {
  display: flex;
  align-items: center;
  color: #3498db;
  font-size: 18px;
  text-decoration: none;
  font-weight: 500;
  padding: 5px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.menu-link:hover {
  color: #fff;
  background-color: #3498db;
}


@media (max-width: 768px) {
  .menu-container {
    flex-direction: column;
    align-items: center;
  }

  .menu-item {
    width: 100%; /* 在移动端菜单项宽度自适应 */
  }
}

</style>