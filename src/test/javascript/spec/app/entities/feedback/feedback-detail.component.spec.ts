/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { FeedbackDetailComponent } from '../../../../../../main/webapp/app/entities/feedback/feedback-detail.component';
import { FeedbackService } from '../../../../../../main/webapp/app/entities/feedback/feedback.service';
import { Feedback } from '../../../../../../main/webapp/app/entities/feedback/feedback.model';

describe('Component Tests', () => {

    describe('Feedback Management Detail Component', () => {
        let comp: FeedbackDetailComponent;
        let fixture: ComponentFixture<FeedbackDetailComponent>;
        let service: FeedbackService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [FeedbackDetailComponent],
                providers: [
                    FeedbackService
                ]
            })
            .overrideTemplate(FeedbackDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FeedbackDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeedbackService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Feedback(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.feedback).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
