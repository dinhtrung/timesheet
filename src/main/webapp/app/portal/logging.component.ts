import { Component, OnInit, OnDestroy } from '@angular/core';

import { JhiTrackerService } from '../shared';

@Component({
    selector: 'jhi-app-logging',
    templateUrl: './logging.component.html'
})
export class LoggingComponent implements OnInit, OnDestroy {

    activities: string[];
    subscribe: any;
    constructor(
        private trackerService: JhiTrackerService,
    ) {
      this.activities = ['Welcome to monitoring services'];
    }

    ngOnInit() {
        console.log('Entering Monitor page...');
        this.trackerService.connection.then(() => {
          this.subscribe = this.trackerService.stompClient.subscribe('/topic/logging', (data) => {
            console.log(data);
            this.addActivity(data.body.toString());
          });
        })
    }

    addActivity(activity: string) {
      this.activities.push(activity);
    }

    ngOnDestroy() {
      this.subscribe.unsubscribe();
    }
}
