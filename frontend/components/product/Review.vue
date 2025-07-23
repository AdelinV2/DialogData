<script setup lang="ts">

import type {Review} from "~/types/review";
import type {Product} from "~/types/product";

const props = defineProps<{
  product: Product
}>();

const {t} = useI18n();
const {user} = useUserStorage();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const reviews = ref<Review[]>([]);
const canWriteReview = ref(false);
const dialog = ref(false);
const loading = ref(false);
const toast = useToast();

const pagination = ref({
  page: 0,
  size: 5,
  totalElements: 0,
  totalPages: 0
});

const averageRating = ref({
  average: 0,
  oneStar: 0,
  twoStars: 0,
  threeStars: 0,
  fourStars: 0,
  fiveStars: 0,
  count: 0,
})

const reviewForm = ref<Review>({
  product: props.product,
  user: user.value ? user.value : null,
  rating: 0,
  comment: '',
})

const fetchReviews = () => {
  $fetch(`${apiBaseUrl}/review/product/${props.product.id}`, {
    method: 'GET',
    params: {
      page: pagination.value.page,
      size: pagination.value.size,
      sort: 'createdDate,desc'
    },
    onResponse({response}) {
      if (response.status === 200) {
        reviews.value = response._data.content as Review[];
        pagination.value.totalElements = response._data.totalElements;
        pagination.value.totalPages = response._data.totalPages;
        calculateAverageRating();
        console.log('Reviews fetched successfully');
      } else {
        console.error('Failed to fetch reviews');
      }
    },
    onResponseError({response}) {
      console.error('Error fetching reviews:', response);
    }
  });
}

