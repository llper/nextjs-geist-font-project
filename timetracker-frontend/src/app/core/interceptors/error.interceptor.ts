import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { KeycloakService } from 'keycloak-angular';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(
    private snackBar: MatSnackBar,
    private keycloakService: KeycloakService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'Si è verificato un errore';

        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Errore: ${error.error.message}`;
        } else {
          // Server-side error
          switch (error.status) {
            case 400:
              errorMessage = 'Richiesta non valida';
              break;
            case 401:
              errorMessage = 'Non autorizzato';
              this.keycloakService.login();
              break;
            case 403:
              errorMessage = 'Accesso negato';
              break;
            case 404:
              errorMessage = 'Risorsa non trovata';
              break;
            case 500:
              errorMessage = 'Errore interno del server';
              break;
            default:
              errorMessage = `Errore ${error.status}: ${error.message}`;
          }
        }

        // Show error message to user
        this.snackBar.open(errorMessage, 'Chiudi', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });

        return throwError(() => error);
      })
    );
  }
}
