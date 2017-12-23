import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Meeting } from './meeting.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MeetingService {

    private resourceUrl = SERVER_API_URL + 'api/meetings';
    public entity: Meeting;
    public entities: Meeting[] = [];
    public entityMap: any = {};
    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(meeting: Meeting): Observable<Meeting> {
        const copy = this.convert(meeting);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(meeting: Meeting): Observable<Meeting> {
        const copy = this.convert(meeting);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Meeting> {
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
     * Convert a returned JSON object to Meeting.
     */
    private convertItemFromServer(json: any): Meeting {
        const entity: Meeting = Object.assign(new Meeting(), json);
        entity.startDate = this.dateUtils
            .convertDateTimeFromServer(json.startDate);
        entity.endDate = this.dateUtils
            .convertDateTimeFromServer(json.endDate);
        return entity;
    }

    /**
     * Convert a Meeting to a JSON which can be sent to the server.
     */
    private convert(meeting: Meeting): Meeting {
        const copy: Meeting = Object.assign({}, meeting);

        copy.startDate = this.dateUtils.toDate(meeting.startDate);

        copy.endDate = this.dateUtils.toDate(meeting.endDate);
        return copy;
    }
}
