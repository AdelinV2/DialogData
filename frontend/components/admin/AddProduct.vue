<script setup lang="ts">

import type {Attribute} from "~/types/attribute";
import type {Category} from "~/types/category";
import type {Product} from "~/types/product";


const product = ref<Product>({
  name: '',
  description: '',
  price: 0,
  availableQuantity: 0,
  addedDate: new Date(),
  attributes: [] as Attribute[],
  category: {} as Category,
  imageUrls: [],
  imageFile: [],
})

const categories = ref<Category[]>([])
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

function handleImageChange(event: Event) {
  const target = event.target as HTMLInputElement | null;
  if (!target || !target.files) return;
  const files = Array.from(target.files);
  product.value.imageFile = [];
  product.value.imageUrls = [];

  files.forEach((file, index) => {
    const reader = new FileReader();
    reader.onload = () => {
      product.value.imageFile?.push({
        base64: reader.result as string,
        fileName: `${index}`,
      });
      product.value.imageUrls?.push(reader.result as string);
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
      console.error('Failed to fetch categories');
    }
  },
}).then((data) => {
  console.log(data);
}).catch((error) => {
  console.error('Error fetching categories:', error);
});

function onSubmit() {

}

</script>

<template>

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
            <InputText id="price" type="number" v-model="product.price"/>
            <label for="price">Price</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-box"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <InputText id="availableQuantity" type="number" v-model="product.availableQuantity"/>
            <label for="availableQuantity">Available Quantity</label>
          </FloatLabel>
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <InputText id="addedDate" type="date" v-model="product.addedDate"/>
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
                  @click="product.attributes.push({ name: '', value: '' })"/>
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
                optionValue="id"
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
              :disabled="!product.name || !product.description || product.price <= 0 || !product.addedDate || !product.category.id || product.attributes.length === 0 || product.imageFile?.length === 0" />
    </template>
  </Card>

  <ProductDetails :product="product"/>

</template>

<style scoped>

</style>