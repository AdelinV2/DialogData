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
      <Carousel
          :value="product.images"
          :numVisible="1"
          :numScroll="1"
          circular
          :autoplayInterval="0"
          :showIndicators="product.images.length > 1"
          :showNavigators="product.images.length > 1"
      >
        <template #item="slotProps">
          <Image
              :alt="product.name"
              :src="slotProps.data.imageUrl || slotProps.data.base64"
              style="height: 30rem"
              class="w-full object-contain"
              preview
          />
        </template>
      </Carousel>
      <div>
        <p class="text-2xl font-bold mb-4">
          <span>Price: ${{ Math.floor(product.price) }}</span>
          <span class="text-base align-middle">{{ (product.price % 1).toFixed(2).slice(1) }}</span>
        </p>
        <Toast/>
        <Button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center gap-2"
                icon="pi pi-shopping-cart" label="Add to Cart" @click="() => onAddToCart()"/>
      </div>
    </div>
    <p class="text-2xl font-semibold my-4">Description</p>
    <p class="text-lg mb-4">{{ product.description }}</p>
    <p class="text-2xl font-semibold my-4">Specifications</p>
    <p class="text-lg mb-1" v-for="(spec) in product.attributes" :key="spec.id">
      <span class="font-semibold">{{ spec.name }}:</span> {{ spec.value }}
    </p>
  </div>


  <!--  <Carousel :value="products" :numVisible="3" :numScroll="1" :responsiveOptions="responsiveOptions" circular :autoplayInterval="3000">-->
  <!--    <template #item="slotProps">-->
  <!--      <div class="border border-surface-200 dark:border-surface-700 rounded m-2  p-4">-->
  <!--        <div class="mb-4">-->
  <!--          <div class="relative mx-auto">-->
  <!--            <img :src="'https://primefaces.org/cdn/primevue/images/product/' + slotProps.data.image" :alt="slotProps.data.name" class="w-full rounded" />-->
  <!--            <Tag :value="slotProps.data.inventoryStatus" :severity="getSeverity(slotProps.data.inventoryStatus)" class="absolute" style="left:5px; top: 5px"/>-->
  <!--          </div>-->
  <!--        </div>-->
  <!--        <div class="mb-4 font-medium">{{ slotProps.data.name }}</div>-->
  <!--        <div class="flex justify-between items-center">-->
  <!--          <div class="mt-0 font-semibold text-xl">${{ slotProps.data.price }}</div>-->
  <!--          <span>-->
  <!--                    <Button icon="pi pi-heart" severity="secondary" outlined />-->
  <!--                    <Button icon="pi pi-shopping-cart" class="ml-2"/>-->
  <!--                </span>-->
  <!--        </div>-->
  <!--      </div>-->
  <!--    </template>-->
  <!--  </Carousel>-->


</template>

<style scoped>

</style>