<script setup lang="ts">

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {user} = useUserStorage();

</script>

<template>
  <Navbar />
  <ProductCart class="min-h-[calc(100vh-250px)]" />
  <Footer />
</template>

<style scoped>

</style>