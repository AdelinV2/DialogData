import type {Attribute} from "~/types/attribute";
import type {Category} from "~/types/category";

export interface Product {
    id?: number,
    name: string,
    description: string,
    price: number,
    availableQuantity: number,
    addedDate: Date,
    attributes: Attribute[],
    category: Category
}