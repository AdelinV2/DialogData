<script setup lang="ts">

import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";
import {ref} from "vue";

const props = defineProps<{
  product: Product,
}>()

const {user} = useUserStorage();
const {t} = useI18n();
const quantity = ref(1);

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const toast = useToast();

const responsiveOptions = ref([
  {
    breakpoint: '1300px',
    numVisible: 4
  },
  {
    breakpoint: '575px',
    numVisible: 1
  }
]);

const onAddToCart = () => {

  if (!user.value) {
    navigateTo('/login');
  }

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

const onBuyNow = () => {

  onAddToCart();

  navigateTo('/cart');
}

const getSeverity = () => {
  if (props.product.availableQuantity > 5) return 'success';
  else if (props.product.availableQuantity > 0) return 'warn';
  else return 'danger';
}

const getStockLabel = () => {
  if (props.product.availableQuantity > 5) return t('product.inStock');
  else if (props.product.availableQuantity > 0) return t('product.limitedStock');
  else return t('product.outOfStock');
};

</script>

<template>

  <Toast/>
  <div class="container mx-auto px-10 py-10">
    <div class="flex justify-between">
      <div class="card">
        <Galleria :value="props.product.images" :responsiveOptions="responsiveOptions" :numVisible="5" :circular="true"
                  containerStyle="max-width: 640px" :showItemNavigators="true" :showItemNavigatorsOnHover="true">
          <template #item="slotProps">
            <img :src="slotProps.item.imageUrl || slotProps.item.base64" :alt="slotProps.item.imageUrl"
                 style="width: 100%; display: block"/>
          </template>
          <template #thumbnail="slotProps">
            <img :src="slotProps.item.imageUrl || slotProps.item.base64" :alt="slotProps.item.imageUrl"
                 style="display: block; height: 60px; object-fit: cover;"/>
          </template>
        </Galleria>
      </div>
      <Card :product="props.product" style="width: 20rem; height: fit-content">
        <template #title>
          <h2 class="text-2xl font-semibold">{{ product.name }}</h2>
        </template>
        <template #subtitle>
          <p class="text-2xl font-semibold">${{ product.price }}</p>
        </template>
        <template #content>
          <Tag :value="getStockLabel()" :severity="getSeverity()"></Tag>
          <div class="my-5 flex items-center">
            <Button
                icon="pi pi-minus"
                class="p-button-rounded p-button-text"
                @click="quantity = quantity - 1"
                :disabled="quantity <= 1"
                aria-label="Decrease quantity"
                type="button"
            />
            <InputNumber :min="1" :max="product.availableQuantity" class="px-3 font-semibold text-lg"
                         input-class="w-12"
                         v-model="quantity"/>
            <Button
                icon="pi pi-plus"
                class="p-button-rounded p-button-text"
                @click="quantity = quantity + 1"
                :disabled="quantity >= product.availableQuantity"
                aria-label="Increase quantity"
                type="button"
            />

            <Button severity="success" class="icon-wrapper mb-1 ml-auto flex flex-items-center self-end"
                    @click="onAddToCart"
                    :disabled="product.availableQuantity === 0"
            >
              <i class="pi pi-plus" style="font-weight: bolder; color: #fffaf0"/>
              <i class="pi pi-shopping-cart" style="color: #fffaf0;"/>
            </Button>
          </div>
          <Button
              @click="onBuyNow"
              severity="contrast"
              class="w-full"
              :label="t('product.buyNow')"
              :disabled="product.availableQuantity === 0"
          >
          </Button>
        </template>
      </Card>
    </div>

    <div class="card mt-10">
      <Accordion :value="['0']" multiple>
        <AccordionPanel value="0">
          <AccordionHeader>{{ t('product.description') }}</AccordionHeader>
          <AccordionContent>
            <p class="m-0">
              {{ props.product.description }}
            </p>
          </AccordionContent>
        </AccordionPanel>
        <AccordionPanel value="1">
          <AccordionHeader>{{ t('product.specifications') }}</AccordionHeader>
          <AccordionContent>
            <div class="card">
              <DataTable :value="props.product.attributes" stripedRows tableStyle="min-width: 50rem">
                <Column field="attribute.name" :header="t('product.name')"></Column>
                <Column field="value" :header="t('product.value')"></Column>
              </DataTable>
            </div>
          </AccordionContent>
        </AccordionPanel>
      </Accordion>
    </div>

  </div>


</template>

<style scoped>

</style>