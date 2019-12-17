import {Component, OnInit} from '@angular/core';
import {Word} from "../model/word";
import {FormBuilder, FormGroup} from "@angular/forms";
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-word-find',
  templateUrl: './word-search.component.html',
  styleUrls: ['./word-search.component.css']
})
export class WordSearchComponent implements OnInit {

  private words: Word[];
  private form: FormGroup;
  private readonly findType: { name: string }[];
  private readonly sorts: { name: string }[];

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

  getFindTypes() {
    return [
      {name: 'head'},
      {name: 'reverse'}
    ];
  }

  getSortTypes(){
    return [
      {name: 'alphabetic'},
      {name: 'alphabetic_desc'},
      {name: 'frequency'},
      {name: 'frequency_desc'}];
  }

  findAll() {
    const pattern: string = this.form.controls['pattern'].value;
    const page: number = this.form.controls['page'].value || 1;
    const size: number = this.form.controls['size'].value || 100;
    const sort: string = this.form.controls['sorts'].value;

    return this.wordService.searchAll(pattern, page, size, sort)
      .subscribe(data => {
        this.words = data;
      });
  }

  ngOnInit() {
  }

}
