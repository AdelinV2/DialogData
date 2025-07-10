<script setup lang="ts">

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {user} = useUserStorage();

</script>

<template>
  <Navbar />
  <ProductCart />
  <Footer />
</template>

<style scoped>

</style>