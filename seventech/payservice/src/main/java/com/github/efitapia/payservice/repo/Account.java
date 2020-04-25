package com.github.efitapia.payservice.repo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal balance;

}
