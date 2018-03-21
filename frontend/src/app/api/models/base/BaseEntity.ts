export abstract class BaseEntity<T extends BaseEntity<T>>{
  id: string;
  version: number;
  isDeleted: boolean;
  createdOn: number;
  editedOn: number;
}
