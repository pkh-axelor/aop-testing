package com.axelor.invoice.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

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
	
}
