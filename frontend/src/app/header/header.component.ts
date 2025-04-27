// import { Component, Input} from '@angular/core';
//
// @Component({
//   selector: 'app-header',
//   imports: [],
//   templateUrl: './header.component.html',
//   styleUrl: './header.component.css'
// })
// export class HeaderComponent {
//   @Input() pageTitle!: string;
//   @Input() logoSrc!: string;
// //   i start by adding the input variables
//
// }
import { Component, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  @Input() logoSrc = '';
  @Input() pageTitle = '';

  @Output() loginEvent = new EventEmitter<void>();
  @Output() logoutEvent = new EventEmitter<void>();
}
