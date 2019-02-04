package in.net.kuldeep.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Lane {
    private final int numberOnHighway;
    private final long startLocation;
    private final long endLocation;
    private SortedArrayList<Car> cars = new SortedArrayList<>();

    public void addCar(Car car) {
        cars.insertSorted(car);
    }

    public void removeCar(Car car) {
        cars.remove(car);
    }

    public Car findCarInFront(Car car) {
        int index = cars.indexOf(car);
        if (index > 0) {
            return cars.get(index - 1);
        } else {
            return null;
        }
    }

    public Car findCarInBack(Car car) {
        int index = cars.indexOf(car);
        if ((index + 1) < cars.size()) {
            return cars.get(index + 1);
        } else {
            return null;
        }
    }

    public Car findNearestCarByLocation(long location) {
        long closestDist = Long.MAX_VALUE;
        Car closest = null;
        for (int i = 0; i < cars.size(); i++) {
            long carLoc = cars.get(i).getCurrentPosition().getLocationOnHighway();
            if (Math.abs(carLoc - location) < closestDist) {
                closestDist = Math.abs(carLoc - location);
                closest = cars.get(i);
            }
        }
        return closest;
    }

    public boolean checkForCollision() {
        long previousLoc = Long.MAX_VALUE;
        for (int i = 0; i < cars.size(); i++) {
            long carLoc = cars.get(i).getCurrentPosition().getLocationOnHighway();
            if (previousLoc == carLoc) {
                System.out.println("Collision in Lane:" + numberOnHighway + " at " + carLoc);
                return true;
            }
            previousLoc = carLoc;
        }
        return false;
    }

    public String printStats() {
        return "L:" + numberOnHighway + "; C:" + cars.size() + "; P:" + cars.get(0).getCurrentPosition().getLocationOnHighway() + "; S:" +
                cars.get(0).getCurrentPosition().getSpeed();
    }
}
