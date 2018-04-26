export class BoardEvent {

  type: BoardEventType;
  value: any;

  constructor(type: BoardEventType, value: any) {
    this.type = type;
    this.value = value;
  }
}

export enum BoardEventType {
  EVENT_ADD_RIGHT,
  EVENT_ADD_LEFT,
  EVENT_DELETE,
  EVENT_HAS_CARDS,
  EVENT_STRUCTURE_CHANGED
}
