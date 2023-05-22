import { AfterViewInit, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpService } from './services/http.service';
import { AddNewClientRequest, AddNewElevatorRequestRequest, CreateNewSimulationRequest, DEFAULT_SIMULATION_DATA, SimulationData } from './services/models';

@Component({
  selector: 'app-root',
  template: `
  <div class="content">
    <app-simulation-display [simulationData]="this.simulationData"></app-simulation-display>
    <div class="controls-panel">
      <app-add-new-elevator-request [simulationData]="this.simulationData" (requestCreated)="this.onNewElevatorRequest($event)" [disableControl]="this.waitingForResponse" class="single-control"></app-add-new-elevator-request>
      <app-add-new-client [simulationData]="this.simulationData" (clientCreated)="this.onNewClientCreated($event)" [disableControl]="this.waitingForResponse" class="single-control"></app-add-new-client>
      <app-create-new-simulation (newSimulationCreated)="this.onNewSimulationCreated($event)" [disableControl]="this.waitingForResponse" class="single-control"></app-create-new-simulation>
      <app-elevators-viewer [simulationData]="this.simulationData" class="elevators-viewer"></app-elevators-viewer>
      <div class="bottom-controls">
        <div class="bottom-controls__header">Run simulation step</div>
        <button pButton class="bottom-controls__run-button" [disabled]="this.waitingForResponse" (click) = "this.runSimulationStep()">>>></button>
      </div>
    </div>
  </div>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterViewInit{
  title = 'ElevatorSimulationFrontend';
  waitingForResponse: boolean = true;
  simulationData: SimulationData = DEFAULT_SIMULATION_DATA;

  constructor(private httpService: HttpService, private cd: ChangeDetectorRef){}


  ngAfterViewInit(): void {
    this.waitingForResponse = true;
    this.httpService.getSimulationData().subscribe({
      next: this.updateSimulationData.bind(this),
      complete: () => this.waitingForResponse = false
    });
  }

  public runSimulationStep(){
    this.waitingForResponse = true;
    this.httpService.runSimulationStep().subscribe({
      next: this.updateSimulationData.bind(this),
      complete: () => this.waitingForResponse = false
    });
  }  

  public onNewElevatorRequest(request: AddNewElevatorRequestRequest){
    this.waitingForResponse = true;
    this.httpService.addNewElevatorRequest(request).subscribe({
      next: this.updateSimulationData.bind(this),
      complete: () => this.waitingForResponse = false
    });
  }

  public onNewClientCreated(request: AddNewClientRequest){
    this.waitingForResponse = true;
    this.httpService.addNewClient(request).subscribe({
      next: this.updateSimulationData.bind(this),
      complete: () => this.waitingForResponse = false
    });
  }

  public onNewSimulationCreated(request: CreateNewSimulationRequest){
    this.waitingForResponse = true;
    this.httpService.createNewSimulation(request).subscribe({
      next: this.updateSimulationData.bind(this),
      complete: () => this.waitingForResponse = false
    });
  }

  private updateSimulationData(simulationData: SimulationData){
    this.simulationData = simulationData;
    this.cd.markForCheck();
  }
}
