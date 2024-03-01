package com.axelor.invoice.services;

import java.util.ArrayList;
import java.util.List;

import com.axelor.inject.Beans;
import com.axelor.invoice.db.repo.InvoiceRepository;

public class InvoiceServiceImpl implements InvoiceService {

	@Override
	public void validateMergeInvoices() {
		
		ArrayList<Integer> invoiceLine = new ArrayList<Integer>();
		
		for(Integer invoices : invoiceLine)
			Beans.get(InvoiceRepository.class).find((long) invoices);
			
		{
		}
	}


	
	

//	@Override
//	public void cancelInvoiceList(List<Integer> invoice) {
//		
//	}

	/*
	 * public void cancelInvoice () { for(Long id : ) {
	 * 
	 * if(temp.getStatusSelect() <2) { response.setValue("statusSelect", 3);
	 * Beans.get(InvoiceRepository.class).save(temp); }
	 * 
	 * } }
	 */

}
