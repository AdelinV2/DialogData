<script setup lang="ts">
import {useUserStorage} from '~/composables/useUserStorage';
import type {Cart} from "~/types/cart";
import type {Order} from "~/types/order";
import type {Address} from "~/types/address";
import {navigateTo} from "#app";

const {user} = useUserStorage();
const {t} = useI18n();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

onMounted(() => {
  if (!user.value) {
    navigateTo('/login');
  }
})

const useExistingDelivery = ref(true);
const useExistingBilling = ref(true);
const billingSameAsDelivery = ref(true);

const paymentMethods = [
  {label: 'Credit Card', value: 'CREDIT_CARD'},
  {label: 'PayPal', value: 'PAYPAL'},
  {label: 'Bank Transfer', value: 'BANK_TRANSFER'},
  {label: 'Cash on Delivery', value: 'CASH'}
];

const selectedPayment = ref(paymentMethods[0].value);

const cart = ref<Cart | null>(null);

const fetchCart = () => {
  $fetch(`${apiBaseUrl}/cart/${user.value.id}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        cart.value = response._data as Cart;
        console.log('Cart fetched successfully:', cart.value);
      } else {
        console.error('Failed to fetch cart');
      }
    }
  }).catch((error) => {
    console.error('Error fetching cart total:', error);
  });
};

onMounted(() => {
  fetchCart();
});
const deliveryAddress = computed(() => {
  if (useExistingDelivery.value) {
    return user.value?.deliveryAddress || {};
  }
  return {};
});

const billingAddress = computed(() => {
  if (billingSameAsDelivery.value) {
    return deliveryAddress.value;
  }
  if (useExistingBilling.value) {
    return user.value?.billingAddress || {};
  }
  return {};
});

const confirmOrder = () => {

  const orderDetails = {
    userId: user.value.id,
    cart: cart.value,
    paymentType: selectedPayment.value,
    deliveryAddress: {
      id: user.value.deliveryAddress.id,
      streetLine: deliveryAddress.value.streetLine,
      postalCode: deliveryAddress.value.postalCode,
      city: deliveryAddress.value.city,
      county: deliveryAddress.value.county,
      country: deliveryAddress.value.country,
    } as Address,
    invoiceAddress: {
      id: user.value.billingAddress.id,
      streetLine: billingAddress.value.streetLine,
      postalCode: billingAddress.value.postalCode,
      city: billingAddress.value.city,
      county: billingAddress.value.county,
      country: billingAddress.value.country,
    } as Address,
    totalPrice: cart.value?.totalPrice,
    orderDate: new Date(),
  } as Order;

  console.log(orderDetails);

  $fetch(`${apiBaseUrl}/order`, {
    method: 'POST',
    body: orderDetails,
    onResponse({response}) {
      if (response.status === 201) {
        console.log('Order confirmed successfully');
        navigateTo('/orders');
      } else {
        console.error('Failed to confirm order');
      }
    }
  }).catch((error) => {
    console.error('Error confirming order:', error);
  });

};
</script>

<template>
  <Navbar/>
  <div class="flex flex-row gap-8 container mx-auto py-10 min-h-[calc(100vh-250px)]">
    <div class="flex-1">
      <Card class="mb-6">
        <template #title>{{ t('order.deliveryAddress') }}</template>
        <template #content>
          <div class="mb-3">
            <RadioButton v-model="useExistingDelivery" :value="true" inputId="existingDelivery"/>
            <label for="existingDelivery" class="ml-2">{{ t('order.useExistingAddress') }}</label>
          </div>
          <div v-if="useExistingDelivery && user" class="mb-4">
            <div class="p-3 border rounded bg-surface-100">
              <div>{{ user.deliveryAddress?.streetLine }}</div>
              <div>{{ user.deliveryAddress?.city }}, {{ user.deliveryAddress?.zip }}</div>
              <div>{{ user.deliveryAddress?.country }}</div>
            </div>
          </div>
          <div class="mb-3">
            <RadioButton v-model="useExistingDelivery" :value="false" inputId="newDelivery"/>
            <label for="newDelivery" class="ml-2">{{ t('order.enterNewAddress') }}</label>
          </div>
          <div v-if="!useExistingDelivery">
            <div class="mb-2">
              <InputText v-model="user.deliveryAddress.streetLine" :placeholder="t('register.street')" class="w-full"/>
            </div>
            <div class="mb-2 flex gap-2">
              <InputText v-model="user.deliveryAddress.city" :placeholder="t('register.city')" class="w-1/2"/>
              <InputText v-model="user.deliveryAddress.postalCode" :placeholder="t('register.zip')" class="w-1/2"/>
            </div>
            <div class="mb-2">
              <InputText v-model="user.deliveryAddress.country" :placeholder="t('register.country')" class="w-full"/>
            </div>
          </div>
        </template>
      </Card>

      <Card>
        <template #title>{{ t('order.billingAddress') }}</template>
        <template #content>
          <div class="mb-3">
            <Checkbox v-model="billingSameAsDelivery" :binary="true" inputId="sameAsDelivery"/>
            <label for="sameAsDelivery" class="ml-2">{{ t('register.billingSameAsDelivery')}}</label>
          </div>
          <div v-if="!billingSameAsDelivery">
            <div class="mb-3">
              <RadioButton v-model="useExistingBilling" :value="true" inputId="existingBilling"/>
              <label for="existingBilling" class="ml-2">{{ t('order.useExistingAddress') }}</label>
            </div>
            <div v-if="useExistingBilling" class="mb-4">
              <div class="p-3 border rounded bg-surface-100">
                <div>{{ user.billingAddress?.streetLine }}</div>
                <div>{{ user.billingAddress?.city }}, {{ user.billingAddress?.zip }}</div>
                <div>{{ user.billingAddress?.country }}</div>
              </div>
            </div>
            <div class="mb-3">
              <RadioButton v-model="useExistingBilling" :value="false" inputId="newBilling"/>
              <label for="newBilling" class="ml-2">{{ t('order.enterNewAddress') }}</label>
            </div>
            <div v-if="!useExistingBilling && user">
              <div class="mb-2">
                <InputText v-model="user.billingAddress.streetLine" :placeholder="t('register.street')" class="w-full"/>
              </div>
              <div class="mb-2 flex gap-2">
                <InputText v-model="user.billingAddress.city" :placeholder="t('register.city')" class="w-1/2"/>
                <InputText v-model="user.billingAddress.postalCode" :placeholder="t('register.zip')" class="w-1/2"/>
              </div>
              <div class="mb-2">
                <InputText v-model="user.billingAddress.country" :placeholder="t('register.country')" class="w-full"/>
              </div>
            </div>
          </div>
        </template>
      </Card>
    </div>
    <div class="w-96">
      <Card>
        <template #title>{{ t('order.orderSummary') }}</template>
        <template #content>
          <div class="flex justify-between mb-2">
            <span class="font-bold">Total:</span>
            <span class="text-2xl font-bold">${{ cart?.totalPrice.toFixed(2) }}</span>
          </div>
          <div class="mb-4">
            <label class="block mb-2 font-semibold">{{t('order.paymentMethod')}}</label>
            <Dropdown v-model="selectedPayment" :options="paymentMethods" optionLabel="label" optionValue="value"
                      class="w-full"/>
          </div>
          <Button :label="t('order.confirmOrder')" class="w-full" severity="primary" @click="confirmOrder" :disabled="cart?.cartEntries.length === 0"/>
        </template>
      </Card>
    </div>
  </div>

  <Footer/>

</template>

<style scoped>

</style>