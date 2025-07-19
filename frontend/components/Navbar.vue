<script setup lang="ts">
import {ref} from 'vue'
import {Role} from "~/types/role";

const search = ref('')
const emit = defineEmits<{ (e: 'search', term: string): void }>()
const { t, locale, setLocale } = useI18n()

const onSearch = () => {
  emit('search', search.value)
}

const {user, removeUser} = useUserStorage();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const cartItems = ref(0);

onMounted(() => {
  if (user.value) {
    $fetch(`${apiBaseUrl}/cart/${user.value.id}`, {
      method: 'GET',
      onResponse({response}) {
        if (response.status === 200) {
          cartItems.value = response._data.cartEntries.length;
          console.log('Cart items fetched successfully:', cartItems.value);
        } else {
          console.error('Failed to fetch cart items');
        }
      }
    });
  }
})

const menuItems = [
  {
    label: user.value ? `${user.value.firstName} ${user.value.lastName}` : t('navbar.myAccount'),
    icon: 'pi pi-user',
    submenuIcon: 'pi pi-chevron-down',
    items: user.value ?
        [
          {
            label: t('navbar.editProfile'),
            icon: 'pi pi-pencil',
            command: () => navigateTo('/profile')
          },
          {
            label: t('navbar.orders'),
            icon: 'pi pi-list',
            command: () => navigateTo('/orders')
          },
          {
            label: t('navbar.logout'),
            icon: 'pi pi-sign-out',
            command: () => {
              removeUser()
              window.location.reload()
            }
          }
        ]
        : [
          {
            label: t('navbar.login'),
            icon: 'pi pi-sign-in',
            command: () => navigateTo('/login')
          },
          {
            label: t('navbar.register'),
            icon: 'pi pi-user-plus',
            command: () => navigateTo('/register')
          }
        ]
  }
]

const languages = [
  {
    code: 'en',
    label: 'English',
  },
  {
    code: 'ro',
    label: 'Română',
  }
]

const onLanguageChange = (event: any) => {
  setLocale(event.value);
  setTimeout(() => {
    window.location.reload();
  }, 100);
}

</script>

<template>
  <Toolbar>
    <template #start>
      <img src="/favicon.ico" alt="Logo" @click="navigateTo('/')" style="height: 40px; cursor: pointer;"/>
    </template>

    <template #center>
      <IconField>
        <InputIcon>
          <i class="pi pi-search" @click="onSearch"/>
        </InputIcon>
        <InputText
            v-model="search"
            :placeholder="t('navbar.search')"
            class="rounded-pill md:w-96 lg:w-lg"
            @keyup.enter="onSearch"
        />
      </IconField>
    </template>

    <template #end>
      <AdminMenu v-if="user && user.role === Role.ADMIN" class="me-10" />
      <Dropdown
          v-model="locale"
          :options="languages"
          option-label="label"
          option-value="code"
          @change="onLanguageChange"
          class="me-10"
          style="width: 130px;"
      />
      <Menubar :model="menuItems">
        <template #end>
          <template v-if="cartItems > 0">
            <OverlayBadge
                :value="cartItems"
                severity="info"
                class="p-overlay-badge"
            >
              <i
                  class="pi pi-shopping-cart"
                  @click="user ? navigateTo('/cart') : navigateTo('/login')"
                  style="font-size: 1.5rem; cursor: pointer;"
              />
            </OverlayBadge>
          </template>
          <template v-else>
            <i
                class="pi pi-shopping-cart"
                @click="user ? navigateTo('/cart') : navigateTo('/login')"
                style="font-size: 1.5rem; cursor: pointer;"
            />
          </template>
        </template>
      </Menubar>
    </template>
  </Toolbar>
</template>

<style scoped>

</style>