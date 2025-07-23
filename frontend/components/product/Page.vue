<script setup lang="ts">
import type {Product} from '~/types/product'
import {ref} from "vue";

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl
const route = useRoute()
const router = useRouter()

const {t} = useI18n()
const {attributeValue} = useAttributeValue()
const products = ref<Product[]>([])
const promotedProducts = ref<Product[]>([])
const pageInfo = ref({page: 0, size: 12, totalElements: 0, totalPages: 0})
const sort = ref({by: 'addedDate', order: 'desc'})

const sortOptions = [
  {label: t('navbar.newestFirst'), value: {by: 'addedDate', order: 'desc'}},
  {label: t('navbar.oldestFirst'), value: {by: 'addedDate', order: 'asc'}},
  {label: t('navbar.priceHighToLow'), value: {by: 'price', order: 'desc'}},
  {label: t('navbar.priceLowToHigh'), value: {by: 'price', order: 'asc'}}
]

const responsiveOptions = ref([
  {
    breakpoint: '991px',
    numVisible: 4
  },
  {
    breakpoint: '767px',
    numVisible: 3
  },
  {
    breakpoint: '575px',
    numVisible: 1
  }
]);

const headerImages = ref([
  {
    itemImageSrc: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=2400&q=80',
    alt: 'Ultrawide Desert',
    thumbnailImageSrc: 'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80'
  },
  {
    itemImageSrc: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=2400&q=80',
    alt: 'Ultrawide Forest',
    thumbnailImageSrc: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80'
  },
  {
    itemImageSrc: 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=2400&q=80',
    alt: 'Ultrawide Lake',
    thumbnailImageSrc: 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=400&q=80'
  }
])

const selectedCategory = ref<number | null>(null)
const searchTerm = ref('')

if (route.query.categoryId) {
  selectedCategory.value = Number(route.query.categoryId)
}
if (route.query.search) {
  searchTerm.value = String(route.query.search)
}
if (route.query.attributeValue) {
  const attrValues = Array.isArray(route.query.attributeValue)
      ? route.query.attributeValue
      : [route.query.attributeValue];
  attributeValue.value = attrValues.map(value => ({
    value: String(value),
    id: undefined,
    attribute: {
      id: undefined,
      name: '',
      type: ''
    },
    product: undefined
  }));
}

const fetchPromotedProducts = () => {
  $fetch(`${apiBaseUrl}/products/promoted`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Fetching promoted products')
        promotedProducts.value = response._data as Product[];
        console.log('Promoted products fetched successfully:', promotedProducts.value)
      } else {
        console.error('Failed to fetch promoted products')
      }
    },
  })
}

const fetchProducts = () => {
  const params: Record<string, any> = {
    page: pageInfo.value.page,
    size: pageInfo.value.size,
    sort: `${sort.value.by},${sort.value.order}`,
  }

  if (selectedCategory.value) params.categoryId = selectedCategory.value
  if (searchTerm.value) params.search = searchTerm.value
  if (attributeValue.value.length > 0) {
    params.attributeValue = attributeValue.value.map(av => av.value);
  }

  const res = $fetch(`${apiBaseUrl}/products`, {
    method: 'GET',
    params: params,
    onResponse({response}) {
      if (response.status === 200) {

        console.log('Fetching products with params:', params)

        products.value = response._data.content as Product[];
        pageInfo.value.totalElements = response._data.totalElements
        pageInfo.value.totalPages = response._data.totalPages
        isLoading.value = false

        console.log('Products fetched successfully:', products.value)
      } else {
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
      attributeValue,
    ],
    fetchProducts,
    {immediate: true}
)


watch(
    attributeValue,
    (newVal, oldValue) => {
      router.replace({
        query: {
          ...route.query,
          attributeValue: newVal.map(av => av.value),
        }
      });

    }, { deep: true})

const onSortChange = (newSort: { by: string, order: string }) => {
  sort.value = newSort;
  pageInfo.value.page = 0;
}

const onPageChange = (event: any) => {
  pageInfo.value.page = event.page
  pageInfo.value.size = event.rows
}

const onCategorySelected = (category: number | null) => {
  pageInfo.value.page = 0
  selectedCategory.value = category

  router.replace({
    query: {
      ...route.query,
      categoryId: category
    }
  })
}

const onSearch = (term: string) => {
  pageInfo.value.page = 0
  searchTerm.value = term

  router.replace({
    query: {
      ...route.query,
      search: term,
    }
  })
}

const isLoading = ref(true)

fetchPromotedProducts();

</script>

<template>
  <Navbar @search="onSearch"/>


  <div class="container mx-auto min-h-[calc(100vh-330px)]">
    <div class="flex mt-6 mb-10">
      <div style="min-width: 180px;">
        <ProductCategories @category-selected="onCategorySelected" class="flex-1 h-full"/>
      </div>
      <div class="flex-1 flex justify-center">
        <div class="card">
          <Galleria :value="headerImages" :responsiveOptions="responsiveOptions" :numVisible="5" :circular="true"
                    containerStyle="width: 640px" :auto-play="true" :transition-interval="5000"
                    :showItemNavigators="true" :showThumbnails="false" :showItemNavigatorsOnHover="true"
                    :showIndicators="true">
            <template #item="slotProps">
              <img :src="slotProps.item.itemImageSrc" :alt="slotProps.item.alt" style="width: 100%; display: block;"/>
            </template>
            <template #thumbnail="slotProps">
              <img :src="slotProps.item.thumbnailImageSrc" :alt="slotProps.item.alt" style="display: block;"/>
            </template>
          </Galleria>
        </div>
      </div>
    </div>

    <ProductPromoted :products="promotedProducts" class="my-10"/>

    <div class="flex-auto me-5">
      <ProductList :rows="pageInfo.size" :loading="isLoading" :products="products" :sort="sort"
                   :sortOptions="sortOptions" @sort-change="onSortChange"/>
    </div>
  </div>
  <div class="card mt-12">
    <Paginator
        :rows="pageInfo.size"
        :totalRecords="pageInfo.totalElements"
        :rowsPerPageOptions="[12,24,36]"
        :first="pageInfo.page * pageInfo.size"
        @page="onPageChange"
    />
  </div>
  <Footer/>
</template>

<style scoped>

</style>