import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobCode } from './job-code.model';
import { JobCodePopupService } from './job-code-popup.service';
import { JobCodeService } from './job-code.service';

@Component({
    selector: 'jhi-job-code-delete-dialog',
    templateUrl: './job-code-delete-dialog.component.html'
})
export class JobCodeDeleteDialogComponent {

    jobCode: JobCode;

    constructor(
        private jobCodeService: JobCodeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobCodeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jobCodeListModification',
                content: 'Deleted an jobCode'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-code-delete-popup',
    template: ''
})
export class JobCodeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobCodePopupService: JobCodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobCodePopupService
                .open(JobCodeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
