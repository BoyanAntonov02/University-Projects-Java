package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company extends IdGenerator{

    private String name;

    public Company(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "company")
    private List<Employee> employeeList;


    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
