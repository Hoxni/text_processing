import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {Word} from "../model/word";
import {IDropdownSettings} from "ng-multiselect-dropdown";

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
  private dropdownSettings: IDropdownSettings;
  private tags: { id: string }[];

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {

    this.findType = this.getFindTypes();
    this.sorts = this.getSortTypes();
    this.tags = this.initTags();

    this.form = fb.group({
      pattern: [''],
      page: [''],
      size: [''],
      findType: [this.findType],
      sorts: [this.sorts]
    });

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'id',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      allowSearchFilter: true,
      enableCheckAll: false,
    };

    this.findAll = this.findAll.bind(this);
  };

  changeWord(input) {
    const word = this.words[this.row].word.trim();
    const change = input.innerText;
    this.word = change;
    this.wordService.change(word, new Word({word: change, frequency: null, tags: this.words[this.row].tags, lemma: null, lemmaTags: null}));
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

  initTags() {
    return [
      {id: 'WRB'},
      {id: 'WP$'},
      {id: 'WP'},
      {id: 'WDT'},
      {id: 'VP'},
      {id: 'VBZ'},
      {id: 'VBP'},
      {id: 'VBN'},
      {id: 'VBG'},
      {id: 'VBD'},
      {id: 'VB'},
      {id: 'UH'},
      {id: 'TO'},
      {id: 'SYM'},
      {id: 'RP'},
      {id: 'RBS'},
      {id: 'RBR'},
      {id: 'RB'},
      {id: 'PRP$'},
      {id: 'PRP'},
      {id: 'PP'},
      {id: 'POS'},
      {id: 'PDT'},
      {id: 'NP'},
      {id: 'NNS'},
      {id: 'NNPS'},
      {id: 'NNP'},
      {id: 'NN'},
      {id: 'MD'},
      {id: 'LS'},
      {id: 'JJS'},
      {id: 'JJR'},
      {id: 'JJ'},
      {id: 'IN'},
      {id: 'FW'},
      {id: 'EX'},
      {id: 'DT'},
      {id: 'CD'},
      {id: 'CC'}
    ];
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
    } else if (event.relatedTarget !== null &&
      event.relatedTarget.className === 'dropdown-btn' &&
      event.path[4].rowIndex - 1 === this.row) {
    } else {
      this.row = null;
      input.firstChild.data = this.word;
    }
  }

  ngOnInit() {
  }

  onSelect(e, i) {
    this.words[i].tags.push(e.id);
  }

  onDeSelect(e, i) {
    this.words[i].tags = this.words[i].tags.filter(t => t !== e.id);
  }

}
