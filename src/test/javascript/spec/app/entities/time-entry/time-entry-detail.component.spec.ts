/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { TimeEntryDetailComponent } from '../../../../../../main/webapp/app/entities/time-entry/time-entry-detail.component';
import { TimeEntryService } from '../../../../../../main/webapp/app/entities/time-entry/time-entry.service';
import { TimeEntry } from '../../../../../../main/webapp/app/entities/time-entry/time-entry.model';

describe('Component Tests', () => {

    describe('TimeEntry Management Detail Component', () => {
        let comp: TimeEntryDetailComponent;
        let fixture: ComponentFixture<TimeEntryDetailComponent>;
        let service: TimeEntryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [TimeEntryDetailComponent],
                providers: [
                    TimeEntryService
                ]
            })
            .overrideTemplate(TimeEntryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimeEntryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimeEntryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new TimeEntry(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.timeEntry).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
