<script setup lang="ts">
import {useUserStorage} from '~/composables/useUserStorage';

const {user, saveUser} = useUserStorage();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

if (!user.value) {
  navigateTo('/login');
}

const editUser = ref({...user.value});
const sameAddress = ref(
    JSON.stringify(editUser.value.deliveryAddress) === JSON.stringify(editUser.value.billingAddress)
);

const onSubmit = () => {

  if (sameAddress.value) {
    editUser.value.billingAddress = {...editUser.value.deliveryAddress};
  }

  $fetch(`${apiBaseUrl}/user/${editUser.value.id}`, {
    method: 'PUT',
    body: editUser.value,
    onResponse({response}) {
      if (response.status === 200) {
        saveUser(editUser.value);
        navigateTo('/');
      } else {
        console.error('Failed to update user profile');
      }
    }
  });
};
</script>

<template>
  <Navbar />
  <div class="flex items-center justify-center mt-12">
    <Card class="w-full max-w-sm">
      <template #title>Edit Profile</template>
      <template #content>
        <Form v-slot="$form" @submit="onSubmit" class="flex flex-col gap-5 mt-5">
          <div class="flex flex-col gap-4">
            <FloatLabel variant="on">
              <InputText id="firstName" name="firstName" type="text" class="w-full" v-model="editUser.firstName"/>
              <label for="firstName">First name</label>
            </FloatLabel>
            <FloatLabel variant="on">
              <InputText id="lastName" name="lastName" type="text" class="w-full" v-model="editUser.lastName"/>
              <label for="lastName">Last name</label>
            </FloatLabel>
            <FloatLabel variant="on">
              <InputText id="phoneNumber" name="phoneNumber" type="text" class="w-full" v-model="editUser.phoneNumber"/>
              <label for="phoneNumber">Phone number</label>
            </FloatLabel>
          </div>
          <div class="mt-5">
            <p class="font-bold">Delivery address</p>
            <div class="flex flex-col gap-4 mt-5">
              <FloatLabel variant="on">
                <InputText id="deliveryStreetLine" name="streetLine" type="text" class="w-full" v-model="editUser.deliveryAddress.streetLine"/>
                <label for="deliveryStreetLine">Street line</label>
              </FloatLabel>
              <FloatLabel variant="on">
                <InputText id="deliveryPostalCode" name="postalCode" type="text" class="w-full" v-model="editUser.deliveryAddress.postalCode"/>
                <label for="deliveryPostalCode">Postal code</label>
              </FloatLabel>
              <FloatLabel variant="on">
                <InputText id="deliveryCity" name="city" type="text" class="w-full" v-model="editUser.deliveryAddress.city"/>
                <label for="deliveryCity">City</label>
              </FloatLabel>
              <FloatLabel variant="on">
                <InputText id="deliveryCounty" name="county" type="text" class="w-full" v-model="editUser.deliveryAddress.county"/>
                <label for="deliveryCounty">County</label>
              </FloatLabel>
              <FloatLabel variant="on">
                <InputText id="deliveryCountry" name="country" type="text" class="w-full" v-model="editUser.deliveryAddress.country"/>
                <label for="deliveryCountry">Country</label>
              </FloatLabel>
            </div>
          </div>
          <div class="mt-5">
            <p class="font-bold">Billing address</p>
            <div class="flex flex-col gap-4 mt-5">
              <div class="items-center">
                <Checkbox name="sameAsDelivery" input-id="sameAsDelivery" v-model="sameAddress" binary/>
                <label for="sameAsDelivery" class="ms-3"> Same as delivery address</label>
              </div>
              <div class="flex flex-col gap-4 mt-5" v-if="!sameAddress">
                <FloatLabel variant="on">
                  <InputText id="billingStreetLine" name="streetLine" type="text" class="w-full" v-model="editUser.billingAddress.streetLine"/>
                  <label for="billingStreetLine">Street line</label>
                </FloatLabel>
                <FloatLabel variant="on">
                  <InputText id="billingPostalCode" name="postalCode" type="text" class="w-full" v-model="editUser.billingAddress.postalCode"/>
                  <label for="billingPostalCode">Postal code</label>
                </FloatLabel>
                <FloatLabel variant="on">
                  <InputText id="billingCity" name="city" type="text" class="w-full" v-model="editUser.billingAddress.city"/>
                  <label for="billingCity">City</label>
                </FloatLabel>
                <FloatLabel variant="on">
                  <InputText id="billingCounty" name="county" type="text" class="w-full" v-model="editUser.billingAddress.county"/>
                  <label for="billingCounty">County</label>
                </FloatLabel>
                <FloatLabel variant="on">
                  <InputText id="billingCountry" name="country" type="text" class="w-full" v-model="editUser.billingAddress.country"/>
                  <label for="billingCountry">Country</label>
                </FloatLabel>
              </div>
            </div>
          </div>
          <Button type="submit" severity="secondary" label="Save changes"></Button>
        </Form>
      </template>
    </Card>
  </div>

  <Footer />

</template>

<style scoped>
</style>