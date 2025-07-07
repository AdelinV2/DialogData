<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";

const props = defineProps<{
  product: Product,
}>()

const {user} = useUserStorage();

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const toast = useToast();

const onAddToCart = () => {

  const cartItem = {
    productId: props.product.id,
    quantity: 1,
    pricePerPiece: props.product.price,
    totalPricePerEntry: props.product.price,
  } as CartEntry

  $fetch(`${apiBaseUrl}/cart/add/${user.value.id}`, {
    method: 'POST',
    body: cartItem,
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Product added to cart successfully');
        toast.add({
          severity: 'success',
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
  })
}

</script>

<template>
  <div class="container mx-auto px-4 py-10">
    <p class="text-3xl font-semibold mb-4">{{ product.name }}</p>
    <div class="flex justify-between">
      <img :alt="product.name" :src="product.imageUrl" style="height: 30rem"/>
      <div>
        <p class="text-2xl font-bold mb-4">
          <span>Price: ${{ Math.floor(product.price) }}</span>
          <span class="text-base align-middle">{{ (product.price % 1).toFixed(2).slice(1) }}</span>
        </p>
        <Toast />
        <Button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center gap-2"
                icon="pi pi-shopping-cart" label="Add to Cart" @click="() => onAddToCart()"/>
      </div>
    </div>
    <p class="text-2xl font-semibold mb-4">Description</p>
    <p class="text-lg mb-4">{{ product.description }}</p>
    <p class="text-2xl font-semibold my-4">Specifications</p>
    <p class="text-lg mb-1" v-for="(spec) in product.attributes" :key="spec.id">
      <span class="font-semibold">{{ spec.name }}:</span> {{ spec.value }}
    </p>
  </div>
</template>

<style scoped>

</style>