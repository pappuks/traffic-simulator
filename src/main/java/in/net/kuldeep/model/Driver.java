package in.net.kuldeep.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Driver {

    private int secondsToWaitInSlowTrafficBeforeLaneChange;
    private float preferredCruisingSpeed; // MPH
    private long safeDistanceFromFrontCarToAccelerate; // Feet
    private long safeDistanceFromFrontCarForLaneChange; // Will be less than acceleration distance
    private long safeDistanceFromFrontCarForFollowing; // Will be less than Lane Change distance
    private long safeGapWithRearCarForLaneChange;

    public static Driver slowSafeDriver() {
        return new Driver(
                30,
                60,
                100,
                50,
                30,
                30);
    }

    public static Driver safeDriver() {
        return new Driver(
                20,
                68,
                70,
                30,
                20,
                20
        );
    }

    public static Driver fastSafeDriver() {
        return new Driver(
                15,
                72,
                50,
                20,
                15,
                15
        );
    }

    public static Driver aggressiveDriver() {
        return new Driver(
                5,
                77,
                30,
                20,
                10,
                10
        );
    }



}
