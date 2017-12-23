import { BaseEntity } from './../../shared';

export class MeetingInvitation implements BaseEntity {
    constructor(
        public id?: number,
        public decidedAt?: any,
        public accepted?: boolean,
        public meeting?: BaseEntity,
    ) {
        this.accepted = false;
    }
}
