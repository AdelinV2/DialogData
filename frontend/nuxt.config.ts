// https://nuxt.com/docs/api/configuration/nuxt-config
import Aura from '@primeuix/themes/aura'

export default defineNuxtConfig({
    compatibilityDate: '2025-05-15',
    devtools: {enabled: true},
    plugins: [],
    modules: [
        '@nuxtjs/tailwindcss',
        '@primevue/nuxt-module',
        '@vueuse/nuxt',
        '@nuxtjs/i18n',
    ],
    i18n: {
        lazy: true,
        langDir: '../locales',
        strategy: 'prefix',
        locales: [
            {
                code: 'en',
                name: 'English',
                file: 'en.json',
            },
            {
                code: 'ro',
                name: 'Română',
                file: 'ro.json',
            }
        ],
        defaultLocale: 'en',
    },
    primevue: {
        options: {
            theme: {
                preset: Aura
            }
        }
    },
    css: [
        '@/assets/styles/main.scss',
    ],
    runtimeConfig: {
        public: {
            apiBaseUrl: process.env.NUXT_API_URL || 'http://localhost:8080/api'
        }
    }
})