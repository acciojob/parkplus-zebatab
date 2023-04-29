package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.model.User;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    public ReservationServiceImpl() {
    }

    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user;
        ParkingLot parkingLot;
        try {
            user = (User)this.userRepository3.findById(userId).get();
            parkingLot = (ParkingLot)this.parkingLotRepository3.findById(parkingLotId).get();
        } catch (Exception var12) {
            throw new Exception("Cannot make reservation");
        }

        List<Spot> spotList = parkingLot.getSpotList();
        Spot bookedSpot = null;
        int minPrice = Integer.MAX_VALUE;
        Iterator var10 = spotList.iterator();

        while(var10.hasNext()) {
            Spot s = (Spot)var10.next();
            if (!s.getOccupied() && this.spotTypeUtil(s.getSpotType()) >= numberOfWheels && s.getPricePerHour() * timeInHours < minPrice) {
                minPrice = s.getPricePerHour() * timeInHours;
                bookedSpot = s;
            }
        }

        if (bookedSpot == null) {
            throw new Exception("Cannot make reservation");
        } else {
            Reservation reservation = new Reservation();
            reservation.setSpot(bookedSpot);
            reservation.setUser(user);
            reservation.setNumberOfHours(timeInHours);
            bookedSpot.getReservationList().add(reservation);
            bookedSpot.setOccupied(true);
            user.getReservationList().add(reservation);
            parkingLot.getSpotList().add(bookedSpot);
            this.userRepository3.save(user);
            this.spotRepository3.save(bookedSpot);
            return reservation;
        }
    }

    private int spotTypeUtil(SpotType spotType) {
        if (spotType == SpotType.TWO_WHEELER) {
            return 2;
        } else {
            return spotType == SpotType.FOUR_WHEELER ? 4 : Integer.MAX_VALUE;
        }
    }
}