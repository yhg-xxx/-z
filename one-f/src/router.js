import {createRouter, createWebHashHistory} from "vue-router";
const router =createRouter({
history:createWebHashHistory(import.meta.env.BASE_URL),//路由器的工作模式
    routes:[//一个个路由
        {
            path:'/',
            name:'welcome',
            component:() => import ('@/components/WelcomeView.vue'),
            children:[
                {
                    path:'',
                    name:'welcome-login',
                    component:()=> import('@/components/Login.vue')
                }
            ]
        },{
           path:'/rbao',
            name:'rbao',

            component:()=>import('@/components/Rbao.vue')

        },{
            path:'/view',
            name:'view',

            component:()=>import('@/components/IndexView.vue')

        },{
            path:'/chaxun',
            name:'chaxun',

            component:()=>import('@/components/Chaxun.vue')

        },{
            path:'/shen',
            name:'shen',

            component:()=>import('@/components/Shen.vue')

        },{
            path:'/tong',
            name:'tong',

            component:()=>import('@/components/Tong.vue')

        },{
            path:'/xiao',
            name:'xiao',

            component:()=>import('@/components/Xiao.vue')

        },{
            path:'/zhang',
            name:'zhang',

            component:()=>import('@/components/Zhang.vue')

        },{
            path:'/ping',
            name:'ping',

            component:()=>import('@/components/Ping.vue')

        },
    ]

});

export default router;
