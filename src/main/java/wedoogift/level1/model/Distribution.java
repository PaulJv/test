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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@SequenceGenerator(name="seq_distribution", initialValue=1, allocationSize=100)
public class Distribution {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_distribution")
    private int id;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;

    private String voucherType;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
