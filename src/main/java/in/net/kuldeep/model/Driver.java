package in.net.kuldeep.model;

import lombok.Data;

@Data
public class Driver {

    private int secondsToWaitInSlowTrafficBeforeLaneChange;
    private float preferredCruisingSpeed; // MPH
    private int safeDistanceFromFrontCar; // Feet
    private int safeGapWithRearCarForLaneChange;


}
