import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}

  async canActivate(): Promise<boolean | UrlTree> {
    try {
      const isLoggedIn = await this.keycloakService.isLoggedIn();
      
      if (!isLoggedIn) {
        return this.router.createUrlTree(['/unauthorized']);
      }

      const userRoles = this.keycloakService.getUserRoles();
      const hasAdminRole = userRoles.includes('ADMIN');

      if (!hasAdminRole) {
        return this.router.createUrlTree(['/unauthorized']);
      }

      return true;
    } catch (error) {
      console.error('Error checking admin permissions:', error);
      return this.router.createUrlTree(['/unauthorized']);
    }
  }
}
