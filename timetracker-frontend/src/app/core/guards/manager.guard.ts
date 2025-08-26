import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class ManagerGuard implements CanActivate {

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
      const hasManagerRole = userRoles.some(role => 
        ['ADMIN', 'MANAGER', 'HR_MANAGER'].includes(role)
      );

      if (!hasManagerRole) {
        return this.router.createUrlTree(['/unauthorized']);
      }

      return true;
    } catch (error) {
      console.error('Error checking manager permissions:', error);
      return this.router.createUrlTree(['/unauthorized']);
    }
  }
}
