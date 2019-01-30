package in.net.kuldeep.model;

import lombok.Data;

import java.util.List;

@Data
public class Highway {
    private List<Lane> lanes;
    private List<Car> cars;
    private int totalLanes;

    @Data
    public class Proximity {
        private Car front;
        private Car back;
        private Car leftFront;
        private Car rightFront;
        private Car left;
        private Car right;
        private Car leftBack;
        private Car rightBack;
    }

    public Lane getLeftLane(Lane lane) {
        for (int i = 0; i < lanes.size(); i++) {
            if (lanes.get(i).getNumberOnHighway() < lane.getNumberOnHighway()) {
                return lanes.get(i);
            }
        }
        return null;
    }

    public Lane getRightLane(Lane lane) {
        for (int i = 0; i < lanes.size(); i++) {
            if (lanes.get(i).getNumberOnHighway() > lane.getNumberOnHighway()) {
                return lanes.get(i);
            }
        }
        return null;
    }

    public Proximity findProximity(Car car) {
        Proximity proximity = new Proximity();

        Lane currentLane = car.getCurrentPosition().getLane();
        proximity.front = currentLane.findCarInFront(car);
        proximity.back = currentLane.findCarInBack(car);

        Lane leftLane = getLeftLane(currentLane);
        if (leftLane != null) {
            Car leftCar = leftLane.findNearestCarByLocation(car.getCurrentPosition().getLocationOnHighway());
            proximity.left = leftCar;
            if (leftCar != null) {
                proximity.leftFront = leftLane.findCarInFront(leftCar);
                proximity.leftBack = leftLane.findCarInBack(leftCar);
            }
        }

        Lane rightLane = getRightLane(currentLane);
        if (rightLane != null) {
            Car rightCar = rightLane.findNearestCarByLocation(car.getCurrentPosition().getLocationOnHighway());
            proximity.right = rightCar;
            if (rightCar != null) {
                proximity.rightFront = rightLane.findCarInFront(rightCar);
                proximity.rightBack = rightLane.findCarInBack(rightCar);
            }
        }

        return proximity;
    }

}
