import type {Attribute} from "~/types/attribute";
import type {Category} from "~/types/category";
import type {Image} from "~/types/image";

export interface Product {
    id?: number,
    name: string,
    description: string,
    price: number,
    availableQuantity: number,
    addedDate: Date,
    attributes: Attribute[],
    category: Category,
    images: Image[],
}