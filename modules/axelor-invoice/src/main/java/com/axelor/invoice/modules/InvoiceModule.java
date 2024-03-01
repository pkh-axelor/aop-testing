package com.axelor.invoice.modules;

import com.axelor.app.AxelorModule;
import com.axelor.invoice.services.InvoiceServiceImpl;
import com.axelor.invoice.services.InvoiceService;

public class InvoiceModule extends AxelorModule{
	
	@Override
	protected void configure() {
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
	}
}
