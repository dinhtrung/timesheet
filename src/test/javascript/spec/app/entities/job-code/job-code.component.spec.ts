/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { TimesheetTestModule } from '../../../test.module';
import { JobCodeComponent } from '../../../../../../main/webapp/app/entities/job-code/job-code.component';
import { JobCodeService } from '../../../../../../main/webapp/app/entities/job-code/job-code.service';
import { JobCode } from '../../../../../../main/webapp/app/entities/job-code/job-code.model';

describe('Component Tests', () => {

    describe('JobCode Management Component', () => {
        let comp: JobCodeComponent;
        let fixture: ComponentFixture<JobCodeComponent>;
        let service: JobCodeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TimesheetTestModule],
                declarations: [JobCodeComponent],
                providers: [
                    JobCodeService
                ]
            })
            .overrideTemplate(JobCodeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobCodeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobCodeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new JobCode(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.jobCodes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
