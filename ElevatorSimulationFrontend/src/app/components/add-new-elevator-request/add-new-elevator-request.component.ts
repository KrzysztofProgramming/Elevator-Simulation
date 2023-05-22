import { DEFAULT_SIMULATION_DATA, SimulationData } from './../../services/models';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AddNewElevatorRequestRequest } from 'src/app/services/models';

interface DropboxElement{
  name: string;
  code: number;
}

@Component({
  selector: 'app-add-new-elevator-request',
  template: `
    <p-divider [align]="'left'"><span class="title"> Add new elevator request </span></p-divider>
    <div class="content">
      <p-inputNumber class="content__element" mode="decimal" [useGrouping]="false" [min]="0" [max]="this.simulationData.totalFloors - 1" placeholder="start floor"
       [(ngModel)]="this.startFloor"></p-inputNumber>
      <p-dropdown class="content__element" optionLabel="name" optionValue="code" [options] = "this.directions" [(ngModel)] = "this.direction"></p-dropdown>
      <button pButton class="content__element" [disabled]="!this.isValid() || this.disableControl" (click) = "this.onClick()">Create</button>
    </div>
  `,
  styleUrls: ['./add-new-elevator-request.component.scss']
})
export class AddNewElevatorRequestComponent {
  @Input() simulationData: SimulationData = DEFAULT_SIMULATION_DATA;
  @Input() disableControl: boolean = false;
  @Output() requestCreated = new EventEmitter<AddNewElevatorRequestRequest>();

  directions: DropboxElement[] = [
    {
      name: "UP",
      code: 1
    },
    {
      name: "DOWN",
      code: -1
    }
  ]

  direction: number = 1;
  startFloor: number | null = null;

  public isValid(): boolean{
    return this.direction != 0 && this.startFloor!=null && this.startFloor >= 0;
  }

  public onClick(){
    this.requestCreated.emit(this.buildRequest());
    this.startFloor = null;
  }

  public buildRequest(): AddNewElevatorRequestRequest{
    return {
      direction: this.direction,
      startFloor: this.startFloor!
    };
  }
}
