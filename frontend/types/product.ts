import type {Category} from "~/types/category";
import type {Image} from "~/types/image";
import type {AttributeValue} from "~/types/attributeValue";

export interface Product {
    id?: number,
    name: string,
    description: string,
    price: number,
    availableQuantity: number,
    addedDate: Date,
    attributes: AttributeValue[],
    category: Category,
    images: Image[],
}