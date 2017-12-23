/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { TimesheetTestModule } from '../../../test.module';
import { MeetingInvitationComponent } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.component';
import { MeetingInvitationService } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.service';
import { MeetingInvitation } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.model';

describe('Component Tests', () => {

    describe('MeetingInvitation Management Component', () => {
        let comp: MeetingInvitationComponent;
        let fixture: ComponentFixture<MeetingInvitationComponent>;
        let service: MeetingInvitationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [MeetingInvitationComponent],
                providers: [
                    MeetingInvitationService
                ]
            })
            .overrideTemplate(MeetingInvitationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeetingInvitationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeetingInvitationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new MeetingInvitation(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.meetingInvitations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
