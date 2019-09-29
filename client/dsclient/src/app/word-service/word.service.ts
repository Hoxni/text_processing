import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import LinksService from "../links.service";
import {map} from 'rxjs/operators';
import {Word} from "../model/word";
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class WordService {

  form: FormGroup;

  constructor(private http: HttpClient) {
  };

  getAll(page: number, size: number, sort: string) {
    return this.http.get(`${LinksService.apiUrl}/words?` +
    `page=${page}&size=${size}&sort=${sort}`).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }
}
