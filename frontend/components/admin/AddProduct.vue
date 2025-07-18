<script setup lang="ts">

import type {Category} from "~/types/category";
import type {Product} from "~/types/product";
import type {Image} from "~/types/image";
import type {AttributeValue} from "~/types/attributeValue";

const { user } = useUserStorage();

// TODO check if user is admin

const product = ref<Product>({
  name: '',
  description: '',
  price: 0.0,
  availableQuantity: 0,
  addedDate: new Date(),
  attributes: [] as AttributeValue[],
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

const files = ref<File[]>([]);
const uploadedFiles = ref<File[]>([]);

function onSelectedFiles(event: any) {
  files.value = event.files.map((file: File) => {
    (file as any).objectURL = URL.createObjectURL(file);
    return file;
  });
}

function uploadEvent(callback: Function) {
  files.value.forEach((file: File, idx: number) => {
    const reader = new FileReader();
    reader.onload = () => {
      product.value.images.push({
        base64: reader.result as string,
        fileName: file.name,
        imageUrl: '',
      });
      (file as any).objectURL = (file as any).objectURL || URL.createObjectURL(file);
      uploadedFiles.value.push(file);
      if (idx === files.value.length - 1) {
        files.value = [];
        callback();
      }
    };
    reader.readAsDataURL(file);
  });
}

function onTemplatedUpload() {
  toast.add({severity: 'info', summary: 'Success', detail: 'File Uploaded', life: 3000});
}

function formatSize(bytes: number) {
  const k = 1024;
  const dm = 2;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  if (bytes === 0) return `0 ${sizes[0]}`;
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`;
}

function onRemoveTemplatingFile(file: File, removeFileCallback: Function, index: number) {
  removeFileCallback(index);
  files.value = files.value.filter((f, i) => i !== index);
  if ((file as any).objectURL) {
    URL.revokeObjectURL((file as any).objectURL);
  }
}

function removeUploadedFileCallback(index: number) {
  const file = uploadedFiles.value[index];
  if ((file as any).objectURL) {
    URL.revokeObjectURL((file as any).objectURL);
  }
  uploadedFiles.value.splice(index, 1);
  product.value.images.splice(index, 1);
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
          attributes: [] as AttributeValue[],
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
  if (product.value.attributes.some(attr => attr.attribute.name === newName)) {
    toast.add({
      severity: 'error',
      summary: 'Duplicate Attribute',
      detail: 'Attribute names must be unique.',
      life: 3000,
    });
    return;
  }
  product.value.attributes.push({
    attribute: {
      name: newName,
    },
    value: '',
  });
}

</script>

<template>

  <Toast/>
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
                v-model="attr.attribute.name"
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
          <div class="card w-full">
            <Toast/>
            <FileUpload name="demo[]" url="/api/upload" @upload="onTemplatedUpload" :multiple="true" accept="image/*"
                        @select="onSelectedFiles">
              <template #header="{ chooseCallback, uploadCallback, clearCallback, files }">
                <div class="flex flex-wrap justify-between items-center flex-1 gap-4">
                  <div class="flex gap-2">
                    <Button @click="chooseCallback()" icon="pi pi-images" rounded outlined
                            severity="secondary"></Button>
                    <Button @click="uploadEvent(uploadCallback)" icon="pi pi-cloud-upload" rounded outlined
                            severity="success" :disabled="!files || files.length === 0"></Button>
                    <Button @click="clearCallback()" icon="pi pi-times" rounded outlined severity="danger"
                            :disabled="!files || files.length === 0"></Button>
                  </div>
                </div>
              </template>
              <template #content="{ removeFileCallback }">
                <div class="flex flex-col gap-8 pt-4">
                  <div v-if="files.length > 0">
                    <h5>Pending</h5>
                    <div class="flex flex-wrap gap-4">
                      <div
                          v-for="(file, index) in files"
                          :key="file.name + file.type + file.size"
                          class="p-8 rounded-border flex flex-col border border-surface items-center gap-4"
                      >
                        <img
                            role="presentation"
                            :alt="file.name"
                            :src="(file as any).objectURL"
                            width="100"
                            height="50"
                        />
                        <span class="font-semibold text-ellipsis max-w-60 whitespace-nowrap overflow-hidden">
                          {{ file.name }}
                        </span>
                        <div>{{ formatSize(file.size) }}</div>
                        <Badge value="Pending" severity="warn"/>
                        <Button
                            icon="pi pi-times"
                            @click="onRemoveTemplatingFile(file, removeFileCallback, index)"
                            outlined
                            rounded
                            severity="danger"
                        />
                      </div>
                    </div>
                  </div>

                  <div v-if="uploadedFiles.length > 0">
                    <h5>Completed</h5>
                    <div class="flex flex-wrap gap-4">
                      <div
                          v-for="(file, index) in uploadedFiles"
                          :key="file.name + file.type + file.size"
                          class="p-8 rounded-border flex flex-col border border-surface items-center gap-4"
                      >
                        <img
                            role="presentation"
                            :alt="file.name"
                            :src="product.images[index]?.base64"
                            width="100"
                            height="50"
                        />
                        <span class="font-semibold text-ellipsis max-w-60 whitespace-nowrap overflow-hidden">
                          {{ file.name }}
                        </span>
                        <div>{{ formatSize(file.size) }}</div>
                        <Badge value="Completed" severity="success"/>
                        <Button
                            icon="pi pi-times"
                            @click="removeUploadedFileCallback(index)"
                            outlined
                            rounded
                            severity="danger"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </template>
              <template #empty>
                <div class="flex items-center justify-center flex-col">
                  <i class="pi pi-cloud-upload !border-2 !rounded-full !p-8 !text-4xl !text-muted-color"/>
                  <p class="mt-6 mb-0">Drag and drop files to here to upload.</p>
                </div>
              </template>
            </FileUpload>
          </div>
        </InputGroup>
      </div>
      <Button label="Submit" class="w-full mt-5" @click="onSubmit"
              :disabled="!product.name || !product.description || product.price <= 0 || !product.addedDate || !product.category || product.attributes.length === 0 || product.images?.length === 0"/>
    </template>
  </Card>

  <ProductDetails2 :product="product"/>

</template>

<style scoped>

</style>