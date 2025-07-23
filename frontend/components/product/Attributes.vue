<script setup lang="ts">

import type {Attribute} from "~/types/attribute";
import type {AttributeValue} from "~/types/attributeValue";
import type {TreeNode} from "primevue/treenode";

const apiBaseUrl = useRuntimeConfig().public.apiBaseUrl;

const attributes = ref<Attribute[]>([]);
const selectedKeys = ref<Record<number, boolean>>({});
const selectedAttributeValues = ref<AttributeValue[]>([] as AttributeValue[]);
const attrValuesMap = ref<Record<number, AttributeValue[]>>({});
const {attributeValue} = useAttributeValue();

const fetchAttributes = () => {
  $fetch(`${apiBaseUrl}/product-attribute`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        attributes.value = response._data as Attribute[];
        console.log('Attributes fetched successfully:', attributes.value);
      } else {
        console.error('Failed to fetch attributes');
      }
    }
  });
}

const fetchAttributeValues = (attributeId: number) => {

  if (attrValuesMap.value[attributeId]) return;

  $fetch(`${apiBaseUrl}/product-attribute-value/${attributeId}`, {
    method: 'GET',
    onResponse({response}) {
      if (response.status === 200) {
        attrValuesMap.value[attributeId] = response._data as AttributeValue[];

        attrValuesMap.value[attributeId].forEach((av: AttributeValue) => {
          if (attributeValue.value.map(v => v.value).includes(av.value)) {
            selectedKeys.value[av.id as number] = true;
          }
        });

        console.log(`Attribute values for ${attributeId} fetched successfully:`, attrValuesMap.value[attributeId]);
      } else {
        console.error(`Failed to fetch attribute values for ${attributeId}`);
      }
    }
  });
}

fetchAttributes();

const treeNodes = computed<TreeNode[]>(() =>
    attributes.value.map(attr => {
      const children = attrValuesMap.value[attr.id ?? 0];
      return {
        key: String(attr.id ?? ''),
        label: attr.name,
        data: attr,
        selectable: false,
        leaf: false,
        children: children
            ? children.map((val: AttributeValue) => ({
              key: String(val.id ?? ''),
              label: val.value,
              data: val,
              leaf: true,
              selectable: true
            }))
            : undefined
      } as TreeNode;
    })
);

const onNodeExpand = (node: any) => {

  if (!attrValuesMap.value[node.data.id]) {
    fetchAttributeValues(node.data.id);
  }
}

const onAttributeSelected = () => {
  const allValues = Object.values(attrValuesMap.value).flat();

  console.log('All attribute values:', allValues);

  const selected = Object.keys(selectedKeys.value)
      .map(key => {
        const id = parseInt(key);
        return allValues.find(v => v.id === id);
      })
      .filter((v): v is AttributeValue => !!v);

  selectedAttributeValues.value = selected;
  attributeValue.value = selected;

  console.log('Selected attribute values:', selectedAttributeValues.value);
  console.log('Attribute value in store:', attributeValue.value);
};


</script>

<template>

  <TreeSelect
      v-model="selectedKeys"
      :options="treeNodes"
      selectionMode="multiple"
      :propagateSelectionDown="false"
      :propagateSelectionUp="true"
      display="chip"
      :maxSelectedLabels="3"
      placeholder="Select Attributes"
      @node-expand="onNodeExpand"
      @change="onAttributeSelected"
      class="md:w-80 w-full"
  />

</template>

<style scoped>

</style>