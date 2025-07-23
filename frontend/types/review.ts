import type {Product} from "~/types/product";
import type {User} from "~/types/user";

export interface Review {
    id?: number,
    product: Product,
    user: User,
    rating: number,
    comment: string,
    verified?: boolean
}