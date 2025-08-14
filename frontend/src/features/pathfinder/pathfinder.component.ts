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
  uncheckedLogs: Coordinate[] = []; // Candidates to be removed from all logs
  
  calculatedPath: Map<Area, Coordinate[]> = new Map(); // Tsp solution

  constructor(private pathfinderService: PathfinderService) {}

  async onCalculatePath() {
    try{
      // Parse logs if use input isn't empty
      if(this.userInput){
        this.status = "Parsing logs...";
        var input = this.userInput;
        this.userInput = "";
        const logs = await this.pathfinderService.parseLogs(input);
        this.status = "Logs are parsed.";
        this.logs.push(...logs);         
      }

      // Remove all unchecked logs
      this.logs = this.logs.filter((log)=> !this.uncheckedLogs.some(
        unchecked => this.getLogInfo(unchecked) === this.getLogInfo(log)
      ));

      // Calculate path
      this.status = "Calculating path...";
      const tspResponse = await this.pathfinderService.calculatePath(this.logs);
      this.status = "Path is calculated.";
      this.uncheckedLogs = [];
      this.calculatedPath = new Map();
      tspResponse.forEach(response => this.calculatedPath.set(response.area, response.solution));  
    } catch(error: any){
      this.status = `Error: ${error.message}`;
    }
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

  onReset(){
    this.status = "";
    this.userInput = "";
    this.uncheckedLogs = [];
    this.logs = [];
    this.calculatedPath = new Map();
  }
}
