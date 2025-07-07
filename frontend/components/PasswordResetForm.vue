<script setup lang="ts">

import {ref} from "vue";

const forgotPassword = ref({
  email: '',
  show: false,
  sent: false,
})

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const onForgotSubmit = () => {
  $fetch(apiBaseUrl + '/password-reset/request', {
    method: 'POST',
    body: forgotPassword.value.email,
    onResponse({response}) {
      if (response.status === 200 || response.status === 208) {
        console.log('Reset link sent successfully');
        forgotPassword.value.sent = true;
      } else {
        console.error('Failed to send reset link');
      }
    },
  }).catch((error) => {
    console.error('An error occurred:', error);
  });
}

</script>

<template>
  <Dialog v-model:visible="forgotPassword.show" modal header="Reset Password" :closable="true"
          :style="{ width: '350px' }">
    <div v-if="!forgotPassword.sent" class="flex flex-col gap-4">
      <InputGroup>
        <InputGroupAddon slot="prepend">
          <i class="pi pi-envelope"/>
        </InputGroupAddon>
        <InputText type="email" placeholder="Enter your email" v-model="forgotPassword.email"/>
      </InputGroup>
      <Button label="Send reset link" @click="onForgotSubmit" :disabled="!forgotPassword.email"/>
    </div>
    <div v-else class="flex flex-col items-center justify-center text-center">
      <Message severity="success" size="small" variant="simple">
        If the email exists, a reset link has been sent.
      </Message>
      <Button label="Back to login" @click="() => { forgotPassword.show = false; forgotPassword.sent = false; $router.push('/login'); }"
              class="mt-4"/>
    </div>
  </Dialog>
</template>

<style scoped>

</style>