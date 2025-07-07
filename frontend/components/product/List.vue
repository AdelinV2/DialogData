<script setup lang="ts">

import type {Product} from "~/types/product";

defineProps<{
  products: Product[],
}>()

</script>

<template>

  <div class="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-10">
    <ProductCard v-for="product in products" :key="product.id" :product="product" />
  </div>

</template>

<style scoped>

</style>