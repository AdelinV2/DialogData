<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";

const props = defineProps<{
  products: Product[],
  sort?: { by: string, order: string },
  sortOptions?: { label: string, value: { by: string, order: string } }[],
  loading?: boolean
  rows: number
}>()

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {user} = useUserStorage();
const layout = ref('grid');
const options = ref(['list', 'grid']);
const toast = useToast();

const getSeverity = (product: Product) => {
  if (product.availableQuantity > 5) return 'success';
  else if (product.availableQuantity > 0) return 'warn';
  else return 'danger';
}

const getStockLabel = (product: Product) => {
  if (product.availableQuantity > 5) return 'In Stock';
  else if (product.availableQuantity > 0) return 'Low Stock';
  else return 'Out of Stock';
};

const emit = defineEmits(['sort-change']);

const sortModel = computed({
  get: () => props.sort,
  set: (val) => emit('sort-change', val)
});

const goToProduct = (productId: number) => {
  navigateTo(`/product/${productId}`);
}

const addToCart = (product: Product) => {
  if (!user.value) {
    navigateTo('/login');
    return;
  }
  const cartItem = {
    productId: product.id,
    quantity: 1,
    pricePerPiece: product.price,
    totalPricePerEntry: product.price,
  } as CartEntry

  $fetch(`${apiBaseUrl}/cart/add/${user.value.id}`, {
    method: 'POST',
    body: cartItem,
    onResponse({response}) {
      if (response.status === 200) {
        toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Product added to cart successfully',
          life: 2000
        });
      } else {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to add product to cart',
          life: 3000
        });
      }
    }
  })
}
</script>

<template>
  <Toast/>
  <div class="card">
    <DataView :value="products" :layout="layout">
      <template #header>
        <div class="flex justify-between">
          <Select
              v-model="sortModel"
              :options="props.sortOptions"
              optionLabel="label"
              optionValue="value"
              placeholder="Newest First"
          />
          <div class="flex justify-end">
            <SelectButton v-model="layout" :options="options" :allowEmpty="false">
              <template #option="{ option }">
                <i :class="[option === 'list' ? 'pi pi-bars' : 'pi pi-table']"/>
              </template>
            </SelectButton>
          </div>
        </div>
      </template>
      <template #list="slotProps">
        <div class="flex flex-col">
          <div v-for="(item, index) in slotProps.items" :key="index">
            <div class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                 :class="{ 'border-t border-surface-200 dark:border-surface-700': index !== 0 }"
                 @click="goToProduct(item.id)" style="cursor: pointer">
              <div class="md:w-40 relative">
                <img class="block xl:block mx-auto rounded w-full"
                     :src="item.images[0].imageUrl" :alt="item.name"/>
                <div class="absolute bg-black/70 rounded-border" style="left: 4px; top: 4px">
                  <Tag :value="getStockLabel(item)" :severity="getSeverity(item)"></Tag>
                </div>
              </div>
              <div class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6">
                <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                  <div>
                    <div class="text-lg font-medium mt-2">{{ item.name }}</div>
                  </div>
                </div>
                <div class="flex flex-col md:items-end gap-8">
                    <span class="text-2xl font-bold">
                      <span>${{ Math.floor(item.price) }}</span>
                      <span class="text-base align-middle">{{ (item.price % 1).toFixed(2).slice(1) }}</span>
                    </span>
                  <div class="flex flex-row-reverse md:flex-row gap-2">
                    <Button icon="pi pi-shopping-cart" label="Add to Cart"
                            :disabled="item.availableQuantity === 0"
                            @click.stop="addToCart(item)"
                            class="flex-auto md:flex-initial whitespace-nowrap"></Button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
      <template #grid="slotProps">
        <div class="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          <div v-for="(item, index) in slotProps.items" :key="index"
               class="col-span-1 p-2"
               @click="goToProduct(item.id)" style="cursor: pointer">
            <div
                class="p-6 border border-surface-200 dark:border-surface-700 bg-surface-0 dark:bg-surface-900 rounded flex flex-col">
              <div class="bg-surface-50 flex justify-center rounded p-4">
                <div class="relative mx-auto">
                  <img class="rounded w-full"
                       :src="item.images[0].imageUrl" :alt="item.name"
                       style="max-width: 300px"/>
                  <div class="absolute bg-black/70 rounded-border" style="left: 4px; top: 4px">
                    <Tag :value="getStockLabel(item)" :severity="getSeverity(item)"></Tag>
                  </div>
                </div>
              </div>
              <div class="pt-6">
                <div class="flex flex-row justify-between items-start gap-2">
                  <div>
                    <div class="text-lg font-medium mt-1">{{ item.name }}</div>
                  </div>
                </div>
                <div class="flex flex-col gap-6 mt-6">
                    <span class="text-2xl font-bold">
                      <span>${{ Math.floor(item.price) }}</span>
                      <span class="text-base align-middle">{{ (item.price % 1).toFixed(2).slice(1) }}</span>
                    </span>
                  <div class="flex gap-2">
                    <Button icon="pi pi-shopping-cart" label="Add to Cart"
                            :disabled="item.availableQuantity === 0"
                            @click.stop="addToCart(item)"
                            class="flex-auto whitespace-nowrap"></Button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
      <template #empty>
        <div v-if="props.loading">
          <div v-if="layout === 'list'" class="flex flex-col">
            <div v-for="i in rows" :key="i">
              <div class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                   :class="{ 'border-t border-surface-200 dark:border-surface-700': i !== 0 }">
                <Skeleton class="!w-9/12 sm:!w-64 xl:!w-40 !h-24 mx-auto" />
                <div class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6">
                  <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                    <Skeleton width="8rem" height="2rem" />
                  </div>
                  <div class="flex flex-col md:items-end gap-8">
                    <Skeleton width="4rem" height="2rem" />
                    <Skeleton size="3rem" shape="circle" />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
            <div v-for="i in rows" :key="i" class="col-span-1 p-2">
              <div class="p-6 border border-surface-200 dark:border-surface-700 bg-surface-0 dark:bg-surface-900 rounded flex flex-col">
                <div class="bg-surface-50 flex justify-center rounded p-4">
                  <Skeleton width="75%" height="10rem" />
                </div>
                <div class="pt-6">
                  <div class="flex flex-row justify-between items-start gap-2">
                    <Skeleton width="8rem" height="2rem" />
                  </div>
                  <div class="flex flex-col gap-6 mt-6">
                    <Skeleton width="4rem" height="2rem" />
                    <Skeleton size="3rem" shape="circle" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          No available options
        </div>
      </template>
    </DataView>


