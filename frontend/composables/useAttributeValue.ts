import type {AttributeValue} from "~/types/attributeValue";

const attributeValue = ref<AttributeValue[]>([])

export const useAttributeValue = () => {
    return { attributeValue }
}