import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../shared';
// Third Party for Timesheet display
import { CalendarModule } from 'angular-calendar';
import { CalendarHeaderComponent } from './';

// Extra stuff
import { LoggingComponent } from './';
// MANDATORY
import { PORTAL_ROUTE, PortalComponent } from './';

@NgModule({
    imports: [
        TimesheetSharedModule,
        CalendarModule.forRoot(),
        RouterModule.forChild(PORTAL_ROUTE)
    ],
    declarations: [
        CalendarHeaderComponent,
        PortalComponent,
        LoggingComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetPortalModule {}
