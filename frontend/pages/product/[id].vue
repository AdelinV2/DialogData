<script setup lang="ts">

import type {Product} from "~/types/product";

const id = useRoute().params.id as string;
const product = ref<Product | null>(null);

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

$fetch(`${apiBaseUrl}/products/${id}`, {
  method: 'GET',
  onResponse({response}) {
    if (response.status === 200) {
      product.value = response._data as Product;
      console.log('Product fetched successfully');
    }
  }
}).then((data) => {
  console.log(data);
}).catch((error) => {
  console.error('Error fetching product:', error);
})

</script>

<template>
  <Navbar />
  <ProductDetails :product="product" v-if="product" />
</template>

<style scoped>

</style>