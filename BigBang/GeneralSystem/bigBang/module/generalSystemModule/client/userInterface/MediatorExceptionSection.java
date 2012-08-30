package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Mediator.MediatorException;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class MediatorExceptionSection extends CollapsibleFormViewSection{

	public Button removeButton;
	protected ListBoxFormField referenceType;
	protected FormField<?> referenceClient;
	protected FormField<?> referencePolicy;
	protected NumericTextBoxFormField comission;

	public MediatorExceptionSection() {

		super("Excepção");
		removeButton = new Button("Remover");

		referenceType = new ListBoxFormField("");
		referenceType.addItem("Cliente", BigBangConstants.EntityIds.CLIENT);
		referenceType.addItem("Apólice", BigBangConstants.EntityIds.INSURANCE_POLICY);
		referenceType.setMandatory(true);
		referenceType.removeItem(0); //Removes the empty value

		comission = new NumericTextBoxFormField("Comissão", false);
		comission.setUnitsLabel("%");

		final HorizontalPanel referenceWrapper = new HorizontalPanel();

		referencePolicy = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, new HasParameters());
		referenceClient = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.CLIENT, new HasParameters());
		referencePolicy.setMandatory(true);
		referenceClient.setMandatory(true);
		comission.setMandatory(true);

		referenceWrapper.add(referenceType);
		referenceWrapper.add(referencePolicy);
		referenceWrapper.add(referenceClient);
		referencePolicy.setVisible(false);
		referenceWrapper.add(comission);

		referenceWrapper.setCellVerticalAlignment(referenceType, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(referencePolicy, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(referenceClient, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(comission, HasVerticalAlignment.ALIGN_MIDDLE);

		registerFormField(referenceType);
		registerFormField(referencePolicy);
		registerFormField(referenceClient);
		registerFormField(comission);

		addWidget(referenceWrapper);

		SimplePanel buttonWrapper = new SimplePanel();
		buttonWrapper.add(removeButton);
		buttonWrapper.setWidth("100%");

		referenceType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				referencePolicy.setVisible(event.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY));
				referenceClient.setVisible(!event.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY));
				referencePolicy.setValue(null);
				referenceClient.setValue(null);
			}
		});

		addWidget(buttonWrapper, false);

		this.expand();

	}


	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}


	public MediatorException getException() {
		MediatorException toSend = new MediatorException();

		toSend.policyId = (String) referencePolicy.getValue();
		toSend.clientId = (String) referenceClient.getValue();

		toSend.percent = comission.getValue();

		return toSend;
	}

	@SuppressWarnings("unchecked")
	public void setException(MediatorException exception){
		referenceType.setValue(exception.policyId == null ?  BigBangConstants.EntityIds.CLIENT : BigBangConstants.EntityIds.INSURANCE_POLICY);
		((HasValue<String>)referenceClient).setValue(exception.clientId);
		((HasValue<String>)referencePolicy).setValue(exception.policyId);
		comission.setValue(exception.percent);
	}

}
