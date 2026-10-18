import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Dashboard from '@/views/Dashboard.vue';

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
/*   {
  path: '/register',
  name: 'register',
  component: Register
  }, */
  {
    path: '/dashboard',
    name: 'dashboard',
    component: Dashboard
  }
];

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes,
})

export default router
