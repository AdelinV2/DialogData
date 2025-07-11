<script setup lang="ts">

const {user} = useUserStorage();

if (user.value) {
  navigateTo('/');
}

</script>

<template>

  <Navbar/>
  <div class="mt-12 min-h-[calc(100vh-250px)]">
    <RegisterForm/>
  </div>

  <Footer/>

</template>

<style scoped>

</style>