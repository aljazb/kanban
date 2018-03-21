import { KeycloakService } from 'keycloak-angular';
import {environment} from '../../../environments/environment';

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
