import type {CartEntry} from "~/types/cartEntry";
import type {User} from "~/types/user";

export interface Cart {
    id?: number;
    user: User;
    totalPrice: number;
    cartEntries: CartEntry[];
}