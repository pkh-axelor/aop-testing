package com.axelor.invoice.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.midi.Soundbank;

import com.axelor.db.JPA;
import com.axelor.inject.Beans;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceLineRepository;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.invoice.services.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;


public class InvoiceController {

	
	public void setCurrentDate(ActionRequest request, ActionResponse response) {
		response.setValue("invoiceDateT", LocalDateTime.now());
	}
	
	public void setTaxTotal(ActionRequest request, ActionResponse response) {
        
		Invoice invoice = request.getContext().asType(Invoice.class);
        
        BigDecimal inTaxTotal = new BigDecimal(0);
        BigDecimal exTaxTotal = new BigDecimal(0);
        BigDecimal unitpriceuntaxed = new BigDecimal(0);
        
        for (int i = 0; i < invoice.getInvoiceLineList().size(); i++) {
            unitpriceuntaxed = invoice.getInvoiceLineList().get(i).getUnitPriceUntaxed();
            exTaxTotal = exTaxTotal.add(unitpriceuntaxed.multiply(invoice.getInvoiceLineList().get(i).getQuantity()));
        }
        
        response.setValue("exTaxTotal", exTaxTotal);
        
        for (int i = 0; i < invoice.getInvoiceLineList().size(); i++) {
            unitpriceuntaxed = invoice.getInvoiceLineList().get(i).getUnitPriceUntaxed();
            exTaxTotal = BigDecimal.ZERO;
            exTaxTotal = exTaxTotal.add(unitpriceuntaxed.multiply(invoice.getInvoiceLineList().get(i).getQuantity()));
            inTaxTotal = inTaxTotal.add(exTaxTotal.add(exTaxTotal.multiply(invoice.getInvoiceLineList().get(i).getTaxRate())));
        }
        
        response.setValue("inTaxTotal", inTaxTotal);
    }
	
	public void validateButton(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		
		if (invoice.getInvoiceLineList().isEmpty()) {
			response.setAlert("At least one invoice line is required");
		} 
		
		for(int i=0; i<invoice.getInvoiceLineList().size(); i++)
		{
			if (invoice.getInvoiceLineList().get(i).getExTaxTotal().intValue() <= 0) {
				response.setAlert("One invoice line has a null or negative total");
			}
		}	
		setTaxTotal(request, response);
		response.setValue("statusSelect", 1);
	} 
	
	public void ventilateButton(ActionRequest request, ActionResponse response) {
		setTaxTotal(request, response);
		response.setValue("statusSelect", 2);
	}
	
	public void validateCancelButton(ActionRequest request, ActionResponse response) {
		response.setAlert("This action will cancel this invoice. Do you want to procees?");
	}
	
	public void cancelButton(ActionRequest request, ActionResponse response) {
		setTaxTotal(request, response);
		response.setValue("statusSelect", 3);
	}
	
	@Transactional
	public void cancelInvoices(ActionRequest request, ActionResponse response) {
		
		ArrayList<Integer> invoice = new ArrayList<Integer>();
		invoice = (ArrayList<Integer>) request.getContext().get("_ids");
		
		for(var i : invoice)
		{
			Invoice temp = Beans.get(InvoiceRepository.class).find((long) i);
			if(temp.getStatusSelect() <2)
			{
				temp = Beans.get(InvoiceRepository.class).save(temp);
				temp.setStatusSelect(3);
			} 
			response.setReload(true);
		}
	}
	
	public void mergeInvoicesList(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		
//		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<String, Object>> data = (ArrayList<LinkedHashMap<String, Object>>) request.getContext().get("selectedInvoicesList"); 
		
		List<Integer> listOfInvoiceId = data.stream().flatMap(it -> it.values().stream()).filter(it -> it instanceof Integer).map(it -> (Integer) it).collect(Collectors.toList());
		
//		if(invoice.getInvoiceLineList().size() < 2)
//		{
//			response.setAlert("Please select at least invoice");
//		}
//		
//		if(data.stream().anyMatch(t -> t.containsKey(invoice.getCustomer().getFullName()))) {
//			System.out.println("a");
//		}else {
//			System.out.println("b"); 
//		}
//		
		System.out.println(data);

		
		
//		Beans.get(InvoiceService.class).validateMergeInvoices();
	}
	
}