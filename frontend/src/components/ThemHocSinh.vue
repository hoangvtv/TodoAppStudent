<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="600px">
      <v-card>
        <v-card-title style="background: blueviolet">
          <span class="headline">{{ title }}</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6" md="6">
                <v-text-field
                  label="Tên Học Sinh*"
                  required
                  v-model="hocSinh.hoTen"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-select
                  :items="lstLop"
                  item-text="tenLop"
                  item-value="lopHocId"
                  label="Chọn Lớp*"
                  required
                  v-model="hocSinh.lopHoc.lopHocId"
                ></v-select>
              </v-col>
              <v-col cols="12" sm="6">
                <v-menu
                  ref="menu"
                  v-model="menu"
                  :close-on-content-click="false"
                  :return-value.sync="hocSinh.ngaySinh"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="hocSinh.ngaySinh"
                      label="Ngày sinh*"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="hocSinh.ngaySinh" no-title scrollable>
                    <v-spacer></v-spacer>
                    <v-btn text color="primary" @click="menu = false">
                      Cancel
                    </v-btn>
                    <v-btn
                      text
                      color="primary"
                      @click="$refs.menu.save(hocSinh.ngaySinh), getDate()"
                    >
                      OK
                    </v-btn>
                  </v-date-picker>
                </v-menu>
              </v-col>

              <v-col cols="12" sm="6" md="6">
                <v-text-field
                  label="Quê Quán*"
                  required
                  v-model="hocSinh.diaChi"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog = false">
            Close
          </v-btn>
          <v-btn color="blue darken-1" text @click="save"> {{ btn }} </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <modal-alert-add ref="thongbao"></modal-alert-add>
  </v-row>
</template>

<script>
// import { EventBus } from "@/main.js";
import axios from "axios";
import moment from "moment";
import ModalAlertAdd from "../components/ModalAlertAdd.vue";
export default {
  components: {
    ModalAlertAdd,
  },
  data: () => ({
    title: "",
    dialog: false,
    hocSinh: {
      id: 0,
      hoTen: "",
      lopHoc: {
        lopHocId: 0,
      },
      ngaySinh: new Date().toISOString().substr(0, 10),
      diaChi: "",
    },
    lstLop: [],
    laCapNhat: false,
    btn: "",
    menu: false,
    erroradd: "",
  }),
  created() {
    // EventBus.$on("dialog", this.dialogOpen);
    this.getDSLop();
  },
  methods: {
    getDate() {
      moment(this.hocSinh.ngaySinh).format("YYYY-MM-DD");
    },

    dialogOpen(val, val2, val3, val4) {
      this.title = val;
      this.hocSinh = val2;
      this.dialog = true;
      this.laCapNhat = val3;
      this.btn = val4;
    },
    createPost() {
      axios
        .post("http://localhost:8888/hocVien/themHocVien", this.hocSinh)
        .then(() => {
          this.$emit("getHVien");
        })
        .catch((error) => {
          if (error.response) {
            this.erroradd = error.response.data;
            // console.log(this.erroradd)
            this.getThongBao(this.erroradd);
          }
        });
    },
    getDSLop() {
      axios.get("http://localhost:8888/lopHoc/dsLop").then((response) => {
        this.lstLop = response.data;
      });
    },

    save() {
      this.dialog = false;
      if (this.laCapNhat) {
        this.updatePost();
      } else {
        this.createPost();
      }
    },
    updatePost() {
      axios
        .put("http://localhost:8888/hocVien/suaHocVien", this.hocSinh)
        .then(() => {
          this.$emit("getHVien");
        });
    },
    getThongBao() {
      this.$refs.thongbao.openDialog(this.erroradd);
    },
  },
};
</script>
<style scoped></style>
