/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { MeetingInvitationDetailComponent } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation-detail.component';
import { MeetingInvitationService } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.service';
import { MeetingInvitation } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.model';

describe('Component Tests', () => {

    describe('MeetingInvitation Management Detail Component', () => {
        let comp: MeetingInvitationDetailComponent;
        let fixture: ComponentFixture<MeetingInvitationDetailComponent>;
        let service: MeetingInvitationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [MeetingInvitationDetailComponent],
                providers: [
                    MeetingInvitationService
                ]
            })
            .overrideTemplate(MeetingInvitationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeetingInvitationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeetingInvitationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new MeetingInvitation(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.meetingInvitation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
