/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { TimesheetDetailComponent } from '../../../../../../main/webapp/app/entities/timesheet/timesheet-detail.component';
import { TimesheetService } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.service';
import { Timesheet } from '../../../../../../main/webapp/app/entities/timesheet/timesheet.model';

describe('Component Tests', () => {

    describe('Timesheet Management Detail Component', () => {
        let comp: TimesheetDetailComponent;
        let fixture: ComponentFixture<TimesheetDetailComponent>;
        let service: TimesheetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [TimesheetDetailComponent],
                providers: [
                    TimesheetService
                ]
            })
            .overrideTemplate(TimesheetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimesheetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimesheetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Timesheet(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.timesheet).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
