<script setup lang="ts">

import type {Attribute} from "~/types/attribute";
import type {Category} from "~/types/category";
import type {Product} from "~/types/product";
import type {Image} from "~/types/image";


const product = ref<Product>({
  name: '',
  description: '',
  price: 0.0,
  availableQuantity: 0,
  addedDate: new Date(),
  attributes: [] as Attribute[],
  category: {} as Category,
  images: [] as Image[],
})

const toast = useToast();
const categories = ref<Category[]>([])
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

function handleImageChange(event: Event) {
  const target = event.target as HTMLInputElement | null;
  if (!target || !target.files) return;
  const files = Array.from(target.files);
  product.value.images = [];

  files.forEach((file, index) => {
    const reader = new FileReader();
    reader.onload = () => {
      product.value.images.push({
        base64: reader.result as string,
        fileName: index.toString(),
        imageUrl: '',
      });
    };
    reader.readAsDataURL(file);
  });
}

$fetch(`${apiBaseUrl}/category`, {
  method: 'GET',
  onResponse({response}) {
    if (response.status === 200) {
      categories.value = response._data as Category[];
      console.log('Categories fetched successfully:', categories.value);
    } else {
      toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to fetch categories',
        life: 3000,
      });
      console.error('Failed to fetch categories');
    }
  },
}).catch((error) => {
  toast.add({
    severity: 'error',
    summary: 'Error',
    detail: 'Error fetching categories',
    life: 3000,
  });
  console.error('Error fetching categories:', error);
});

function onSubmit() {
  $fetch(`${apiBaseUrl}/products`, {
    method: 'POST',
    body: product.value,
    onResponse({response}) {
      if (response.status === 201) {
        console.log('Product added successfully:', response._data);
        toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Product added successfully',
          life: 3000,
        });
        product.value = {
          name: '',
          description: '',
          price: 0,
          availableQuantity: 0,
          addedDate: new Date(),
          attributes: [] as Attribute[],
          category: {} as Category,
          images: [] as Image[],
        };
      } else {
        console.error('Failed to add product');
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to add product',
          life: 3000,
        });
      }
    },
  }).catch((error) => {
    console.error('Error adding product:', error);
  });
}

function addAttribute() {
  const newName = '';
  if (product.value.attributes.some(attr => attr.name === newName)) {
    toast.add({
      severity: 'error',
      summary: 'Duplicate Attribute',
      detail: 'Attribute names must be unique.',
      life: 3000,
    });
    return;
  }
  product.value.attributes.push({ name: newName, value: '' });
}

</script>

<template>

  <Toast />
  <Card class="mt-8 mb-14" style="width: 600px; justify-self: center;">
    <template #title>
      Add Product
    </template>
    <template #content>
      <div class="flex flex-col gap-4">
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-tag"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <InputText id="name" v-model="product.name"/>
            <label for="name">Name</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-align-left"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <Textarea id="description" class="w-full" v-model="product.description"/>
            <label for="description">Description</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-dollar"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <InputNumber id="price" type="number" :max-fraction-digits="2" v-model="product.price"/>
            <label for="price">Price</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-box"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <InputNumber id="availableQuantity" type="number" v-model="product.availableQuantity"/>
            <label for="availableQuantity">Available Quantity</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <DatePicker id="addedDate" date-format="dd / mm / yy" type="date" v-model="product.addedDate"/>
            <label for="addedDate">Added Date</label>
          </FloatLabel>
        </InputGroup>

        <div class="flex flex-col">
          <div v-for="(attr, index) in product.attributes" :key="index" class="flex gap-2 mb-2">
            <InputText
                :id="`attribute-name-${index}`"
                v-model="attr.name"
                placeholder="Name"
                class="w-1/2"
            />
            <InputText
                :id="`attribute-value-${index}`"
                v-model="attr.value"
                placeholder="Value"
                class="w-1/2"
            />
            <Button icon="pi pi-trash" severity="danger" @click="product.attributes.splice(index, 1)"/>
          </div>
          <Button icon="pi pi-plus" class="w-fit self-center mb-3" label="Add Attribute"
                  @click="addAttribute"/>
        </div>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-folder"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <Dropdown
                id="category"
                v-model="product.category"
                :options="categories"
                optionLabel="name"
                class="w-full"
            />
            <label for="category">Category</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-image"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <input
                id="imageUrls"
                type="file"
                multiple
                @change="handleImageChange"
                class="p-inputtext p-component w-full"
            />
          </FloatLabel>
        </InputGroup>
      </div>
      <Button label="Submit" class="w-full mt-5" @click="onSubmit"
              :disabled="!product.name || !product.description || product.price <= 0 || !product.addedDate || !product.category || product.attributes.length === 0 || product.images?.length === 0" />
    </template>
  </Card>

  <ProductDetails :product="product"/>

</template>

<style scoped>

</style>