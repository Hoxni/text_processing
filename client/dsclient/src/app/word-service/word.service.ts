import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import LinksService from "../links.service";
import {map} from 'rxjs/operators';
import {Word} from "../model/word";
import {FormGroup} from "@angular/forms";
import {ExtractRequestModel} from "../model/extract-request-model";
import {throwError} from "rxjs";

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

  addWord(word: string) {
    return this.http.post(`http://localhost:8086/api/v1/words`,
      new Word({word: word, frequency: 0, tags: null, lemma: null, lemmaTags: null}))
      .subscribe();
  }

  deleteWord(word: string) {
    return this.http.delete(`http://localhost:8086/api/v1/words/${word}`).subscribe();
  }

  change(word: string, change: Word) {
    return this.http.put(`${LinksService.apiUrl}/words/${word}`, change).subscribe();
  }

  extractWords(file: string) {
    return this.http.post(`${LinksService.apiUrl}/words/extract`, file).subscribe();
  }

  findAll(pattern: string, page: number, size: number, sort: string) {
    return this.http.get(`${LinksService.apiUrl}/words/find?` +
      `pattern=${pattern}&page=${page}&size=${size}&sort=${sort}`).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }

  text(file: string) {
    return this.http.post(`${LinksService.apiUrl}/words/text`, file);
  }

  stat(file: string, sort : string) {
    return this.http.post(`${LinksService.apiUrl}/words/stat`, new Word({word : file, frequency : null, lemmaTags: null, tags: null, lemma: sort})).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }

  save(text: string) {
    return this.http.post(`http://localhost:8086/api/v1/text`,
      new Word({word: text, frequency: 0, tags: null, lemma: null, lemmaTags: null}))
      .subscribe();
  }

  tagStat(sort : string) {
    return this.http.post(`${LinksService.apiUrl}/words/stat-a`, sort).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }

  tagStatA(file: string, sort: string) {
    return this.http.post(`${LinksService.apiUrl}/words/stat-tag`, new Word({word : file, frequency : null, lemmaTags: null, tags: null, lemma: sort})).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }

  searchAll(pattern: string, page: number, size: number, sort: string) {
    return this.http.get(`${LinksService.apiUrl}/words/search?` +
      `pattern=${pattern}&page=${page}&size=${size}`).pipe(
      map((response: []) => {
        return response.map(o => new Word(o));
      }));
  }
}
