import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {Word} from "../model/word";

@Component({
  selector: 'app-word-stat',
  templateUrl: './word-stat.component.html',
  styleUrls: ['./word-stat.component.css']
})
export class WordStatComponent implements OnInit {

  @ViewChild('fileInput', null) el: ElementRef;
  private file: string;
  private text: string;
  private stats: Word[];
  private sorts: { name: string}[];
  private sort: string;

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.sort = 'alphabetic';
    this.sorts = this.getSortTypes();
  }

  fileUpload(e) {
    console.log(this.el);
    this.file = e.target.files[0].name;
  }

  stat() {
    return this.wordService.tagStatA(this.file, this.sort)
      .subscribe(data => {
        this.stats = data;
      });
  }

  getSortTypes(){
    return [
      {name: 'alphabetic'},
      {name: 'alphabetic_desc'},
      {name: 'frequency'},
      {name: 'frequency_desc'}];
  }

  setSort(e) {
    this.sort = e.currentTarget.value;
  }

  statTags(){
    this.wordService.tagStat(this.sort)
      .subscribe(data => {
        this.stats = data;
      });
  }

  ngOnInit() {
  }

}
