import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WordChangeComponent } from './word-change.component';

describe('WordChangeComponent', () => {
  let component: WordChangeComponent;
  let fixture: ComponentFixture<WordChangeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WordChangeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WordChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
