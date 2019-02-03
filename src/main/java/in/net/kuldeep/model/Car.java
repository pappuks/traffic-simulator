package in.net.kuldeep.model;

import lombok.Data;

@Data
public class Car {

    public final long ONE_MILE = 5280; // 1 Mile = 5280 feet
    public final float FRUSTRATING_SPEED_DIFF_FROM_CRUISING_SPEED = 8; // 8 MPH

    @Data
    public class Position {
        private long locationOnHighway;
        private Lane lane;
        private float speed; // MPH
        private float distanceFromFrontCar; // Feet
        private float distanceFromRearRightCar;
        private float distanceFromRearLeftCar;

        public Position clone() {
            Position retVal = new Position();
            retVal.locationOnHighway = locationOnHighway;
            retVal.lane = lane;
            retVal.speed = speed;
            retVal.distanceFromFrontCar = distanceFromFrontCar;
            retVal.distanceFromRearRightCar = distanceFromRearRightCar;
            retVal.distanceFromRearLeftCar = distanceFromRearLeftCar;
            return retVal;
        }

        public void updateLocationBasedOnSpeed() {
            locationOnHighway += (speed * ONE_MILE) / 3600;
        }
    }

    private int length;
    private float acceleration = 1; // 1 MPH
    private boolean isHovAllowed;
    private Driver driver;
    private Position currentPosition;
    private Position nextPosition;
    private long entryLocationOnHighway;
    private long exitLocationOnHighway;
    private final long EXIT_DISTANCE_THRESHOLD = 2 * ONE_MILE; // 2 Miles

    public void calculateNextPosition(Highway highway) {
        nextPosition = currentPosition.clone();

        boolean needToExit = ((exitLocationOnHighway - currentPosition.locationOnHighway) < EXIT_DISTANCE_THRESHOLD);

        if (!needToExit && amISlower()) {
            // Check if we can increase speed or change lanes
            Highway.Proximity proximity = highway.findProximity(this);
            long distanceFromFrontCar = (proximity.getFront().currentPosition.locationOnHighway - currentPosition.locationOnHighway);

            if (distanceFromFrontCar > driver.getSafeDistanceFromFrontCarToAccelerate()) {
                if (proximity.getFront().currentPosition.speed >= currentPosition.speed) {
                    // Increase speed as we have gap
                    nextPosition.speed += acceleration; // should be 1 mile per hour increase
                }
            } else if (distanceFromFrontCar < driver.getSafeDistanceFromFrontCarForCruising()){
                // Slow down as we don't have much gap
                nextPosition.speed -= 2 * acceleration; // slow down faster
            } else if (distanceFromFrontCar > driver.getSafeDistanceFromFrontCarForLaneChange()) {
                // Try to see if we can change lanes

            }
        }

        nextPosition.updateLocationBasedOnSpeed();
    }

    private boolean canChangeLanes(Highway.Proximity proximity) {
        if (proximity.getLeft() != null) {

        }

    }

    private boolean amISlower() {
        return (driver.getPreferredCruisingSpeed() - currentPosition.speed) > FRUSTRATING_SPEED_DIFF_FROM_CRUISING_SPEED;
    }

    public long compareLocationTo(Car toCompare) {
        return (this.currentPosition.locationOnHighway - toCompare.currentPosition.locationOnHighway);
    }


}
