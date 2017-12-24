import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';
import { Account, LoginModalService, Principal } from '../shared';
// App services and entities
import { JobCode, JobCodeService } from '../entities/job-code';
import { Meeting, MeetingService } from '../entities/meeting';
import { LeaveRequest, LeaveRequestService } from '../entities/leave-request';
import { Timesheet, TimesheetService } from '../entities/timesheet';
import { TimeEntry, TimeEntryService, TimeEntryPopupService, TimeEntryDialogComponent, TimeEntryDeleteDialogComponent } from '../entities/time-entry';
import { Feedback, FeedbackService } from '../entities/feedback';
import { MeetingInvitation, MeetingInvitationService } from '../entities/meeting-invitation';
// moment
import * as moment from 'moment';
// angular calendar
import { ChangeDetectionStrategy, ViewChild, TemplateRef } from '@angular/core';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { Subject } from 'rxjs/Subject';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarEventTitleFormatter } from 'angular-calendar';

export class TimeEntryTitleFormatter extends CalendarEventTitleFormatter {
  monthTooltip(event: CalendarEvent): string {
    return event.meta.description;
  }

  weekTooltip(event: CalendarEvent): string {
    return event.meta.description;
  }

  dayTooltip(event: CalendarEvent): string {
    return event.meta.description;
  }
}

@Component({
    selector: 'jhi-portal',
    templateUrl: './portal.component.html',
    providers: [
    {
      provide: CalendarEventTitleFormatter,
      useClass: TimeEntryTitleFormatter
    }
  ]
})
export class PortalComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    // Calendar current view
    view = 'week';
    // Calendar view date
    viewDate: Date = new Date();

    modalData: {
        action: string;
        event: CalendarEvent;
    };
    // Action to bind into Calendar Action
    actions: CalendarEventAction[] = [
        {
            label: '<i class="fa fa-pencil"></i>',
            onClick: ({ event }: { event: CalendarEvent }) => this.updateTimeEntry(event.meta)
        },
        {
            label: '<i class="fa fa-remove"></i>',
            onClick: ({ event }: { event: CalendarEvent }) => this.deleteTimeEntry(event.meta)
        }
    ];

    activeDayIsOpen = true;

    refresh: Subject<any> = new Subject();

    events: CalendarEvent[] = [];
    eventSubscriber: Subscription;
    year: number;
    week: number;

    constructor(
        private principal: Principal,
        private timeEntryPopupService: TimeEntryPopupService,
        public timeEntryService: TimeEntryService,
        public timesheetService: TimesheetService,
        public leaveRequestService: LeaveRequestService,
        public jobCodeService: JobCodeService,
        public meetingService: MeetingService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.year = moment().years();
        this.week = moment().weeks();
        this.loadTimesheet()
        this.eventSubscriber = this.eventManager.subscribe('timeEntryListModification', (response) => this.loadTimesheet());
    }
    // Load Time Sheet
    loadTimesheet() {
        this.timesheetService.entity = new Timesheet();
        this.timesheetService.query({ search: { 'year.equals': this.year, 'week.equals': this.week } })
            .subscribe((res) => {
                Object.assign(this.timesheetService.entity, res.json[0]);
                if (this.timesheetService.entity.id) {
                    this.timeEntryService.query({ search: { 'timesheetId.equals': this.timesheetService.entity.id } })
                        .subscribe((entries) => {
                            this.timeEntryService.entities = entries.json;
                            this.events = [];
                            entries.json.forEach((k) => {
                                this.events.push({
                                    meta: k,
                                    start: startOfDay(k.date),
                                    title: k.duration + ': ' + ( k.jobCode ? k.jobCode.name : '' ),
                                    color: {
                                        primary: '#ad2121',
                                        secondary: '#FAE3E3'
                                    },
                                    actions: this.actions,
                                });
                            });
                        });
                }
            });
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

    dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
        console.log('Day Clicked', date, events);
        if (isSameMonth(date, this.viewDate)) {
            if (
                (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
                events.length === 0
            ) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
                this.viewDate = date;
            }
        }
    }

    leaveRequestDayClicked(data: any) {
        console.log('leave Request clicked', data);
    }

    eventTimesChanged({
      event,
        newStart,
        newEnd
    }: CalendarEventTimesChangedEvent): void {
        event.start = newStart;
        event.end = newEnd;
        this.handleEvent('Dropped or resized', event);
        this.refresh.next();
    }

    handleEvent(action: string, event: CalendarEvent): void {
        console.log('Handle event of type ' + action, event);
        this.modalData = { event, action };
        this.timeEntryPopupService.open(TimeEntryDialogComponent as Component).then((modalRef) => {
            this.modalRef = modalRef;
            console.log('New created entry: ', modalRef.componentInstance.timeEntry);
        });
    }
    // Custom Function for Entries
    newTimeEntry(date: any) {
        this.timeEntryService.entity = new TimeEntry();
        this.timeEntryService.entity.date = date;
        this.timeEntryPopupService.open(TimeEntryDialogComponent as Component, this.timeEntryService.entity).then((modalRef) => {
            this.modalRef = modalRef;
        });
    }
    updateTimeEntry(entity) {
      this.timeEntryPopupService.open(TimeEntryDialogComponent as Component, entity).then((modalRef) => {
          this.modalRef = modalRef;
      });
    }
    deleteTimeEntry(entity) {
      this.timeEntryPopupService.open(TimeEntryDeleteDialogComponent as Component, entity.id).then((modalRef) => {
          this.modalRef = modalRef;
      });
    }
}
