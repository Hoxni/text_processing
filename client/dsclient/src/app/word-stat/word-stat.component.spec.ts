import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WordStatComponent } from './word-stat.component';

describe('WordStatComponent', () => {
  let component: WordStatComponent;
  let fixture: ComponentFixture<WordStatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WordStatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WordStatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
