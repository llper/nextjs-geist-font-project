export const environment = {
  production: true,
  apiUrl: 'http://timetracker-backend-service:8080/api',
  keycloak: {
    url: 'http://idp.localhost',
    realm: 'tt-realm',
    clientId: 'PEAX-agent'
  }
};
