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

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.sort = 'alphabetic';
    this.sorts = this.getSortTypes();
  }

  fileUpload(e) {
    console.log(this.el);
    this.file = e.target.files[0].name;
  }

  annotateText() {
    this.wordService.text(this.file).subscribe((data : Word) => {
      this.text = data.word;
    });
  }

  saveText(e) {
    this.wordService.save(e);
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
