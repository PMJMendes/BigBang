package bigBang.module.receiptModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.shared.Receipt.PaymentInfo.Payment;
import bigBang.library.client.Session;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class PaymentsForm extends FormView<Payment[]> {

	protected List<PaymentSection> sections;
	protected FormViewSection addSection;
	public Button confirmButton, newButton;
	protected NumericTextBoxFormField totalLabel;
	
	public PaymentsForm(){
		sections = new ArrayList<PaymentSection>();

		addSection("Marcar Cobrança");

		totalLabel = new NumericTextBoxFormField("Total", true);
		totalLabel.setFieldWidth("175px");
		totalLabel.setUnitsLabel(Session.getCurrency());
		totalLabel.setEditable(false);
		addWidget(totalLabel, true);
		
		confirmButton = new Button("Confirmar Cobrança");
		confirmButton.getElement().getStyle().setProperty("marginTop", "20px");
		addWidget(confirmButton, false);
		
		addSection = new FormViewSection("Pagamento");
		newButton = new Button("Novo Pagamento");
		addSection.addWidget(newButton);
		
		setValidator(new PaymentsFormValidator(this));
	}

	@Override
	public Payment[] getInfo() {
		Payment[] result = new Payment[sections.size()];

		int i = 0;
		for(PaymentSection section : sections) {
			result[i] = section.getPayment();
			i++;
		}

		return result;
	}

	@Override
	public void setInfo(Payment[] info) {
		clear();
		if(info != null){
			for(Payment payment : info) {
				addPayment(payment);
			}
		}
	}

	public void addPayment(Payment payment) {
		final PaymentSection section = new PaymentSection();
		section.setPayment(payment);
		addSection(section);
		sections.add(section);

		section.removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeSection(section);
			}
		});
		
		section.paymentValue.addValueChangeHandler(new ValueChangeHandler<Double>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				updateTotal();
			}
		});
		
		if(sections.size() > 1) {
			for(PaymentSection paymentSection : sections) {
				paymentSection.removeButton.setEnabled(true);
			}
		}else{
			section.removeButton.setEnabled(false);
		}
		
		addSection(addSection);
		updateTotal();
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		clear();
	}
	
	protected void clear(){
		for(PaymentSection section : sections) {
			removeSection(section);
		}
	}

	@Override
	public void removeSection(FormViewSection section) {
		super.removeSection(section);
		section.removeFromParent();
		sections.remove(section);
		
		if(sections.size() == 1) {
			for(PaymentSection paymentSection : sections) {
				paymentSection.removeButton.setEnabled(false);
			}
		}
		updateTotal();
	}
	
	protected void updateTotal(){
		double total = 0;
		
		for(PaymentSection section : sections) {
			Payment payment = section.getPayment();
			if(payment.value != null){
				total += payment.value;
			}
		}
		
		totalLabel.setValue(total);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(newButton != null) {
			newButton.setEnabled(!readOnly);
		}
	}

}
