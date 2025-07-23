<script setup lang="ts">

import type {Category} from "~/types/category";

const {user} = useUserStorage();
const {t} = useI18n();
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;
const categories = ref<Category[]>([]);
const editDialogVisible = ref(false);
const addDialogVisibile = ref(false);

const newCategory = ref<Category>({
  id: undefined,
  name: '',
  description: ''
})

const fetchCategories = () => {
  $fetch(`${apiBaseUrl}/category`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Categories fetched successfully:', response._data);
        categories.value = response._data as Category[];
      } else {
        console.error('Failed to fetch categories');
      }
    }
  });
}

const onAddCategory = () => {
  $fetch(`${apiBaseUrl}/category`, {
    method: 'POST',
    body: newCategory.value,
    onResponse({response}) {
      if (response.status === 201) {
        console.log('Category added successfully:', response._data);
        fetchCategories();
        newCategory.value = {
          id: undefined,
          name: '',
          description: ''
        };
        addDialogVisibile.value = false;
      } else {
        console.error('Failed to add category');
      }
    }
  })
}

const onUpdateCategory = () => {
  $fetch(`${apiBaseUrl}/category/${newCategory.value.id}`, {
    method: 'PUT',
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Category updated successfuly');
        fetchCategories();
        newCategory.value = {
          id: undefined,
          name: '',
          description: ''
        };
        editDialogVisible.value = false;
      } else {
        console.log('Failed to delete category');
      }
    }
  })
}

const onDeleteCategory = (categoryId: number) => {
  $fetch(`${apiBaseUrl}/category/${categoryId}`, {
    method: 'DELETE',
    onResponse({response}) {
      if (response.status === 204) {
        console.log('Category deleted successfully');
        fetchCategories();
      } else {
        console.error('Failed to delete category');
      }
    }
  })
}

onMounted(() => {
  fetchCategories();
});

</script>

<template>

  <Navbar/>

  <div class="container mx-auto px-12 my-12">
    <Button label="Add category" icon="pi pi-plus" @click="addDialogVisibile = true" class="mb-4"/>
    <DataTable :value="categories" dataKey="id" striped-rows responsiveLayout="scroll">
      <Column field="id" header="ID" class="w-1/6"/>
      <Column field="name" :header="t('admin.category.name')"/>
      <Column field="description" :header="t('admin.category.description')"/>
      <Column :header="t('admin.category.actions')" class=" flex gap-5 !text-center">
        <template #body="{ data }">
          <Button
              icon="pi pi-pencil"
              class="p-button-rounded p-button-info ml-2"
              @click="editDialogVisible = true; newCategory = {...data}"
              :aria-label="t('admin.category.edit')"
          />
          <Button
            icon="pi pi-trash"
            class="p-button-rounded p-button-danger"
            @click="onDeleteCategory(data.id)"
            :aria-label="t('admin.category.delete')"
          />
        </template>
      </Column>
    </DataTable>
  </div>

  <Dialog v-model:visible="editDialogVisible" modal :header="t('admin.category.editCategory')" class="w-xl">
    <FloatLabel variant="on" class="mt-5">
      <InputText id="category_name" v-model="newCategory.name" class="w-full"/>
      <label for="category_name">{{ t('admin.category.name') }}</label>
    </FloatLabel>
    <FloatLabel variant="on" class="mt-5">
      <Textarea id="category_description" v-model="newCategory.description" class="w-full"/>
      <label for="category_description">{{ t('admin.category.description') }}</label>
    </FloatLabel>
    <Button :label="t('admin.category.save')" @click="onUpdateCategory" class="mt-5 w-full"
            :disabled="newCategory.name === '' || newCategory.description ===''"/>
  </Dialog>

  <Dialog v-model:visible="addDialogVisibile" modal :header="t('admin.category.addCategory')" class="w-xl">
    <FloatLabel variant="on" class="mt-5">
      <InputText id="category_name" v-model="newCategory.name" class="w-full"/>
      <label for="category_name">{{ t('admin.category.name') }}</label>
    </FloatLabel>
    <FloatLabel variant="on" class="mt-5">
      <Textarea id="category_description" v-model="newCategory.description" class="w-full"/>
      <label for="category_description">{{ t('admin.category.description') }}</label>
    </FloatLabel>
    <Button :label="t('admin.category.save')" @click="onAddCategory" class="mt-5 w-full"
            :disabled="newCategory.name === '' || newCategory.description ===''"/>
  </Dialog>

  <Footer/>

</template>

<style scoped>

</style>