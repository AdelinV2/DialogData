<script setup lang="ts">

import {Role} from "~/types/role";

const { user } = useUserStorage();

onMounted(() => {
  if (!user.value || user.value.role !== Role.ADMIN) {
    navigateTo('/');
  }
})

</script>

<template>

  <Navbar />

  <AdminAddNewsletter />

  <Footer />

</template>

<style scoped>

</style>