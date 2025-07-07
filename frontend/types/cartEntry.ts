
export interface CartEntry {
    id?: number,
    productId: number,
    quantity: number,
    pricePerPiece: number,
    totalPricePerEntry: number,
}