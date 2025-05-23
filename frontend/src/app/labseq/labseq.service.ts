import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LabseqService {
  private baseUrl = 'http://localhost:8080/labseq';

  constructor(private http: HttpClient) {}

  calculate(n: number): Observable<string> {
    return this.http.get(`${this.baseUrl}/${n}`, { responseType: 'text' });
  }

  clearCache(): Observable<string> {
    return this.http.get(`${this.baseUrl}/clearCache`, { responseType: 'text' });
  }

  printCache(): Observable<string> {
    return this.http.get(`${this.baseUrl}/printCache`, { responseType: 'text' });
  }

  getCacheSize(): Observable<string> {
    return this.http.get(`${this.baseUrl}/size`, { responseType: 'text' });
  }
}