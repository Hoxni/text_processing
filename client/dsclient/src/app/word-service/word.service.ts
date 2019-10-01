import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import LinksService from "../links.service";
import {map} from 'rxjs/operators';
import {Word} from "../model/word";
import {FormGroup} from "@angular/forms";
import {ExtractRequestModel} from "../model/extract-request-model";

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

  deleteWord(word: string){
    return this.http.delete(`http://localhost:8086/api/v1/words/${word}`).subscribe();
  }

  change(word: string, change: Word){
    return this.http.put(`${LinksService.apiUrl}/words/${word}`, change).subscribe();
  }

  extractWords(file: string){
    return this.http.post(`${LinksService.apiUrl}/words/extract`, file).subscribe();
  }

  findAll(pattern: string, page: number, size: number, sort: string) {
    return this.http.get(`${LinksService.apiUrl}/words/find?` +
      `pattern=${pattern}&page=${page}&size=${size}&sort=${sort}`).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }

}
