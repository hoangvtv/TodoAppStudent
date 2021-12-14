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
          v-model="search"
          style="width: 200px"
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
      sort-by="tenLop"
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
    <them-lop-hoc @getLopHoc="getLopHoc" ref="themLopHoc"></them-lop-hoc>
    <modal-alert ref="thongBao" @confirm="confirm"></modal-alert>
  </v-app>
</template>

<script>
import axios from "axios";
import ModalAlert from "@/components/ModalAlert.vue";
// import { EventBus } from "@/main.js";
import ThemLopHoc from "../components/ThemLopHoc.vue";
export default {
  components: {
    ModalAlert,
    ThemLopHoc,
  },
  data: () => ({
    dialog: false,
    dialogDelete: false,
    search: "",
    headers: [
      {
        text: "Tên Lớp Học",
        align: "start",
        sortable: false,
        value: "tenLop",
      },
      { text: "Sỉ số", value: "siSo" },
      { text: "Thao tác", value: "actions", sortable: false },
    ],
    desserts: [],
    editedIndex: -1,
    editedItem: {},
    post: {},
    laCapNhat: false,
    defaultItem: {
      lopHocId: 0,
      tenLop: "",
      siSo: 0,
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
      this.$refs.themLopHoc.dialogOpen(
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
      this.deleteLopHoc();
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
      let lopHoc = {
        tenLop: "",
        siSo: 0,
      };
      this.laCapNhat = false;
      this.$refs.themLopHoc.dialogOpen(
        "THÊM LỚP HỌC",
        lopHoc,
        this.laCapNhat,
        "Thêm"
      );
    },

    getPosts() {
      axios.get("http://localhost:8888/lopHoc/dsLop").then((response) => {
        this.desserts = response.data;
      });
    },

    deleteLopHoc() {
      axios
        .delete(
          `http://localhost:8888/lopHoc/xoalophoc?lopHocId=${this.editedItem.lopHocId}`
        )
        // .delete("http://localhost:8888/hocVien/xoaHocVien/" + this.editedItem.id)
        .then(() => {
          this.getPosts();
        });
    },

    getLopHoc() {
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
    // },`
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
