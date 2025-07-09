<script setup lang="ts">

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {user} = useUserStorage();

if (!user.value) {
  navigateTo('/login');
}

</script>

<template>
  <Navbar />
  <ProductCart />
</template>

<style scoped>

</style>