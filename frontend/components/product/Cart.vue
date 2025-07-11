<script setup lang="ts">
import type {Product} from "~/types/product";
import type {CartEntry} from "~/types/cartEntry";

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const products = ref({} as Record<number, Product>);
const cartEntries = ref([] as CartEntry[]);
const {user} = useUserStorage();
const toast = useToast();

onMounted(() => {
  if (!user.value) {
    navigateTo('/login');
  }
  fetchCart();
});

const fetchCart = () => {
  $fetch(`${apiBaseUrl}/cart/${user.value.id}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        cartEntries.value = response._data.cartEntries as CartEntry[];
        console.log('Cart fetched successfully');
        fetchProducts();
      }
    }
  }).catch((error) => {
    console.error('Error fetching cart:', error);
  });
};

const onQuantityChange = (event: any) => {

  const entry = cartEntries.value[event.index];
  entry.quantity = event.value;

  if (entry.product && entry.product.id) {
    $fetch(`${apiBaseUrl}/cart/update`, {
      method: 'PUT',
      body: entry,
      params: {
        userId: user.value.id,
        productId: entry.product.id,
        quantity: entry.quantity,
      },
      onResponse({response}) {
        if (response.status === 200) {
          console.log('Cart updated successfully');
        } else {
          console.error('Failed to update cart');
        }
      }
    }).catch((error) => {
      console.error('Error updating cart:', error);
    });
  }
};

const onRemoveItem = (index: number) => {
  const entry = cartEntries.value[index];
  if (entry.product && entry.product.id) {
    $fetch(`${apiBaseUrl}/cart/remove`, {
      method: 'DELETE',
      params: {
        userId: user.value.id,
        productId: entry.product.id
      },
      onResponse({response}) {
        if (response.status === 200) {
          cartEntries.value.splice(index, 1);
          delete products.value[entry.product.id as number];

          toast.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Item removed from cart',
            life: 3000
          });
        } else {
          toast.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to remove item from cart',
            life: 3000
          });
        }
      }
    }).catch((error) => {
      console.error('Error removing item from cart:', error);
    });
  }
};

const fetchProducts = () => {
  products.value = {};
  for (const entry of cartEntries.value) {
    if (entry.product && entry.product.id) {
      $fetch(`${apiBaseUrl}/products/${entry.product.id}`, {
        method: 'GET',
        onResponse({response}) {
          if (response.status === 200) {
            const product = response._data as Product;
            products.value[entry.product.id as number] = product;
            console.log(`Product ${product.name} fetched successfully: `, product);
          }
        }
      }).catch((error) => {
        console.error('Error fetching product:', error);
      });
    }
  }
};

const getSeverity = (product: Product) => {
  if (product.availableQuantity > 5) {
    return 'success';
  } else if (product.availableQuantity > 0) {
    return 'warn';
  } else {
    return 'danger';
  }
};

const getStockName = (product: Product) => {
  if (product.availableQuantity > 5) {
    return 'In Stock';
  } else if (product.availableQuantity > 0) {
    return 'Low Stock';
  } else {
    return 'Out of Stock';
  }
};
</script>

<template>
  <Toast/>
  <div class="container flex flex-1 flex-row justify-betweenq mx-auto px-4 py-10">
    <div class="flex-1 min-h-0">
      <DataView :value="cartEntries">
        <template #list="slotProps">
          <div class="flex flex-col">
            <div v-for="(entry, index) in cartEntries" :key="entry.id">
              <div class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                   :class="{ 'border-t border-surface-200 dark:border-surface-700': index !== 0 }">
                <div class="md:w-40 relative">
                  <img
                      v-if="entry.product && entry.product.id && products[entry.product.id]"
                      class="block xl:block mx-auto rounded w-full"
                      :src="`${products[entry.product.id].images[0].imageUrl}`"
                      :alt="products[entry.product.id].name"
                  />
                </div>
                <div class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6">
                  <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                    <div class="relative bg-black/70 rounded-border" style="left: 4px; top: 4px">
                      <Tag
                          v-if="entry.product && entry.product.id && products[entry.product.id]"
                          :value="getStockName(products[entry.product.id])"
                          :severity="getSeverity(products[entry.product.id])"
                      ></Tag>
                    </div>
                    <div>
                      <div class="text-lg font-medium mt-2">
                        {{ entry.product && entry.product.id && products[entry.product.id]?.name || 'Product' }}
                      </div>
                    </div>
                  </div>
                  <div class="flex flex-col md:items-end gap-8">
                              <span class="text-2xl font-bold">
                                <span v-if="entry.product && entry.product.id && products[entry.product.id]">${{
                                    Math.floor(products[entry.product.id].price)
                                  }}</span>
                                <span v-if="entry.product && entry.product.id && products[entry.product.id]"
                                      class="text-base align-middle">
                                  {{ (products[entry.product.id].price % 1).toFixed(2).slice(1) }}
                                </span>
                              </span>
                    <div class="flex items-center mt-2">
                      <Button
                          icon="pi pi-minus"
                          class="p-button-rounded p-button-text"
                          @click="onQuantityChange({ index, value: Math.max(1, entry.quantity - 1) })"
                          :disabled="entry.quantity <= 1"
                          aria-label="Decrease quantity"
                          type="button"
                      />
                      <InputNumber :min="1" :max="entry.quantity" class="px-3 font-semibold text-lg" input-class="w-15"
                                   v-model="entry.quantity"/>
                      <Button
                          icon="pi pi-plus"
                          class="p-button-rounded p-button-text"
                          @click="onQuantityChange({ index, value: entry.quantity + 1 })"
                          :disabled="!!(entry.product && entry.product.id && products[entry.product.id]?.availableQuantity !== undefined && products[entry.product.id].availableQuantity <= entry.quantity)"
                          aria-label="Increase quantity"
                          type="button"
                      />
                      <Button
                          icon="pi pi-times"
                          class="p-button-rounded p-button-text custom-remove-btn text-white ms-5"
                          @click="onRemoveItem(index)"
                          aria-label="Remove from cart"
                          type="button"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </DataView>
    </div>
    <Card class="w-96 ms-5">
      <template #title>Shopping Cart</template>
      <template #content>
        <div class="flex flex-col gap-4">
          <div v-for="(entry, index) in cartEntries" :key="entry.id" class="flex items-center justify-between">
                      <span>
                        {{ entry.product && entry.product.id && products[entry.product.id]?.name || 'Product' }} <span
                          class="font-bold"> ({{ entry.quantity }}) </span>
                      </span>
            <span>${{ (entry.pricePerPiece * entry.quantity).toFixed(2) }}</span>
          </div>
          <hr class="my-4"/>
          <div class="flex items-center justify-between">
            <span class="font-bold">Total:</span>
            <span class="text-2xl font-bold">
              ${{cartEntries.reduce((total, entry) => total + (entry.pricePerPiece * entry.quantity), 0).toFixed(2) }}
            </span>
          </div>
        </div>
        <Button label="Checkout" class="mt-4 w-full" severity="primary" @click="() => navigateTo('/checkout')"
                :disabled="cartEntries.length === 0"/>
      </template>
    </Card>
  </div>
</template>