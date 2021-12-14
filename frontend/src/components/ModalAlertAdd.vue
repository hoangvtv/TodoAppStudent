<template>
  <v-dialog v-model="dialogAlert" max-width="500px">
    <v-card>
      <v-card-title class="headline">{{ message }}</v-card-title>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="closedialogAlert">OK</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
export default {
  data() {
    return {
      dialogAlert: false,
      message: "",
    };
  },
  methods: {
    openDialog(mess) {
      this.message = mess;
      this.dialogAlert = true;
    },
    closedialogAlert() {
      this.dialogAlert = false;
    },
  },
};
</script>
