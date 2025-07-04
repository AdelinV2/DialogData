<script setup lang="ts">
import {ref} from 'vue'

const search = ref('')
const {user, removeUser} = useUserStorage()

const menuItems = [
  {
    label: user.value ? `${user.value.firstName} ${user.value.lastName}` : 'My account',
    icon: 'pi pi-user',
    submenuIcon: 'pi pi-chevron-down',
    items: user.value ?
        [
          {
            label: 'Edit Profile',
            icon: 'pi pi-pencil',
            command: () => navigateTo('/profile')
          },
          {
            label: 'Logout',
            icon: 'pi pi-sign-out',
            command: () => {
              removeUser()
              window.location.reload()
            }
          }
        ]
        : [
          {
            label: 'Login',
            icon: 'pi pi-sign-in',
            command: () => navigateTo('/login')
          }
        ]
  },
  {
    label: 'Cart',
    icon: 'pi pi-shopping-cart',
    command: () => {
      user.value ? navigateTo('/cart') : navigateTo('/login');
    }
  }
]

</script>

<template>
  <Toolbar>
    <template #start>
      <img src="/favicon.ico" alt="Logo" style="height: 40px"/>
    </template>
    <template #center>
      <IconField>
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText v-model="search" placeholder="Search..." class="rounded-pill" style=""/>
      </IconField>
    </template>
    <template #end>
      <Menubar :model="menuItems">
      </Menubar>
    </template>
  </Toolbar>
</template>

<style scoped>

</style>