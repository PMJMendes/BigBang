package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.FormField;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSelectionViewPresenter;
import bigBang.module.insurancePolicyModule.client.resources.Resources;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Image;

public class SubPolicyForm extends FormView<SubPolicy>{

	protected TextBoxFormField number;
	protected ExpandableSelectionFormField client;
	protected TextBoxFormField policyStatus;
	protected DatePickerFormField startDate;
	protected DatePickerFormField endDate;
	protected ExpandableListBoxFormField fractioning;
	protected NumericTextBoxFormField premium;
	protected Image statusIcon;
	protected TextAreaFormField notes;
	
	
	private HeaderFieldsSection headerForm;


	public SubPolicyForm() {
		super();
		addSection("Cabeçalho de Apólice");
		number  = new TextBoxFormField("Número");
		number.setFieldWidth("175px");

		ExpandableSelectionFormFieldPanel clientSelectionPanel = (ExpandableSelectionFormFieldPanel) ViewPresenterFactory.getInstance().getViewPresenter("INSURANCE_POLICY_SUB_POLICY_CLIENT_SELECTION");
		((ClientSelectionViewPresenter)clientSelectionPanel).go();
		client = new ExpandableSelectionFormField(BigBangConstants.EntityIds.CLIENT, "Cliente Aderente", clientSelectionPanel); //TODO
		number.setFieldWidth("175px");
		startDate = new DatePickerFormField("Data de Início");
		startDate.setMandatory(true);
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
		premium = new NumericTextBoxFormField("Prémio Comercial Anual", true);
		premium.setFieldWidth("175px");
		premium.setUnitsLabel("€");

		policyStatus = new TextBoxFormField("Estado");
		policyStatus.setFieldWidth("100%");
		policyStatus.setEditable(false);
		statusIcon = new Image();
		policyStatus.add(statusIcon);

		addFormField(client, false);

		addFormFieldGroup(new FormField<?>[]{
				number,
				policyStatus,
				premium
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				fractioning,
				startDate,
				endDate,
		}, true);


		headerForm = new HeaderFieldsSection();
		addSection(headerForm);	

	}

	@Override
	public SubPolicy getInfo() {
		SubPolicy result = this.value;

		if(result != null) {
			result.number = number.getValue();
			result.startDate = startDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(startDate.getValue());
			result.clientId = client.getValue();
			result.expirationDate = endDate.getValue() == null ? null :  DateTimeFormat.getFormat("yyyy-MM-dd").format(endDate.getValue());

			result.fractioningId = fractioning.getValue();
			result.premium = premium.getValue();
		
			result.headerFields = headerForm.getValue();

			return result;
		}
		return null;
	}

	@Override
	public void setInfo(SubPolicy info) {

		if(info == null) {
			clearInfo();
			clearValue();
		}else{
			this.number.setValue(info.number);
			this.client.setValue(info.clientId);
			this.fractioning.setValue(info.fractioningId);
			this.policyStatus.setValue(info.statusText);
			Resources resources = GWT.create(Resources.class);
			if(value.statusIcon == null) {
				statusIcon.setResource(resources.provisionalPolicyIcon());
			}else{
				switch(value.statusIcon){
				case OBSOLETE:
					statusIcon.setResource(resources.inactivePolicyIcon());
					break;
				case PROVISIONAL:
					statusIcon.setResource(resources.provisionalPolicyIcon());
					break;
				case VALID:
					statusIcon.setResource(resources.activePolicyIcon());
					break;
				default:
					return;
				}
			}
		}

	}

}
