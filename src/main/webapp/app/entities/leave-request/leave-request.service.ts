import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { LeaveRequest } from './leave-request.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LeaveRequestService {

    private resourceUrl = SERVER_API_URL + 'api/leave-requests';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(leaveRequest: LeaveRequest): Observable<LeaveRequest> {
        const copy = this.convert(leaveRequest);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(leaveRequest: LeaveRequest): Observable<LeaveRequest> {
        const copy = this.convert(leaveRequest);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<LeaveRequest> {
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
     * Convert a returned JSON object to LeaveRequest.
     */
    private convertItemFromServer(json: any): LeaveRequest {
        const entity: LeaveRequest = Object.assign(new LeaveRequest(), json);
        entity.startDate = this.dateUtils
            .convertDateTimeFromServer(json.startDate);
        entity.endDate = this.dateUtils
            .convertDateTimeFromServer(json.endDate);
        entity.updatedAt = this.dateUtils
            .convertDateTimeFromServer(json.updatedAt);
        return entity;
    }

    /**
     * Convert a LeaveRequest to a JSON which can be sent to the server.
     */
    private convert(leaveRequest: LeaveRequest): LeaveRequest {
        const copy: LeaveRequest = Object.assign({}, leaveRequest);

        copy.startDate = this.dateUtils.toDate(leaveRequest.startDate);

        copy.endDate = this.dateUtils.toDate(leaveRequest.endDate);

        copy.updatedAt = this.dateUtils.toDate(leaveRequest.updatedAt);
        return copy;
    }
}
