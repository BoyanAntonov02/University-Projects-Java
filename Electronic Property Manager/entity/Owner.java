package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Owner extends IdGenerator{

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Apartment> apartmentList;
}
