package in.net.kuldeep.model;

import javafx.geometry.Pos;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Car {

    public final long ONE_MILE = 5280; // 1 Mile = 5280 feet
    public final float FRUSTRATING_SPEED_DIFF = 8; // 8 MPH

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
    }

    private int length;
    private float acceleration; // 0 to 60 MPH time
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
            // Check if we can increase speed of change lanes
            Highway.Proximity proximity = highway.findProximity(this);

            if ((proximity.getFront().currentPosition.locationOnHighway - currentPosition.locationOnHighway) >
                    driver.getSafeDistanceFromFrontCar()) {
                // Increase speed
                nextPosition.speed += acceleration;
            }



        }
    }

    private boolean amISlower() {
        return (driver.getPreferredCruisingSpeed() - currentPosition.speed) > FRUSTRATING_SPEED_DIFF;
    }

    public long compareLocationTo(Car toCompare) {
        return (this.currentPosition.locationOnHighway - toCompare.currentPosition.locationOnHighway);
    }
}
