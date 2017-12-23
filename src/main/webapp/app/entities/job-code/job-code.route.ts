import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { JobCodeComponent } from './job-code.component';
import { JobCodeDetailComponent } from './job-code-detail.component';
import { JobCodePopupComponent } from './job-code-dialog.component';
import { JobCodeDeletePopupComponent } from './job-code-delete-dialog.component';

@Injectable()
export class JobCodeResolvePagingParams implements Resolve<any> {

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

export const jobCodeRoute: Routes = [
    {
        path: 'job-code',
        component: JobCodeComponent,
        resolve: {
            'pagingParams': JobCodeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.jobCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-code/:id',
        component: JobCodeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.jobCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobCodePopupRoute: Routes = [
    {
        path: 'job-code-new',
        component: JobCodePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.jobCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-code/:id/edit',
        component: JobCodePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.jobCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-code/:id/delete',
        component: JobCodeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'timesheetApp.jobCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
