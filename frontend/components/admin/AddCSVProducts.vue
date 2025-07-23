<script setup lang="ts">
const report = ref({
  successCount: 0,
  failedLine: [],
  failedCount: 0,
})

const file = ref<File | null>(null);
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const loading = ref(false);

const onFileSelect = (event: any) => {
  if (event.files && event.files.length > 0) {
    file.value = event.files[0];
  }
}

const onUpload = () => {
  loading.value = true;
  if (!file.value) return;
  const formData = new FormData();
  formData.append('file', file.value);

  $fetch(`${apiBaseUrl}/products/csv`, {
    method: 'POST',
    body: formData,
    onResponse({ response }) {
      if (response.status === 201) {
        report.value.successCount = response._data.successCount;
        report.value.failedLine = response._data.failedLine;
        report.value.failedCount = report.value.failedLine ? report.value.failedLine.length : 0;
        console.log('CSV uploaded successfully:', response._data);
        loading.value = false;
      } else {
        console.error('Failed to upload CSV');
      }
    },
    onResponseError({ response }) {
      console.error('Error uploading CSV:', response.status, response.statusText);
    }
  })
}
</script>

<template>
  <Navbar />
  <div class="container mx-auto px-36 my-10">
    <div v-if="report.successCount === 0 && report.failedCount === 0" class="text-center">
      <h2 class="text-2xl font-bold mb-4">Upload CSV Products</h2>
      <p class="mb-10">
        <span class="font-bold">Sample format:</span> {Product name},{Description},{Price},{Available quantity},{Category ID},{Attribute}/{Attribute value};{Attribute}/{Attribute Value},{Image link};{Image link}
      </p>
      <div class="card flex flex-wrap gap-6 items-center justify-between">
        <FileUpload mode="basic" accept=".csv" :auto="false" @select="onFileSelect" />
        <Button label="Upload" @click="onUpload" severity="secondary" :disabled="!file || loading" />
      </div>
      <div class="card my-8">
        <ProgressBar v-if="loading" mode="indeterminate" style="height: 6px"></ProgressBar>
      </div>
    </div>
    <div v-else class="text-center">
      <h2 class="text-2xl font-bold mb-4">Upload Results</h2>
      <p class="mb-4">Successfully uploaded: {{ report.successCount }}</p>
      <p class="mb-4">Failed lines: {{ report.failedCount }}</p>
      <ul class="list-disc list-inside">
        <li v-for="line in report.failedLine" :key="line">{{ line }}</li>
      </ul>
    </div>
  </div>
  <Footer />
</template>

<style scoped>

</style>