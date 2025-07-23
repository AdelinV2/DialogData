<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";

const props = defineProps<{
  products: Product[]
}>();

const {user} = useUserStorage();
const {t} = useI18n();
const toast = useToast();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const responsiveOptions = ref([
  {
    breakpoint: '1400px',
    numVisible: 4,
    numScroll: 4
  },
  {
    breakpoint: '1199px',
    numVisible: 3,
    numScroll: 3
  },
  {
    breakpoint: '767px',
    numVisible: 2,
    numScroll: 2
  },
  {
    breakpoint: '575px',
    numVisible: 1,
    numScroll: 1
  }
]);

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

  <div class="card mx-auto" style="" v-if="props.products && props.products.length > 0">
    <Carousel :value="props.products" :numVisible="3" :numScroll="3" :responsiveOptions="responsiveOptions"
              :circular="true"
              :autoplayInterval="3000" class="w-full" vertical-view-port-height="96px">
      <template #item="slotProps">
        <div class="border border-surface-200 dark:border-surface-700 rounded m-2  p-4"
             @click="() => navigateTo(`/product/${slotProps.data.id}`)"
             style="cursor: pointer">
          <div class="mb-4">
            <div class="relative mx-auto">
              <img :src="slotProps.data.images?.[0].imageUrl" :alt="slotProps.data.name" class="w-full rounded"/>
              <div class="absolute bg-black/70 rounded-border" style="left: 4px; top: 4px">
                <Tag :value="getStockLabel(slotProps.data)" :severity="getSeverity(slotProps.data)"></Tag>
              </div>
            </div>
          </div>
          <div class="mb-4 font-medium">{{ slotProps.data.name }}</div>
          <div class="flex justify-between items-center">
            <div v-if="slotProps.data.promotionPrice" class="flex flex-row">
              <div class="flex flex-col">
                        <span class="text-red-600 font-bold text-3xl">
                        <span>${{ Math.floor(slotProps.data.promotionPrice) }}</span>
                          <span class="text-base align-middle">{{
                              (slotProps.data.promotionPrice % 1).toFixed(2).slice(1)
                            }}</span>
                        </span>
              </div>
              <span class="text-gray-400 line-through text-xl ms-4">${{ slotProps.data.price.toFixed(2) }}</span>
            </div>
            <div v-else>
                     <span class="text-2xl font-bold">
                       <span>${{ Math.floor(slotProps.data.price) }}</span>
                       <span class="text-base align-middle">{{ (slotProps.data.price % 1).toFixed(2).slice(1) }}</span>
                      </span>
            </div>
            <div class="flex flex-row">
              <div class="me-3">
                <div class="bg-surface-100 p-1 bg-white" style="border-radius: 30px">
                  <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                       style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">
                          <span class="text-surface-900 font-medium text-sm text-gray-900">
                            {{ slotProps.data.averageRating || 0 }}
                          </span>
                    <i class="pi pi-star-fill text-yellow-500"></i>
                  </div>
                </div>
              </div>
              <Button @click.stop="addToCart(slotProps.data)" icon="pi pi-shopping-cart" class="ml-2"/>
            </div>
          </div>
        </div>
      </template>
    </Carousel>
  </div>
</template>

<style scoped>

</style>
