import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {Board} from '../../../api/models/Board';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {

  }

  goToBoard(board: Board): void {
    this.router.navigate(['/board/' + board.id]);
  }

}
