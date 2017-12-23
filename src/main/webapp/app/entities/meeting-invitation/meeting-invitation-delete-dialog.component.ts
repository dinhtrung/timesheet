import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MeetingInvitation } from './meeting-invitation.model';
import { MeetingInvitationPopupService } from './meeting-invitation-popup.service';
import { MeetingInvitationService } from './meeting-invitation.service';

@Component({
    selector: 'jhi-meeting-invitation-delete-dialog',
    templateUrl: './meeting-invitation-delete-dialog.component.html'
})
export class MeetingInvitationDeleteDialogComponent {

    meetingInvitation: MeetingInvitation;

    constructor(
        private meetingInvitationService: MeetingInvitationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.meetingInvitationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'meetingInvitationListModification',
                content: 'Deleted an meetingInvitation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-meeting-invitation-delete-popup',
    template: ''
})
export class MeetingInvitationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private meetingInvitationPopupService: MeetingInvitationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.meetingInvitationPopupService
                .open(MeetingInvitationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
