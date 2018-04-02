import { Component } from '@angular/core';
import {ToasterConfig} from 'angular5-toaster/dist';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Kanban';

  public toasterconfig : ToasterConfig =
    new ToasterConfig({
      positionClass: 'toast-bottom-left'
    });
}
