import type {CartEntry} from "~/types/cartEntry";

export interface Cart {
    id?: number;
    userId: number;
    totalPrice: number;
    entries: CartEntry[];
}