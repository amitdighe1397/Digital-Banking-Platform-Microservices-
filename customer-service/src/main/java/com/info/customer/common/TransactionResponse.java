package com.info.customer.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionResponse {

	private Long id;
	private String referenceId;
	private TransactionType type;
	private BigDecimal amount;
	private LocalDateTime transactionTime;

	// FLATTENED FIELD
	private String accountNumber;

}
