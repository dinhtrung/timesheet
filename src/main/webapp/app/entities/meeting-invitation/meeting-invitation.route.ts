import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MeetingInvitationComponent } from './meeting-invitation.component';
import { MeetingInvitationDetailComponent } from './meeting-invitation-detail.component';
import { MeetingInvitationPopupComponent } from './meeting-invitation-dialog.component';
import { MeetingInvitationDeletePopupComponent } from './meeting-invitation-delete-dialog.component';

@Injectable()
export class MeetingInvitationResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const meetingInvitationRoute: Routes = [
    {
        path: 'meeting-invitation',
        component: MeetingInvitationComponent,
        resolve: {
            'pagingParams': MeetingInvitationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meetingInvitation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'meeting-invitation/:id',
        component: MeetingInvitationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meetingInvitation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const meetingInvitationPopupRoute: Routes = [
    {
        path: 'meeting-invitation-new',
        component: MeetingInvitationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meetingInvitation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'meeting-invitation/:id/edit',
        component: MeetingInvitationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meetingInvitation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'meeting-invitation/:id/delete',
        component: MeetingInvitationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meetingInvitation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
