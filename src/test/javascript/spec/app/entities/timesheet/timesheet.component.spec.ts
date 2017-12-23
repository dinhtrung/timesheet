/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { TimesheetTestModule } from '../../../test.module';
import { TimesheetComponent } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.component';
import { TimesheetService } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.service';
import { Timesheet } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.model';

describe('Component Tests', () => {

    describe('Timesheet Management Component', () => {
        let comp: TimesheetComponent;
        let fixture: ComponentFixture<TimesheetComponent>;
        let service: TimesheetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [TimesheetComponent],
                providers: [
                    TimesheetService
                ]
            })
            .overrideTemplate(TimesheetComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimesheetComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimesheetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Timesheet(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.timesheets[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
