import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { KeycloakInitializer } from './api/keycloak/keycloak-init';
//import { KeycloakAuthGuardService } from './utils/app-auth-guard.service';

import { HttpClientInMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService }  from './demo/in-memory-data.service';

import { AppRoutingModule }     from './app-routing.module';

import { AppComponent }         from './app.component';
import { DashboardComponent }   from './view/route/dashboard/dashboard.component';
import { HeroDetailComponent }  from './view/route/hero-detail/hero-detail.component';
import { HeroesComponent }      from './view/route/heroes/heroes.component';
import { HeroSearchComponent }  from './view/components/hero-search/hero-search.component';
import { HeroService }          from './demo/hero.service';
import { MessageService }       from './demo/message.service';
import { MessagesComponent }    from './view/components/messages/messages.component';
import { AppHeaderComponent }   from './view/base/header/header.component';
import {ApiService} from './api/Api';
import {KeycloakAuthGuardService} from './api/keycloak/keycloak-auth-guard.service';
import { FooterComponent } from './view/base/footer/footer.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    KeycloakAngularModule,

    // The HttpClientInMemoryWebApiModule module intercepts HTTP requests
    // and returns simulated server responses.
    // Remove it when a real server is ready to receive requests.
    HttpClientInMemoryWebApiModule.forRoot(
      InMemoryDataService, { dataEncapsulation: false }
    )
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    HeroesComponent,
    HeroDetailComponent,
    MessagesComponent,
    HeroSearchComponent,
    AppHeaderComponent,
    FooterComponent
  ],
  providers: [
    HeroService,
    MessageService,
    KeycloakAuthGuardService,
    ApiService,
    {
      provide: APP_INITIALIZER,
      useFactory: KeycloakInitializer,
      multi: true,
      deps: [KeycloakService]
    }
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
