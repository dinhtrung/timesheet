import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetService } from './timesheet.service';
// Pull related timeEntry
import { TimeEntry, TimeEntryService } from '../time-entry';

@Component({
    selector: 'jhi-timesheet-detail',
    templateUrl: './timesheet-detail.component.html'
})
export class TimesheetDetailComponent implements OnInit, OnDestroy {

    timesheet: Timesheet;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        public timeEntryService: TimeEntryService,
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private timesheetService: TimesheetService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
            this.loadEntries(params['id']);
        });
        this.registerChangeInTimesheets();
    }

    load(id) {
        this.timesheetService.find(id).subscribe((timesheet) => {
            this.timesheet = timesheet;
        });
    }
    loadEntries(id) {
      this.timeEntryService.query({ search: { 'timesheetId.equals' : id }})
      .subscribe((entries) => {
        this.timeEntryService.totalHours = 0;
        this.timeEntryService.entities = entries.json;
        entries.json.forEach((k) => this.timeEntryService.totalHours += k.duration );
      });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTimesheets() {
        this.eventSubscriber = this.eventManager.subscribe(
            'timesheetListModification',
            (response) => this.load(this.timesheet.id)
        );
    }
}
