import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Meeting } from './meeting.model';
import { MeetingPopupService } from './meeting-popup.service';
import { MeetingService } from './meeting.service';

@Component({
    selector: 'jhi-meeting-delete-dialog',
    templateUrl: './meeting-delete-dialog.component.html'
})
export class MeetingDeleteDialogComponent {

    meeting: Meeting;

    constructor(
        private meetingService: MeetingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.meetingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'meetingListModification',
                content: 'Deleted an meeting'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-meeting-delete-popup',
    template: ''
})
export class MeetingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private meetingPopupService: MeetingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.meetingPopupService
                .open(MeetingDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
