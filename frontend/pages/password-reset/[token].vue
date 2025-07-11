<script setup lang="ts">

import PasswordResetForm from "~/components/PasswordResetForm.vue";

const token = useRoute().params.token as string;
const validToken = ref('loading');

const passwordResetForm = ref({
  password: '',
  confirmPassword: '',
  mismatch: false,
  weakPassword: false,
})

const showForgotPassword = ref(false);
const resetSuccess = ref(false);

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const verifyToken = () => {
  $fetch(`${apiBaseUrl}/password-reset/${token}`, {
    method: 'GET',
    onResponse({ response }) {
      if (response.status === 200) {
        console.log('Token verified successfully');
        validToken.value = 'true';
      } else {
        console.error('Token verification failed');
        validToken.value = 'false';
      }
    }
  }).then((data) => {
    console.log(data);
  }).catch((error) => {
    console.error('Error verifying token:', error);
  });
}

const onSubmit = () => {

  passwordResetForm.value.mismatch = false;
  passwordResetForm.value.weakPassword = false;

  if (passwordResetForm.value.password !== passwordResetForm.value.confirmPassword) {
    passwordResetForm.value.mismatch = true;
    return;
  }

  if (passwordResetForm.value.password.length < 8) {
    passwordResetForm.value.weakPassword = true;
    return;
  }

  $fetch(`http://localhost:8080/api/password-reset/${token}`, {
    method: 'POST',
    body: passwordResetForm.value.password,
    onResponse({ response }) {
      if (response.status === 200) {
        console.log('Password reset successfully');
        navigateTo('/login');
      } else {
        console.error('Password reset failed');
      }
    }
  }).catch((error) => {
    console.error('Error resetting password:', error);
  });
}

verifyToken();

</script>

<template>
  <Navbar/>
  <Card class="w-full max-w-sm mt-12 justify-self-center min-h-[calc(100vh-250px)]" v-if="validToken === 'true'">
    <template #title>Reset password</template>
    <template #content>
      <Form v-slot="$form" @submit="onSubmit" class="flex justify-center flex-col gap-6 mt-5">
        <div class="flex flex-col gap-1">
          <Password name="password" type="password" placeholder="Password" v-model="passwordResetForm.password" fluid
                    toggle-mask/>
          <Message v-if="passwordResetForm.weakPassword" severity="error" size="small" variant="simple">
            Password must be at least 8 characters long.
          </Message>
        </div>
        <div class="flex flex-col gap-1">
          <Password name="confirmPassword" type="password" placeholder="Confirm Password" :feedback="false"
                    v-model="passwordResetForm.confirmPassword" fluid toggle-mask/>
          <Message v-if="passwordResetForm.mismatch" severity="error" size="small" variant="simple">
            Passwords do not match.
          </Message>
        </div>
        <Button label="Reset Password" type="submit" :disabled="!passwordResetForm.password || !passwordResetForm.confirmPassword"
                severity="secondary"/>
      </Form>
    </template>
  </Card>
  <Card class="w-full max-w-sm mt-12 justify-self-center" v-else-if="validToken === 'false'">
    <template #title>
      <p class="text-center">Token is invalid!</p>
    </template>
    <template #content>
      <p class="text-center">The password reset token is invalid or has expired. Please request a new password reset.</p>
      <div class="text-center mt-1">
        <Button href="#" @click.prevent="showForgotPassword = true" class="text-sm hover:underline mt-4">
          Request a new password reset
        </Button>
      </div>
      <PasswordResetForm v-model:visible="showForgotPassword" @close="showForgotPassword = false"/>
    </template>
  </Card>
  <Card class="w-full max-w-sm mt-12 justify-self-center" v-else-if="validToken === 'loading'">
    <template #title>
      <p class="text-center">Loading...</p>
    </template>
    <template #content>
      <p class="text-center">Please wait while we verify your token.</p>
    </template>
  </Card>
  <Footer />
</template>

<style scoped>

</style>