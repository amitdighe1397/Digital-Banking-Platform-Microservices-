package com.info.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.info.customer.common.PageResponse;
import com.info.customer.common.TransactionRequest;
import com.info.customer.common.TransactionResponse;
import com.info.customer.sevice.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping("/credit")
	public ResponseEntity<String> credit(@Valid @RequestBody TransactionRequest request) {

		transactionService.credit(request);
		return ResponseEntity.ok("Amount credited successfully");

	}

	@PostMapping("/debit")
	public ResponseEntity<String> debit(@Valid @RequestBody TransactionRequest request) {

		transactionService.debit(request);
		return ResponseEntity.ok("Amount debited successfully");

	}

	@GetMapping("/{accountNumber}")
	public ResponseEntity<PageResponse<TransactionResponse>> getTransactionHistory(@PathVariable String accountNumber,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "transactionDate") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		return ResponseEntity.ok(transactionService.getTransactionHistory(accountNumber, page, size, sortBy, sortDir));

	}

}
