<script setup lang="ts">

import type {Category} from "~/types/category";
import type {Product} from "~/types/product";
import type {Image} from "~/types/image";
import type {AttributeValue} from "~/types/attributeValue";

const {user} = useUserStorage();
const route = useRoute();
const productId = route.params.id;
const loading = ref(false);
const document = ref<File | null>(null);
const uploadedDocument = ref<File | null>(null);

const product = ref<Product>({
  name: '',
  description: '',
  price: 0.0,
  availableQuantity: 0,
  addedDate: new Date(),
  attributes: [] as AttributeValue[],
  category: {} as Category,
  images: [] as Image[],
  promoted: false,
  promotionPrice: undefined,
})

const toast = useToast();
const categories = ref<Category[]>([])
const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

async function urlToBase64(url: string): Promise<string> {

  const response = await fetch(url);
  const blob = await response.blob();

  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result as string);
    reader.onerror = reject;
    reader.readAsDataURL(blob);
  });
}

async function processProductImages() {
  if (!product.value?.images) return;

  for (let idx = 0; idx < product.value.images.length; idx++) {
    const img = product.value.images[idx];
    const base64 = await urlToBase64(img.imageUrl);
    img.base64 = base64;
    img.fileName = idx.toString();
    const arr = base64.split(',');
    const mime = arr[0].match(/:(.*?);/)?.[1] || 'image/png';
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) u8arr[n] = bstr.charCodeAt(n);
    const file = new File([u8arr], `${idx}.png`, {type: mime});
    (file as any).objectURL = base64;
    uploadedFiles.value[idx] = file;
  }
}

async function processDocument() {
  if (!product.value.document)
    return;

  const doc = product.value.document;

  if (doc.data) {
    const [_, base64] = doc.data.includes(',') ? doc.data.split(',') : ['', doc.data];
    const binary = atob(base64);
    const len = binary.length;
    const u8arr = new Uint8Array(len);

    for (let i = 0; i < len; i++) {
      u8arr[i] = binary.charCodeAt(i);
    }

    const file = new File([u8arr], doc.fileName, { type: doc.contentType });
    (file as any).objectURL = URL.createObjectURL(file);
    uploadedDocument.value = file;
    document.value = file;
  } else {
    if (doc.fileName && doc.contentType) {
      const file = new File([], doc.fileName, { type: doc.contentType });
      (file as any).objectURL = URL.createObjectURL(file);
      uploadedDocument.value = file;
      document.value = file;
    } else {
      console.warn('Document data is empty or missing fileName/contentType');
      uploadedDocument.value = null;
      document.value = null;
    }
  }
}

