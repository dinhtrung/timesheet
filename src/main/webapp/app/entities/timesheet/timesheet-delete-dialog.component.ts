import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { TimesheetPopupService } from './timesheet-popup.service';
import { TimesheetService } from './timesheet.service';

@Component({
    selector: 'jhi-timesheet-delete-dialog',
    templateUrl: './timesheet-delete-dialog.component.html'
})
export class TimesheetDeleteDialogComponent {

    timesheet: Timesheet;

    constructor(
        private timesheetService: TimesheetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.timesheetService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'timesheetListModification',
                content: 'Deleted an timesheet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-timesheet-delete-popup',
    template: ''
})
export class TimesheetDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timesheetPopupService: TimesheetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.timesheetPopupService
                .open(TimesheetDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
