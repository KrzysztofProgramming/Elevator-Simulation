import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AddNewClientRequest, DEFAULT_SIMULATION_DATA, SimulationData } from 'src/app/services/models';

@Component({
  selector: 'app-add-new-client',
  template: `
    <p-divider [align]="'left'"><span class="title"> Add new elevator client </span></p-divider>
    <div class="content">
      <p-inputNumber class="content__element content__number-input" mode="decimal" [useGrouping]="false" [min]="0" [max]="this.simulationData.totalFloors - 1"
       placeholder="start floor" [(ngModel)]  = "this.startFloor"></p-inputNumber>

      <p-inputNumber class="content__element content__number-input" mode="decimal" [useGrouping]="false" [min]="0" [max]="this.simulationData.totalFloors - 1"
       placeholder="target floor" [(ngModel)] = "this.targetFloor"></p-inputNumber>
      <button pButton class="content__element" [disabled]="!this.isValid() || this.disableControl" (click) = "this.onClick()">Add</button>
    </div>
  `,
  styleUrls: ['./add-new-client.component.scss']
})
export class AddNewClientComponent {
  @Input() simulationData: SimulationData = DEFAULT_SIMULATION_DATA;
  @Input() disableControl: boolean = false;
  @Output() clientCreated = new EventEmitter<AddNewClientRequest>;

  startFloor: number | null = null;
  targetFloor: number | null = null; 

  public isValid(): boolean{
    return this.startFloor != null && this.targetFloor != null && this.startFloor != this.targetFloor;
  }

  public onClick(){
    this.clientCreated.emit(this.buildClient());
    this.startFloor = null;
    this.targetFloor = null;
  }

  public buildClient(): AddNewClientRequest{
    return {
      startFloor: this.startFloor!,
      endFloor: this.targetFloor!,
      createRequest: true
    };
  }

}
