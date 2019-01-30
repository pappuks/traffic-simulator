package in.net.kuldeep.model;

import lombok.Data;

@Data
public class Lane {
    private int numberOnHighway;
    private long startLocation;
    private long endLocation;
    private SortedArrayList<Car> cars;

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
        if (index < (cars.size() + 1)) {
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
}
