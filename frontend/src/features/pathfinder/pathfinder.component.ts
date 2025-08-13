import { Component } from '@angular/core';

import { NgFor, NgIf } from '@angular/common';
import { Area, Coordinate } from '../../core/models/models';
import { PathfinderService } from '../../core/service/pathfinder.service';
import { KeyValuePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MapComponent } from '../map/map.component';

@Component({
  selector: 'app-pathfinder',
  imports: [NgFor, NgIf, KeyValuePipe, FormsModule, MapComponent],
  templateUrl: './pathfinder.component.html',
  styleUrl: './pathfinder.component.css'
})

export class PathfinderComponent {
  userInput: String = ""; // Text field value, for in-game chat logs
  status: String = ""; // Parsing, calculating path, errors status
  
  logs: Coordinate[] = []; // All logs (ordered and not ordered)
  newLogs: Coordinate[] = []; // Parsed, not ordered
  uncheckedLogs: Coordinate[] = []; // Candidates to be removed from all logs
  
  calculatedPath: Map<Area, Coordinate[]> = new Map(); // Tsp solution

  constructor(private pathfinderService: PathfinderService) {}

  onParseLogs(input: string) {
    this.status = "Parsing logs...";
    this.pathfinderService.parseLogs(input).subscribe({
      next: logs => {
        this.status = "Logs are parsed.";
        this.newLogs.push(...logs);
        this.logs.push(...logs);
      },
      error: err => {
        //console.error('Error parsing logs:', err);
        this.status = `Error: ${err.message}`;
      }
    });
  }

  onCalculatePath() {
    this.status = "Calculating path...";
    // Remove all unchecked logs
    this.logs = this.logs.filter((log)=> !this.uncheckedLogs.some(
      unchecked => this.getLogInfo(unchecked) === this.getLogInfo(log)
    ));

    this.pathfinderService.calculatePath(this.logs).subscribe({
      next: path=>{
      this.status = "Path is calculated.";
      this.newLogs = [];
      this.uncheckedLogs = [];
      this.calculatedPath = new Map();
      path.forEach(item => this.calculatedPath.set(item.area, item.solution));
      },
      error: err => {
        //console.error('Error parsing logs:', err);
        this.status = `Error: ${err.message}`;
      }    
    });
  }

  getLogInfo(log: Coordinate): string {
    return log.name + log.areaName + log.x + log.y;
  }

  // On toggling checkboxes of logs
  onToggleLog(log: Coordinate) {
    var index = this.uncheckedLogs.indexOf(log, 0);
    if (index > -1) {
      this.uncheckedLogs.splice(index, 1);
    } else {
      this.uncheckedLogs.push(log);
    }
  }

  // On copypaste to the text field
  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedText = event.clipboardData?.getData('text') ?? '';
    if(pastedText){
      this.onParseLogs(pastedText);
    }
  }

  onReset(){
    this.status = "";
    this.userInput = "";
    this.newLogs = [];
    this.uncheckedLogs = [];
    this.logs = [];
    this.calculatedPath = new Map();
  }
}
