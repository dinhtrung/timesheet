import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { LeaveRequest } from './leave-request.model';
import { LeaveRequestService } from './leave-request.service';

@Injectable()
export class LeaveRequestPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private leaveRequestService: LeaveRequestService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.leaveRequestService.find(id).subscribe((leaveRequest) => {
                    leaveRequest.startDate = this.datePipe
                        .transform(leaveRequest.startDate, 'yyyy-MM-ddTHH:mm:ss');
                    leaveRequest.endDate = this.datePipe
                        .transform(leaveRequest.endDate, 'yyyy-MM-ddTHH:mm:ss');
                    leaveRequest.updatedAt = this.datePipe
                        .transform(leaveRequest.updatedAt, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.leaveRequestModalRef(component, leaveRequest);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.leaveRequestModalRef(component, new LeaveRequest());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    leaveRequestModalRef(component: Component, leaveRequest: LeaveRequest): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.leaveRequest = leaveRequest;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
