package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee extends IdGenerator{

    private String name;

    public Employee(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToMany(mappedBy = "employee")
    private List<Building> building;

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
