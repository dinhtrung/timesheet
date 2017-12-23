import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import { TimesheetAdminModule } from '../../admin/admin.module';
import {
    JobCodeService,
    JobCodePopupService,
    JobCodeComponent,
    JobCodeDetailComponent,
    JobCodeDialogComponent,
    JobCodePopupComponent,
    JobCodeDeletePopupComponent,
    JobCodeDeleteDialogComponent,
    jobCodeRoute,
    jobCodePopupRoute,
    JobCodeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...jobCodeRoute,
    ...jobCodePopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        TimesheetAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        JobCodeComponent,
        JobCodeDetailComponent,
        JobCodeDialogComponent,
        JobCodeDeleteDialogComponent,
        JobCodePopupComponent,
        JobCodeDeletePopupComponent,
    ],
    entryComponents: [
        JobCodeComponent,
        JobCodeDialogComponent,
        JobCodePopupComponent,
        JobCodeDeleteDialogComponent,
        JobCodeDeletePopupComponent,
    ],
    providers: [
        JobCodeService,
        JobCodePopupService,
        JobCodeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetJobCodeModule {}
