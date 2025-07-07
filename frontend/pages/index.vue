<script setup lang="ts">

import type {Product} from "~/types/product";

const products = ref([] as Product[]);

const pageInfo = ref({
  page: 0,
  size: 10,
  totalElements: 0,
  totalPages: 0
});

const sort = ref({
  by: 'addedDate',
  order: 'desc'
})

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

onMounted(() => {
  fetchProducts();
});

const fetchProducts = () => {
  $fetch(`${apiBaseUrl}/products`, {
    method: 'GET',
    params: {
      page: pageInfo.value.page,
      size: pageInfo.value.size,
      sort: `${sort.value.by},${sort.value.order}`
    },
    onResponse({response}) {
      if (response.status === 200) {
        products.value = response._data.content as Product[];
        pageInfo.value.totalElements = response._data.totalElements;
        pageInfo.value.totalPages = response._data.totalPages;
        console.log('Products fetched successfully');
      }
    }
  }).then((data) => {
    console.log(data);
  }).catch((error) => {
    console.error('Error fetching products:', error);
  });
}
watch(() => [pageInfo.value.page, pageInfo.value.size, sort.value.by, sort.value.order], fetchProducts, {immediate: true});

const onPageChange = (event: any) => {
  pageInfo.value.page = event.page;
  pageInfo.value.size = event.rows;
  fetchProducts();
}

</script>

<template>
  <Navbar/>

  <div class="flex justify-end mt-6 me-6">
    <Dropdown
        v-model="sort"
        :options="[
        { label: 'Newest', value: { by: 'addedDate', order: 'desc' } },
        { label: 'Price Low to High', value: { by: 'price', order: 'asc' } },
        { label: 'Price High to Low', value: { by: 'price', order: 'desc' } }
      ]"
        optionLabel="label"
        optionValue="value"
        placeholder="Sort By"
        @change="(e) => { sort = e.value; }"
        class="w-56"
    />
  </div>

  <div class="mt-6" style="display: flex;">
    <div class="ms-2 me-10" style="flex: 0 0 auto;">
      <ProductCategories/>
    </div>
    <div style="flex: 1;">
      <ProductList :products="products"/>
    </div>
  </div>

  <div class="card mt-12">
    <Paginator :rows="pageInfo.size" :totalRecords="pageInfo.totalElements" :rowsPerPageOptions="[10, 20, 30]"
               :first="pageInfo.page * pageInfo.size" @page="onPageChange"></Paginator>
  </div>
</template>

<style scoped>

</style>