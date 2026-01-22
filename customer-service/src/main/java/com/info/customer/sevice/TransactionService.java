package com.info.customer.sevice;

import com.info.customer.common.PageResponse;
import com.info.customer.common.TransactionRequest;
import com.info.customer.common.TransactionResponse;

public interface TransactionService {

	public void credit(TransactionRequest requestDTO);

	public void debit(TransactionRequest requestDTO);

	public PageResponse<TransactionResponse> getTransactionHistory(String accountNumber, int page, int size, String sortBy, String sortDir);

}
