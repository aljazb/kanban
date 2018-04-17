import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {Board} from '../../../api/models/Board';
import {LoginService} from '../../../api/login.service';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  isKanbanMaster: boolean;

  constructor(private router: Router, private loginService: LoginService) { }

  ngOnInit() {
    this.loginService.getUser().subscribe(u => this.isKanbanMaster = u.inRoleKanbanMaster);
  }

  goToBoard(board: Board): void {
    this.router.navigate(['/board/' + board.id]);
  }

}
