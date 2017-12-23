/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { TimesheetTestModule } from '../../../test.module';
import { LeaveRequestComponent } from '../../../../../../main/webapp/app/entities/leave-request/leave-request.component';
import { LeaveRequestService } from '../../../../../../main/webapp/app/entities/leave-request/leave-request.service';
import { LeaveRequest } from '../../../../../../main/webapp/app/entities/leave-request/leave-request.model';

describe('Component Tests', () => {

    describe('LeaveRequest Management Component', () => {
        let comp: LeaveRequestComponent;
        let fixture: ComponentFixture<LeaveRequestComponent>;
        let service: LeaveRequestService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [LeaveRequestComponent],
                providers: [
                    LeaveRequestService
                ]
            })
            .overrideTemplate(LeaveRequestComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeaveRequestComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeaveRequestService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new LeaveRequest(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.leaveRequests[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
