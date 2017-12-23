import { Route } from '@angular/router';

import { PortalComponent } from './';
import { UserRouteAccessService } from '../shared';

export const PORTAL_ROUTE: Route[] = [{
    path: 'portal',
    component: PortalComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'portal.title',
    }
}];
