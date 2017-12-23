import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { MeetingInvitation } from './meeting-invitation.model';
import { MeetingInvitationService } from './meeting-invitation.service';

@Injectable()
export class MeetingInvitationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private meetingInvitationService: MeetingInvitationService

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
                this.meetingInvitationService.find(id).subscribe((meetingInvitation) => {
                    meetingInvitation.decidedAt = this.datePipe
                        .transform(meetingInvitation.decidedAt, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.meetingInvitationModalRef(component, meetingInvitation);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.meetingInvitationModalRef(component, new MeetingInvitation());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    meetingInvitationModalRef(component: Component, meetingInvitation: MeetingInvitation): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.meetingInvitation = meetingInvitation;
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
