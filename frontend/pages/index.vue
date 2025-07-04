<script setup lang="ts">

import type {Product} from "~/types/product";

const products = ref([] as Product[]);

$fetch('http://localhost:8080/api/products', {
  method: 'GET',
  onResponse({response}) {
    if (response.status === 200) {
      products.value = response._data.content as Product[];
      console.log('Products fetched successfully');
    }
  }
}).then((data) => {
  console.log(data);
}).catch((error) => {
  console.error('Error fetching products:', error);
});

</script>

<template>
  <Navbar/>
  <ProductList :products="products"/>
</template>

<style scoped>

</style>