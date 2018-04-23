import {BaseEntity} from './base/BaseEntity';
import {Board} from './Board';
import {Card} from './Card';
import {isNullOrUndefined} from 'util';

export class BoardPart extends BaseEntity<BoardPart> {
  name: string;
  maxWip: number;

  orderIndex: number;
  leaf: boolean;

  board: Board;

  parent: BoardPart;
  children: BoardPart[];

  cards: Card[];

  static getLeftLeaf(bp: BoardPart) {
    if (isNullOrUndefined(bp.children) || bp.children.length == 0) {
      return bp;
    } else {
      return this.getLeftLeaf(bp.children[0])
    }
  }

  static getRightLeaf(bp: BoardPart) {
    if (isNullOrUndefined(bp.children) || bp.children.length == 0) {
      return bp;
    } else {
      return this.getRightLeaf(bp.children[bp.children.length - 1])
    }
  }

  static getRelativeChild(root: BoardPart, child: BoardPart, deltaIdx: number) {
    let childIdx = root.children.findIndex(bp => bp === child);
    let newIdx = childIdx + deltaIdx;

    if (newIdx < 0 || newIdx >= root.children.length) {
      if (isNullOrUndefined(root.parent)) {
        let rootBps = root.board.boardParts.sort((a, b) => a.orderIndex - b.orderIndex);
        let rootChildIdx = rootBps.findIndex(bp => bp === root);
        let newRootIdx = rootChildIdx + deltaIdx;

        if (newRootIdx < 0 || newRootIdx >= rootBps.length) {
          return null;
        }

        return rootBps[newRootIdx];
      }

      return this.getRelativeChild(root.parent, root, deltaIdx)
    }

    return root.children[newIdx];
  }

  static getNextBoardPart(bp: BoardPart) {
    if (isNullOrUndefined(bp.parent)) {
      return null;
    }

    return this.getLeftLeaf(this.getRelativeChild(bp.parent, bp, 1));
  }

  static getPreviousBoardPart(bp: BoardPart) {
    if (isNullOrUndefined(bp.parent)) {
      return null;
    }

    return this.getRightLeaf(this.getRelativeChild(bp.parent, bp, -1));
  }
}
