import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ExtractRequestModel} from "../model/extract-request-model";
import {IDropdownSettings} from 'ng-multiselect-dropdown';

@Component({
  selector: 'app-words-extract',
  templateUrl: './words-extract.component.html',
  styleUrls: ['./words-extract.component.css']
})
export class WordsExtractComponent implements OnInit {

  private form: FormGroup;
  private tags: { id: string }[];
  private dropdownSettings: IDropdownSettings;
  private selectedTags: string[];

  constructor(public dialog: MatDialog, private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.tags = this.initTags();
    this.selectedTags = [];
    this.form = fb.group({
      tags: [this.tags]
    });
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'id',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 5,
      allowSearchFilter: true,
      enableCheckAll: false
    };
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

  @ViewChild('fileInput', null) el: ElementRef;

  private file: string;

  fileUpload(e) {
    console.log(this.el);
    this.file = e.target.files[0].name;
  }

  extractWords() {
    this.wordService.extractWords(this.file);
  }

  addWord(word: string, tags: string) {
    this.wordService.addWord(word.trim());
  }

  ngOnInit() {
  }

  onSelect(e) {
    this.selectedTags.push(e.id);
  }

  onDeSelect(e) {
    this.selectedTags = this.selectedTags.filter(t => t !== e.id);
  }

}
