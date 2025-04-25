import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-buttons',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './buttons.component.html',
  styleUrl: './buttons.component.css'
})
export class ButtonsComponent {
  @Output() loginEvent = new EventEmitter();
  @Output() logoutEvent = new EventEmitter();
}
