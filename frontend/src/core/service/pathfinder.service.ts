import { Injectable } from '@angular/core';
import { catchError, Observable, retry, throwError } from "rxjs";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";

import { Area, Coordinate, TspSolutionResponse } from '../../core/models/models';


@Injectable({
    providedIn: 'root'
})
export class PathfinderService {

    constructor(private http: HttpClient) { }

    parseLogs(userInput: String): Observable<Coordinate[]>{
        return this.http.post<Coordinate[]>('/route/parse', { userInput }).pipe(
            retry(2),
            catchError((err: HttpErrorResponse) => this.handleError(err))
        );
    }

    calculatePath(logs: Coordinate[]): Observable<TspSolutionResponse[]>{
        return this.http.post<TspSolutionResponse[]>('/route/calculate', { logs }).pipe(
            retry(2),
            catchError((err: HttpErrorResponse) => this.handleError(err))
        );
    }

    private handleError(error: HttpErrorResponse) {
        console.error('Error occurred:', error);

        let errorMessage = '';
        if (error.error instanceof ErrorEvent) {
            errorMessage = `Client error: ${error.error.message}`;
        } else {
            errorMessage = `Server error ${error.status}: ${error.message}`;
        }

        return throwError(() => new Error(errorMessage));
    }
}