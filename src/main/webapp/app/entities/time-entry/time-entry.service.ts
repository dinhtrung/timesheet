import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TimeEntry } from './time-entry.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TimeEntryService {

    private resourceUrl = SERVER_API_URL + 'api/time-entries';
    public entity: TimeEntry;
    public entities: TimeEntry[] = [];
    public entityMap: any = {};
    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(timeEntry: TimeEntry): Observable<TimeEntry> {
        const copy = this.convert(timeEntry);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(timeEntry: TimeEntry): Observable<TimeEntry> {
        const copy = this.convert(timeEntry);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TimeEntry> {
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
     * Convert a returned JSON object to TimeEntry.
     */
    private convertItemFromServer(json: any): TimeEntry {
        const entity: TimeEntry = Object.assign(new TimeEntry(), json);
        entity.date = this.dateUtils
            .convertLocalDateFromServer(json.date);
        return entity;
    }

    /**
     * Convert a TimeEntry to a JSON which can be sent to the server.
     */
    private convert(timeEntry: TimeEntry): TimeEntry {
        const copy: TimeEntry = Object.assign({}, timeEntry);
        copy.date = this.dateUtils
            .convertLocalDateToServer(timeEntry.date);
        return copy;
    }
}
