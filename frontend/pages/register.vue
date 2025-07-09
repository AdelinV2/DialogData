<script setup lang="ts">

const {user} = useUserStorage();

if (user.value) {
  navigateTo('/');
}

</script>

<template>
  <Navbar/>
  <div class="mt-12">
    <RegisterForm/>
  </div>
</template>

<style scoped>

</style>