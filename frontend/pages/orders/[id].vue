<script setup lang="ts">

import {useUserStorage} from "~/composables/useUserStorage";
import {onMounted} from "vue";
import type {Order} from "~/types/order";

const {user} = useUserStorage();
const {t} = useI18n();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const order = ref<Order | null>(null);
const dialogVisible = ref(false);
const toast = useToast();

onMounted(() => {
  if (!user.value) {
    navigateTo('/login');
  }
  fetchOrder();
})

const fetchOrder = () => {

  const orderId = parseInt(useRoute().params.id as string);

  if (isNaN(orderId)) {
    console.error('Invalid order ID');
    return;
  }

  $fetch(`${apiBaseUrl}/order/${orderId}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        order.value = response._data as Order;

        if (order.value.userId !== user.value.id) {
          console.error('Order does not belong to the current user');
          navigateTo('/orders')
          return;
        }

        console.log('Order fetched successfully:', response._data);
      } else {
        console.error('Failed to fetch order');
      }
    }
  }).catch((error) => {
    console.error('Error fetching order:', error);
  });
}

const updateOrder = (updatedOrder: Order) => {
  $fetch(`${apiBaseUrl}/order/${updatedOrder.id}`, {
    method: 'PUT',
    body: updatedOrder,
    onResponse({response}) {
      if (response.status === 200) {
        order.value = response._data as Order;
        dialogVisible.value = false;
        toast.add({
          severity: 'success',
          summary: t('order.updateSuccess'),
          detail: t('order.orderUpdated'),
          life: 3000
        });
        console.log('Order updated successfully');
      } else {
        toast.add({
          severity: 'error',
          summary: t('order.updateError'),
          detail: t('order.orderUpdateFailed'),
          life: 3000
        });
        console.error('Failed to update order');
      }
    }
  }).catch((error) => {
    toast.add({
      severity: 'error',
      summary: t('order.updateError'),
      detail: t('order.orderUpdateFailed'),
      life: 3000
    });
    console.error('Error updating order:', error);
  });
}

</script>

<template>

  <Toast />
  <Navbar/>

  <div v-if="order" class="container mx-auto py-10 min-h-[calc(100vh-250px)]">
    <div class="flex flex-col md:flex-row items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold">{{ t('order.order') }} #{{ order.id }}</h1>
        <div class="text-gray-500 text-lg">{{ t('order.orderDate') }}: {{ order.orderDate }}</div>
      </div>
      <Button v-if="user.role === 'ADMIN'" class="ml-auto" @click="dialogVisible = true">
        {{ t('order.editOrder') }}
      </Button>
    </div>

    <Dialog v-model="dialogVisible" :header="t('order.editOrder')" :modal="true" :closable="true">
      <template #footer>
        <Button label="Cancel" @click="dialogVisible = false" class="p-button-text" />
        <Button label="Save" @click="updateOrder(order)" class="p-button-primary" />
      </template>
      <label>{{ t('order.orderStatus') }}</label>
      <Dropdown v-model="order.status" :options="['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED']" />
    </Dialog>

    <div class="flex flex-col md:flex-row gap-6">
      <Card class="flex-1">
        <template #title>{{ t('order.deliveryAddress') }}</template>
        <template #content>
          <div>
            <div>{{ order.deliveryAddress.streetLine }}</div>
            <div>{{ order.deliveryAddress.city }}, {{ order.deliveryAddress.postalCode }}</div>
            <div>{{ order.deliveryAddress.country }}</div>
            <div>{{ t('order.phoneNumber') }}: {{ user.phoneNumber }}</div>
          </div>
        </template>
      </Card>

      <Card class="flex-1">
        <template #title>{{ t('order.billingAddress') }}</template>
        <template #content>
          <div>
            <div>{{ order.invoiceAddress.streetLine }}</div>
            <div>{{ order.invoiceAddress.city }}, {{ order.invoiceAddress.postalCode }}</div>
            <div>{{ order.invoiceAddress.country }}</div>
          </div>
        </template>
      </Card>

      <Card class="flex-1">
        <template #title>{{ t('order.paymentMethod') }} & {{ t('order.totalPrice') }}</template>
        <template #content>
          <div class="mb-2">
            <span class="font-semibold">{{ t('order.paymentMethod') }}:</span>
            <Tag :value="order.paymentType" class="ml-2"/>
          </div>
          <div class="mt-4">
            <span class="font-semibold">{{ t('order.totalPrice') }}:</span>
            <span class="text-2xl font-bold ml-2">${{ order.totalPrice.toFixed(2) }}</span>
          </div>
        </template>
      </Card>
    </div>

    <Card class="mt-12">
      <template #title>{{ t('order.items') }}</template>
      <template #content>
        <DataTable :value="order.cart.cartEntries" dataKey="id" responsiveLayout="scroll">
          <Column field="product.name" :header="t('order.product')">
            <template #body="{ data }">
              <div class="flex items-center">
                <img
                    v-if="data.product.images && data.product.images.length"
                    :src="data.product.images[0].imageUrl"
                    alt="Product"
                    style="height: 60px; margin-right: 12px; cursor: pointer;"
                    @click="navigateTo(`/product/${data.product.id}`)"
                />
                <span @click="navigateTo(`/product/${data.product.id}`)" style="cursor: pointer">
                  {{ data.product.name }}
                </span>
              </div>
            </template>
          </Column>
          <Column field="quantity" :header="t('order.quantity')"/>
          <Column field="pricePerPiece" :header="t('order.pricePerPiece')">
            <template #body="{ data }">
              ${{ data.pricePerPiece.toFixed(2) }}
            </template>
          </Column>
          <Column field="totalPricePerEntry" header="Total">
            <template #body="{ data }">
              ${{ data.totalPricePerEntry.toFixed(2) }}
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>

  <Footer/>

</template>

<style scoped>

</style>