const fetchUserCanWriteReview = () => {
  if (!user.value) return false;

  $fetch(`${apiBaseUrl}/review/check/${props.product.id}/${user.value.id}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        canWriteReview.value = !response._data as boolean;
        console.log('User can write review:', canWriteReview.value);
      } else {
        console.error('Failed to check if user can write review');
      }
    },
    onResponseError({response}) {
      console.error('Error checking if user can write review:', response);
    }
  });

}

const submitReview = () => {
  if (!user.value) {
    console.error('User is not logged in');
    return;
  }

  reviewForm.value.user = user.value;

  $fetch(`${apiBaseUrl}/review`, {
    method: 'POST',
    body: reviewForm.value,
    onResponse({response}) {

      reviewForm.value = {
        product: props.product,
        user: user.value ? user.value : null,
        rating: 0,
        comment: '',
      };

      loading.value = false;

      if (response.status === 201) {
        console.log('Review submitted successfully');

        toast.add({
          severity: 'success',
          summary: t('review.success'),
          detail: t('review.reviewSubmitted'),
          life: 3000
        });
        dialog.value = false;
        fetchReviews();
      } else {
        console.error('Failed to submit review');
        toast.add({
          severity: 'error',
          summary: t('review.error'),
          detail: t('review.reviewSubmissionFailed'),
          life: 3000
        });
      }
    },
    onResponseError({response}) {
      console.error('Error submitting review:', response);
      toast.add({
        severity: 'error',
        summary: t('review.error'),
        detail: t('review.reviewSubmissionFailed'),
        life: 3000
      });
    }
  });
}

onMounted(() => {
  fetchReviews();
  fetchUserCanWriteReview();
});
const calculateAverageRating = () => {

  if (reviews.value.length === 0) {
    averageRating.value = {
      average: 0,
      oneStar: 0,
      twoStars: 0,
      threeStars: 0,
      fourStars: 0,
      fiveStars: 0,
      count: 0,
    };
    return;
  }

  const totalRating = reviews.value.reduce((sum, review) => sum + review.rating, 0);
  averageRating.value.count = reviews.value.length;

  averageRating.value.average = parseFloat((totalRating / averageRating.value.count).toFixed(2));

  averageRating.value.oneStar = reviews.value.filter(r => r.rating === 1).length;
  averageRating.value.twoStars = reviews.value.filter(r => r.rating === 2).length;
  averageRating.value.threeStars = reviews.value.filter(r => r.rating === 3).length;
  averageRating.value.fourStars = reviews.value.filter(r => r.rating === 4).length;
  averageRating.value.fiveStars = reviews.value.filter(r => r.rating === 5).length;
};

</script>

<template>

  <Toast/>

  <Card class="w-fit">
    <template #title>
      <h2 class="text-2xl font-bold">{{ t('review.reviews') }}</h2>
    </template>
    <template #content>
      <div class="flex flex-row gap-4">
        <div class="mt-8">
          <div class="bg-surface-100 p-1 bg-white mb-2" style="border-radius: 30px">
            <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                 style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">
                <span class="text-surface-900 font-medium text-sm text-gray-900">
                  {{ averageRating.average ? averageRating.average.toFixed(2) : 0 }}</span>
              <i class="pi pi-star-fill text-yellow-500"></i>
            </div>
          </div>
          <span class="text-sm text-gray-400">{{ averageRating.count }} {{ t('review.reviews') }}</span>
        </div>
        <div class="ms-10">
          <div class="flex flex-row items-center">
            <span class="text-md font-semibold">5 {{ t('review.stars') }}</span>
            <ProgressBar :value="averageRating.fiveStars / averageRating.count * 100"
                         :showValue="false"
                         class="w-72 ml-2"
                         style="--p-progressbar-value-background: #22c55e;"/>
            <span class="ml-2 text-md text-gray-500">({{ averageRating.fiveStars }})</span>
          </div>
          <div class="flex flex-row items-center mt-2">
            <span class="text-md font-semibold">4 {{ t('review.stars') }}</span>
            <ProgressBar :value="averageRating.fourStars / averageRating.count * 100"
                         :showValue="false"
                         class="w-72 ml-2"
                         style="--p-progressbar-value-background: #4ade80;"/>
            <span class="ml-2 text-md text-gray-500">({{ averageRating.fourStars }})</span>
          </div>
          <div class="flex flex-row items-center mt-2">
            <span class="text-md font-semibold">3 {{ t('review.stars') }}</span>
            <ProgressBar :value="averageRating.threeStars / averageRating.count * 100"
                         :showValue="false"
                         class="w-72 ml-2"
                         style="--p-progressbar-value-background: #fde047;"/>
            <span class="ml-2 text-md text-gray-500">({{ averageRating.threeStars }})</span>
          </div>
          <div class="flex flex-row items-center mt-2">
            <span class="text-md font-semibold">2 {{ t('review.stars') }}</span>
            <ProgressBar :value="averageRating.twoStars / averageRating.count * 100"
                         :showValue="false"
                         class="w-72 ml-2"
                         style="--p-progressbar-value-background: #fbbf24;"/>
            <span class="ml-2 text-md text-gray-500">({{ averageRating.twoStars }})</span>
          </div>
          <div class="flex flex-row items-center mt-2">
            <span class="text-md font-semibold">1 {{ t('review.stars') }}</span>
            <ProgressBar :value="averageRating.oneStar / averageRating.count * 100"
                         :showValue="false"
                         class="w-72 ml-2"
                         style="--p-progressbar-value-background: #ef4444;"/>
            <span class="ml-2 text-md text-gray-500">({{ averageRating.oneStar }})</span>
          </div>
        </div>
        <div>
          <Button :label="t('review.writeReview')" severity="warn" class="ms-4" @click="dialog = true"
                  v-if="user && user.id && canWriteReview" />
          <Dialog v-model:visible="dialog" :style="{width: '450px'}" :header="t('review.writeReview')">
            <div class="flex flex-col gap-4">
              <div class="flex flex-col items-center">
                <label for="rating" class="font-semibold">{{ t('review.rating') }}</label>
                <Rating v-model="reviewForm.rating" :cancel="false" :stars="5" class="mt-2"/>
              </div>
              <div class="flex flex-col">
                <label for="comment" class="font-semibold">{{ t('review.comment') }}</label>
                <Textarea v-model="reviewForm.comment" rows="5" class="mt-2"/>
              </div>
              <Button :label="t('review.submitReview')" :disabled="reviewForm.rating === 0 || loading" severity="success"
                      @click="submitReview"/>
              <div class="card mt-1" v-if="loading">
                <ProgressBar mode="indeterminate" style="height: 6px"></ProgressBar>
              </div>
            </div>
          </Dialog>
        </div>
      </div>
    </template>
  </Card>

  <div class="card mt-10" v-if="reviews.length > 0">
    <DataView
        :value="reviews"
        paginator
        :rows="pagination.size"
        :totalRecords="pagination.totalElements"
        :first="pagination.page * pagination.size"
        @page="(e) => { pagination.page = e.page; fetchReviews(); }"
    >
      <template #list="slotProps">
        <div class="flex flex-col">
          <div v-for="(item, index) in slotProps.items" :key="index">
            <div class="flex flex-col sm:flex-row sm:items-center p-6 gap-4"
                 :class="{ 'border-t border-surface-200 dark:border-surface-700': index !== 0 }">
              <div class="md:w-40 relative">
                <div>
                  {{ item.createdDate }}
                </div>
              </div>
              <div class="flex flex-col md:flex-row justify-between md:items-center flex-1 gap-6">
                <div class="flex flex-row md:flex-col justify-between items-start gap-2">
                  <div>
                    <span v-if="item.verified" class="font-medium text-surface-500 dark:text-surface-400 text-sm">
                      {{ t('review.verified') }}
                    </span>
                    <div class="text-lg font-medium mt-2">{{ item.user.firstName }} {{ item.user.lastName }}</div>
                  </div>
                  <div>
                    <div class="bg-surface-100 p-1 bg-white" style="border-radius: 30px">
                      <div class="bg-surface-0 flex items-center gap-2 justify-center py-1 px-2"
                           style="border-radius: 30px; box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.04), 0px 1px 2px 0px rgba(0, 0, 0, 0.06)">
                          <span class="text-surface-900 font-medium text-sm text-gray-900">
                            {{ item.rating }}
                          </span>
                        <i class="pi pi-star-fill text-yellow-500"></i>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="flex-1">
                  <p class="text-gray-700 dark:text-gray-300 mt-2">{{ item.comment }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </DataView>
  </div>


</template>

<style scoped>

</style>