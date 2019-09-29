import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WordsListComponent } from './words-list/words-list.component';
import {RouterModule, Routes} from "@angular/router";
import {WordService} from "./word-service/word.service";
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";

const appRoutes: Routes = [
  {
    path: 'words',
    component: WordsListComponent
  },
];

@NgModule({
  declarations: [
    AppComponent,
    WordsListComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [
    WordService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
