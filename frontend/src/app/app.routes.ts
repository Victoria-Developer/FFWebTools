import { Routes } from '@angular/router';
import { PathfinderComponent } from '../features/pathfinder/pathfinder.component';
import { HomeComponent } from '../features/home/home.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'route', component: PathfinderComponent },
];
