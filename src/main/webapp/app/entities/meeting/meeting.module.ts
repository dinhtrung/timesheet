import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import { TimesheetAdminModule } from '../../admin/admin.module';
import {
    MeetingService,
    MeetingPopupService,
    MeetingComponent,
    MeetingDetailComponent,
    MeetingDialogComponent,
    MeetingPopupComponent,
    MeetingDeletePopupComponent,
    MeetingDeleteDialogComponent,
    meetingRoute,
    meetingPopupRoute,
    MeetingResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...meetingRoute,
    ...meetingPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        TimesheetAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MeetingComponent,
        MeetingDetailComponent,
        MeetingDialogComponent,
        MeetingDeleteDialogComponent,
        MeetingPopupComponent,
        MeetingDeletePopupComponent,
    ],
    entryComponents: [
        MeetingComponent,
        MeetingDialogComponent,
        MeetingPopupComponent,
        MeetingDeleteDialogComponent,
        MeetingDeletePopupComponent,
    ],
    providers: [
        MeetingService,
        MeetingPopupService,
        MeetingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetMeetingModule {}
