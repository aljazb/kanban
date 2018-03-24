import { KeycloakService } from 'keycloak-angular';
import {environment} from '../../../environments/environment';

export let ROLE_ADMINISTRATOR: string = "ADMINISTRATOR";
export let ROLE_PRODUCT_OWNER: string = "PRODUCT_OWNER";
export let ROLE_KANBAN_MASTER: string = "KANBAN_MASTER";
export let ROLE_DEVELOPER: string = "DEVELOPER";
export let ROLE_USER: string = "USER";


export function KeycloakInitializer(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: {
            url: environment.authHostname + '/auth',
            realm: 'Kis',
            clientId: 'frontend-angular-client'
          },
          initOptions: {
            onLoad: 'check-sso',
            checkLoginIframe: false,
            localStorage: true
          },
          bearerExcludedUrls: [
            '/^(?!\/api).*/'
          ]
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}
