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
                  label="Tên Lớp Học*"
                  required
                  v-model="lopHoc.tenLop"
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
  </v-row>
</template>

<script>
// import { EventBus } from "@/main.js";
import axios from "axios";
export default {
  data: () => ({
    title: "",
    dialog: false,
    lopHoc: {
      lopHocId: 0,
      tenLop: "",
      siSo: 0,
    },
    lstLop: [],
    laCapNhat: false,
    btn: "",
  }),
  created() {
    // EventBus.$on("dialog", this.dialogOpen);
    this.getDSLop();
  },
  methods: {
    dialogOpen(val, val2, val3, val4) {
      this.title = val;
      this.lopHoc = val2;
      this.dialog = true;
      this.laCapNhat = val3;
      this.btn = val4;
    },
    createPost() {
      axios
        .post("http://localhost:8888/lopHoc/themLopHoc", this.lopHoc)
        .then(() => {
          this.$emit("getLopHoc");
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
        .put("http://localhost:8888/lopHoc/sualophoc", this.lopHoc)
        .then(() => {
          this.$emit("getLopHoc");
        });
    },
  },
};
</script>
<style scoped></style>
