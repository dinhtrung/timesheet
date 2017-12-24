import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
// App services and entities
import { JobCode, JobCodeService  } from '../entities/job-code';
import { Meeting, MeetingService } from '../entities/meeting';
import { LeaveRequest, LeaveRequestService } from '../entities/leave-request';
import { Timesheet, TimesheetService } from '../entities/timesheet';
import { TimeEntry, TimeEntryService, TimeEntryPopupService, TimeEntryDialogComponent } from '../entities/time-entry';
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

    colors: any = {
      red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
      },
      blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
      },
      yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
      }
    };
    // Calendar current view
    view = 'month';
    // Calendar view date
    viewDate: Date = new Date();

    modalData: {
      action: string;
      event: CalendarEvent;
    };

    actions: CalendarEventAction[] = [
    {
      label: '<i class="fa fa-fw fa-pencil"></i>',
      onClick: ({ event }: { event: CalendarEvent }) => {
        this.handleEvent('Edited', event);
      }
    },
    {
      label: '<i class="fa fa-fw fa-times"></i>',
      onClick: ({ event }: { event: CalendarEvent }) => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.handleEvent('Deleted', event);
      }
    }
  ];

  activeDayIsOpen = true;

  refresh: Subject<any> = new Subject();

  events: CalendarEvent[] = [
    {
      start: subDays(startOfDay(new Date()), 1),
      end: addDays(new Date(), 1),
      title: 'A 3 day event',
      color: this.colors.red,
      actions: this.actions
    },
    {
      start: startOfDay(new Date()),
      title: 'An event with no end date',
      color: this.colors.yellow,
      actions: this.actions
    },
    {
      start: subDays(endOfMonth(new Date()), 3),
      end: addDays(endOfMonth(new Date()), 3),
      title: 'A long event that spans 2 months',
      color: this.colors.blue
    },
    {
      start: addHours(startOfDay(new Date()), 2),
      end: new Date(),
      title: 'A draggable and resizable event',
      color: this.colors.yellow,
      actions: this.actions,
      resizable: {
        beforeStart: true,
        afterEnd: true
      },
      draggable: true
    }
  ];

    constructor(
        private principal: Principal,
        private timeEntryPopupService: TimeEntryPopupService,
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

    addEvent(): void {
      this.events.push({
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: this.colors.red,
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true
        }
      });
      this.refresh.next();
    }
}
