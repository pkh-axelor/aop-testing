package com.axelor.invoice.web;

import java.math.BigDecimal;

import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;

public class InvoiceLineController {

	public void setTaxRate(ActionRequest request, ActionResponse response) {

		Context context = request.getContext();
		Invoice invoice = context.getParent().asType(Invoice.class);

		if (!invoice.getCustomer().getIsSubjectToTaxes()) {
			response.setHidden("taxRate", true);
		} else {
			response.setValue("taxRate", "0.2");
		}
	}

	public void onChangeProduct(ActionRequest request,ActionResponse response) {
        InvoiceLine invoiceLine=request.getContext().asType(InvoiceLine.class);
        
        
        BigDecimal quantity=new BigDecimal(0);
        quantity=invoiceLine.getQuantity();
        
        BigDecimal unitpriceuntaxed=new BigDecimal(0);
        unitpriceuntaxed=invoiceLine.getProduct().getUnitPriceUntaxed();
        
        BigDecimal exTaxTotal=new BigDecimal(0);
        exTaxTotal=quantity.multiply(unitpriceuntaxed);
        
        BigDecimal taxRate=new BigDecimal(0);
        taxRate=invoiceLine.getTaxRate();
        
        BigDecimal inTaxTotal=new BigDecimal(0);
        inTaxTotal=exTaxTotal.add(exTaxTotal.multiply(taxRate));
        
        response.setValue("description", invoiceLine.getProduct().getName());
        response.setValue("unitPriceUntaxed", unitpriceuntaxed);
        response.setValue("exTaxTotal", exTaxTotal);
        response.setValue("inTaxTotal", inTaxTotal);
    }
	
}
