
export interface Newsletter {
    id?: number;
    subscriptionDate: Date;
    content: string;
    scheduleDate: Date;
    repeat: boolean;
    repeatInterval?: number;
}