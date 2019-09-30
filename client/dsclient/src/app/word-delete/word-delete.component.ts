import { Component, OnInit } from '@angular/core';
import {WordService} from "../word-service/word.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-word-delete',
  templateUrl: './word-delete.component.html',
  styleUrls: ['./word-delete.component.css']
})
export class WordDeleteComponent {

  private form: FormGroup;

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.form = fb.group({
      word: ['']
    });

    this.deleteWord = this.deleteWord.bind(this);
  }

  deleteWord(){
    const word = this.form.controls['word'].value;
    this.wordService.deleteWord(word);
  }

}
