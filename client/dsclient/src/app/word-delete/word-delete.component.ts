import {Component, OnInit} from '@angular/core';
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Word} from "../model/word";

@Component({
  selector: 'app-word-delete',
  templateUrl: './word-delete.component.html',
  styleUrls: ['./word-delete.component.css']
})
export class WordDeleteComponent {

  private words: { word: Word, selected: boolean }[];
  private form: FormGroup;
  private findType: { name: string }[];
  private sorts: { name: string }[];

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.findType = this.getFindTypes();
    this.sorts = this.getSortTypes();

    this.form = fb.group({
      pattern: [''],
      page: [''],
      size: [''],
      findType: [this.findType],
      sorts: [this.sorts]
    });

    this.deleteWords = this.deleteWords.bind(this);
  }

  getFindTypes() {
    return [
      {name: 'head'},
      {name: 'reverse'}
    ];
  }

  getSortTypes() {
    return [
      {name: 'alphabetic'},
      {name: 'alphabetic_desc'},
      {name: 'frequency'},
      {name: 'frequency_desc'}];
  }

  deleteWords() {
    this.words.forEach(value => {
      if (value.selected) {
        this.wordService.deleteWord(value.word.word)
      }
    });

    this.words = this.words.filter(value => !value.selected);
  }

  findAll() {
    const pattern: string = this.form.controls['pattern'].value;
    const page: number = this.form.controls['page'].value;
    const size: number = this.form.controls['size'].value;
    const sort: string = this.form.controls['sorts'].value;

    return this.wordService.findAll(pattern, page, size, sort)
      .subscribe(data => {
        this.words = [];
        data.forEach(value => this.words.push({word: value, selected: false}));
      });
  }

}
