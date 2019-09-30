import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WordDeleteComponent } from './word-delete.component';

describe('WordDeleteComponent', () => {
  let component: WordDeleteComponent;
  let fixture: ComponentFixture<WordDeleteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WordDeleteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WordDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
