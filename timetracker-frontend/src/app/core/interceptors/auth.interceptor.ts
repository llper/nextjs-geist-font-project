import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { KeycloakService } from 'keycloak-angular';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private keycloakService: KeycloakService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add authorization header with JWT token if available
    if (this.keycloakService.getToken()) {
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${this.keycloakService.getToken()}`
        }
      });
      return next.handle(authReq);
    }

    return next.handle(req);
  }
}
