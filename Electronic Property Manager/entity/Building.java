package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Building extends IdGenerator{

    private int floors;
    private String address;

    public Building(int floors, String address) {
        this.floors = floors;
        this.address = address;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @OneToMany(mappedBy = "building")
    private List<Apartment> apartmentList;

    @OneToOne(mappedBy = "building")
    private Tax tax;

    @Override
    public String toString() {
        return "Building{" +
                "floors=" + floors +
                ", address='" + address + '\'' +
                "} " + super.toString();
    }
}
