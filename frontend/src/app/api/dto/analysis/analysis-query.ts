import {Project} from '../../models/Project';

export class AnalysisQuery {

    project: Project;

    createdFrom: number;
    createdTo: number;

    finishedFrom: number;
    finishedTo: number;

    devStartFrom: number;
    devStartTo: number;

    workloadFrom: number;
    workloadTo: number;

    silverBullet: boolean;
    rejected: boolean;
    newFunctionality: boolean;

}
