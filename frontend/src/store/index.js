import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

// export default new Vuex.Store({
//   state: {
//     isAdmin: false,
//   },
//   mutations: {},
//   actions: {},
//   modules: {},
// });

const storeData = {
  state: {
    auth: {
      isAdmin: false,
    },
  },
};

const store = new Vuex.Store(storeData);
export default store;
