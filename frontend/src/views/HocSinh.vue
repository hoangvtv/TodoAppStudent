<template>
  <v-app>
    <div id="header">
      <div id="search ">
        <!--<input
          v-model="search"
          class="search"
          type="search"
          name="Search"
          placeholder="Search..."
        />
        <v-btn x-small @click="timKiem"><i class="fas fa-search"></i></v-btn> -->
        <v-text-field
          style="width: 200px"
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
      </div>
      <div id="add">
        <v-btn color="primary" dark shaped @click="openMyDialog">
          <i class="fas fa-plus-circle"></i>
        </v-btn>
      </div>
    </div>

    <v-data-table
      :headers="headers"
      :items="desserts"
      sort-by="hoTen"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="initialize"> Reset </v-btn>
      </template>
    </v-data-table>
    <them-hoc-sinh @getHVien="getHVien" ref="themHocSinh"></them-hoc-sinh>
    <modal-alert ref="thongBao" @confirm="confirm"></modal-alert>
  </v-app>
</template>

<script>
import axios from "axios";
import ThemHocSinh from "@/components/ThemHocSinh.vue";
import ModalAlert from "@/components/ModalAlert.vue";
// import { EventBus } from "@/main.js";
import moment from "moment";
export default {
  components: {
    ThemHocSinh,
    ModalAlert,
  },
  data: () => ({
    dialog: false,
    dialogDelete: false,
    search: "",
    headers: [
      {
        text: "Tên Học Viên",
        align: "start",
        sortable: false,
        value: "hoTen",
      },
      { text: "Lớp", value: "lopHoc.tenLop" },
      { text: "Ngày Sinh", value: "ngaySinh" },
      { text: "Quê Quán", value: "diaChi" },
      { text: "Thao tác", value: "actions", sortable: false },
    ],
    desserts: [],
    editedIndex: -1,
    editedItem: {},
    post: {},
    laCapNhat: false,
    defaultItem: {
      idHocVien: 0,
      hoTen: "",
      lopHoc: {
        lopHocId: 0,
      },
      diaChi: "",
    },
    isConfirm: false,
  }),

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  created() {
    this.getPosts();
  },

  methods: {
    initialize() {
      this.desserts = [];
    },

    editItem(item) {
      this.laCapNhat = true;
      this.$refs.themHocSinh.dialogOpen(
        "CHỈNH SỬA THÔNG TIN",
        item,
        this.laCapNhat,
        "Cập Nhập"
      );
    },

    deleteItem(item) {
      this.editedItem = item;
      // console.log(this.editedItem);
      this.$refs.thongBao.openDialog();
    },
    confirm() {
      this.deleteHocSinh();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem);
      } else {
        this.desserts.push(this.editedItem);
      }
      this.close();
    },

    openMyDialog() {
      let hocSinh = {
        hoTen: "",
        lopHoc: {
          lopHocId: 0,
        },
        diaChi: "",
      };
      this.laCapNhat = false;
      this.$refs.themHocSinh.dialogOpen(
        "THÊM HỌC SINH",
        hocSinh,
        this.laCapNhat,
        "Thêm"
      );
    },

    formatDate(date) {
      return moment(date).format("YYYY-MM-DD");
    },

    getPosts() {
      axios.get("http://localhost:8888/hocVien/dsHocVien").then((response) => {
        this.desserts = response.data;
        for (let i = 0; i < this.desserts.length; i++) {
          this.desserts[i].ngaySinh = this.formatDate(
            this.desserts[i].ngaySinh
          );
        }
      });
    },

    deleteHocSinh() {
      axios
        .delete(
          `http://localhost:8888/hocVien/xoaHocVien?idHocVien=${this.editedItem.idHocVien}`
        )
        // .delete("http://localhost:8888/hocVien/xoaHocVien/" + this.editedItem.id)
        .then(() => {
          this.getPosts();
        });
    },

    getHVien() {
      this.getPosts();
    },

    // getDataFromName() {
    //   axios
    //     .get("http://localhost:8888/hocVien/timKiem?timKiem=" + this.search)
    //     .then((response) => {
    //       //console.log(response.data);
    //       this.desserts = response.data;
    //       for (let i = 0; i < this.desserts.length; i++) {
    //         this.desserts[i].ngaySinh = this.formatDate(
    //           this.desserts[i].ngaySinh
    //         );
    //       }
    //     });
    // },
    // timKiem() {
    //   this.getDataFromName();
    // },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#header {
  height: 70px;
}
#search {
  margin-bottom: 20px;
}
#add {
  display: inline;
  float: right;
}
</style>
