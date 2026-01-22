package com.info.customer.common;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionRequest {

	@NotBlank(message = "account number must be 12 digits")
	private String accountNumber;
	@NotBlank(message = "ammount must be decimal format")
	private BigDecimal ammount;

}
