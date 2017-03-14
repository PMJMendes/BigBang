package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyInsurerRequest;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

public class SubCasualtyInsurerRequestSection extends CollapsibleFormViewSection {
	
	protected SubCasualty.SubCasualtyInsurerRequest currentRequest;
	
	protected ExpandableListBoxFormField requestType;
	protected DatePickerFormField requestDate;
	protected DatePickerFormField acceptanceDate;
	protected DatePickerFormField resendDate;
	protected DatePickerFormField clarificationDate;
	protected CheckBoxFormField conforms;
	
	protected Button removeButton;
	
	public SubCasualtyInsurerRequestSection(SubCasualtyInsurerRequest request) {
		
		super("");
		
		removeButton = new Button("Remover");
		// requestType = new ExpandableListBoxFormField(null, "Tipo de Pedido");
		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INSURER_REQUEST_TYPE, "Tipo de Pedido");
		conforms = new CheckBoxFormField("Conformidade com Pedido");
		requestDate = new DatePickerFormField("Data de Pedido");
		acceptanceDate = new DatePickerFormField("Data de Recepção");
		resendDate = new DatePickerFormField("Data de Reenvio (a preencher em caso de conformidade)");
		clarificationDate = new DatePickerFormField("Data de Pedido de Esclarecimento  (a preencher em caso de não conformidade)");
		
		addFormFieldGroup(new FormField<?>[]{
				requestType,
				requestDate,
				acceptanceDate
		}, true);
		
		addFormFieldGroup(new FormField<?>[]{
				conforms,
				resendDate,
				clarificationDate
		}, true);
		
		SimplePanel buttonWrapper = new SimplePanel();
		buttonWrapper.add(removeButton);
		buttonWrapper.setWidth("100%");

		addWidget(buttonWrapper, false);
		
		setRequest(request);
	}
	
	public void setRequest(final SubCasualtyInsurerRequest request) {
		this.currentRequest = request;
		
		if(request != null) {
			this.headerLabel.setText("Pedido de Segurador");
			
			// setRequestType();
			requestType.setValue(request.insurerRequestTypeId);
			conforms.setValue(request.conforms);
			requestDate.setValue(request.requestDate);
			acceptanceDate.setValue(request.acceptanceDate);
			resendDate.setValue(request.resendDate);
			clarificationDate.setValue(request.clarificationDate);
		}
	}
	
	/*public void setRequestType() {
		
		requestType.setListId(BigBangConstants.TypifiedListIds.INSURER_REQUEST_TYPE, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
		
	}*/
	
	public SubCasualtyInsurerRequest getRequest() {
		SubCasualtyInsurerRequest result = this.currentRequest;

		if(result != null) {
			
			result.insurerRequestTypeId = requestType.getValue();
			result.requestDate = requestDate.getStringValue();
			result.acceptanceDate = acceptanceDate.getStringValue();
			result.resendDate = acceptanceDate.getStringValue();
			result.clarificationDate = acceptanceDate.getStringValue();
			result.conforms = conforms.getValue();
		}

		return result;
	}
	
	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		removeButton.setVisible(!readOnly);
	}
} 
