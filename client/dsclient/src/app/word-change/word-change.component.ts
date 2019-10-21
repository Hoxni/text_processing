import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {Word} from "../model/word";

@Component({
  selector: 'app-word-change',
  templateUrl: './word-change.component.html',
  styleUrls: ['./word-change.component.css']
})
export class WordChangeComponent implements OnInit {

  private words: Word[];
  private form: FormGroup;
  private findType: { name: string }[];
  private sorts: { name: string }[];
  private row: number;
  private word: string;

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


    this.findAll = this.findAll.bind(this);
  };

  changeWord(input) {
    const word = this.words[this.row].word.trim();
    const change = input.innerText;
    this.word = change;
    this.wordService.change(word, new Word({word: change, frequency: null}));
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

  findAll() {
    const pattern: string = this.form.controls['pattern'].value;
    const page: number = this.form.controls['page'].value;
    const size: number = this.form.controls['size'].value;
    const sort: string = this.form.controls['sorts'].value;

    return this.wordService.findAll(pattern, page, size, sort)
      .subscribe(data => {
        this.words = data;
      });
  }

  getRow() {
    return this.row;
  }

  setRow(i) {
    this.row = i;
    this.word = this.words[i].word;
  }

  onBlur(event, input) {
    if (event.relatedTarget !== null && event.relatedTarget.id === 'change') {
      this.changeWord(input);
    } else {
      this.row = null;
      input.firstChild.data = this.word;
    }
  }

  ngOnInit() {
  }

}
