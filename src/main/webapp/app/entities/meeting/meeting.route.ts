import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MeetingComponent } from './meeting.component';
import { MeetingDetailComponent } from './meeting-detail.component';
import { MeetingPopupComponent } from './meeting-dialog.component';
import { MeetingDeletePopupComponent } from './meeting-delete-dialog.component';

@Injectable()
export class MeetingResolvePagingParams implements Resolve<any> {

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

export const meetingRoute: Routes = [
    {
        path: 'meeting',
        component: MeetingComponent,
        resolve: {
            'pagingParams': MeetingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meeting.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'meeting/:id',
        component: MeetingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meeting.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const meetingPopupRoute: Routes = [
    {
        path: 'meeting-new',
        component: MeetingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meeting.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'meeting/:id/edit',
        component: MeetingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meeting.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'meeting/:id/delete',
        component: MeetingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.meeting.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
