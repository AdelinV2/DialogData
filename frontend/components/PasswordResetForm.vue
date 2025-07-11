<script setup lang="ts">

import {ref} from "vue";

const forgotPassword = ref({
  email: '',
  show: false,
  sent: false,
})

const {t} = useI18n();
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
  <Dialog v-model:visible="forgotPassword.show" modal :header="t('resetPassword.resetPassword')" :closable="true"
          :style="{ width: '350px' }">
    <div v-if="!forgotPassword.sent" class="flex flex-col gap-4">
      <InputGroup>
        <InputGroupAddon slot="prepend">
          <i class="pi pi-envelope"/>
        </InputGroupAddon>
        <InputText type="email" :placeholder="t('resetPassword.email')" v-model="forgotPassword.email"/>
      </InputGroup>
      <Button label="Send reset link" @click="onForgotSubmit" :disabled="!forgotPassword.email"/>
    </div>
    <div v-else class="flex flex-col items-center justify-center text-center">
      <Message severity="success" size="small" variant="simple">
        {{ t('resetPassword.resetLinkSent') }}
      </Message>
      <Button :label="t('resetPassword.backToLogin')"
              @click="() => { forgotPassword.show = false; forgotPassword.sent = false; $router.push('/login'); }"
              class="mt-4"/>
    </div>
  </Dialog>
</template>

<style scoped>

</style>