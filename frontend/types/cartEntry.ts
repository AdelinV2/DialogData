import type {Product} from "~/types/product";
import type {Cart} from "~/types/cart";

export interface CartEntry {
    id?: number,
    product: Product,
    cart: Cart,
    quantity: number,
    pricePerPiece: number,
    totalPricePerEntry: number,
}