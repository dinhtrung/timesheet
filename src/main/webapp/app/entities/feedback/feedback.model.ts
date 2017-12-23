import { BaseEntity, User } from './../../shared';

export class Feedback implements BaseEntity {
    constructor(
        public id?: number,
        public createdAt?: any,
        public name?: string,
        public note?: any,
        public repliedTo?: BaseEntity,
        public timesheet?: BaseEntity,
        public createdBy?: User,
    ) {
    }
}
