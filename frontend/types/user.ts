import type {Address} from "~/types/address";
import type {Role} from "~/types/role";

export interface User {
    id?: number,
    firstName: string,
    lastName: string,
    email: string,
    phoneNumber: string,
    password: string,
    deliveryAddress: Address,
    billingAddress: Address,
    subscribed: boolean,
    role: Role
}
