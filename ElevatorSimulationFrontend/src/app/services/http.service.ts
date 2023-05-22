import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AddNewClientRequest, AddNewElevatorRequestRequest, CreateNewSimulationRequest, SimulationData } from './models';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  readonly URL: string = "";

  constructor(private httpClient: HttpClient) { }

  public getSimulationData(): Observable<SimulationData>{
    return this.httpClient.get<SimulationData>(`${this.URL}/getSimulationData`);
  }

  public addNewClient(requestBody: AddNewClientRequest): Observable<SimulationData>{
    return this.httpClient.post<SimulationData>(`${this.URL}/addNewClient`, requestBody)
  }

  public addNewElevatorRequest(elevatorRequest: AddNewElevatorRequestRequest): Observable<SimulationData>{
    return this.httpClient.post<SimulationData>(`${this.URL}/addNewElevatorRequest`, elevatorRequest);
  }

  public createNewSimulation(simulationRequest: CreateNewSimulationRequest): Observable<SimulationData>{
    return this.httpClient.post<SimulationData>(`${this.URL}/createNewSimulation`, simulationRequest);
  }

  public runSimulationStep(): Observable<SimulationData>{
    return this.httpClient.post<SimulationData>(`${this.URL}/runSimulationStep`, {});
  }
}
