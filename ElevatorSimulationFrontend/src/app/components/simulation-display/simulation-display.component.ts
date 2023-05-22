import { AfterViewInit, Component, ElementRef, HostListener, Input, OnInit, ViewChild } from '@angular/core';
import { DEFAULT_SIMULATION_DATA, SimulationData } from 'src/app/services/models';

@Component({
  selector: 'app-simulation-display',
  template: `
    <canvas class="canvas" #canvas></canvas>
  `,
  styleUrls: ['./simulation-display.component.scss']
})
export class SimulationDisplayComponent implements OnInit{
  
  @ViewChild('canvas', {static: false})
  canvasRef?: ElementRef<HTMLCanvasElement>;
  personIcon: HTMLImageElement = new Image();

  _simulationData: SimulationData = DEFAULT_SIMULATION_DATA;

  @Input()
  set simulationData(value: SimulationData){
    this._simulationData = value;
    this.updateCanvas();
  }

  ngOnInit(): void {
    this.personIcon = new Image();
    this.personIcon.src = "../../../assets/img/user_icon.png"
  }

  get simulationData(): SimulationData{
    return this._simulationData;
  }

  @HostListener("window:resize", ["$event"])
  public onResize(event: any){
    this.updateCanvas();
  }

  readonly ELEVATOR_MAX_WIDTH = 45;
  readonly ELEVATOR_MAX_HEIGTH = 60;
  readonly FLOOR_MAX_HEIGTH = 80;
  readonly MAX_SPACE_BETWEEN_ELEVATORS = 50;
  readonly MARGIN = 10;

  private updateCanvas(): void{
    if(this.canvasRef == undefined) return;
    
    let canvas: HTMLCanvasElement = this.canvasRef.nativeElement;
    canvas.width = canvas.clientWidth;
    canvas.height = canvas.clientHeight;
    let ctx = canvas.getContext('2d')!;
    
    const margin = this.MARGIN;
    const fontSize = 20;
    const floorHeight = Math.min(this.FLOOR_MAX_HEIGTH, (canvas.height - 2 * margin) / this.simulationData.totalFloors);
    const elevatorHeight = (this.ELEVATOR_MAX_HEIGTH / this.FLOOR_MAX_HEIGTH) * floorHeight;

    let elevatorAndNewSpaceWidth = (canvas.width - 2 * margin - 2 * fontSize) / (this._simulationData.elevatorsList.length + 1)

    const elevatorWidth = Math.min(this.ELEVATOR_MAX_WIDTH, (this.ELEVATOR_MAX_WIDTH / this.FLOOR_MAX_HEIGTH) * floorHeight,
    elevatorAndNewSpaceWidth - (elevatorAndNewSpaceWidth / (this.MAX_SPACE_BETWEEN_ELEVATORS + this.ELEVATOR_MAX_WIDTH)) * this.MAX_SPACE_BETWEEN_ELEVATORS); 
    const spacesBetweenElevators = (this.MAX_SPACE_BETWEEN_ELEVATORS / this.ELEVATOR_MAX_WIDTH) * elevatorWidth; 

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.lineWidth = 1;
    ctx.font = `${fontSize}px Arial`;

    for(let i=margin, j = 0; i<(this.simulationData.totalFloors) * floorHeight + margin; i+=floorHeight, j++){
      ctx.moveTo(margin, canvas.height - i);
      ctx.lineTo(canvas.width - margin, canvas.height - i);
      ctx.fillText( " " + j.toString(), margin, canvas.height - i - (floorHeight - fontSize/2 ) / 2);
    }
    let elevatorCount = 0;
    this.simulationData.elevatorsList.forEach(elevatorData=>{
      ctx.fillStyle = "#DC143C";
      let x = margin + 2 * fontSize + (spacesBetweenElevators + elevatorWidth) * (elevatorData.elevatorId - 1);
      let y = canvas.height - (floorHeight * elevatorData.currentFloor + elevatorHeight + margin + (floorHeight - elevatorHeight) / 2);
      ctx.fillRect(x, y, elevatorWidth, elevatorHeight);

      ctx.fillStyle = "black";
      let quotient = Math.floor(elevatorData.elevatorId / 10);
      x += (elevatorWidth - fontSize/2) / (2 * (quotient + 1));
      y += (elevatorHeight + fontSize/2) / 2;
      ctx.fillText(elevatorData.elevatorId.toString(), x, y);
      elevatorCount++;
    })
    
    this.simulationData.floorDataList.forEach(floorData =>{
      let x = margin + elevatorCount * elevatorWidth + spacesBetweenElevators * elevatorCount + 2 * fontSize;
      let y = canvas.height - (margin + floorHeight * floorData.floorNumber + (floorHeight - fontSize/2)/2);
      
      ctx.fillText(floorData.targetFloors.length.toString(), x, y);
      let quotient = Math.floor(floorData.targetFloors.length / 10);

      x += (quotient + 1) * 0.9 * fontSize;
      y -= 0.9* fontSize;
      ctx.drawImage(this.personIcon, x, y, fontSize, fontSize);
    })
    ctx.stroke();
  }
}
