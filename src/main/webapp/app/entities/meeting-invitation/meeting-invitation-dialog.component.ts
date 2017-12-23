import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MeetingInvitation } from './meeting-invitation.model';
import { MeetingInvitationPopupService } from './meeting-invitation-popup.service';
import { MeetingInvitationService } from './meeting-invitation.service';
import { Meeting, MeetingService } from '../meeting';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-meeting-invitation-dialog',
    templateUrl: './meeting-invitation-dialog.component.html'
})
export class MeetingInvitationDialogComponent implements OnInit {

    meetingInvitation: MeetingInvitation;
    isSaving: boolean;

    meetings: Meeting[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private meetingInvitationService: MeetingInvitationService,
        private meetingService: MeetingService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.meetingService.query()
            .subscribe((res: ResponseWrapper) => { this.meetings = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.meetingInvitation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.meetingInvitationService.update(this.meetingInvitation));
        } else {
            this.subscribeToSaveResponse(
                this.meetingInvitationService.create(this.meetingInvitation));
        }
    }

    private subscribeToSaveResponse(result: Observable<MeetingInvitation>) {
        result.subscribe((res: MeetingInvitation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MeetingInvitation) {
        this.eventManager.broadcast({ name: 'meetingInvitationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMeetingById(index: number, item: Meeting) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-meeting-invitation-popup',
    template: ''
})
export class MeetingInvitationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private meetingInvitationPopupService: MeetingInvitationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.meetingInvitationPopupService
                    .open(MeetingInvitationDialogComponent as Component, params['id']);
            } else {
                this.meetingInvitationPopupService
                    .open(MeetingInvitationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
