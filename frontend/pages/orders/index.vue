<script setup lang="ts">

import type {Order} from "~/types/order";
import {navigateTo} from "#app";

const orders = ref([] as Order[])
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {user} = useUserStorage();
const {t} = useI18n();

onMounted(() => {
  if (!user.value) {
    navigateTo('/login');
  }
  fetchOrders();
});

const pageInfo = ref({ page: 0, size: 10, totalElements: 0, totalPages: 0 })
const fetchOrders = () => {

  const params: Record<string, any> = {
    page: pageInfo.value.page,
    size: pageInfo.value.size,
    sort: 'orderDate,desc',
  }

  $fetch(`${apiBaseUrl}/order/user/${user.value.id}`, {
    method: 'GET',
    params: params,
    onResponse({response}) {
      if (response.status === 200) {
        orders.value = response._data.content as Order[];
        console.log('Orders fetched successfully:', orders.value);
      } else {
        console.error('Failed to fetch orders');
      }
    },
    onResponseError({response}) {
      console.error('Error fetching orders:', response.status, response.statusText);
    }
  })
}

const onPageChange = (event: any) => {
  pageInfo.value.page = event.page
  pageInfo.value.size = event.rows
  fetchOrders();
}

</script>

<template>

  <Navbar />

  <div class="card mt-8 mx-14 min-h-[calc(100vh-250px)]">
    <h2 class="text-xl font-bold mb-4">{{ t('order.myOrders') }}</h2>
    <DataTable :value="orders" dataKey="id" responsiveLayout="scroll">
      <Column field="id" :header="t('order.orderNumber')" />
      <Column field="orderDate" :header="t('order.orderDate')">
        <template #body="{ data }">
          {{ new Date(data.orderDate).toLocaleString() }}
        </template>
      </Column>
      <Column field="totalPrice" :header="t('order.totalPrice')">
        <template #body="{ data }">
          ${{ data.totalPrice.toFixed(2) }}
        </template>
      </Column>
      <Column :header="t('order.actions')">
        <template #body="{ data }">
          <Button :label="t('order.viewOrder')" icon="pi pi-search" @click="navigateTo(`/orders/${data.id}`)" />
        </template>
      </Column>
    </DataTable>
    <div v-if="orders.length === 0" class="text-center py-4">{{ t('order.noOrders') }}</div>
  </div>

  <div class="card mt-12">
    <Paginator
        :rows="pageInfo.size"
        :totalRecords="pageInfo.totalElements"
        :rowsPerPageOptions="[10,20,30]"
        :first="pageInfo.page * pageInfo.size"
        @page="onPageChange"
    />
  </div>
  <Footer />

</template>

<style scoped>

</style>