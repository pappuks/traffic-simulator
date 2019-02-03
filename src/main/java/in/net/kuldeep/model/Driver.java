package in.net.kuldeep.model;

import lombok.Data;

@Data
public class Driver {

    private int secondsToWaitInSlowTrafficBeforeLaneChange;
    private float preferredCruisingSpeed; // MPH
    private long safeDistanceFromFrontCarToAccelerate; // Feet
    private long safeDistanceFromFrontCarForLaneChange;
    private long safeDistanceFromFrontCarForCruising;
    private long safeGapWithRearCarForLaneChange;


}
