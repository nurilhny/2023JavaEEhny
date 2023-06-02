import VueRouter from 'vue-router'
import Login from "../views/Login";
import Register from "../views/Register"
import MyLayout from "../views/MyLayOut"
import User from "../views/User";
import Order from "../views/Order";


export default new VueRouter({
    routes:[
        {
            path:'/',
            redirect:'/login'

        }
        ,
        {
            path: '/login',
            component:Login
        }
        ,
        {
            path: '/register',
            component:Register
        }
        ,
        {
            path: '/welcome',
            component:MyLayout,
            children:[
                {
                    path:'user',
                    component:User
                },
                {
                    path:'order',
                    component:Order
                }

            ]
        }

    ]
})

