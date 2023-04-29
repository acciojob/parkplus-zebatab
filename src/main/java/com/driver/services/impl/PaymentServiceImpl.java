package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    public PaymentServiceImpl() {
    }

    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        PaymentMode paymentMode;
        if (mode.equalsIgnoreCase("cash")) {
            paymentMode = PaymentMode.CASH;
        } else if (mode.equalsIgnoreCase("upi")) {
            paymentMode = PaymentMode.UPI;
        } else {
            if (!mode.equalsIgnoreCase("card")) {
                throw new Exception("Payment mode not detected");
            }

            paymentMode = PaymentMode.CARD;
        }

        Payment payment = new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        Reservation reservation = (Reservation)this.reservationRepository2.findById(reservationId).get();
        int bill = reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours();
        if (amountSent < bill) {
            throw new Exception("Insufficient Amount");
        } else {
            payment.setReservation(reservation);
            reservation.setPayment(payment);
            this.reservationRepository2.save(reservation);
            return payment;
        }
    }
}