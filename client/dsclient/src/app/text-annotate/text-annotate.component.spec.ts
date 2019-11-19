import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TextAnnotateComponent } from './text-annotate.component';

describe('TextAnnotateComponent', () => {
  let component: TextAnnotateComponent;
  let fixture: ComponentFixture<TextAnnotateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TextAnnotateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TextAnnotateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
