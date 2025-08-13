import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { Coordinate } from '../../core/models/models';

@Component({
  selector: 'app-map',
  imports: [],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements AfterViewInit{
  @Input() backgroundSrc!: string;
  @Input() logs: Coordinate[] = [];
  @ViewChild('canvas', {static: false}) canvas!: ElementRef<HTMLCanvasElement>;
  ctx!: CanvasRenderingContext2D;
  
  mapDiffStartPoint = 1;
  mapCellNumber = 41;
  backgroundWidth = 800;
  backgroundHeight = 800;
  iconWidth = 30;
  iconHeight = 30;

  ngAfterViewInit(): void {
    if (!this.canvas) {
      console.error('Canvas not found!');
      return;
    }
    const canvasEl = this.canvas.nativeElement;
    canvasEl.width = this.backgroundWidth;
    canvasEl.height = this.backgroundHeight;

    const ctx = canvasEl.getContext('2d');
    if (!ctx) {
      console.error("Canvas 2D context not available");
      return;
    }
    this.ctx = ctx;
    // Scale all logs
    this.logs.forEach(log=> this.scaleCoordinate(log));
    // Draw map with scaled logs
    this.onDrawMap(canvasEl);
  }

  onDrawMap(canvasEl: HTMLCanvasElement) {
    const background = new Image();
    background.src = this.backgroundSrc;
    background.onload = () => {
        this.ctx.drawImage(background, 0, 0, this.backgroundWidth, this.backgroundHeight); // Draw background
        for (let step = 0; step < this.logs.length; step++) { // Draw coordinates
          this.ctx.strokeStyle = "purple";
          this.ctx.font = "bold 16px serif";
          this.ctx.fillStyle = "#ff0000";
          this.ctx.lineWidth = 3;
          this.ctx.beginPath();

          const currentLog = this.logs[step];
          this.ctx.moveTo(currentLog.scaledX, currentLog.scaledY);
          
          const icon = new Image();
          let iconSrc = currentLog.teleport == true? "tp.png" : "x_mark.png"
          icon.src = "/images/" + iconSrc;
          icon.onload = () => {
            const iconWidthCenter = this.iconWidth / 2;
            const iconHeightCenter = this.iconHeight / 2;
            const iconX = currentLog.scaledX - iconWidthCenter;
            const iconY = currentLog.scaledY - iconHeightCenter;
            this.ctx.drawImage(icon, iconX, iconY, this.iconWidth, this.iconHeight);
            
            var matches = currentLog.name.match(/\b(\w)/g);
            var acronym = matches?.join('')  + `(${currentLog.x}, ${currentLog.y})`;
            this.drawOutlinedText(acronym?? "Unnamed", iconX + iconWidthCenter, iconY);
          }

          // Draw line between two points on the map
          if (step !== this.logs.length - 1 && this.logs[step + 1].teleport === false) {
            var nextLog = this.logs[step + 1];
            this.ctx.lineTo(nextLog.scaledX, nextLog.scaledY);
          }
          this.ctx.stroke();
        }
        
        // Mouse listener on mouse hover event
        canvasEl.addEventListener("mousemove", (e) => {
            this.onShowTooltip(e, canvasEl);
        });
    }

  }

  // Scale point to the map image
  scaleCoordinate(coordinate: Coordinate) {
    var gameX = coordinate.x - this.mapDiffStartPoint;
    var gameY = coordinate.y - this.mapDiffStartPoint;

    var cellHeight = this.backgroundHeight / this.mapCellNumber;
    var cellWidth = this.backgroundWidth / this.mapCellNumber;

    coordinate.scaledY = cellHeight * gameY;
    coordinate.scaledX = cellWidth * gameX;
  }

  drawOutlinedText(text: string, x: number, y: number) {
    const fontSize = 16;
    const fontFace = "verdana";
    this.ctx.font = `${fontSize}px ${fontFace}`;

    this.ctx.textAlign = "center";
    this.ctx.textBaseline = "middle";
    this.ctx.strokeStyle = "black";
    this.ctx.strokeText(text, x, y);
    this.ctx.fillStyle = "white";
    this.ctx.fillText(text, x, y);
  }

  onShowTooltip(event: Event, canvasEl: HTMLCanvasElement) {
    const e = event as MouseEvent;
    e.preventDefault();
    e.stopPropagation();
    canvasEl.title = "";

    const bounds = canvasEl.getBoundingClientRect();
    const currentX = e.clientX - bounds.left;
    const currentY = e.clientY - bounds.top;
    const margin = 7;

    for(let log of this.logs){
      if (Math.abs(log.scaledX - currentX) < margin && Math.abs(log.scaledY - currentY) < margin) {
        canvasEl.title = `${log.name} (${log.x}, ${log.y})`;
      }
    }
  }
}
