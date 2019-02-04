package in.net.kuldeep.model;

import lombok.Data;

@Data
public class Car {

    public final long ONE_MILE = 5280; // 1 Mile = 5280 feet
    public final float FRUSTRATING_SPEED_DIFF_FROM_CRUISING_SPEED = 5; // 5 MPH

    @Data
    public class Position {
        private long locationOnHighway;
        private Lane lane;
        private float speed; // MPH
        private float distanceFromFrontCar; // Feet
        private float distanceFromRearRightCar;
        private float distanceFromRearLeftCar;

        public Position() {}

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

    enum LaneChange {
        Left,
        Right,
        NoChange
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

    public void setStartPosition(Lane lane, float speed) {
        currentPosition = new Position();
        currentPosition.lane = lane;
        currentPosition.locationOnHighway = 0;
        currentPosition.speed = speed;
    }

    public void makeNextPositionAsCurrent() {
        if (needToExit()) {
            // For now just remove the car from the highway
            currentPosition.lane.removeCar(this);
            return;
        }

        if (nextPosition.lane.getNumberOnHighway() != currentPosition.lane.getNumberOnHighway()) {
            currentPosition.lane.removeCar(this);
            currentPosition = nextPosition.clone();
            currentPosition.lane.addCar(this);
        } else {
            currentPosition = nextPosition.clone();
        }
    }

    public void calculateNextPosition(Highway highway) {
        nextPosition = currentPosition.clone();

        if (!needToExit() && amISlower()) {
            // Check if we can increase speed or change lanes
            Highway.Proximity proximity = highway.findProximity(this);
            if (proximity.getFront() != null) {
                long distanceFromFrontCar = (proximity.getFront().currentPosition.locationOnHighway - currentPosition.locationOnHighway);

                if (distanceFromFrontCar > driver.getSafeDistanceFromFrontCarToAccelerate()) {
                    if (proximity.getFront().currentPosition.speed >= currentPosition.speed) {
                        // Increase speed as we have gap
                        nextPosition.speed += acceleration; // should be 1 mile per hour increase
                    }
                } else if (distanceFromFrontCar < driver.getSafeDistanceFromFrontCarForFollowing()) {
                    // Slow down as we don't have much gap
                    nextPosition.speed -= 2 * acceleration; // slow down faster
                } else if (distanceFromFrontCar > driver.getSafeDistanceFromFrontCarForLaneChange()) {
                    // Try to see if we can change lanes
                    switch (canChangeLanes(proximity)) {
                        case Left:
                            nextPosition.lane = highway.getLeftLane(nextPosition.lane);
                            break;
                        case Right:
                            nextPosition.lane = highway.getRightLane(nextPosition.lane);
                            break;
                        case NoChange:
                        default:
                            break;
                    }
                }
            } else {
                nextPosition.speed += acceleration;
            }
        }

        nextPosition.updateLocationBasedOnSpeed();
        // Change lanes when switching from next position to current position
    }

    private LaneChange canChangeLanes(Highway.Proximity proximity) {
        if (proximity.getLeft() != null) {
            Car leftCar = proximity.getLeft();
            if (leftCar.currentPosition.locationOnHighway > currentPosition.locationOnHighway) {
                if ((leftCar.currentPosition.locationOnHighway - currentPosition.locationOnHighway) >
                        driver.getSafeDistanceFromFrontCarForLaneChange()) {
                    if (proximity.getLeftBack() != null) {
                        if ((currentPosition.locationOnHighway - proximity.getLeftBack().currentPosition.locationOnHighway) >
                                driver.getSafeGapWithRearCarForLaneChange()) {
                            return LaneChange.Left;
                        }
                    } else {
                        return LaneChange.Left;
                    }
                }
            } else {
                if ((currentPosition.locationOnHighway - leftCar.currentPosition.locationOnHighway) >
                        driver.getSafeGapWithRearCarForLaneChange()) {
                    if (proximity.getLeftFront() != null) {
                        if ((proximity.getLeftFront().currentPosition.locationOnHighway - currentPosition.locationOnHighway) >
                                driver.getSafeDistanceFromFrontCarForLaneChange()) {
                            return LaneChange.Left;
                        }
                    } else {
                        return LaneChange.Left;
                    }
                }
            }
        } else if (proximity.getRight() != null) {
            Car rightCar = proximity.getRight();
            if (rightCar.currentPosition.locationOnHighway > currentPosition.locationOnHighway) {
                if ((rightCar.currentPosition.locationOnHighway - currentPosition.locationOnHighway) >
                        driver.getSafeDistanceFromFrontCarForLaneChange()) {
                    if (proximity.getRightBack() != null) {
                        if ((currentPosition.locationOnHighway - proximity.getRightBack().currentPosition.locationOnHighway) >
                                driver.getSafeGapWithRearCarForLaneChange()) {
                            return LaneChange.Right;
                        }
                    } else {
                        return LaneChange.Right;
                    }
                }
            } else {
                if ((currentPosition.locationOnHighway - rightCar.currentPosition.locationOnHighway) >
                        driver.getSafeGapWithRearCarForLaneChange()) {
                    if (proximity.getRightFront() != null) {
                        if ((proximity.getRightFront().currentPosition.locationOnHighway - currentPosition.locationOnHighway) >
                                driver.getSafeDistanceFromFrontCarForLaneChange()) {
                            return LaneChange.Right;
                        }
                    } else {
                        return LaneChange.Right;
                    }
                }
            }
        }

        return LaneChange.NoChange;
    }

    private boolean amISlower() {
        return (driver.getPreferredCruisingSpeed() - currentPosition.speed) > FRUSTRATING_SPEED_DIFF_FROM_CRUISING_SPEED;
    }

    private boolean needToExit() {
        return  ((exitLocationOnHighway - currentPosition.locationOnHighway) < EXIT_DISTANCE_THRESHOLD);
    }

    public long compareLocationTo(Car toCompare) {
        return (this.currentPosition.locationOnHighway - toCompare.currentPosition.locationOnHighway);
    }


}
