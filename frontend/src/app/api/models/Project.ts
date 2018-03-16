import {BaseEntity} from './base/BaseEntity';
import {FlowTable} from './FlowTable';
import {DevTeam} from './DevTeam';

export class Project extends BaseEntity<Project> {
  name: string;
  description: string;

  flowTable: FlowTable;
  devTeam: DevTeam;

}
