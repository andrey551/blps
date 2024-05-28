package tad.blps.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "payment")
public class Payment implements Persistable<Long>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "withdraw_account")
    private String withdrawAccount;


    @Override
    public boolean isNew() {
        return false;
    }
}
