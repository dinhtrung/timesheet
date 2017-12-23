import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JobCode } from './job-code.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobCodeService {

    private resourceUrl = SERVER_API_URL + 'api/job-codes';
    public entity: JobCode;
    public entities: JobCode[] = [];
    public entityMap: any = {};
    constructor(private http: Http) { }

    create(jobCode: JobCode): Observable<JobCode> {
        const copy = this.convert(jobCode);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobCode: JobCode): Observable<JobCode> {
        const copy = this.convert(jobCode);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobCode> {
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
     * Convert a returned JSON object to JobCode.
     */
    private convertItemFromServer(json: any): JobCode {
        const entity: JobCode = Object.assign(new JobCode(), json);
        return entity;
    }

    /**
     * Convert a JobCode to a JSON which can be sent to the server.
     */
    private convert(jobCode: JobCode): JobCode {
        const copy: JobCode = Object.assign({}, jobCode);
        return copy;
    }
}
