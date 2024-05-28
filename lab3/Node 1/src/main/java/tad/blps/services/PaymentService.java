package tad.blps.services;

import bitronix.tm.BitronixTransactionManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.DTO.PaymentDTO;
import tad.blps.entity.Payment;
import tad.blps.repositories.PaymentRepository;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentService {
    @Autowired
    private BitronixTransactionManager userTransaction;
    
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    @Transactional
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional
    public Optional<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Transactional
    public void deletePayment(Payment payment) {
        paymentRepository.delete(payment);
    }
    
    @Transactional
    public void updatePayment(PaymentDTO payment, Long userId) {
        Optional<Payment> existingPayment = paymentRepository
                .findByUserId(userId);

        if(existingPayment.isPresent()) {
            try {
                userTransaction.begin();
                
                Payment paymentToUpdate = existingPayment.get();

                paymentToUpdate.setBalance(payment.getBalance());
                paymentToUpdate.setWithdrawAccount(payment.getWithdrawAccount());
                
                userTransaction.commit();
            } catch ( Exception e) {
                try {
                    userTransaction.rollback();
                } catch (IllegalStateException ex) {
                    Logger.getLogger(PaymentService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(PaymentService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.transaction.SystemException ex) {
                    Logger.getLogger(PaymentService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            String message = "Not found payment with user id = " + userId;
            throw new EntityNotFoundException(message);
        }
    }


}
