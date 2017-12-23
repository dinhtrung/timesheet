import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Timesheet } from './timesheet.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TimesheetService {

    private resourceUrl = SERVER_API_URL + 'api/timesheets';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(timesheet: Timesheet): Observable<Timesheet> {
        const copy = this.convert(timesheet);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(timesheet: Timesheet): Observable<Timesheet> {
        const copy = this.convert(timesheet);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Timesheet> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Timesheet.
     */
    private convertItemFromServer(json: any): Timesheet {
        const entity: Timesheet = Object.assign(new Timesheet(), json);
        entity.approvedAt = this.dateUtils
            .convertDateTimeFromServer(json.approvedAt);
        return entity;
    }

    /**
     * Convert a Timesheet to a JSON which can be sent to the server.
     */
    private convert(timesheet: Timesheet): Timesheet {
        const copy: Timesheet = Object.assign({}, timesheet);

        copy.approvedAt = this.dateUtils.toDate(timesheet.approvedAt);
        return copy;
    }
}
