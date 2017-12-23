import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TimesheetJobCodeModule } from './job-code/job-code.module';
import { TimesheetLeaveRequestModule } from './leave-request/leave-request.module';
import { TimesheetMeetingInvitationModule } from './meeting-invitation/meeting-invitation.module';
import { TimesheetMeetingModule } from './meeting/meeting.module';
import { TimesheetTimeEntryModule } from './time-entry/time-entry.module';
import { TimesheetTimesheetModule } from './timesheet/timesheet.module';
import { TimesheetFeedbackModule } from './feedback/feedback.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TimesheetJobCodeModule,
        TimesheetLeaveRequestModule,
        TimesheetMeetingInvitationModule,
        TimesheetMeetingModule,
        TimesheetTimeEntryModule,
        TimesheetTimesheetModule,
        TimesheetFeedbackModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetEntityModule {}
