<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";

const props = defineProps<{
  product: Product
}>()

const {user} = useUserStorage();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const toast = useToast();
const onAddToCart = () => {

  const cartEntry = {
    productId: props.product.id,
    quantity: 1,
    pricePerPiece: props.product.price,
    totalPricePerEntry: props.product.price,
  } as CartEntry;

  $fetch(`${apiBaseUrl}/cart/add/${user.value.id}`, {
    method: 'POST',
    body: cartEntry,
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Product added to cart successfully');
        toast.add({
          severity: 'contrast',
          summary: 'Success',
          detail: 'Product added to cart successfully',
          life: 3000
        });
      } else {
        console.error('Failed to add product to cart');
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to add product to cart',
          life: 3000
        });
      }
    }
  });
}

</script>

<template>

  <Toast/>
  <Card :product="product" style="width: 240px; height: 380px; cursor: pointer;">
    <template #header> <!-- TODO use product's image -->
      <img alt="user header" @click="navigateTo('/product/' + product.id)" style="height: 15rem;"
           src="https://picsum.photos/240"/>
    </template>
    <template #title>
      <div class="flex-1 overflow-hidden line-clamp-2 min-h-[2.5em] leading-[1.25em]"
           @click="navigateTo('/product/' + product.id)">{{ product.name }}
      </div>
    </template>
    <template #footer>
      <div class="flex gap-10 mt-auto justify-between ">
          <span class="text-2xl font-bold">
            <span>${{ Math.floor(product.price) }}</span>
            <span class="text-base align-middle">{{ (product.price % 1).toFixed(2).slice(1) }}</span>
          </span>
        <Button severity="primary" class="p-2" @click="onAddToCart">
          <i class="pi pi-shopping-cart"></i>
        </Button>
      </div>
    </template>
  </Card>

</template>

<style scoped>

</style>