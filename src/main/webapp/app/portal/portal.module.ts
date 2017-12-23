import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TimesheetSharedModule } from '../shared';
import { CalendarModule } from 'angular-calendar';

import { PORTAL_ROUTE, PortalComponent } from './';

@NgModule({
    imports: [
        TimesheetSharedModule,
        CalendarModule.forRoot(),
        RouterModule.forChild(PORTAL_ROUTE)
    ],
    declarations: [
        PortalComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TimesheetPortalModule {}
