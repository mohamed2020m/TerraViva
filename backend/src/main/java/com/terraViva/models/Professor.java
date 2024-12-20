package com.terraViva.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("PROF")
public class Professor extends User {

    @ManyToOne
//    @JoinColumn(unique = true)
    private Category category;

    @OneToMany(mappedBy="professor", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<ThreeDObject> threeDObjects;

    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", name='" + getFirstName() + " " + getLastName() + '\'' +
            ", categoryId=" + (category != null ? category.getId() : null) +
            '}';
    }

}