<script setup lang="ts">

import type {Category} from "~/types/category";
import type {ListboxChangeEvent} from "primevue";
import {navigateTo} from "#app";

const categories = ref([] as Category[])
const selectedCategory = ref<Category | null>(null);
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;


$fetch(apiBaseUrl + '/category', {
  method: 'GET',
  onResponse({response}) {
    if (response.status === 200) {
      categories.value = response._data as Category[];
      console.log('Categories fetched successfully');
    }
  }
}).then((data) => {
  console.log(data);
}).catch((error) => {
  console.error('Error fetching categories:', error);
});

const onCategoryChange = (event: ListboxChangeEvent) => {
  const categoryId = event.value as number;
  navigateTo(`/category/${categoryId}`);
}

</script>

<template>
  <Listbox v-model="selectedCategory" :options="categories" option-label="name" option-value="id" class="w-full" @change="onCategoryChange">
    <template #header>
      <h2 class="text-lg font-semibold">Select a Category</h2>
    </template>
  </Listbox>
</template>

<style scoped>

</style>