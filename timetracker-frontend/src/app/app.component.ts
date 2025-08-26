import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Time Tracker';
  isLoggedIn = false;
  userProfile: KeycloakProfile | null = null;
  userRoles: string[] = [];

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit() {
    this.isLoggedIn = await this.keycloakService.isLoggedIn();
    
    if (this.isLoggedIn) {
      this.userProfile = await this.keycloakService.loadUserProfile();
      this.userRoles = this.keycloakService.getUserRoles();
    }
  }

  logout() {
    this.keycloakService.logout();
  }

  hasRole(role: string): boolean {
    return this.userRoles.includes(role);
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isManager(): boolean {
    return this.hasRole('MANAGER') || this.hasRole('HR_MANAGER') || this.hasRole('ADMIN');
  }

  getUserName(): string {
    return this.userProfile?.firstName && this.userProfile?.lastName 
      ? `${this.userProfile.firstName} ${this.userProfile.lastName}`
      : this.userProfile?.username || 'User';
  }
}
