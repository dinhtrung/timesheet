/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { MeetingDetailComponent } from '../../../../../../main/webapp/app/entities/meeting/meeting-detail.component';
import { MeetingService } from '../../../../../../main/webapp/app/entities/meeting/meeting.service';
import { Meeting } from '../../../../../../main/webapp/app/entities/meeting/meeting.model';

describe('Component Tests', () => {

    describe('Meeting Management Detail Component', () => {
        let comp: MeetingDetailComponent;
        let fixture: ComponentFixture<MeetingDetailComponent>;
        let service: MeetingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [MeetingDetailComponent],
                providers: [
                    MeetingService
                ]
            })
            .overrideTemplate(MeetingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeetingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeetingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Meeting(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.meeting).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
