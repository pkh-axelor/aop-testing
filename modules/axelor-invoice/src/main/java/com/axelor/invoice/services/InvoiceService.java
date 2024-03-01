package com.axelor.invoice.services;

import java.util.List;

import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public interface InvoiceService {
//	public void cancelInvoiceList(List<Integer> invoice);
	
	void validateMergeInvoices();


}
