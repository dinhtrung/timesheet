import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { JobCode } from './job-code.model';
import { JobCodeService } from './job-code.service';

@Component({
    selector: 'jhi-job-code-detail',
    templateUrl: './job-code-detail.component.html'
})
export class JobCodeDetailComponent implements OnInit, OnDestroy {

    jobCode: JobCode;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private jobCodeService: JobCodeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobCodes();
    }

    load(id) {
        this.jobCodeService.find(id).subscribe((jobCode) => {
            this.jobCode = jobCode;
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

    registerChangeInJobCodes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobCodeListModification',
            (response) => this.load(this.jobCode.id)
        );
    }
}
