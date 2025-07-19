<script setup lang="ts">

const { user } = useUserStorage();

onMounted(() => {
  if (!user.value || user.value.role !== 'ADMIN') {
    navigateTo('/');
  }
});

</script>

<template>

  <Navbar />
  <AdminAddProduct />
  <Footer />

</template>

<style scoped>

</style>