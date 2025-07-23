<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";
import {Role} from "~/types/role";

const props = defineProps<{
  products: Product[],
  sort?: { by: string, order: string },
  sortOptions?: { label: string, value: { by: string, order: string } }[],
  loading?: boolean
  rows: number
}>()

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {user} = useUserStorage();
const {t} = useI18n();
const layout = ref('grid');
const options = ref(['list', 'grid']);
const toast = useToast();

const getSeverity = (product: Product) => {
  if (product.availableQuantity > 5) return 'success';
  else if (product.availableQuantity > 0) return 'warn';
  else return 'danger';
}

const getStockLabel = (product: Product) => {
  if (product.availableQuantity > 5) return t('product.inStock');
  else if (product.availableQuantity > 0) return t('product.limitedStock');
  else return t('product.outOfStock');
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
    product: product,
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
          detail: t('product.addedToCart'),
          life: 2000
        });
      } else {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: t('product.failedToAdd'),
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
          <ProductAttributes/>
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
                  <div>
                    <div class="bg-surface-100 p-1 bg-white" style="border-radius: 30px">
                      <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                           style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">
                          <span class="text-surface-900 font-medium text-sm text-gray-900">
                            {{ item.averageRating || 0 }}
                          </span>
                        <i class="pi pi-star-fill text-yellow-500"></i>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="flex flex-col md:items-end gap-8">
                  <div class="flex flex-row">
                    <Button v-if="user && user.role === Role.ADMIN"
                            icon="pi pi-pencil"
                            @click.stop="navigateTo(`/admin/product/${item.id}`)"
                            class="p-button-info self-end me-6"/>
                    <div v-if="item.promotionPrice" class="flex flex-row">
                      <div class="flex flex-col">
                        <span class="text-red-600 font-bold text-3xl">
                        <span>${{ Math.floor(item.promotionPrice) }}</span>
                          <span class="text-base align-middle">{{ (item.promotionPrice % 1).toFixed(2).slice(1) }}</span>
                        </span>
                      </div>
                      <span class="text-gray-400 line-through text-xl ms-4">${{ item.price.toFixed(2) }}</span>
                    </div>
                    <div v-else>
                     <span class="text-2xl font-bold">
                       <span>${{ Math.floor(item.price) }}</span>
                       <span class="text-base align-middle">{{ (item.price % 1).toFixed(2).slice(1) }}</span>
                      </span>
                    </div>
                  </div>
                  <div class="flex flex-row-reverse md:flex-row gap-2">
                    <Button icon="pi pi-shopping-cart" :label="t('product.addToCart')"
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
                  <div class="flex flex-row">
                    <div v-if="item.promotionPrice" class="flex flex-row">
                      <div class="flex flex-col">
                        <span class="text-red-600 font-bold text-3xl">
                        <span>${{ Math.floor(item.promotionPrice) }}</span>
                          <span class="text-base align-middle">{{ (item.promotionPrice % 1).toFixed(2).slice(1) }}</span>
                        </span>
                      </div>
                      <span class="text-gray-400 line-through text-xl ms-4">${{ item.price.toFixed(2) }}</span>
                    </div>
                    <div v-else>
                     <span class="text-2xl font-bold">
                       <span>${{ Math.floor(item.price) }}</span>
                       <span class="text-base align-middle">{{ (item.price % 1).toFixed(2).slice(1) }}</span>
                      </span>
                    </div>
                    <Button v-if="user && user.role === Role.ADMIN"
                            icon="pi pi-pencil"
                            @click.stop="navigateTo(`/admin/product/${item.id}`)"
                            class="p-button-info ml-auto"/>
                    <div class="ml-auto">
                      <div class="bg-surface-100 p-1 bg-white" style="border-radius: 30px">
                        <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                             style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">
                          <span class="text-surface-900 font-medium text-sm text-gray-900">
                            {{ item.averageRating || 0 }}
                          </span>
                          <i class="pi pi-star-fill text-yellow-500"></i>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="flex gap-2">
                    <Button icon="pi pi-shopping-cart" :label="t('product.addToCart')"
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
                <Skeleton class="!w-9/12 sm:!w-64 xl:!w-40 !h-24 mx-auto"/>
                <div class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6">
                  <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                    <Skeleton width="8rem" height="2rem"/>
                  </div>
                  <div class="flex flex-col md:items-end gap-8">
                    <Skeleton width="4rem" height="2rem"/>
                    <Skeleton size="3rem" shape="circle"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
            <div v-for="i in rows" :key="i" class="col-span-1 p-2">
              <div
                  class="p-6 border border-surface-200 dark:border-surface-700 bg-surface-0 dark:bg-surface-900 rounded flex flex-col">
                <div class="bg-surface-50 flex justify-center rounded p-4">
                  <Skeleton width="75%" height="10rem"/>
                </div>
                <div class="pt-6">
                  <div class="flex flex-row justify-between items-start gap-2">
                    <Skeleton width="8rem" height="2rem"/>
                  </div>
                  <div class="flex flex-col gap-6 mt-6">
                    <Skeleton width="4rem" height="2rem"/>
                    <Skeleton size="3rem" shape="circle"/>
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

  </div>

</template>

<style scoped>

</style>