import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { TimeEntryPopupService } from './time-entry-popup.service';
import { TimeEntryService } from './time-entry.service';
import { Timesheet, TimesheetService } from '../timesheet';
import { JobCode, JobCodeService } from '../job-code';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-time-entry-dialog',
    templateUrl: './time-entry-dialog.component.html'
})
export class TimeEntryDialogComponent implements OnInit {

    timeEntry: TimeEntry;
    isSaving: boolean;

    timesheets: Timesheet[];

    jobcodes: JobCode[];
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private timeEntryService: TimeEntryService,
        private timesheetService: TimesheetService,
        private jobCodeService: JobCodeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.timesheetService.query()
            .subscribe((res: ResponseWrapper) => { this.timesheets = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.jobCodeService.query()
            .subscribe((res: ResponseWrapper) => { this.jobcodes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.timeEntry.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timeEntryService.update(this.timeEntry));
        } else {
            this.subscribeToSaveResponse(
                this.timeEntryService.create(this.timeEntry));
        }
    }

    private subscribeToSaveResponse(result: Observable<TimeEntry>) {
        result.subscribe((res: TimeEntry) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TimeEntry) {
        this.eventManager.broadcast({ name: 'timeEntryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTimesheetById(index: number, item: Timesheet) {
        return item.id;
    }

    trackJobCodeById(index: number, item: JobCode) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-time-entry-popup',
    template: ''
})
export class TimeEntryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timeEntryPopupService: TimeEntryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.timeEntryPopupService
                    .open(TimeEntryDialogComponent as Component, params['id']);
            } else {
                this.timeEntryPopupService
                    .open(TimeEntryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
