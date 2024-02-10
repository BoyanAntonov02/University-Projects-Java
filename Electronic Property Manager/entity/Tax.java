package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Tax extends IdGenerator{

    private double perSquareFootage;

    private double perLiver;

    private double perPet;

    @OneToOne(fetch = FetchType.LAZY)
    private Building building;

    public Tax(double perSquareFootage, double perLiver, double perPet) {
        this.perSquareFootage = perSquareFootage;
        this.perLiver = perLiver;
        this.perPet = perPet;
    }
}
