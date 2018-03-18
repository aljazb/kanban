import {BaseEntity} from './base/BaseEntity';
import {DevTeam} from './DevTeam';
import {BoardPart} from './BoardPart';
import {BoardLane} from './BoardLane';

export class Board extends BaseEntity<Board> {
  name: string;

  devTeam: DevTeam;
  boardParts: BoardPart[];
  boardLanes: BoardLane[];
}
