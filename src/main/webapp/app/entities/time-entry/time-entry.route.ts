import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TimeEntryComponent } from './time-entry.component';
import { TimeEntryDetailComponent } from './time-entry-detail.component';
import { TimeEntryPopupComponent } from './time-entry-dialog.component';
import { TimeEntryDeletePopupComponent } from './time-entry-delete-dialog.component';

@Injectable()
export class TimeEntryResolvePagingParams implements Resolve<any> {

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

export const timeEntryRoute: Routes = [
    {
        path: 'time-entry',
        component: TimeEntryComponent,
        resolve: {
            'pagingParams': TimeEntryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'time-entry/:id',
        component: TimeEntryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timeEntryPopupRoute: Routes = [
    {
        path: 'time-entry-new',
        component: TimeEntryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time-entry/:id/edit',
        component: TimeEntryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time-entry/:id/delete',
        component: TimeEntryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.timeEntry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
