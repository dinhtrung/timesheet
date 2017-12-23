import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { LeaveRequest } from './leave-request.model';
import { LeaveRequestPopupService } from './leave-request-popup.service';
import { LeaveRequestService } from './leave-request.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-leave-request-dialog',
    templateUrl: './leave-request-dialog.component.html'
})
export class LeaveRequestDialogComponent implements OnInit {

    leaveRequest: LeaveRequest;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private leaveRequestService: LeaveRequestService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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
        if (this.leaveRequest.id !== undefined) {
            this.subscribeToSaveResponse(
                this.leaveRequestService.update(this.leaveRequest));
        } else {
            this.subscribeToSaveResponse(
                this.leaveRequestService.create(this.leaveRequest));
        }
    }

    private subscribeToSaveResponse(result: Observable<LeaveRequest>) {
        result.subscribe((res: LeaveRequest) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: LeaveRequest) {
        this.eventManager.broadcast({ name: 'leaveRequestListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-leave-request-popup',
    template: ''
})
export class LeaveRequestPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private leaveRequestPopupService: LeaveRequestPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.leaveRequestPopupService
                    .open(LeaveRequestDialogComponent as Component, params['id']);
            } else {
                this.leaveRequestPopupService
                    .open(LeaveRequestDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
