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

  private form: FormGroup;

  constructor(private wordService: WordService, private router: Router, private fb: FormBuilder) {
    this.form = fb.group({
      word: [''],
      change: ['']
    });

    this.changeWord = this.changeWord.bind(this);
  }

  changeWord() {
    const word = this.form.controls['word'].value;
    const change = this.form.controls['change'].value;
    this.wordService.change(word, new Word({word: change, frequency: null}));
  }

  ngOnInit() {
  }

}
