import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WordsListComponent } from './words-list/words-list.component';
import {RouterModule, Routes} from "@angular/router";
import {WordService} from "./word-service/word.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { WordDeleteComponent } from './word-delete/word-delete.component';
import { WordFindComponent } from './word-find/word-find.component';
import { WordChangeComponent } from './word-change/word-change.component';
import { WordsExtractComponent } from './words-extract/words-extract.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import {MatTooltipModule} from "@angular/material/tooltip";
import { TooltipModule } from 'ng2-tooltip-directive';
import {PopoverModule} from 'ngx-popover';

const appRoutes: Routes = [
  {
    path: 'words',
    component: WordsListComponent
  },
  {
    path: 'delete',
    component: WordDeleteComponent
  },
  {
    path: 'find',
    component: WordFindComponent
  },
  {
    path: 'change',
    component: WordChangeComponent
  },
  {
    path: 'add',
    component: WordsExtractComponent
  },
];

@NgModule({
  declarations: [
    AppComponent,
    WordsListComponent,
    WordDeleteComponent,
    WordFindComponent,
    WordChangeComponent,
    WordsExtractComponent,
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatDialogModule,
    FormsModule,
    MatInputModule,
    MatSelectModule,
    NgMultiSelectDropDownModule,
    MatTooltipModule,
    TooltipModule,
    PopoverModule,
  ],

  providers: [
    WordService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
