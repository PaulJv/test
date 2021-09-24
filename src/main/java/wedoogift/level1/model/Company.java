package wedoogift.level1.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@Entity
@SequenceGenerator(name="seq_company", initialValue=1, allocationSize=100)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_company")
    private int id;
    private String name;
    private double balance;
}
