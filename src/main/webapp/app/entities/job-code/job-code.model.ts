import { BaseEntity, User } from './../../shared';

export class JobCode implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public users?: User[],
    ) {
    }
}
