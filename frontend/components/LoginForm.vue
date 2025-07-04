<script setup lang="ts">
import {ref} from 'vue';
import {useUserStorage} from "~/composables/useUserStorage";
import {navigateTo} from "#app";

const loginForm = ref({
  email: '',
  password: '',
})

const failed = ref(false);
const { saveUser } = useUserStorage();

const onSubmit = () => {

  $fetch('http://localhost:8080/api/user/login', {
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
      <template #title>Login</template>
      <template #content>
        <Form v-slot="$form" @submit="onSubmit" class="flex justify-center flex-col gap-6 mt-5">
          <div class="flex flex-col gap-1">
            <InputText name="email" type="email" placeholder="Email" v-model="loginForm.email"/>
            <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">
              {{ $form.email.error?.message }}
            </Message>
          </div>
          <div class="flex flex-col gap-1">
            <Password name="password" type="password" placeholder="Password" v-model="loginForm.password" fluid
                      toggle-mask :feedback="false"/>
            <Message v-if="$form.password?.invalid" severity="error" size="small" variant="simple">
              {{ $form.password.error?.message }}
            </Message>
          </div>
          <Message v-if="failed" severity="error" size="small" variant="simple">
            Invalid email or password.
          </Message>
          <Button type="submit" severity="secondary" label="Submit"
                  :disabled="!loginForm.email || !loginForm.password"/>
        </Form>
      </template>
    </Card>
  </div>
</template>

<style scoped>

</style>