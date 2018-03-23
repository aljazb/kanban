// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

import {environmentLocal} from './environment-local';

export const environment = {
  production: false,
  debugMode: true,
  apiHostname: environmentLocal.apiHostname, // http://localhost:8080   http://kamban.smrpo7:8080
  authHostname: environmentLocal.authHostname // http://localhost:8081  http://kamban.smrpo7:8081
};
