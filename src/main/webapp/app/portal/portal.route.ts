import { Routes } from '@angular/router';

import { PortalComponent, LoggingComponent } from './';
import { UserRouteAccessService } from '../shared';

export const PORTAL_ROUTE: Routes = [
  {
    path: 'portal',
    component: PortalComponent,
    canActivate: [ UserRouteAccessService ],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'portal.title',
    }
  },
  {
    component: LoggingComponent,
    path: 'monitor',
    canActivate: [ UserRouteAccessService ],
    data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'portal.title',
    }
  }
];
