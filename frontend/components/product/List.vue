<script setup lang="ts">

import type {Product} from "~/types/product";
import {ref} from "vue";

defineProps<{
  products: Product[],
}>()

const layout = ref('grid');
const options = ref(['list', 'grid']);

const getSeverity = (product: Product) => {
  if (product.availableQuantity > 5) {
    return 'success';
  } else if (product.availableQuantity > 0) {
    return 'warn';
  } else {
    return 'danger';
  }
}

</script>

<template>

  <!--  <div class="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-10">-->
  <!--    <ProductCard v-for="product in products" :key="product.id" :product="product"/>-->
  <!--  </div>-->


  <div class="card">
    <DataView :value="products" :layout="layout">
      <template #header>
        <div class="flex justify-end">
          <SelectButton v-model="layout" :options="options" :allowEmpty="false">
            <template #option="{ option }">
              <i :class="[option === 'list' ? 'pi pi-bars' : 'pi pi-table']"/>
            </template>
          </SelectButton>
        </div>
      </template>

      <template #list="slotProps">
        <div class="flex flex-col">
          <div v-for="(item, index) in slotProps.items" :key="index">
            <div class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                 :class="{ 'border-t border-surface-200 dark:border-surface-700': index !== 0 }">
              <div class="md:w-40 relative">
                <img class="block xl:block mx-auto rounded w-full"
                     :src="item.imageUrls[0]" :alt="item.name"/>
                <div class="absolute bg-black/70 rounded-border" style="left: 4px; top: 4px">
                  <Tag :value="item.inventoryStatus" :severity="getSeverity(item)"></Tag>
                </div>
              </div>
              <div class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6">
                <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                  <div>
                    <div class="text-lg font-medium mt-2">{{ item.name }}</div>
                  </div>
                  <!--                    RATING    -->

                  <!--                    <div class="bg-surface-100 p-1" style="border-radius: 30px">-->
                  <!--                      <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"-->
                  <!--                           style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">-->
                  <!--                        <span class="text-surface-900 font-medium text-sm">{{ item.rating }}</span>-->
                  <!--                        <i class="pi pi-star-fill text-yellow-500"></i>-->
                  <!--                      </div>-->
                  <!--                    </div>-->
                </div>
                <div class="flex flex-col md:items-end gap-8">
                  <span class="text-2xl font-bold">
                    <span>${{ Math.floor(item.price) }}</span>
                    <span class="text-base align-middle">{{ (item.price % 1).toFixed(2).slice(1) }}</span>
                  </span>
                  <div class="flex flex-row-reverse md:flex-row gap-2">
                    <Button icon="pi pi-shopping-cart" label="Buy Now"
                            :disabled="item.inventoryStatus === 'OUTOFSTOCK'"
                            class="flex-auto md:flex-initial whitespace-nowrap"></Button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>

      <template #grid="slotProps">
        <div class="grid grid-cols-12 gap-4">
          <div v-for="(item, index) in slotProps.items" :key="index"
               class="col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-6 p-2">
            <div
                class="p-6 border border-surface-200 dark:border-surface-700 bg-surface-0 dark:bg-surface-900 rounded flex flex-col">
              <div class="bg-surface-50 flex justify-center rounded p-4">
                <div class="relative mx-auto">
                  <img class="rounded w-full"
                       :src="item.imageUrls[0]" :alt="item.name"
                       style="max-width: 300px"/>
                  <div class="absolute bg-black/70 rounded-border" style="left: 4px; top: 4px">
                    <Tag :value="item.inventoryStatus" :severity="getSeverity(item)"></Tag>
                  </div>
                </div>
              </div>
              <div class="pt-6">
                <div class="flex flex-row justify-between items-start gap-2">
                  <div>
                    <div class="text-lg font-medium mt-1">{{ item.name }}</div>
                  </div>
                  <!--                  RATING    -->
                  <!--                  <div class="bg-surface-100 p-1" style="border-radius: 30px">-->
                  <!--                    <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"-->
                  <!--                         style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">-->
                  <!--                      <span class="text-surface-900 font-medium text-sm">{{ item.rating }}</span>-->
                  <!--                      <i class="pi pi-star-fill text-yellow-500"></i>-->
                  <!--                    </div>-->
                  <!--                  </div>-->
                </div>
                <div class="flex flex-col gap-6 mt-6">
                  <span class="text-2xl font-bold">
                    <span>${{ Math.floor(item.price) }}</span>
                    <span class="text-base align-middle">{{ (item.price % 1).toFixed(2).slice(1) }}</span>
                  </span>
                  <div class="flex gap-2">
                    <Button icon="pi pi-shopping-cart" label="Buy Now"
                            :disabled="item.inventoryStatus === 'OUTOFSTOCK'"
                            class="flex-auto whitespace-nowrap"></Button>
                    <!--                    <Button icon="pi pi-heart" outlined></Button>-->
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </DataView>
  </div>


</template>

<style scoped>

</style>