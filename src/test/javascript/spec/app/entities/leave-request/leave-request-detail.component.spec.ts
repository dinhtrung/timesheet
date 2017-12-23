/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { LeaveRequestDetailComponent } from '../../../../../../main/webapp/app/entities/leave-request/leave-request-detail.component';
import { LeaveRequestService } from '../../../../../../main/webapp/app/entities/leave-request/leave-request.service';
import { LeaveRequest } from '../../../../../../main/webapp/app/entities/leave-request/leave-request.model';

describe('Component Tests', () => {

    describe('LeaveRequest Management Detail Component', () => {
        let comp: LeaveRequestDetailComponent;
        let fixture: ComponentFixture<LeaveRequestDetailComponent>;
        let service: LeaveRequestService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [LeaveRequestDetailComponent],
                providers: [
                    LeaveRequestService
                ]
            })
            .overrideTemplate(LeaveRequestDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeaveRequestDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeaveRequestService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new LeaveRequest(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.leaveRequest).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
