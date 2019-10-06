import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {ExtractRequestModel} from "../model/extract-request-model";

@Component({
  selector: 'app-words-extract',
  templateUrl: './words-extract.component.html',
  styleUrls: ['./words-extract.component.css']
})
export class WordsExtractComponent implements OnInit {

  constructor(public dialog: MatDialog, private wordService: WordService, private router: Router, private fb: FormBuilder) {
  }

  @ViewChild('fileInput', null) el: ElementRef;

  private file: string;

  fileUpload(e) {
    console.log(this.el);
    this.file = e.target.files[0].name;
  }

  extractWords(){
    this.wordService.extractWords(this.file);
  }

  addWord(word: string){
    this.wordService.addWord(word);
  }

  ngOnInit() {
  }

}
