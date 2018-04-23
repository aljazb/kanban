import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/services/api.service';
import {ActivatedRoute} from '@angular/router';
import {Card} from '../../../api/models/Card';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent implements OnInit {

  id: string;
  card: Card;

  constructor(private route: ActivatedRoute,
              private api: ApiService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.card.get(this.id).subscribe(card => this.card = card);
  }

}