if (productId) {
  $fetch(`${apiBaseUrl}/products/${productId}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        product.value = response._data as Product;

        processProductImages();
        processDocument();

        console.log('Product fetched successfully:', product.value);
      } else {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch product',
          life: 3000,
        });
        console.error('Failed to fetch product');
      }
    },
  }).catch((error) => {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Error fetching product',
      life: 3000,
    });
    console.error('Error fetching product:', error);
  });
}

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

function handleDocumentSelect(files: File[], clearCallback: Function) {
  const file = files[0];
  if (file) {
    (file as any).objectURL = URL.createObjectURL(file);
    uploadedDocument.value = file;
    document.value = file;
  }
  clearCallback();
}

function onDocumentUpload(event: any) {

  const file = event.files[0];

  if (file) {
    if ((uploadedDocument.value as any)?.objectURL) {
      URL.revokeObjectURL((uploadedDocument.value as any).objectURL);
    }
    (file as any).objectURL = URL.createObjectURL(file);
    uploadedDocument.value = file;
    document.value = file;
  }
}

function removeUploadedDocument() {

  if ((uploadedDocument.value as any)?.objectURL) {
    URL.revokeObjectURL((uploadedDocument.value as any).objectURL);
  }

  uploadedDocument.value = null;
  document.value = null;
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

function onUpdate() {

  loading.value = true;
  console.log('Updating product:', product.value);
  console.log('Using document:', document.value);

  const formData = new FormData();
  formData.append('productDto', new Blob([JSON.stringify(product.value)], { type: 'application/json' }));

  if (document.value) {
    formData.append('file', document.value);
  }

  $fetch(`${apiBaseUrl}/products/${productId}`, {
    method: 'PUT',
    body: formData,
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Product updated successfully:', response._data);
        toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Product updated successfully',
          life: 3000,
        });
      } else {
        console.error('Failed to update product');
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to update product',
          life: 3000,
        });
      }
    },
  }).catch((error) => {
    console.error('Error updating product:', error);
  }).finally(() => {
    loading.value = false;
  });
}

function onSubmit() {

  loading.value = true;

  $fetch(`${apiBaseUrl}/products`, {
    method: 'POST',
    params: document,
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
          promoted: false,
          promotionPrice: undefined,
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

const onDelete = () => {
  $fetch(`${apiBaseUrl}/products/${productId}`, {
    method: 'DELETE',
    onResponse({response}) {
      if (response.status === 200) {
        console.log('Product deleted successfully');
        toast.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Product deleted successfully',
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
          promoted: false,
          promotionPrice: undefined,
        };
      } else {
        console.error('Failed to delete product');
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete product',
          life: 3000,
        });
      }
    },
  }).catch((error) => {
    console.error('Error deleting product:', error);
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
            <i class="pi pi-dollar"></i>
          </InputGroupAddon>
          <FloatLabel variant="on">
            <InputNumber id="promotionPrice" type="number" :max="product.price" :max-fraction-digits="2"
                         v-model="product.promotionPrice"/>
            <label for="promotionPrice">Promotion Price (optional)</label>
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

        <div class="flex items-center gap-2 mb-4">
          <i class="mx-2 pi pi-star text-yellow-500"></i>
          <label for="promoted">Promoted</label>
          <ToggleSwitch v-model="product.promoted"/>
        </div>


        <div class="card">
          <FileUpload
            name="technicalDocument"
            :custom-upload="true"
            :multiple="false"
            @upload="onDocumentUpload"
          >
            <template #header="{ chooseCallback, clearCallback, files }">
              <Button
                  @click="chooseCallback()"
                  icon="pi pi-folder-open"
                  label="Select Document"
                  outlined
              />
              <Button
                  @click="handleDocumentSelect(files, clearCallback)"
                  icon="pi pi-upload"
                  label="Upload"
                  outlined
                  :disabled="!files || files.length === 0"
              />
              <Button
                  @click="clearCallback()"
                  icon="pi pi-times"
                  label="Clear"
                  outlined
                  :disabled="!files || files.length === 0"
              />
            </template>
            <template #empty>
              <span>Drag and drop technical file here.</span>
            </template>
          </FileUpload>
          <div v-if="uploadedDocument" class="mt-4">
            <h5 class="mb-2">Uploaded Document</h5>
            <div class="p-8 rounded-border flex flex-col border border-surface items-center gap-4">
              <i class="pi pi-file" style="font-size: 2rem;"></i>
              <span class="font-semibold text-ellipsis max-w-60 whitespace-nowrap overflow-hidden">
                 {{ uploadedDocument.name }}
               </span>
              <div>{{ formatSize(uploadedDocument.size) }}</div>
              <Badge value="Completed" severity="success"/>
              <Button
                  icon="pi pi-times"
                  @click="removeUploadedDocument"
                  outlined
                  rounded
                  severity="danger"
              />
              <a v-if="(uploadedDocument as any).objectURL"
                 :href="(uploadedDocument as any).objectURL"
                 :download="uploadedDocument.name"
                 class="mt-2"
              >
                <span class="pi pi-download"></span>
              </a>
            </div>
          </div>
        </div>


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
                    <h5 class="mb-2">Pending</h5>
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
                    <h5 class="mb-2">Completed</h5>
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
      <Button v-if="productId === null" label="Submit" class="w-full mt-5" @click="onSubmit"
              :disabled="!product.name || !product.description || product.price <= 0 || !product.addedDate || !product.category || product.attributes.length === 0 || product.images?.length === 0 || loading"/>
      <div v-else class="mt-5">
        <Button label="Update Product" class="w-full mt-2" severity="contrast"
                @click="onUpdate"
                :disabled="!product.name || !product.description || product.price <= 0 || !product.addedDate || !product.category || product.attributes.length === 0 || product.images?.length === 0 || loading"/>
        <Button v-if="productId" label="Delete Product" class="w-full mt-4" severity="danger"
                @click="onDelete" :disabled="!productId"/>
      </div>
      <div class="card my-6">
        <ProgressBar v-if="loading" mode="indeterminate" style="height: 6px"></ProgressBar>
      </div>
    </template>
  </Card>

  <ProductDetails2 :product="product"/>

</template>

<style scoped>

</style>