<!--    <DataView :value="products" :layout="layout">-->
<!--      <template #header>-->
<!--        <div class="flex justify-end">-->
<!--          <SelectButton v-model="layout" :options="options" :allowEmpty="false">-->
<!--            <template #option="{ option }">-->
<!--              <i :class="[option === 'list' ? 'pi pi-bars' : 'pi pi-table']" />-->
<!--            </template>-->
<!--          </SelectButton>-->
<!--        </div>-->
<!--      </template>-->

<!--      <template #list>-->
<!--        <div class="flex flex-col">-->
<!--          <div v-for="i in 6" :key="i">-->
<!--            <div class="flex flex-col xl:flex-row xl:items-start p-6 gap-6" :class="{ 'border-t border-surface-200 dark:border-surface-700': i !== 0 }">-->
<!--              <Skeleton class="!w-9/12 sm:!w-64 xl:!w-40 !h-24 mx-auto" />-->
<!--              <div class="flex flex-col sm:flex-row justify-between items-center xl:items-start flex-1 gap-6">-->
<!--                <div class="flex flex-col items-center sm:items-start gap-4">-->
<!--                  <Skeleton width="8rem" height="2rem" />-->
<!--                  <Skeleton width="6rem" height="1rem" />-->

<!--                  <div class="flex items-center gap-4">-->
<!--                    <Skeleton width="6rem" height="1rem" />-->
<!--                    <Skeleton width="3rem" height="1rem" />-->
<!--                  </div>-->
<!--                </div>-->
<!--                <div class="flex sm:flex-col items-center sm:items-end gap-4 sm:gap-2">-->
<!--                  <Skeleton width="4rem" height="2rem" />-->
<!--                  <Skeleton size="3rem" shape="circle" />-->
<!--                </div>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </template>-->

<!--      <template #grid>-->
<!--        <div class="grid grid-cols-12 gap-4">-->
<!--          <div v-for="i in 6" :key="i" class="col-span-12 sm:col-span-6 xl:col-span-4 p-2">-->
<!--            <div class="p-6 border border-surface-200 dark:border-surface-700 bg-surface-0 dark:bg-surface-900 rounded">-->
<!--              <div class="flex flex-wrap items-center justify-between gap-2">-->
<!--                <Skeleton width="6rem" height="2rem" />-->
<!--                <Skeleton width="3rem" height="1rem" />-->
<!--              </div>-->
<!--              <div class="flex flex-col items-center gap-4 py-8">-->
<!--                <Skeleton width="75%" height="10rem" />-->
<!--                <Skeleton width="8rem" height="2rem" />-->
<!--                <Skeleton width="6rem" height="1rem" />-->
<!--              </div>-->
<!--              <div class="flex items-center justify-between">-->
<!--                <Skeleton width="4rem" height="2rem" />-->
<!--                <Skeleton width="6rem" height="1rem" shape="circle" size="3rem" />-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </template>-->
<!--    </DataView>-->
  </div>

</template>

<style scoped>

</style>