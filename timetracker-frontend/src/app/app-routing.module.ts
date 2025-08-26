import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { AdminGuard } from './core/guards/admin.guard';
import { ManagerGuard } from './core/guards/manager.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'time-tracking',
    loadChildren: () => import('./features/time-tracking/time-tracking.module').then(m => m.TimeTrackingModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'vacation',
    loadChildren: () => import('./features/vacation/vacation.module').then(m => m.VacationModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'reports',
    loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule),
    canActivate: [AuthGuard, ManagerGuard]
  },
  {
    path: 'approvals',
    loadChildren: () => import('./features/approvals/approvals.module').then(m => m.ApprovalsModule),
    canActivate: [AuthGuard, ManagerGuard]
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin.module').then(m => m.AdminModule),
    canActivate: [AuthGuard, AdminGuard]
  },
  {
    path: 'profile',
    loadChildren: () => import('./features/profile/profile.module').then(m => m.ProfileModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'unauthorized',
    loadChildren: () => import('./features/unauthorized/unauthorized.module').then(m => m.UnauthorizedModule)
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: false, // Set to true for debugging
    preloadingStrategy: undefined // Lazy loading
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
