<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="订单编号" prop="orderId" >
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单编号"
          clearable
          :disabled="querySingle"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          v-if="$store.state.usertype==='user'"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          v-if="$store.state.usertype==='user'"
          @click="handleCancel"

        >取消</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          v-if="$store.state.usertype==='user'"
          @click="handleDelete"

        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="orderId" />
      <el-table-column label="商品" align="center" prop="productName"/>
      <el-table-column label="数量" align="center" prop="quantity"/>
      <el-table-column label="发货地址" align="center" prop="srcAddress"/>
      <el-table-column label="收货地址" align="center" prop="desAddress"/>
      <el-table-column label="状态" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus==='completed'" type="success">
            已签收
          </el-tag>
          <el-tag v-else-if="scope.row.orderStatus==='commit'" type="warning">
            待运输
          </el-tag>
          <el-tag v-else-if="scope.row.orderStatus==='shipping'" type="warning">
            配送中
          </el-tag>
          <el-tag v-else type="info">
            已取消
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-more"
              @click="handleDetail(scope.row)"
              v-if="$store.state.usertype==='user'"

          >查看</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-more"
              v-if="$store.state.usertype==='user'"
              @click="handleAccept(scope.row)"

          >签收</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-more"
              v-if="$store.state.usertype==='courier'"
              @click="handleShip(scope.row)"

          >取单</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              v-if="$store.state.usertype==='courier'"
              @click="handleSite(scope.row)"

          >转站</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="$store.state.usertype==='user'"
            @click="handleCancel(scope.row)"

          >取消</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="$store.state.usertype==='user'"
            @click="handleDelete(scope.row)"

          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="myForm" :model="myForm" label-width="100px">
        <el-form-item label="商品" prop="productName">
          <el-select v-model="myForm.productName" placeholder="商品" clearable >
            <el-option
                v-for="product in this.products"
                :key="product.productName"
                :label="product.productName"
                :value="product.productName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="收货地址" prop="desAddress">
          <el-input v-model="myForm.desAddress" placeholder="收货地址" ></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input v-model="myForm.quantity" placeholder="数量" ></el-input>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm(myForm)">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 改站对话框 -->
    <el-dialog title="转站" :visible.sync="openSite" width="800px" append-to-body>
      <el-form ref="myForm" :model="myForm" label-width="100px">
        <el-form-item label="站点" prop="siteId">
          <el-select v-model="myForm.siteId" placeholder="站名" clearable >
            <el-option
                v-for="site in this.sites"
                :key="site.siteId"
                :label="site.siteName"
                :value="site.siteId"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSite(myForm)">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="订单详情" :visible.sync="detail" width="1000px" append-to-body>
      <el-table v-loading="loading" :data="itemList" height="400px">

        <el-table-column label="订单编号" align="center" prop="orderId" />
        <el-table-column label="订单状态" align="center" prop="orderStatus">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderStatus==='completed'" type="success">
              已签收
            </el-tag>
            <el-tag v-else-if="scope.row.orderStatus==='commit'" type="warning">
              待运输
            </el-tag>
            <el-tag v-else-if="scope.row.orderStatus==='shipping'" type="warning">
              配送中
            </el-tag>
            <el-tag v-else type="info">
              已取消
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="送货员" align="center" prop="courierName"/>
        <el-table-column label="当前所在站" align="center" prop="siteName"/>

      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";
axios.defaults.headers.post['Content-Type']='application/json;charset=UTF-8';
import dayjs from "dayjs";

