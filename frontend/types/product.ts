import type {Category} from "~/types/category";
import type {Image} from "~/types/image";
import type {AttributeValue} from "~/types/attributeValue";
import type {Document} from "~/types/document";

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
    promoted: boolean,
    promotionPrice?: number,
    averageRating?: number,
    document?: Document
}