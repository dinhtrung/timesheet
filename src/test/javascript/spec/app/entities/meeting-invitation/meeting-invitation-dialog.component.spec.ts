/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TimesheetTestModule } from '../../../test.module';
import { MeetingInvitationDialogComponent } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation-dialog.component';
import { MeetingInvitationService } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.service';
import { MeetingInvitation } from '../../../../../../main/webapp/app/entities/meeting-invitation/meeting-invitation.model';
import { MeetingService } from '../../../../../../main/webapp/app/entities/meeting';

describe('Component Tests', () => {

    describe('MeetingInvitation Management Dialog Component', () => {
        let comp: MeetingInvitationDialogComponent;
        let fixture: ComponentFixture<MeetingInvitationDialogComponent>;
        let service: MeetingInvitationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [MeetingInvitationDialogComponent],
                providers: [
                    MeetingService,
                    MeetingInvitationService
                ]
            })
            .overrideTemplate(MeetingInvitationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeetingInvitationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeetingInvitationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MeetingInvitation(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.meetingInvitation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'meetingInvitationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MeetingInvitation();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.meetingInvitation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'meetingInvitationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
