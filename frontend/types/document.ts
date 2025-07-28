import type {Product} from "~/types/product";

export interface Document {
    id?: number,
    fileName: string,
    contentType: string,
    data: string,
    product: Product
}