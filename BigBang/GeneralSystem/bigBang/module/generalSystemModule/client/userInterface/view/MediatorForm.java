package bigBang.module.generalSystemModule.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Category;
import bigBang.definitions.shared.CommissionProfile;
import bigBang.definitions.shared.Mediator;
import bigBang.definitions.shared.Mediator.MediatorException;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.client.userInterface.MediatorCategoryFormSection;
import bigBang.module.generalSystemModule.client.userInterface.MediatorExceptionSection;
import bigBang.module.generalSystemModule.client.userInterface.NewExceptionSection;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.formValidator.MediatorFormValidator;

public class MediatorForm extends FormView<Mediator> {

	private TextBoxFormField name;
	private TextBoxFormField ISPNumber;
	private TextBoxFormField taxNumber;
	private ExpandableListBoxFormField comissionProfile;
	private NumericTextBoxFormField commissionPercentage;
	private AddressFormField address;
	private CheckBoxFormField hasRetention;
	private TextBoxFormField NIB;
	private NewExceptionSection newException;

	private Collection<MediatorCategoryFormSection> categorySections;

	protected Collection<MediatorExceptionSection> exceptionsSection;

	public MediatorForm() {
		addSection("Informação geral");

		categorySections = new ArrayList<MediatorCategoryFormSection>();

		name = new TextBoxFormField("Nome", new MediatorFormValidator.NameValidator());
		ISPNumber = new TextBoxFormField("Número do ISP", new MediatorFormValidator.ISPNumberValidator());
		taxNumber = new TextBoxFormField("NIF/NIPC", new MediatorFormValidator.TaxNumberValidator());
		comissionProfile = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CommissionProfiles, "Perfil de comissão", new MediatorFormValidator.ComissionProfileValidator());
		commissionPercentage = new NumericTextBoxFormField("Comissão", false);
		commissionPercentage.setUnitsLabel("%");
		NIB = new TextBoxFormField("NIB/IBAN", new MediatorFormValidator.NIBValidator());
		hasRetention = new CheckBoxFormField("Retenção na Fonte");

		addFormField(name);
		addFormField(taxNumber);
		addFormField(NIB);
		addFormField(ISPNumber);
		addFormField(comissionProfile, true);
		addFormField(commissionPercentage, true);
		addFormField(hasRetention, true);

		addSection("Morada");

		address = new AddressFormField(new MediatorFormValidator.AddressValidator());
		addFormField(address);

		newException = new NewExceptionSection();
		addSection(newException);
		newException.setVisible(false);

		exceptionsSection = new ArrayList<MediatorExceptionSection>();

		this.newException.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addException(new MediatorException());
			}
		});

		comissionProfile.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setCommissioningProfile(event.getValue(), value.dealPercents);
			}
		});

		setCommissioningProfile(null, null);
	}

	public void setComissionProfiles(CommissionProfile[] comissionProfiles) {
		comissionProfile.clearValues();
		for(int i = 0; i < comissionProfiles.length; i++) {
			comissionProfile.addItem(comissionProfiles[i].value, comissionProfiles[i].id);
		}
	}

	@Override
	public Mediator getInfo() {
		Mediator info = this.value == null ? new Mediator() : new Mediator(value);
		info.name = name.getValue();
		info.ISPNumber = ISPNumber.getValue();
		info.taxNumber = taxNumber.getValue();
		info.comissionProfile.id = comissionProfile.getValue();
		info.comissionProfile.value = comissionProfile.getSelectedItemText();
		info.address = address.getValue();
		info.NIB = NIB.getValue();
		info.basePercent = commissionPercentage.getValue();
		info.hasRetention = this.hasRetention.getValue();

		Map<String, Double> values = new HashMap<String, Double>();
		for(MediatorCategoryFormSection section : categorySections) {
			values.putAll(section.getValues());
		}

		info.exceptions = new MediatorException[exceptionsSection.size()];


		int i = 0;
		for(MediatorExceptionSection section : exceptionsSection){

			info.exceptions[i++] = section.getException();

		}

		info.dealPercents = values;

		return info;
	}

	@Override
	public void setInfo(Mediator info) {
		if(info == null){
			clearInfo();
			return;
		}

		for(MediatorExceptionSection exception : exceptionsSection){
			exception.removeFromParent();
		}

		exceptionsSection.clear();

		name.setValue(info.name);
		ISPNumber.setValue(info.ISPNumber);
		taxNumber.setValue(info.taxNumber);
		comissionProfile.setValue(info.comissionProfile.id);
		commissionPercentage.setValue(info.basePercent);
		address.setValue(info.address);
		NIB.setValue(info.NIB);
		hasRetention.setValue(info.hasRetention);

		setCommissioningProfile(info.comissionProfile.id, info.dealPercents);

		for(MediatorException exception : info.exceptions){
			addException(exception);
		}
	}

	protected void setCommissioningProfile(String profileId, Map<String, Double> values){
		hideProfileFields();
		if(profileId == null) {
			//do nothing
		}else if(profileId.equalsIgnoreCase(BigBangConstants.TypifiedListValues.MEDIATOR_COMMISSIONING_PROFILE.NEGOTIATED)){
			this.setCommissioningSections(values);
		}else if(profileId.equalsIgnoreCase(BigBangConstants.TypifiedListValues.MEDIATOR_COMMISSIONING_PROFILE.PERCENTAGE)){
			this.commissionPercentage.setVisible(true);
			newException.setVisible(!isReadOnly());
		}
	}

	protected void hideProfileFields(){
		this.commissionPercentage.setVisible(false);

		for(MediatorCategoryFormSection section : this.categorySections) {
			this.removeSection(section);
			section.removeFromParent();
		}

		for(MediatorExceptionSection section : this.exceptionsSection){
			this.removeSection(section);
			section.removeFromParent();
		}
		this.exceptionsSection.clear();
		this.categorySections.clear();

		newException.setVisible(false);

	}

	protected void setCommissioningSections(final Map<String, Double> values) {
		hideProfileFields();

		CoverageBroker coverageBroker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);

		coverageBroker.getCategories(new ResponseHandler<Category[]>() {

			@Override
			public void onResponse(Category[] response) {
				for(Category cat : response) {
					MediatorCategoryFormSection section = new MediatorCategoryFormSection(cat, values);
					section.collapse();
					addSection(section);
					categorySections.add(section);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	protected void addException(MediatorException exception){

		final MediatorExceptionSection section = new MediatorExceptionSection();		
		section.setReadOnly(this.isReadOnly());
		this.exceptionsSection.add(section);
		addSection(section);

		section.getRemoveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeItemAndSection(section);
			}
		});

		section.setException(exception);
		section.removeButton.setVisible(!isReadOnly());
		addSection(newException);
	}

	protected void removeItemAndSection(MediatorExceptionSection section) {
		section.removeFromParent();
		exceptionsSection.remove(section);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		if(newException != null)
			newException.setVisible(!readOnly && comissionProfile.getValue().equalsIgnoreCase(BigBangConstants.TypifiedListValues.MEDIATOR_COMMISSIONING_PROFILE.PERCENTAGE));

		if(exceptionsSection != null)
			for(MediatorExceptionSection section : exceptionsSection){
				section.removeButton.setVisible(!readOnly);
			}
		if(newException != null)
			newException.setVisible(!readOnly);
	}
}
