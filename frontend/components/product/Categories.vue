<script setup lang="ts">
import type { Category } from '~/types/category'
import type {ListboxChangeEvent} from "primevue";

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl
const categories = ref<Category[]>([])
const selectedCategory = ref<number|null>(null)
const { t } = useI18n()

const emit = defineEmits<{
  (e: 'category-selected', categoryId: number|null): void
}>()

$fetch(`${apiBaseUrl}/category`, {
  method: 'GET',
  onResponse({ response }) {
    if (response.status === 200) {
      categories.value = response._data as Category[]
      console.log('Categories fetched successfully:', categories.value)
    } else {
      console.error('Failed to fetch categories')
    }
  }
})

const onCategoryChange = (event: ListboxChangeEvent) => {
  selectedCategory.value = event.value as number
  emit('category-selected', selectedCategory.value)
}

</script>

<template>
  <Listbox
    v-model="selectedCategory"
    :options="categories"
    option-label="name"
    option-value="id"
    class="w-full"
    @change="onCategoryChange"
  >
    <template #header>
      <h2 class="text-lg font-semibold">{{ t('product.categories') }}</h2>
    </template>
  </Listbox>
</template>

<style scoped>

</style>