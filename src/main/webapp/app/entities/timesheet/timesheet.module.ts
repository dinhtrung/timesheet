import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import { TimesheetAdminModule } from '../../admin/admin.module';
import {
    TimesheetService,
    TimesheetPopupService,
    TimesheetComponent,
    TimesheetDetailComponent,
    TimesheetDialogComponent,
    TimesheetPopupComponent,
    TimesheetDeletePopupComponent,
    TimesheetDeleteDialogComponent,
    timesheetRoute,
    timesheetPopupRoute,
    TimesheetResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...timesheetRoute,
    ...timesheetPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        TimesheetAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TimesheetComponent,
        TimesheetDetailComponent,
        TimesheetDialogComponent,
        TimesheetDeleteDialogComponent,
        TimesheetPopupComponent,
        TimesheetDeletePopupComponent,
    ],
    entryComponents: [
        TimesheetComponent,
        TimesheetDialogComponent,
        TimesheetPopupComponent,
        TimesheetDeleteDialogComponent,
        TimesheetDeletePopupComponent,
    ],
    providers: [
        TimesheetService,
        TimesheetPopupService,
        TimesheetResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetTimesheetModule {}
