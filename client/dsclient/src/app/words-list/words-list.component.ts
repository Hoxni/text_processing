import {Component} from '@angular/core';
import {Word} from "../model/word";
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-words-list',
  templateUrl: './words-list.component.html',
  styleUrls: ['./words-list.component.css']
})
export class WordsListComponent {

  private words: Word[];
  private form: FormGroup;
  private sorts: { name: string}[];

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.wordService.getAll(1, 10, 'alphabetic')
      .subscribe(data => {
        this.words = data;
      });

    this.sorts = this.getSortTypes();

    this.form = fb.group({
      page: [''],
      size: [''],
      sort: [this.sorts],
    });

    this.getAll = this.getAll.bind(this);
  };

  getSortTypes(){
    return [
      {name: 'alphabetic'},
      {name: 'alphabetic_desc'},
      {name: 'frequency'},
      {name: 'frequency_desc'}];
  }

  getAll() {
    const page: number = this.form.controls['page'].value;
    const size: number = this.form.controls['size'].value;
    const sort: string = this.form.controls['sort'].value;

    return this.wordService.getAll(page, size, sort)
      .subscribe(data => {
        this.words = data;
      });
  }

  showPopover(e){

  }

}
