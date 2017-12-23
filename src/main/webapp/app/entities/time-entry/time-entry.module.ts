import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../../shared';
import {
    TimeEntryService,
    TimeEntryPopupService,
    TimeEntryComponent,
    TimeEntryDetailComponent,
    TimeEntryDialogComponent,
    TimeEntryPopupComponent,
    TimeEntryDeletePopupComponent,
    TimeEntryDeleteDialogComponent,
    timeEntryRoute,
    timeEntryPopupRoute,
    TimeEntryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...timeEntryRoute,
    ...timeEntryPopupRoute,
];

@NgModule({
    imports: [
        TimesheetSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TimeEntryComponent,
        TimeEntryDetailComponent,
        TimeEntryDialogComponent,
        TimeEntryDeleteDialogComponent,
        TimeEntryPopupComponent,
        TimeEntryDeletePopupComponent,
    ],
    entryComponents: [
        TimeEntryComponent,
        TimeEntryDialogComponent,
        TimeEntryPopupComponent,
        TimeEntryDeleteDialogComponent,
        TimeEntryDeletePopupComponent,
    ],
    providers: [
        TimeEntryService,
        TimeEntryPopupService,
        TimeEntryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetTimeEntryModule {}
