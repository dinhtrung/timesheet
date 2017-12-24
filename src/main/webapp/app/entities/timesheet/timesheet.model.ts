import { BaseEntity, User } from './../../shared';

export const enum ReviewState {
    'REJECTED',
    'PENDING',
    'APPROVED'
}

export class Timesheet implements BaseEntity {
    constructor(
        public id?: number,
        public approvedAt?: any,
        public approvalNote?: any,
        public year?: number,
        public week?: number,
        public state?: ReviewState,
        public comment?: any,
        public owner?: User,
        public approvedBy?: User,
        // Extra attributes
        public firstDate?: any,
        public lastDate?: any,
    ) {
    }
}
