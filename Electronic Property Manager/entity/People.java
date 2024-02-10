package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class People extends IdGenerator{

    private String name;
    private LocalDate bornDate;
    private boolean isUsedElevator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    public People(String name, LocalDate bornDate, boolean isUsedElevator) {
        this.name = name;
        this.bornDate = bornDate;
        this.isUsedElevator = isUsedElevator;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", bornDate=" + bornDate +
                "} " + super.toString();
    }
}
