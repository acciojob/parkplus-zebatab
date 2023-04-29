package com.driver.services.impl;

        import com.driver.model.ParkingLot;
        import com.driver.model.Spot;
        import com.driver.model.SpotType;
        import com.driver.repository.ParkingLotRepository;
        import com.driver.repository.SpotRepository;
        import com.driver.services.ParkingLotService;
        import java.util.Iterator;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;

    public ParkingLotServiceImpl() {
    }

    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        this.parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = (ParkingLot)this.parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        if (numberOfWheels <= 2) {
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels <= 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        } else {
            spot.setSpotType(SpotType.OTHERS);
        }

        spot.setParkingLot(parkingLot);
        spot.setOccupied(false);
        parkingLot.getSpotList().add(spot);
        this.parkingLotRepository1.save(parkingLot);
        return spot;
    }

    public void deleteSpot(int spotId) {
        this.spotRepository1.deleteById(spotId);
    }

    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = (ParkingLot)this.parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = null;
        Iterator var6 = parkingLot.getSpotList().iterator();

        while(var6.hasNext()) {
            Spot s = (Spot)var6.next();
            if (spotId == s.getId()) {
                s.setPricePerHour(pricePerHour);
                this.spotRepository1.save(s);
                spot = s;
                break;
            }
        }

        return spot;
    }

    public void deleteParkingLot(int parkingLotId) {
        this.parkingLotRepository1.deleteById(parkingLotId);
    }
}