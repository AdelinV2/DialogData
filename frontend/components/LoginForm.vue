<script setup lang="ts">
import {ref} from 'vue';
import {useUserStorage} from "~/composables/useUserStorage";
import {navigateTo} from "#app";
import PasswordResetForm from "~/components/PasswordResetForm.vue";

const loginForm = ref({
  email: '',
  password: '',
})

const { t } = useI18n();

const showForgotPassword = ref(false);

const failed = ref(false);
const {saveUser, user} = useUserStorage();

if (user.value) {
  navigateTo('/');
}

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const onSubmit = () => {

  $fetch(apiBaseUrl + '/user/login', {
        method: 'POST',
        body: loginForm.value,
        onResponse({response}) {
          if (response.status === 200) {
            console.log('Login successful');
            console.log(response);
            failed.value = false;
            saveUser(response._data);
            navigateTo('/');
          } else {
            console.error('Login failed');
            loginForm.value.password = '';
            failed.value = true;
          }
        },
      }
  ).catch((error) => {
        console.error('An error occurred:', error);
        failed.value = true;
      }
  )
}

</script>

<template>
  <div class="flex items-center justify-center">
    <Card class="w-full max-w-sm">
      <template #title>{{ t('login.login') }}</template>
      <template #content>
        <Form v-slot="$form" @submit="onSubmit" class="flex justify-center flex-col gap-6 mt-5">
          <div class="flex flex-col gap-1">
            <InputGroup>
              <InputGroupAddon slot="prepend">
                <i class="pi pi-envelope"/>
              </InputGroupAddon>
              <InputText name="email" type="email" placeholder="Email" v-model="loginForm.email"/>
            </InputGroup>
            <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">
              {{ $form.email.error?.message }}
            </Message>
          </div>
          <div class="flex flex-col gap-1">
            <InputGroup>
              <InputGroupAddon slot="prepend">
                <i class="pi pi-lock"/>
              </InputGroupAddon>
              <Password name="password" type="password" :placeholder="t('login.password')" v-model="loginForm.password" fluid
                        toggle-mask :feedback="false"/>
            </InputGroup>
            <Message v-if="$form.password?.invalid" severity="error" size="small" variant="simple">
              {{ $form.password.error?.message }}
            </Message>
          </div>
          <Message v-if="failed" severity="error" size="small" variant="simple">
            {{ t('login.invalidCredentials') }}
          </Message>

          <Button type="submit" severity="secondary" :label="t('login.submit')"
                  :disabled="!loginForm.email || !loginForm.password"/>
        </Form>
        <div class="text-center mt-1">
          <a href="#" @click.prevent="showForgotPassword = true" class="text-sm text-blue-600 hover:underline">
            {{ t('login.forgotPassword') }}
          </a>
        </div>
        <PasswordResetForm v-model:visible="showForgotPassword" @close="showForgotPassword = false"/>
        <div class="text-center mt-1">
          <a href="/register" class="text-sm text-blue-600 hover:underline mt-4">
            {{ t('login.createAccount') }}
          </a>
        </div>
      </template>
    </Card>
  </div>
</template>

<style scoped>

</style>