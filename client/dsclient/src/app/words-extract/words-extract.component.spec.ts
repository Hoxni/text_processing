import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WordsExtractComponent } from './words-extract.component';

describe('WordsExtractComponent', () => {
  let component: WordsExtractComponent;
  let fixture: ComponentFixture<WordsExtractComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WordsExtractComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WordsExtractComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
