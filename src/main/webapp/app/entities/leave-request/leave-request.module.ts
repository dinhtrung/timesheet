import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import { TimesheetAdminModule } from '../../admin/admin.module';
import {
    LeaveRequestService,
    LeaveRequestPopupService,
    LeaveRequestComponent,
    LeaveRequestDetailComponent,
    LeaveRequestDialogComponent,
    LeaveRequestPopupComponent,
    LeaveRequestDeletePopupComponent,
    LeaveRequestDeleteDialogComponent,
    leaveRequestRoute,
    leaveRequestPopupRoute,
    LeaveRequestResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...leaveRequestRoute,
    ...leaveRequestPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        TimesheetAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LeaveRequestComponent,
        LeaveRequestDetailComponent,
        LeaveRequestDialogComponent,
        LeaveRequestDeleteDialogComponent,
        LeaveRequestPopupComponent,
        LeaveRequestDeletePopupComponent,
    ],
    entryComponents: [
        LeaveRequestComponent,
        LeaveRequestDialogComponent,
        LeaveRequestPopupComponent,
        LeaveRequestDeleteDialogComponent,
        LeaveRequestDeletePopupComponent,
    ],
    providers: [
        LeaveRequestService,
        LeaveRequestPopupService,
        LeaveRequestResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetLeaveRequestModule {}
