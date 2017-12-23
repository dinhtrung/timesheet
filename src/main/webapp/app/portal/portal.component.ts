import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
// App services and entities
import { JobCode, JobCodeService  } from '../entities/job-code';
import { Meeting, MeetingService } from '../entities/meeting';
import { LeaveRequest, LeaveRequestService } from '../entities/leave-request';
import { Timesheet, TimesheetService } from '../entities/timesheet';
import { TimeEntry, TimeEntryService } from '../entities/time-entry';
import { Feedback, FeedbackService } from '../entities/feedback';
import { MeetingInvitation, MeetingInvitationService } from '../entities/meeting-invitation';
// moment
import * as moment from 'moment';
// angular calendar
import { ChangeDetectionStrategy, ViewChild, TemplateRef } from '@angular/core';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { Subject } from 'rxjs/Subject';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent } from 'angular-calendar';

@Component({
    selector: 'jhi-portal',
    templateUrl: './portal.component.html'
})
export class PortalComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