export default {
  name: "Order",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单表格数据
      postList: [],
      // 订单项目数据
      itemList: [],
      // 商品数据
      products:[],
      // 站点数据
      sites:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示转站层
      openSite: false,
      // 是否弹出详情层
      detail: false,
      // 查询互斥单量
      querySingle: false,
      // 查询互斥多量
      queryMutex: false,
      // 查询参数
      queryParams: {
        orderId: undefined,
      },
      // 表单参数
      myForm: {
      },

    };
  },

  created() {
    axios.defaults.headers.common['token']=this.$store.state.token;
    this.getList();
    this.getProducts();
    this.getSites();
  },
  methods: {
    /** 查询订单列表 */
    getList() {
      this.loading = true;
      axios.get("http://localhost:8181/orders/"+this.$store.state.usertype+"/"+this.$store.state.userid).then((response)=>{
        console.log('我tm来啦');
        console.log(response.data);
        if(Array.isArray(response.data)){
          this.postList = response.data;
        }else {
          this.postList = [];
          this.postList.push(response.data);
        }
      });
      this.loading = false;

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.detail = false;
      this.openSite = false;
      this.reset();
      this.getList();
    },
    // 表单重置
    reset() {
      this.myForm ={
      };
      this.itemList = [];
      this.resetForm("form");
    },
    resetForm(form){
      this.form = form;
      this.queryParams = {
        orderId: undefined,
      };
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.queryParams.orderId){
        axios.get("http://localhost:8181/orders/"+this.queryParams.orderId).then((response)=>{
          this.loading = true;
          if(response.data===""){
            this.postList = [];
          }else {
            console.log(response);
            this.postList = [];
            this.postList.push(response.data);
          }

          this.loading = false;
        });
      }
      else if(this.queryParams.paymentMethod){
        if(this.queryParams.ordersStatus){
          axios.get("http://localhost:8181/orders/paymentMethod/"+this.queryParams.paymentMethod+"/"+this.queryParams.ordersStatus)
              .then((response)=>{
            this.loading = true;
            // 判断是不是列表
            if(Array.isArray(response.data)){
              this.postList = response.data;
            }else {
              this.postList = [];
              this.postList.push(response.data);

            }
            this.loading = false;
          });
        }else {
          axios.get("http://localhost:8181/orders/paymentMethod/"+this.queryParams.paymentMethod).then((response)=>{
            this.loading = true;
            // 判断是不是列表
            if(Array.isArray(response.data)){
              this.postList = response.data;
            }else {
              this.postList = [];
              this.postList.push(response.data);

            }
            this.loading = false;
          });
        }
      }
      else if(this.queryParams.ordersStatus){
        axios.get("http://localhost:8181/orders/status/"+this.queryParams.ordersStatus).then((response)=>{
          this.loading = true;
          // 判断是不是列表
          if(Array.isArray(response.data)){
            this.postList = response.data;
          }else {
            this.postList = [];
            this.postList.push(response.data);

          }
          this.loading = false;
        });
      }
      else {
        this.getList();
      }


    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },
    /** 取消按钮操作 */
    handleCancel(row) {
      this.reset();
      axios.put('/orders/user/'+row.orderId+"/cancelled").then((response)=>{
        let flag = response.data;
        if(flag){
          this.$modal.msgSuccess("取消成功!");
        }else {
          this.$modal.msgError("取消失败!");
        }

        this.cancel();
        this.getList();
      });

    },
    /** 签收按钮操作 */
    handleAccept(row) {
      this.reset();
      axios.put('/orders/user/'+row.orderId+"/completed").then((response)=>{
        let flag = response.data;
        if(flag){
          this.$modal.msgSuccess("签收成功!");
        }else {
          this.$modal.msgError("签收失败!");
        }
        this.getList();
      });

    },
    /** 取单按钮操作 */
    handleShip(row) {
      this.reset();
      let order = {
        orderId:row.orderId,
        orderStatus:'shipping',
        courierId:this.$store.state.userid
      };
      axios.put('/orders/courier',order).then((response)=>{
        let flag = response.data;
        if(flag){
          this.$modal.msgSuccess("取单成功!");
        }else {
          this.$modal.msgError("取单失败!");
        }
        this.getList();
      });

    },
    /** 转站按钮操作 */
    handleSite(row) {
      this.reset();
      this.openSite = true;
      this.myForm.orderId = row.orderId;
      this.myForm.courierId = this.$store.state.userid;
    },
    /** 提交按钮 */
    async submitForm(orders) {
      orders.userId = this.$store.state.userid;
      console.log(orders);

      // 添加订单
      axios.post('/orders/user', orders).then((response)=>{
        console.log("1");
        let flag = response.data;
        if(flag){
          this.$modal.msgSuccess("添加成功！");
        }else {
          this.$modal.msgError("添加失败!");
        }

        this.cancel();
        this.getList();
      }).catch((err) => {
        console.log(err);
        this.$modal.msgError("添加失败!");
        this.cancel();
        this.getList();
      });

    },
    /** 提交按钮 */
    async submitSite(order) {

      console.log(order);

      // 添加订单
      axios.put('/orders/courier/changeSite', order).then((response)=>{
        console.log("1");
        let flag = response.data;
        if(flag){
          this.$modal.msgSuccess("转站成功！");
        }else {
          this.$modal.msgError("转站失败!");
        }

        this.cancel();
        this.getList();
      }).catch((err) => {
        console.log(err);
        this.$modal.msgError("转站失败!");
        this.cancel();
        this.getList();
      });

    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ordersIds = row.orderId || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + ordersIds + '"的数据项？').then(function() {

      }).then(() => {
        axios.delete('/orders/'+ordersIds).then((response)=>{
          let flag = response.data;
          if(flag){
            this.$modal.msgSuccess("删除成功!");
          }else {
            this.$modal.msgError("删除失败!");
          }

          this.getList();
        });



      }).catch(() => {});
    },
    // 处理详情操作
    handleDetail(row){
      axios.get("http://localhost:8181/orders/user/detail/"+row.orderId).then((response)=>{
        this.itemList = [];
        this.itemList.push(response.data);
        this.detail = true;
      })
    },
    // 获取全部商品
    getProducts(){
      axios.get("http://localhost:8181/products/").then((response)=>{
        console.log('我tm带着商品来啦');
        if(Array.isArray(response.data)){
          this.products = response.data;
        }else {
          this.products = [];
          this.products.push(response.data);
        }
      });
    },
    // 获取全部站点
    getSites(){
      axios.get("http://localhost:8181/sites/").then((response)=>{
        console.log('我tm带着站点来啦');
        if(Array.isArray(response.data)){
          console.log(response.data);
          this.sites = response.data;
        }else {
          this.sites = [];
          this.sites.push(response.data);
        }
      });
    },

  },
  filters:{
    timeFormater(value){
      return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
    },
    categoryFormater(value){
      if (value === "electronic"){
        return "电子支付";
      }else if(value === "cash"){
        return "现金支付";
      }else {
        return "其他支付";
      }
    },
  },
};
</script>

<style>

</style>