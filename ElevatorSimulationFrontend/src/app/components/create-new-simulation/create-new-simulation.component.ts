import { Component, EventEmitter, Input, NgModule, Output } from '@angular/core';
import { CreateNewSimulationRequest } from 'src/app/services/models';

@Component({
  selector: 'app-create-new-simulation',
  template: `
  <p-divider [align]="'left'"><span class="title"> Create new simulation </span></p-divider>
    <div class="content">
      <p-inputNumber class="content__element" mode="decimal" [useGrouping]="false" [min]="1" [max]="100" placeholder="number of floors" [(ngModel)] = "this.numberOfFloors"></p-inputNumber>
      <p-inputNumber class="content__element" mode="decimal" [useGrouping]="false" [min]="1" [max]="20" placeholder="number of elevators" [(ngModel)] = "this.numberOfElevators"></p-inputNumber>
      <button pButton class="content__element" [disabled]="!this.isValid() || this.disableControl" (click)="this.onClick()">Create</button>
    </div>
  `,
  styleUrls: ['./create-new-simulation.component.scss']
})
export class CreateNewSimulationComponent {
  @Input() disableControl: boolean = false;
  @Output() newSimulationCreated = new EventEmitter<CreateNewSimulationRequest>;

  numberOfFloors: number | null = null;
  numberOfElevators: number | null = null;

  public isValid(): boolean{
    return this.numberOfFloors != null && this.numberOfElevators != null;
  }

  public onClick(){
    this.newSimulationCreated.emit(this.buildSimulationRequest())
    this.numberOfFloors = null;
    this.numberOfElevators = null;
  }

  public buildSimulationRequest(): CreateNewSimulationRequest{
    return {
      numberOfElevators: this.numberOfElevators!,
      totalFloors: this.numberOfFloors!
    }
  }
}
