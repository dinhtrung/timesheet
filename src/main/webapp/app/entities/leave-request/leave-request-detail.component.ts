import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { LeaveRequest } from './leave-request.model';
import { LeaveRequestService } from './leave-request.service';

@Component({
    selector: 'jhi-leave-request-detail',
    templateUrl: './leave-request-detail.component.html'
})
export class LeaveRequestDetailComponent implements OnInit, OnDestroy {

    leaveRequest: LeaveRequest;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private leaveRequestService: LeaveRequestService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLeaveRequests();
    }

    load(id) {
        this.leaveRequestService.find(id).subscribe((leaveRequest) => {
            this.leaveRequest = leaveRequest;
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

    registerChangeInLeaveRequests() {
        this.eventSubscriber = this.eventManager.subscribe(
            'leaveRequestListModification',
            (response) => this.load(this.leaveRequest.id)
        );
    }
}
