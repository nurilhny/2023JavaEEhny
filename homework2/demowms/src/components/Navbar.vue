<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="true" class="hamburger-container" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="true"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="true"/>

    <div class="right-menu">
      <template v-if="true">
        <search id="header-search" class="right-menu-item" />

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img src="../assets/images/user1.jpeg" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided>
            <span @click="loginOut">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import Breadcrumb from './Breadcrumb'
import TopNav from './TopNav'
import Hamburger from './Hamburger'
import Screenfull from './Screenfull'
import SizeSelect from './SizeSelect'
import Search from './HeaderSearch'

import axios from "axios";
axios.defaults.headers.post['Content-Type']='application/json;charset=UTF-8';


export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search
  },
  methods: {
    loginOut(){

      axios.defaults.headers.common['token']=this.$store.state.token;
      axios.get("http://localhost:8181/common/logout").then((response)=>{
        console.log('我tm来啦');
        console.log(response.data);
        if(response.data.success){
          this.$message({
            type:"success",
            message:"退出成功！"
          })
          this.$store.dispatch("setToken",'');
          this.$store.dispatch("setUserid",'');
          this.$store.dispatch("setUsertype",'');
          this.$store.dispatch("setUsername",'');
          this.$router.push('/login');
        }else {
          this.$message({
            type:"error",
            message:"退出失败！"
          })
        }
      });


    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 66px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
