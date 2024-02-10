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
public class PayTaxes extends IdGenerator{

    private LocalDate payDate;
    private double paySum;
    private double paidSum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    public PayTaxes(LocalDate payDate, double paySum, double paidSum) {
        this.payDate = payDate;
        this.paySum = paySum;
        this.paidSum = paidSum;
    }
}
