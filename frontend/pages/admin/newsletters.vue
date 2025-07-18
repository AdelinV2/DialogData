<script setup lang="ts">

import type {Newsletter} from "~/types/newsletter";

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {t} = useI18n();
const newsletters = ref([] as Newsletter[]);

const fetchNewsletters = () => {
  $fetch(`${apiBaseUrl}/newsletter/all`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        newsletters.value = response._data as Newsletter[];
      } else {
        console.error('Failed to fetch newsletters');
      }
    },
    onResponseError({response}) {
      console.error('Error fetching newsletters:', response);
    }
  });
};

onMounted(() => {
  fetchNewsletters();
});

</script>

<template>



</template>

<style scoped>

</style>