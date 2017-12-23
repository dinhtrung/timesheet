import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LeaveRequest } from './leave-request.model';
import { LeaveRequestPopupService } from './leave-request-popup.service';
import { LeaveRequestService } from './leave-request.service';

@Component({
    selector: 'jhi-leave-request-delete-dialog',
    templateUrl: './leave-request-delete-dialog.component.html'
})
export class LeaveRequestDeleteDialogComponent {

    leaveRequest: LeaveRequest;

    constructor(
        private leaveRequestService: LeaveRequestService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.leaveRequestService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'leaveRequestListModification',
                content: 'Deleted an leaveRequest'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-leave-request-delete-popup',
    template: ''
})
export class LeaveRequestDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private leaveRequestPopupService: LeaveRequestPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.leaveRequestPopupService
                .open(LeaveRequestDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
