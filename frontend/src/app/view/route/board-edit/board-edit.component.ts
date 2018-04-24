import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {ActivatedRoute, Router} from '@angular/router';
import {LocalBoardsService} from '../../../services/local-boards/local-boards.service';
import {ApiService} from '../../../api/services/api.service';
import {ToasterService} from 'angular5-toaster/dist';

@Component({
  selector: 'app-board-edit',
  templateUrl: './board-edit.component.html',
  styleUrls: ['./board-edit.component.css']
})
export class BoardEditComponent implements OnInit {

  @ViewChild(BoardBaseFormComponent)
  boardBaseFormComp;

  selectedBoard: Board;

  constructor(private api: ApiService,
              private router: Router,
              private route: ActivatedRoute,
              private localBoards: LocalBoardsService,
              private toaster: ToasterService) { }

  ngOnInit() {
    let id = this.route.snapshot.fragment;
    this.selectedBoard = this.localBoards.getBoards().find(b => b.id == id);
  }

  create(): void {
    if(this.boardBaseFormComp.isValid()) {
      this.api.board.post(this.selectedBoard).subscribe(board => {
        this.toaster.pop("success", `Board ${this.selectedBoard.name} was created`);
        this.localBoards.boardDeleteRef(this.selectedBoard);
        this.selectedBoard = null;
        this.router.navigate(['/board/' + board.id]);
      }, error2 => {
        this.toaster.pop("error", `Could not create board ${this.selectedBoard.name}`);
      });
    }
  }

  back(): void {
    this.selectedBoard = null;
    this.localBoards.persist();
  }

  selectBoard(board: Board): void {
    this.selectedBoard = board;
  }

  newBoard(): void {
    this.selectedBoard = this.localBoards.newBoard();
    this.toaster.pop("success", `New board was created`);
  }

  boardDelete(index: number): void {
    this.localBoards.boardDelete(index);
    this.toaster.pop("success", `Board was deleted created`);
  }

  get boards(): Board[] {
    return this.localBoards.getBoards();
  }



}
