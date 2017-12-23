/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { TimesheetTestModule } from '../../../test.module';
import { MeetingComponent } from '../../../../../../main/webapp/app/entities/meeting/meeting.component';
import { MeetingService } from '../../../../../../main/webapp/app/entities/meeting/meeting.service';
import { Meeting } from '../../../../../../main/webapp/app/entities/meeting/meeting.model';

describe('Component Tests', () => {

    describe('Meeting Management Component', () => {
        let comp: MeetingComponent;
        let fixture: ComponentFixture<MeetingComponent>;
        let service: MeetingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [MeetingComponent],
                providers: [
                    MeetingService
                ]
            })
            .overrideTemplate(MeetingComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeetingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeetingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Meeting(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.meetings[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
