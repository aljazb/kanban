import {BaseEntity} from './base/BaseEntity';
import {DevTeam} from './DevTeam';
import {BoardPart} from './BoardPart';
import {BoardLane} from './BoardLane';

export class Board extends BaseEntity<Board> {
  name: string;
  highestPriority: number;
  startDev: number;
  endDev: number;
  acceptanceTesting: number;

  devTeam: DevTeam;
  boardParts: BoardPart[];
  boardLanes: BoardLane[];
}
