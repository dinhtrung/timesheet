import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { LeaveRequestComponent } from './leave-request.component';
import { LeaveRequestDetailComponent } from './leave-request-detail.component';
import { LeaveRequestPopupComponent } from './leave-request-dialog.component';
import { LeaveRequestDeletePopupComponent } from './leave-request-delete-dialog.component';

@Injectable()
export class LeaveRequestResolvePagingParams implements Resolve<any> {

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

export const leaveRequestRoute: Routes = [
    {
        path: 'leave-request',
        component: LeaveRequestComponent,
        resolve: {
            'pagingParams': LeaveRequestResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.leaveRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'leave-request/:id',
        component: LeaveRequestDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.leaveRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const leaveRequestPopupRoute: Routes = [
    {
        path: 'leave-request-new',
        component: LeaveRequestPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.leaveRequest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'leave-request/:id/edit',
        component: LeaveRequestPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.leaveRequest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'leave-request/:id/delete',
        component: LeaveRequestDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.leaveRequest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
