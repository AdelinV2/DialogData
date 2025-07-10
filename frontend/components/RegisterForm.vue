<script setup lang="ts">
import {ref} from 'vue';

import type {User} from "~/types/user";
import {useUserStorage} from "~/composables/useUserStorage";
import {navigateTo} from "#app";

const user = ref<User>({
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  password: '',
  deliveryAddress: {
    streetLine: '',
    postalCode: '',
    city: '',
    county: '',
    country: '',
  },
  billingAddress: {
    streetLine: '',
    postalCode: '',
    city: '',
    county: '',
    country: '',
  },
})

const firstRegisterForm = ref({
  email: '',
  password: '',
  confirmPassword: '',
  emailAlreadyExists: false,
  passwordMismatch: false,
  weakPassword: false,
  correctlyFilled: false,
})

const sameAddress = ref(false);

const {saveUser} = useUserStorage();

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const onFirstSubmit = async () => {

  firstRegisterForm.value.emailAlreadyExists = false;
  firstRegisterForm.value.passwordMismatch = false;
  firstRegisterForm.value.weakPassword = false;

  await $fetch(`${apiBaseUrl}/user/exists/${firstRegisterForm.value.email}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        firstRegisterForm.value.emailAlreadyExists = true;
        console.log('Email already exists');
        return;
      }
    }
  }).catch((error) => {
    if (error.status === 404) {
      firstRegisterForm.value.emailAlreadyExists = false;
      console.log('Email does not exists');
    } else {
      console.error('An error occurred:', error);
      return;
    }
  })

  if (firstRegisterForm.value.password.length < 8) {
    firstRegisterForm.value.weakPassword = true;
    console.log('Password must be at least 8 characters long');
    return;
  }

  if (firstRegisterForm.value.password !== firstRegisterForm.value.confirmPassword) {
    firstRegisterForm.value.passwordMismatch = true;
    console.log('Passwords do not match');
    return;
  }

  if (!firstRegisterForm.value.emailAlreadyExists && !firstRegisterForm.value.passwordMismatch) {
    firstRegisterForm.value.correctlyFilled = true;
    user.value.email = firstRegisterForm.value.email;
    user.value.password = firstRegisterForm.value.password;
  }
}

const onSecondSubmit = () => {

  if (sameAddress.value) {
    user.value.billingAddress = {...user.value.deliveryAddress};
  }

  $fetch(`${apiBaseUrl}/user`, {
    method: 'POST',
    body: user.value,
    onResponse({response}) {
      if (response.status === 201) {
        console.log('User registered successfully');
        saveUser(response._data);
        navigateTo('/');
      } else {
        console.error('Registration failed');
      }
    },
  }).catch((error) => {
    console.error('An error occurred:', error);
  });
}

</script>

<template>
  <div class="flex items-center justify-center">

    <!--    First form  -->
    <Card class="w-full max-w-sm" v-if="firstRegisterForm.correctlyFilled === false">
      <template #title>Register</template>
      <template #content>
        <Form v-slot="$form" @submit="onFirstSubmit" class="flex justify-center flex-col gap-6 mt-5">
          <div class="flex flex-col gap-1">
            <InputText name="email" type="email" placeholder="Email" v-model="firstRegisterForm.email"/>
            <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">
              {{ $form.email.error?.message }}
            </Message>
            <Message v-if="firstRegisterForm.emailAlreadyExists" severity="error" size="small" variant="simple">
              Email already exists.
            </Message>
          </div>
          <div class="flex flex-col gap-1">
            <Password name="password" type="password" placeholder="Password" v-model="firstRegisterForm.password" fluid
                      toggle-mask/>
            <Message v-if="firstRegisterForm.weakPassword" severity="error" size="small" variant="simple">
              Password must be at least 8 characters long.
            </Message>
          </div>
          <div class="flex flex-col gap-1">
            <Password name="confirmPassword" type="password" placeholder="Confirm Password"
                      v-model="firstRegisterForm.confirmPassword" fluid toggle-mask/>
            <Message v-if="firstRegisterForm.passwordMismatch" severity="error" size="small" variant="simple">
              Passwords do not match.
            </Message>
          </div>
          <Button type="submit" severity="secondary" label="Submit"
                  :disabled="!firstRegisterForm.email || !firstRegisterForm.password || !firstRegisterForm.confirmPassword"></Button>
        </Form>
      </template>
    </Card>

    <!--    Second form  -->
    <Card class="w-full max-w-sm" v-if="firstRegisterForm.correctlyFilled === true">
      <template #title>Basic informations</template>
      <template #content>
        <Form v-slot="$form" @submit="onSecondSubmit" class="flex justify-center flex-col gap-5 mt-5">
          <div class="flex flex-col gap-4">
            <InputText name="firstName" type="text" placeholder="First name" v-model="user.firstName"/>
            <InputText name="lastName" type="text" placeholder="Last name" v-model="user.lastName"/>
            <InputText name="phoneNumber" type="text" placeholder="Phone number" v-model="user.phoneNumber"/>
          </div>
          <div class="mt-5">
            <p class="font-bold">Delivery address</p>
            <div class="flex flex-col gap-4 mt-5">
              <InputText name="streetLine" type="text" placeholder="Street line"
                         v-model="user.deliveryAddress.streetLine"/>
              <InputText name="postalCode" type="text" placeholder="Postal code"
                         v-model="user.deliveryAddress.postalCode"/>
              <InputText name="city" type="text" placeholder="City" v-model="user.deliveryAddress.city"/>
              <InputText name="county" type="text" placeholder="County" v-model="user.deliveryAddress.county"/>
              <InputText name="country" type="text" placeholder="Country" v-model="user.deliveryAddress.country"/>
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
                <InputText name="streetLine" type="text" placeholder="Street line"
                           v-model="user.billingAddress.streetLine"/>
                <InputText name="postalCode" type="text" placeholder="Postal code"
                           v-model="user.billingAddress.postalCode"/>
                <InputText name="city" type="text" placeholder="City" v-model="user.billingAddress.city"/>
                <InputText name="county" type="text" placeholder="County" v-model="user.billingAddress.county"/>
                <InputText name="country" type="text" placeholder="Country" v-model="user.billingAddress.country"/>
              </div>
            </div>
          </div>
          <Button type="submit" severity="secondary" label="Create account"
                  :disabled="!user.firstName || !user.lastName || !user.phoneNumber || !user.deliveryAddress.streetLine || !user.deliveryAddress.postalCode || !user.deliveryAddress.city || !user.deliveryAddress.county || !user.deliveryAddress.country || (sameAddress === false && (!user.billingAddress.streetLine || !user.billingAddress.postalCode || !user.billingAddress.city || !user.billingAddress.county || !user.billingAddress.country))"
          ></Button>
        </Form>
      </template>
    </Card>
  </div>
</template>

<style scoped>

</style>
