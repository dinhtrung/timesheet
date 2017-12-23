import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { MeetingInvitation } from './meeting-invitation.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MeetingInvitationService {

    private resourceUrl = SERVER_API_URL + 'api/meeting-invitations';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(meetingInvitation: MeetingInvitation): Observable<MeetingInvitation> {
        const copy = this.convert(meetingInvitation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(meetingInvitation: MeetingInvitation): Observable<MeetingInvitation> {
        const copy = this.convert(meetingInvitation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<MeetingInvitation> {
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
     * Convert a returned JSON object to MeetingInvitation.
     */
    private convertItemFromServer(json: any): MeetingInvitation {
        const entity: MeetingInvitation = Object.assign(new MeetingInvitation(), json);
        entity.decidedAt = this.dateUtils
            .convertDateTimeFromServer(json.decidedAt);
        return entity;
    }

    /**
     * Convert a MeetingInvitation to a JSON which can be sent to the server.
     */
    private convert(meetingInvitation: MeetingInvitation): MeetingInvitation {
        const copy: MeetingInvitation = Object.assign({}, meetingInvitation);

        copy.decidedAt = this.dateUtils.toDate(meetingInvitation.decidedAt);
        return copy;
    }
}
