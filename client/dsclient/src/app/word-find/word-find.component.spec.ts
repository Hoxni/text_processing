import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WordFindComponent } from './word-find.component';

describe('WordFindComponent', () => {
  let component: WordFindComponent;
  let fixture: ComponentFixture<WordFindComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WordFindComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WordFindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
