import { Injectable } from '@angular/core';
import { catchError, lastValueFrom,retry, throwError, timeout } from "rxjs";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";

import { Coordinate, TspSolutionResponse } from '../../core/models/models';


@Injectable({
    providedIn: 'root'
})
export class PathfinderService {
    timeoutTime : number = 10000;

    constructor(private http: HttpClient) { }

    parseLogs(userInput: String): Promise<Coordinate[]>{
        return lastValueFrom(this.http.post<Coordinate[]>('/route/parse', { userInput }).pipe(
            timeout(this.timeoutTime),
            retry(2),
            catchError((err: HttpErrorResponse) => this.handleError(err))
        ));
    }

    calculatePath(logs: Coordinate[]): Promise<TspSolutionResponse[]> {
        return lastValueFrom(
            this.http.post<TspSolutionResponse[]>('/route/calculate', { logs: logs }).pipe(
            timeout(this.timeoutTime),
            retry(2),
            catchError((err: HttpErrorResponse) => this.handleError(err))
            )
        );
    }

    private handleError(error: HttpErrorResponse) {
        let errorMessage = '';
        if (error.error instanceof ErrorEvent) {
            errorMessage = `Client error: ${error.error.message}`;
        } else {
            errorMessage = `Server error ${error.status}`;
        }

        return throwError(() => new Error(errorMessage));
    }
}