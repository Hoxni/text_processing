import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {Word} from "../model/word";

@Component({
  selector: 'app-text-annotate',
  templateUrl: './text-annotate.component.html',
  styleUrls: ['./text-annotate.component.css']
})
export class TextAnnotateComponent implements OnInit {

  @ViewChild('fileInput', null) el: ElementRef;
  private file: string;
  private text: string;
  private stats: Word[];
  private sorts: { name: string}[];
  private sort: string;
  private visible: boolean;
  private words: Word[];

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.sort = 'alphabetic';
    this.sorts = this.getSortTypes();
    this.visible = true;
  }

  fileUpload(e) {
    console.log(this.el);
    this.file = e.target.files[0].name;
  }

  annotateText() {
    this.visible = true;
    this.wordService.text(this.file).subscribe((data : Word) => {
      this.text = data.word;
    });
  }

  getList(e) {
    this.visible = false;
    this.wordService.getList(e)
      .subscribe(data => {
        this.words = data;
      });
  }

  sortList() {
    switch (this.sort) {
      case 'alphabetic_desc': {
        this.words = this.words.sort((a, b) => {
          return -a.word.localeCompare(b.word, 'en')
        });
        break;
      }
      case 'frequency': {
        this.words = this.words.sort((a, b) => {
          return a.frequency > b.frequency ? 1 : -1;
        });
        break;
      }
      case 'frequency_desc': {
        this.words = this.words.sort((a, b) => {
          return a.frequency > b.frequency ? -1 : 1;
        });
        break;
      }
      default: {
        this.words = this.words.sort((a, b) => {
          return a.word.localeCompare(b.word, 'en')
        });
        break;
      }
    }
  }

  saveText() {
    this.wordService.save(this.text);
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

  stat() {
    return this.wordService.stat(this.file, this.sort)
      .subscribe(data => {
        this.stats = data;
      });
  }

  ngOnInit() {
  }

}
