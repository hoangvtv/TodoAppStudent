import Vue from "vue";
import VueRouter from "vue-router";
import HocSinh from "../views/HocSinh.vue";
import LopHoc from "../views/LopHoc.vue";

Vue.use(VueRouter);
Vue.use(require("vue-moment"));

const routes = [
  {
    path: "/",
    name: "HocVien",
    component: HocSinh,
  },
  {
    path: "/lophoc",
    name: "LopHoc",
    component: LopHoc,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
