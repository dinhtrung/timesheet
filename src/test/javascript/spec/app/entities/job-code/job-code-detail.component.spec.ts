/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { TimesheetTestModule } from '../../../test.module';
import { JobCodeDetailComponent } from '../../../../../../main/webapp/app/entities/job-code/job-code-detail.component';
import { JobCodeService } from '../../../../../../main/webapp/app/entities/job-code/job-code.service';
import { JobCode } from '../../../../../../main/webapp/app/entities/job-code/job-code.model';

describe('Component Tests', () => {

    describe('JobCode Management Detail Component', () => {
        let comp: JobCodeDetailComponent;
        let fixture: ComponentFixture<JobCodeDetailComponent>;
        let service: JobCodeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [JobCodeDetailComponent],
                providers: [
                    JobCodeService
                ]
            })
            .overrideTemplate(JobCodeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobCodeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobCodeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new JobCode(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.jobCode).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
