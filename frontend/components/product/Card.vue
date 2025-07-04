<script setup lang="ts">

import type {Product} from "~/types/product";

defineProps<{
  product: Product
}>()

// const product = {
//   id: 1,
//   name: 'Sample Product',
//   description: 'This is a sample product description.',
//   price: 19.99,
// } as Product;

</script>

<template>

  <Card :product="product" style="width: 240px; height: 400px; overflow: hidden">
    <template #header> <!-- TODO use product's image -->
      <img alt="user header" style="height: 15rem;" src="https://picsum.photos/240"/>
    </template>
    <template #title>{{ product.name }}</template>
    <template #footer>
      <div class="flex gap-10 mt-1">
        <span class="text-2xl font-bold">${{ product.price.toFixed(2) }}</span>
        <Button severity="primary" class="p-2">
          <i class="pi pi-shopping-cart"></i>
        </Button>
      </div>
    </template>
  </Card>

</template>

<style scoped>

</style>