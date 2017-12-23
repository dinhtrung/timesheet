import { BaseEntity, User } from './../../shared';

export class Meeting implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public note?: any,
        public allDay?: boolean,
        public startDate?: any,
        public endDate?: any,
        public user?: User,
    ) {
        this.allDay = false;
    }
}
