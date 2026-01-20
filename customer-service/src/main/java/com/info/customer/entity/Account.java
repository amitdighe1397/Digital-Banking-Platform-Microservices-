package com.info.customer.entity;

import java.math.BigDecimal;

import com.info.customer.common.AccountType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "accounts")
@Data
public class Account {

	private Long id;
	private String accountNumber;
	private AccountType accountType;
	private BigDecimal balance;
	
	
}
