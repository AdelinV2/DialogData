import type {Product} from "~/types/product";
import type {Attribute} from "~/types/attribute";

export interface AttributeValue {
    id?: number,
    value: string,
    product?: Product,
    attribute: Attribute
}