package com.info.customer.sevice.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.info.customer.common.PageResponse;
import com.info.customer.common.TransactionRequest;
import com.info.customer.common.TransactionResponse;
import com.info.customer.common.TransactionType;
import com.info.customer.entity.Account;
import com.info.customer.entity.Transaction;
import com.info.customer.exceptions.AccountNotFoundException;
import com.info.customer.exceptions.InsufficientBalanceException;
import com.info.customer.repository.AccountRepository;
import com.info.customer.repository.TransactionRepository;
import com.info.customer.sevice.TransactionService;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;
	private Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	@Transactional
	public void credit(TransactionRequest request) {

		Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
				.orElseThrow(() -> new AccountNotFoundException("Account Not Found"));

		if (account.isActive()) {
			Transaction txn = new Transaction();
			txn.setReferenceId(UUID.randomUUID().toString());
			txn.setType(TransactionType.CREDIT);
			txn.setAmount(request.getAmmount());
			txn.setTransactionTime(LocalDateTime.now());
			txn.setAccount(account);
			account.setBalance(account.getBalance().add(request.getAmmount()));
			transactionRepository.save(txn);
			accountRepository.save(account);
			logger.info("Creadited Ammount {} to account {}" + request.getAmmount(), account.getAccountNumber());

		} else {

			throw new AccountNotFoundException("Account not active");
		}

	}

	@Override
	@Transactional
	public void debit(TransactionRequest request) {

		Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
				.orElseThrow(() -> new AccountNotFoundException("Account Not Found"));

		if (account.isActive()) {

			if (account.getBalance().compareTo(request.getAmmount()) < 0) {
				Transaction txn = new Transaction();
				txn.setReferenceId(UUID.randomUUID().toString());
				txn.setType(TransactionType.CREDIT);
				txn.setAmount(request.getAmmount());
				txn.setTransactionTime(LocalDateTime.now());
				txn.setAccount(account);
				account.setBalance(account.getBalance().subtract(request.getAmmount()));
				transactionRepository.save(txn);
				accountRepository.save(account);
				logger.info("Debited ammount {} from account " + request.getAccountNumber(),
						account.getAccountNumber());

			} else {

				throw new InsufficientBalanceException("Insufficient balance");

			}

		} else {

			throw new AccountNotFoundException("Account not active");

		}

	}

	@Override
	public PageResponse<TransactionResponse> getTransactionHistory(String accountNumber, int page, int size,
			String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Transaction> transactionPage = transactionRepository.findByAccountNumber(accountNumber, pageable);
		List<TransactionResponse> transactionResponses = transactionPage.getContent().stream().map(this::mapToDTO)
				.toList();
		PageResponse<TransactionResponse> response = new PageResponse<>();
		response.setContent(transactionResponses);
		response.setPageNumber(transactionPage.getNumber());
		response.setPageSize(transactionPage.getSize());
		response.setTotalElements(transactionPage.getTotalElements());
		response.setTotalPages(transactionPage.getTotalPages());
		response.setLast(transactionPage.isLast());
		return response;
	}

	private TransactionResponse mapToDTO(Transaction tx) {

		TransactionResponse dto = new TransactionResponse();
		dto.setId(tx.getId());
		dto.setReferenceId(tx.getReferenceId());
		dto.setType(tx.getType());
		dto.setAmount(tx.getAmount());
		dto.setTransactionTime(tx.getTransactionTime());
		// IMPORTANT PART
		dto.setAccountNumber(tx.getAccount().getAccountNumber());

		return dto;

	}

}
