import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Meeting } from './meeting.model';
import { MeetingService } from './meeting.service';

@Component({
    selector: 'jhi-meeting-detail',
    templateUrl: './meeting-detail.component.html'
})
export class MeetingDetailComponent implements OnInit, OnDestroy {

    meeting: Meeting;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private meetingService: MeetingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMeetings();
    }

    load(id) {
        this.meetingService.find(id).subscribe((meeting) => {
            this.meeting = meeting;
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

    registerChangeInMeetings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'meetingListModification',
            (response) => this.load(this.meeting.id)
        );
    }
}
