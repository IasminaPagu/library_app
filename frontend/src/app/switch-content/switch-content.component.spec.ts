import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SwitchContentComponent } from './switch-content.component';

describe('SwitchContentComponent', () => {
  let component: SwitchContentComponent;
  let fixture: ComponentFixture<SwitchContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SwitchContentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SwitchContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
