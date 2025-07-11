<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";

const props = defineProps<{
  product: Product,
}>()

const {user} = useUserStorage();
const {t} = useI18n();
const quantity = ref(1);

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const toast = useToast();

const onAddToCart = () => {

  const cartItem = {
    product: props.product,
    quantity: quantity.value,
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
      <div style="width: 50rem; max-width: 100%;">
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
                style="max-height: 30rem; width: auto; object-fit: contain; display: block; margin: 0 auto;"
                class="w-full"
                preview
            />
          </template>
        </Carousel>
      </div>
      <div class="flex flex-col">
        <p class="text-2xl font-bold mb-4">
          <span>{{ t('product.price') }}: ${{ Math.floor(product.price) }}</span>
          <span class="text-base align-middle">{{ (product.price % 1).toFixed(2).slice(1) }}</span>
        </p>
        <div class="mb-5">
          <Button
              icon="pi pi-minus"
              class="p-button-rounded p-button-text"
              @click="quantity = quantity - 1"
              :disabled="quantity <= 1"
              aria-label="Decrease quantity"
              type="button"
          />
          <InputNumber :min="1" :max="product.availableQuantity" class="px-3 font-semibold text-lg" input-class="w-12"
                       v-model="quantity"/>
          <Button
              icon="pi pi-plus"
              class="p-button-rounded p-button-text"
              @click="quantity = quantity + 1"
              :disabled="quantity >= product.availableQuantity"
              aria-label="Increase quantity"
              type="button"
          />
        </div>
        <Toast/>
        <Button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center gap-2 mb-8"
                icon="pi pi-shopping-cart" :label="t('product.addToCart')" @click="() => onAddToCart()"/>
        <p class="text-2xl font-semibold my-4">{{ t('product.specifications') }}</p>
        <p class="text-lg mb-1" v-for="(spec) in product.attributes" :key="spec.id">
          <span class="font-semibold">{{ spec.name }}:</span> {{ spec.value }}
        </p>
      </div>
    </div>
    <p class="text-2xl font-semibold my-4">{{ t('product.description') }}</p>
    <p class="text-lg mb-4">{{ product.description }}</p>
    <!--    <p class="text-2xl font-semibold my-4">Specifications</p>-->
    <!--    <p class="text-lg mb-1" v-for="(spec) in product.attributes" :key="spec.id">-->
    <!--      <span class="font-semibold">{{ spec.name }}:</span> {{ spec.value }}-->
    <!--    </p>-->
  </div>

</template>

<style scoped>

</style>