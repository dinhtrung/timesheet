import { BaseEntity } from './../../shared';

export class TimeEntry implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public duration?: number,
        public description?: any,
        public timesheet?: BaseEntity,
        public jobCode?: BaseEntity,
    ) {
    }
}
