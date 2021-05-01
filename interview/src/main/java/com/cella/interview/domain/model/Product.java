package com.cella.interview.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "products")
@EqualsAndHashCode()
@ToString()
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(required = false, hidden = true)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(unique = true)
    private String name;

    @NotNull
    @Size(min = 1)
    private String description;

    @NotNull
    @PositiveOrZero
    private double weight;

    @NotNull
    @Positive
    private double price;

    @NotNull
    @Size(min=3, max=3)
    private String country;

}
