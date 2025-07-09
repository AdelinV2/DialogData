<script setup lang="ts">
import type { Product } from '~/types/product'

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl

const products = ref<Product[]>([])
const pageInfo = ref({ page: 0, size: 10, totalElements: 0, totalPages: 0 })
const sort = ref({ by: 'addedDate', order: 'desc' })

const selectedCategory = ref<number | null>(null)
const searchTerm = ref('')

const fetchProducts = () => {
  const params: Record<string, any> = {
    page: pageInfo.value.page,
    size: pageInfo.value.size,
    sort: `${sort.value.by},${sort.value.order}`,
  }

  if (selectedCategory.value) params.categoryId = selectedCategory.value
  if (searchTerm.value) params.search = searchTerm.value

  const res = $fetch(`${apiBaseUrl}/products`, {
    method: 'GET',
    params: params,
    onResponse({ response }) {
      if (response.status === 200) {

        products.value = response._data.content as Product[];
        pageInfo.value.totalElements = response._data.totalElements
        pageInfo.value.totalPages = response._data.totalPages

        console.log('Products fetched successfully:', products.value)
      }
      else {
        console.error('Failed to fetch products')
      }
    },
  })
}

watch(
  [
    () => pageInfo.value.page,
    () => pageInfo.value.size,
    () => sort.value.by,
    () => sort.value.order,
    selectedCategory,
    searchTerm,
  ],
  fetchProducts,
  { immediate: true }
)

const onPageChange = (event: any) => {
  pageInfo.value.page = event.page
  pageInfo.value.size = event.rows
}

const onCategorySelected = (category: number | null) => {
  pageInfo.value.page = 0
  selectedCategory.value = category
}

const onSearch = (term: string) => {
  pageInfo.value.page = 0
  searchTerm.value = term
}

onMounted(() => {
  fetchProducts()
})

</script>

<template>
  <Navbar @search="onSearch" />

  <div class="flex mt-6">
    <div class="flex-none me-10">
      <ProductCategories @category-selected="onCategorySelected" />
    </div>
    <div class="flex-auto">
      <ProductList :products="products" />
    </div>
  </div>

  <div class="card mt-12">
    <Paginator
      :rows="pageInfo.size"
      :totalRecords="pageInfo.totalElements"
      :rowsPerPageOptions="[10,20,30]"
      :first="pageInfo.page * pageInfo.size"
      @page="onPageChange"
    />
  </div>
</template>