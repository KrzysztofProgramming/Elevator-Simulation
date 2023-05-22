
export interface AddNewClientRequest{
  startFloor: number;
  endFloor: number;
  createRequest: boolean;
}

export interface AddNewElevatorRequestRequest{
  startFloor: number;
  direction: number;
}

export interface CreateNewSimulationRequest{
  totalFloors: number;
  numberOfElevators: number;
}

export interface ElevatorData{
  elevatorId: number;
  currentFloor: number;
  numberOfPassengers: number;
  targetFloors: number[]; 
}

export interface FloorData{
  floorNumber: number;
  targetFloors: number[];
}

export interface SimulationData{
  totalFloors: number;
  numberOfElevators: number;
  elevatorsList: ElevatorData[]; 
  floorDataList: FloorData[];
}

export const DEFAULT_SIMULATION_DATA: SimulationData = {
  totalFloors: 1,
  numberOfElevators: 1,
  elevatorsList: [{elevatorId: 1, currentFloor: 1, numberOfPassengers: 0, targetFloors: []}],
  floorDataList: []
}