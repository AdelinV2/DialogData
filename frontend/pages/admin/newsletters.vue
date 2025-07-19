<script setup lang="ts">

import type {Newsletter} from "~/types/newsletter";
import {Role} from "~/types/role";
import dayjs from "dayjs";

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const {t} = useI18n();
const {user} = useUserStorage();
const newsletters = ref([] as Newsletter[]);

const intervalOptions = [
  {
    label: t('newsletter.interval.daily'),
    value: 86400,
  },
  {
    label: t('newsletter.interval.weekly'),
    value: 604800,
  },
  {
    label: t('newsletter.interval.monthly'),
    value: 2592000,
  }
]

const fetchNewsletters = () => {
  $fetch(`${apiBaseUrl}/newsletter/all`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        newsletters.value = response._data as Newsletter[];
      } else if (response.status === 204) {
        newsletters.value = [];
        console.log('No newsletters found');
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
  if (!user.value || user.value.role !== Role.ADMIN) {
    navigateTo('/');
  }
  fetchNewsletters();
});

const onDelete = (id: number) => {
  if (confirm(t('newsletter.confirmDelete'))) {
    $fetch(`${apiBaseUrl}/newsletter/${id}`, {
      method: 'DELETE',
      onResponse({response}) {
        if (response.status === 200) {
          fetchNewsletters();
          console.log('Newsletter deleted successfully');
        } else {
          console.error('Failed to delete newsletter');
        }
      },
      onResponseError({response}) {
        console.error('Error deleting newsletter:', response);
      }
    });
  }
};

</script>

<template>

  <Navbar/>

  <div class="container mx-auto py-14 min-h-[calc(100vh-200px)]">

    <Button :label="t('newsletter.addNewsletter')" icon="pi pi-plus" class="mb-5" @click="() => navigateTo('/admin/add-newsletter')" />

    <div class="card">
      <DataTable :value="newsletters" stripedRows tableStyle="min-width: 50rem">
        <Column field="scheduleDate" :header="t('newsletter.scheduleDate')">
          <template #body="slotProps">
            <span>{{ dayjs(slotProps.data.scheduleDate).format('DD/MM/YYYY HH:mm') }}</span>
          </template>
        </Column>
        <Column field="content" :header="t('newsletter.content')" body-class="html-content">
          <template #body="slotProps">
            <span v-html="slotProps.data.content"></span>
          </template>
        </Column>
        <Column field="repeatInterval" :header="t('newsletter.repeatInterval')">
          <template #body="slotProps">
            <span>
              {{
                intervalOptions.find(opt => opt.value === slotProps.data.repeatInterval)?.label ||
                slotProps.data.repeatInterval
              }}
            </span>
          </template>
        </Column>
        <Column :header="t('newsletter.actions')">
          <template #body="slotProps">
            <Button icon="pi pi-trash" class="ml-2" severity="danger" @click="onDelete(slotProps.data.id)"/>
          </template>
        </Column>
      </DataTable>
    </div>

  </div>

  <Footer/>

</template>

<style scoped>

</style>