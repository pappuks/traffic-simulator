package in.net.kuldeep;

import in.net.kuldeep.model.Car;
import in.net.kuldeep.model.Driver;
import in.net.kuldeep.model.Highway;
import in.net.kuldeep.model.Lane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TrafficSimulator {

    private Highway highway;
    private List<Driver> drivers;
    private Random random = new Random();
    private List<Float> startSpeeds;

    public static void main(String args[]) {

        TrafficSimulator trafficSimulator = new TrafficSimulator();

        trafficSimulator.initializeHighway();
        trafficSimulator.initializeDrivers();

        while (true) {
            trafficSimulator.addCars();
            trafficSimulator.highway.getLanes().forEach(l -> {
                l.getCars().forEach(c -> c.calculateNextPosition(trafficSimulator.highway));
            });
            trafficSimulator.highway.getLanes().forEach(l -> {
                l.getCars().forEach(c -> c.makeNextPositionAsCurrent());
            });

            boolean accident = false;

            for (Lane lane : trafficSimulator.highway.getLanes()) {
                if (lane.checkForCollision()) {
                    System.out.println("Got accident");
                    accident = true;
                }
            }
            if (accident) {
                break;
            }

            trafficSimulator.highway.getLanes().forEach(l -> {
                System.out.print(l.printStats() + " | ");
            });
            System.out.println();

        }

    }

    public void initializeHighway() {
        highway = new Highway();

        List<Lane> lanes = new ArrayList<>();
        IntStream.range(1, 2).forEach(i -> lanes.add(new Lane(i, 0, 20000)));
        highway.setLanes(lanes);

        startSpeeds = new ArrayList<>();
        IntStream.range(35, 65).forEach(i -> startSpeeds.add((float)i));
    }

    public void initializeDrivers() {
        drivers = new ArrayList<>();
        drivers.add(Driver.slowSafeDriver());
        drivers.add(Driver.safeDriver());
        drivers.add(Driver.fastSafeDriver());
        drivers.add(Driver.aggressiveDriver());
    }

    public void addCars() {
        highway.getLanes().forEach(l -> {
            Float carSpeed = startSpeeds.get(random.nextInt(startSpeeds.size()));
            if (l.getCars().size() > 0) {
                carSpeed = l.getCars().get(0).getCurrentPosition().getSpeed();
            }
            Car car = new Car();
            car.setDriver(drivers.get(random.nextInt(drivers.size())));
            car.setStartPosition(l, carSpeed);
            car.setExitLocationOnHighway(20000);
            l.addCar(car);
        });
    }



}
