package tad.blps.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.DTO.PaymentDTO;
import tad.blps.entity.Payment;
import tad.blps.repositories.PaymentRepository;

import java.util.Optional;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public void deletePayment(Payment payment) {
        paymentRepository.delete(payment);
    }

    @Transactional
    public void updatePayment(PaymentDTO payment, Long userId) {
        Optional<Payment> existingPayment = paymentRepository.findByUserId(userId);

        if(existingPayment.isPresent()) {
            Payment paymentToUpdate = existingPayment.get();

            paymentToUpdate.setBalance(payment.getBalance());
            paymentToUpdate.setWithdrawAccount(payment.getWithdrawAccount());
        } else {
            String message = "Not found payment with user id = " + userId;
            throw new EntityNotFoundException(message);
        }
    }


}
