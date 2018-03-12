import { KeycloakService } from 'keycloak-angular';

export function initializer(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: {
            url: 'http://localhost:8081/auth',
            realm: 'Kis',
            clientId: 'frontend-angular-client'
          },
          initOptions: {
            onLoad: 'check-sso',
            checkLoginIframe: false,
            localStorage: true
          },
          bearerExcludedUrls: [
            '/src'
          ]
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}
