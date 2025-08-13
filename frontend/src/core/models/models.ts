export class Coordinate {
    name: string = "";
    areaName: string = "";
    imgSrc: string = "";
    teleport: boolean = false;
    x: number = 0;
    y: number = 0;
    scaledX: number = 0;
    scaledY: number = 0;
} 

export class Area {
    name: string = "";
    imgSrc: string = "";
} 

export class TspSolutionResponse {
    area: Area = new Area;
    solution: Coordinate[] = [];
}