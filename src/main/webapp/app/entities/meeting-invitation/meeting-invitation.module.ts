import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import {
    MeetingInvitationService,
    MeetingInvitationPopupService,
    MeetingInvitationComponent,
    MeetingInvitationDetailComponent,
    MeetingInvitationDialogComponent,
    MeetingInvitationPopupComponent,
    MeetingInvitationDeletePopupComponent,
    MeetingInvitationDeleteDialogComponent,
    meetingInvitationRoute,
    meetingInvitationPopupRoute,
    MeetingInvitationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...meetingInvitationRoute,
    ...meetingInvitationPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MeetingInvitationComponent,
        MeetingInvitationDetailComponent,
        MeetingInvitationDialogComponent,
        MeetingInvitationDeleteDialogComponent,
        MeetingInvitationPopupComponent,
        MeetingInvitationDeletePopupComponent,
    ],
    entryComponents: [
        MeetingInvitationComponent,
        MeetingInvitationDialogComponent,
        MeetingInvitationPopupComponent,
        MeetingInvitationDeleteDialogComponent,
        MeetingInvitationDeletePopupComponent,
    ],
    providers: [
        MeetingInvitationService,
        MeetingInvitationPopupService,
        MeetingInvitationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetMeetingInvitationModule {}
