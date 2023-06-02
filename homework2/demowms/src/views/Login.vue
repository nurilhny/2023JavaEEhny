<template>
  <div class="loginbody">
    <div class="logindata">
      <div class="logintext">
        <h2>物流管理系统</h2>
      </div>
      <div class="formdata">
        <el-form ref="form" :model="form" :rules="rules">
          <el-form-item prop="username">
            <el-input
                v-model="form.username"
                clearable
                placeholder="请输入账号"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="form.password"
                clearable
                placeholder="请输入密码"
                show-password
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="butt">
        <el-button type="primary" @click.native.prevent="login(form)"
        >登录</el-button
        >
        <el-button class="shou" @click="register">注册</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
axios.defaults.headers.post['Content-Type']='application/json;charset=UTF-8';
export default {
  name: "Login",
  data() {
    return {
      form: {
        password: "",
        username: "",
      },
      checked: false,
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { max: 10, message: "不能大于10个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { max: 10, message: "不能大于10个字符", trigger: "blur" },
        ],
      },
    };
  },
  mounted() {
    if(localStorage.getItem("news")){
      this.form=JSON.parse(localStorage.getItem("news"))
      this.checked=true
    }
  },
  methods: {
    login(form){
      let user = {
        password: "",
        username: "",
        };

      user.username = form.username;
      user.password = form.password;


      this.$store.dispatch("setUsername",user.username)

      console.log(form);
      axios.post("http://localhost:8181/common/login",user).then((response)=>{
        let flag = response.data.success;
        console.log(flag);
        if (flag){
          let curuser = response.data.data.user;
          this.$store.dispatch("setUserid",curuser.userId);
          this.$store.dispatch("setUsertype",curuser.userType);
          this.$store.dispatch("setToken",response.data.data.token);
          if(curuser.userType=="user"){
            this.$router.push("/welcome/order");
          }else if(curuser.userType=="manager"){
            this.$router.push("/welcome/user");
          }else if(curuser.userType=="courier"){
            this.$router.push("/welcome/order")
          }else {
            this.$message({
              type: 'error',
              message: '该用户信息有误！'
            });
          }

        }else{
          this.$message({
            type: 'error',
            message: '登录信息有误！'
          });
        }

      })

    },
    remenber(data){
      this.checked=data
      if(this.checked){
        localStorage.setItem("news",JSON.stringify(this.form))
      }else{
        localStorage.removeItem("news")
      }
    },
    forgetpas() {
      this.$message({
        type:"info",
        message:"功能尚未开发额",
        showClose:true
      })
    },
    register() {
      this.$router.push("/register");
    },
  },
};
</script>

<style scoped>
.loginbody {
  width: 100%;
  height: 100%;
  background-image: url("../assets/login.jpg");
  background-size: 100% 100%;
  background-position: center center;
  overflow: auto;
  background-repeat: no-repeat;
  position: fixed;
  line-height: 100%;

}

.logintext {
  margin-bottom: 20px;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
  color: white;
  text-shadow: 2px 2px 4px #000000;
}

.logindata {
  width: 400px;
  height: 300px;
  transform: translate(-50%);
  margin-left: 50%;
}

.tool {
  display: flex;
  justify-content: space-between;
  color:#606266;
}

.butt {
  margin-top: 10px;
  text-align: center;
}

.shou {
  cursor: pointer;
  color: #606266;
}

</style>