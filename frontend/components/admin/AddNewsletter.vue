<script setup lang="ts">

import type {Newsletter} from "~/types/newsletter";

const {user} = useUserStorage()
const {t} = useI18n()
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const toast = useToast()
const newsletter = ref<Newsletter>({
  content: '',
  subscriptionDate: new Date(),
  scheduleDate: new Date(),
  repeat: false,
  repeatInterval: undefined
})

const loading = ref(false);
const intervalValue = ref<number | undefined>(undefined);
const intervalOptions = [
  {
    label: t('newsletter.interval.daily'),
    value: 864000,
  },
  {
    label: t('newsletter.interval.weekly'),
    value: 604800000,
  },
  {
    label: t('newsletter.interval.monthly'),
    value: 2592000000,
  }
]

const submitNewsletter = () => {
  loading.value = true;

  if (newsletter.value.repeat) {
    newsletter.value.repeatInterval = intervalValue.value;
  } else {
    newsletter.value.repeatInterval = undefined;
  }

  $fetch(apiBaseUrl + '/newsletter', {
    method: 'POST',
    body: newsletter.value,
    onResponse({response}) {
      if (response.status === 201) {
        newsletter.value = {
          content: '',
          subscriptionDate: new Date(),
          scheduleDate: new Date(),
          repeat: false,
          repeatInterval: undefined
        }
        toast.add({severity: 'success', summary: t('newsletter.success')})
      } else {
        toast.add({severity: 'error', summary: t('newsletter.error')})
      }
    },
    onResponseError({response}) {
      toast.add({severity: 'error', summary: t('newsletter.error')})
    }
  })

}

</script>

<template>

  <Toast />

  <div class="max-w-4xl mx-auto mt-8">
    <h1 class="text-2xl font-bold mb-4">{{ t('newsletter.create') }}</h1>


    <Editor v-model="newsletter.content" name="content" editorStyle="height: 320px"/>

    <div class="flex flex-col md:flex-row gap-4 my-4">
      <h2 class="text-xl font-semibold mb-2 self-center">{{ t('newsletter.scheduleDate') }}</h2>
      <Calendar v-model="newsletter.scheduleDate"
                :showTime="true"
                :showIcon="true"
                :placeholder="t('newsletter.scheduleDate')"
                class="w-fit my-4"
      />
    </div>

    <div class="flex flex-col md:flex-row gap-4 my-4">
      <h2 class="text-xl font-semibold mb-2 self-center">{{ t('newsletter.repeat') }}</h2>
      <Checkbox v-model="newsletter.repeat" :label="t('newsletter.repeat')" class="self-center" binary/>
      <Dropdown v-model="intervalValue" :options="intervalOptions" optionLabel="label"
                :placeholder="t('newsletter.repeatInterval')" class="w-full md:w-1/3 ms-14"
                :disabled="!newsletter.repeat"/>
    </div>

    <div class="my-4">
      <h1 class="text-3xl font-semibold my-6">{{ t('newsletter.preview') }}</h1>
      <div class="border p-4 rounded" v-html="newsletter.content"></div>
    </div>

    <div class="flex justify-center my-8">
      <Button @click="submitNewsletter" :disabled="loading" :loading="loading">
        {{ t('newsletter.submit') }}
      </Button>
    </div>

    <div class="card mb-6">
      <ProgressBar v-if="loading" mode="indeterminate" style="height: 6px"></ProgressBar>
    </div>
  </div>

</template>

<style scoped>

</style>