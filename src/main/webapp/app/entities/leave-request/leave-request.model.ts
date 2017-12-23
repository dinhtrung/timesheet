import { BaseEntity, User } from './../../shared';

export const enum ReviewState {
    'REJECTED',
    'PENDING',
    'APPROVED'
}

export class LeaveRequest implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public note?: any,
        public allDay?: boolean,
        public startDate?: any,
        public endDate?: any,
        public number?: number,
        public state?: ReviewState,
        public updatedAt?: any,
        public approvalNote?: any,
        public owner?: User,
        public approvedBy?: User,
    ) {
        this.allDay = false;
    }
}
