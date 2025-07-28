import type {Cart} from "~/types/cart";
import type {Address} from "~/types/address";
import type {OrderStatus} from "~/types/orderStatus";

export interface Order {
    id?: number;
    userId: number;
    cart: Cart;
    paymentType: string;
    deliveryAddress: Address;
    invoiceAddress: Address;
    totalPrice: number;
    orderDate: Date;
    status: OrderStatus
}