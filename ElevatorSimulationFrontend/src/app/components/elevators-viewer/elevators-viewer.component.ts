import { Component, Input } from '@angular/core';
import { elementAt } from 'rxjs';
import { SimulationData, DEFAULT_SIMULATION_DATA } from 'src/app/services/models';

@Component({
  selector: 'app-elevators-viewer',
  template: `
    <p-divider [align]="'left'"><span class="title"> Elevators info </span></p-divider>
    <div class="row row--titles">
        <div class="row__cell row__cell-id">Id</div>
        <div class="row__cell row__cell-passengers">Different Passengers</div>
        <div class="row__cell row__cell-floor">Next Floor</div>
      </div>
    <div class="content">
    <div class="wrapper">
      <div class="row" *ngFor="let elevator of this.simulationData.elevatorsList">
      <div class="row__cell row__cell-id">{{elevator.elevatorId}}</div>
      <div class="row__cell row__cell-passengers">{{elevator.numberOfPassengers}}</div>
      <div class="row__cell row__cell-floor">{{elevator.targetFloors.length != 0 ? elevator.targetFloors.at(0) : 'none'}}</div>
      </div>
    </div>
  </div>
  `,
  styleUrls: ['./elevators-viewer.component.scss']
})
export class ElevatorsViewerComponent {
  @Input() simulationData: SimulationData = DEFAULT_SIMULATION_DATA;

  public elo(){
    this.simulationData.elevatorsList.at(0)?.targetFloors.at(0)
  }
}
