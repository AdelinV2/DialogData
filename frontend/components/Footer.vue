<script setup lang="ts">

const {user, saveUser} = useUserStorage();
const {t} = useI18n();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const showSubscribeDialog = ref(false);
const toast = useToast();

const onSubscribeSubmit = () => {
  showSubscribeDialog.value = false;
  user.value.subscribed = !user.value.subscribed;

  $fetch(`${apiBaseUrl}/user/${user.value.id}`, {
    method: 'PUT',
    body: user.value,
    onResponse({response}) {
      if (response.status === 200) {
        saveUser(user.value);
        if (user.value.subscribed) {
          toast.add({severity: 'success', summary: t('newsletter.subscribeSuccess')});
        } else {
          toast.add({severity: 'info', summary: t('newsletter.unsubscribeSuccess')});
        }
      } else {
          user.value.subscribed = !user.value.subscribed;
        toast.add({severity: 'error', summary: "Error"});
      }
    }
  })

};

</script>

<template>

  <Toast/>

  <footer class="bg-gray-950 text-gray-200 py-6">
    <div class="max-w-5xl mx-auto px-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="font-semibold tracking-wide">© 2025 Dialog Data</span>
      <nav class="flex gap-6">
        <span v-if="user !== null" @click="showSubscribeDialog = true" class="hover:text-primary-400 transition-colors cursor-pointer">
          {{ t('newsletter.subscribe') }}
        </span>
        <a href="#" class="hover:text-primary-400 transition-colors">Home</a>
        <a href="#" class="hover:text-primary-400 transition-colors">About</a>
        <a href="#" class="hover:text-primary-400 transition-colors">Contact</a>
      </nav>
    </div>
    <Divider class="!my-4 !border-gray-700"/>
    <div class="text-center text-sm text-gray-500">
      Built with <span class="text-primary-400 font-medium">PrimeVue</span> &amp; <span
        class="text-blue-400 font-medium">Tailwind CSS</span>
    </div>
  </footer>

  <Dialog v-model:visible="showSubscribeDialog" :style="{ width: '450px' }"
          :header="user?.subscribed ? t('newsletter.unsubscribe') : t('newsletter.subscribe')">
    <Button :label="t('newsletter.yes')"
            class="w-full mt-5"
            @click="onSubscribeSubmit"/>
  </Dialog>

</template>

<style scoped>

</style>