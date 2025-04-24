import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-buttons',
  imports: [],
  standalone: true,
  templateUrl: './buttons.component.html',
  styleUrl: './buttons.component.css'
})
export class ButtonsComponent {
  @Output() loginEvent = new EventEmitter();
  @Output() logoutEvent = new EventEmitter();
}
