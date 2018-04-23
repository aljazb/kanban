import { TestBed, inject } from '@angular/core/testing';

import { LocalBoardsService } from './local-boards.service';

describe('LocalBoardsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LocalBoardsService]
    });
  });

  it('should be created', inject([LocalBoardsService], (service: LocalBoardsService) => {
    expect(service).toBeTruthy();
  }));
});
