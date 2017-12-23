import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Meeting } from './meeting.model';
import { MeetingService } from './meeting.service';

@Injectable()
export class MeetingPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private meetingService: MeetingService

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
                this.meetingService.find(id).subscribe((meeting) => {
                    meeting.startDate = this.datePipe
                        .transform(meeting.startDate, 'yyyy-MM-ddTHH:mm:ss');
                    meeting.endDate = this.datePipe
                        .transform(meeting.endDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.meetingModalRef(component, meeting);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.meetingModalRef(component, new Meeting());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    meetingModalRef(component: Component, meeting: Meeting): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.meeting = meeting;
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
