import type {Address} from "~/types/address";

export interface User {
    id?: number,
    firstName: string,
    lastName: string,
    email: string,
    phoneNumber: string,
    password: string,
    deliveryAddress: Address,
    billingAddress: Address,
}