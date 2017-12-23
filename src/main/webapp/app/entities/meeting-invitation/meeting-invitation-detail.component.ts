import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { MeetingInvitation } from './meeting-invitation.model';
import { MeetingInvitationService } from './meeting-invitation.service';

@Component({
    selector: 'jhi-meeting-invitation-detail',
    templateUrl: './meeting-invitation-detail.component.html'
})
export class MeetingInvitationDetailComponent implements OnInit, OnDestroy {

    meetingInvitation: MeetingInvitation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private meetingInvitationService: MeetingInvitationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMeetingInvitations();
    }

    load(id) {
        this.meetingInvitationService.find(id).subscribe((meetingInvitation) => {
            this.meetingInvitation = meetingInvitation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMeetingInvitations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'meetingInvitationListModification',
            (response) => this.load(this.meetingInvitation.id)
        );
    }
}
