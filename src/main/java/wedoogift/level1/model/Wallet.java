package wedoogift.level1.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@Entity
@SequenceGenerator(name="seq_wallet", initialValue=1, allocationSize=100)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wallet")
    private int id;

    private double amount;

    private String walletType;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
