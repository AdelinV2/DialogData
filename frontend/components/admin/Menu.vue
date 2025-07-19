<script setup lang="ts">

const { user } = useUserStorage();
const { t } = useI18n();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const menuItems = [
  {
    label: 'Admin panel',
    icon: 'pi pi-cog',
    submenuIcon: 'pi pi-chevron-down',
    items: [
      {
        label: t('admin.menu.addProduct'),
        icon: 'pi pi-box',
        command: () => navigateTo('/admin/add-product')
      },
      {
        label: t('admin.menu.addCSVProducts'),
        icon: 'pi pi-file',
        command: () => navigateTo('/admin/add-csv-products')
      },
      {
        label: t('admin.menu.categories'),
        icon: 'pi pi-tags',
        command: () => navigateTo('/admin/categories')
      },
      {
        label: t('admin.menu.orders'),
        icon: 'pi pi-shopping-cart',
        command: () => navigateTo('/orders')
      },
      {
        label: t('admin.menu.newsletters'),
        icon: 'pi pi-envelope',
        command: () => navigateTo('/admin/newsletters')
      }
    ]
  }
]

</script>

<template>

  <Menubar :model="menuItems" />

</template>

<style scoped>

</style>