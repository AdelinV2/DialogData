import type {AttributeValue} from "~/types/attributeValue";


export const useAttributeValue = createSharedComposable(() => {
    const attributeValue = ref<AttributeValue[]>([])

    return { attributeValue }
});