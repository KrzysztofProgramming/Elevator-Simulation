import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { AddNewElevatorRequestComponent } from './components/add-new-elevator-request/add-new-elevator-request.component';
import { AddNewClientComponent } from './components/add-new-client-component/add-new-client.component';
import { SimulationDisplayComponent } from './components/simulation-display/simulation-display.component';
import { CreateNewSimulationComponent } from './components/create-new-simulation/create-new-simulation.component'
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ElevatorsViewerComponent } from './components/elevators-viewer/elevators-viewer.component'; 


@NgModule({
  declarations: [
    AppComponent,
    AddNewElevatorRequestComponent,
    AddNewClientComponent,
    SimulationDisplayComponent,
    CreateNewSimulationComponent,
    ElevatorsViewerComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    InputNumberModule,
    ButtonModule,
    DividerModule,
    DropdownModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
