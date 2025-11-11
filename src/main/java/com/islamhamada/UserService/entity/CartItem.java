package com.islamhamada.UserService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue
    private long id;

    private long productId;

    private int quantity;
}
