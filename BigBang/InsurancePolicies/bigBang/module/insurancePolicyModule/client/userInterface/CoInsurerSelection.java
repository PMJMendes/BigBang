package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy.CoInsurer;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.NumericTextBoxFormField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CoInsurerSelection extends FormField<CoInsurer[]>{

	public class CoInsuranceListEntry extends ListEntry<CoInsurer>{

		protected Button add;
		protected boolean isButton = false;
		protected ExpandableListBoxFormField agency;
		protected NumericTextBoxFormField infoValue;
		private Button remove;

		public CoInsuranceListEntry(CoInsurer coInsuranceInfo) {
			super(coInsuranceInfo);
			setHeight("40px");
		}

		@Override
		public CoInsurer getValue(){
			return super.getValue();
		}

		@Override
		public void setValue(CoInsurer coInsuranceAgencyInfo) {

			
			
			if(coInsuranceAgencyInfo == null){
				isButton = true;
				add = new Button("Adicionar Co-Seguradora");
				add.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						addNewCoInsuranceAgency();

					}
				});
				add.setWidth("180px");
				this.setLeftWidget(add);
				super.setValue(coInsuranceAgencyInfo);
				return;	
			}

			agency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "");
			agency.setFieldWidth("190px");
			infoValue = new NumericTextBoxFormField(false);
			infoValue.setFieldWidth("40px");
			infoValue.setWidth("40px");
			agency.setValue(coInsuranceAgencyInfo.insuranceAgencyId);
			infoValue.setValue(coInsuranceAgencyInfo.percent);
			infoValue.setUnitsLabel("%");

			remove = new Button("X");
			remove.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeInsuranceCompany(new DeleteRequestEvent(getValue()));
				}
			});
			this.setLeftWidget(agency);
			this.setWidget(infoValue);
			this.setRightWidget(remove);
			super.setValue(coInsuranceAgencyInfo);

		}


		protected Double getPercentage(){

			try{
				if(infoValue.getValue() > 0){
					return infoValue.getValue();
				}else
					return 0.0;
			}catch (Exception e) {
				return 0.0;
			}
		}

		protected void setPercentage(double percentage){

			value.percent = percentage;
			infoValue.setValue(value.percent);

			if(percentage >= 100 || percentage <= 0){
				((TextBox)infoValue.getTextBox()).getElement().getStyle().setColor("red");
			}else
				((TextBox)infoValue.getTextBox()).getElement().getStyle().clearColor();
		}


		public void setEditable(boolean editable){

			if(agency == null){
				this.setVisible(editable);
				return;
			}
			agency.setReadOnly(!editable);
			infoValue.setReadOnly(!editable);
			remove.setVisible(editable);

		}
		public void addPercentageValueChangeHandler() {

			infoValue.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {

					boolean error = false;

					try{
						value.percent = infoValue.getValue();
					}
					catch(NumberFormatException e){
						error = true;
					}



					if(value.percent >= 100 || value.percent <= 0 || error){
						infoValue.getTextBox().getElement().getStyle().setColor("red");
					}else
						infoValue.getTextBox().getElement().getStyle().clearColor();

					refreshPercentageValues();

				}
			});

		}

		private void addAgencyIdValueChangeHandler() {


			agency.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {

					if(event.getValue() != null && event.getValue().length() > 0){
						for(int i = 0; i<coInsuranceAgencies.size()-1; i++){
							if(coInsuranceAgencies.get(i).getValue().insuranceAgencyId != null){
								if(!((ExpandableListBoxFormField)event.getSource()).equals(((CoInsuranceListEntry)coInsuranceAgencies.get(i)).agency) 
										&& coInsuranceAgencies.get(i).getValue().insuranceAgencyId.equalsIgnoreCase(((ExpandableListBoxFormField)event.getSource()).getValue())){
									EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Essa co-seguradora já foi seleccionada."), TYPE.ALERT_NOTIFICATION));

									if(((ExpandableListBoxFormField)event.getSource()).isReadOnly()){
										((CoInsuranceListEntry)coInsuranceAgencies.get(i)).agency.setValue("");
										((CoInsuranceListEntry)coInsuranceAgencies.get(i)).getValue().insuranceAgencyId = "";
										((CoInsuranceListEntry)coInsuranceAgencies.get(i)).setPercentageEditable(false);
										break;
									}else{
										((ExpandableListBoxFormField)event.getSource()).setValue("");
										CoInsuranceListEntry.this.setPercentageEditable(false);
										return;
									}
								}
							}
						}
						value.insuranceAgencyId = event.getValue();
						CoInsuranceListEntry.this.setPercentageEditable(true);
					}
					else{
						CoInsuranceListEntry.this.setPercentageEditable(false);
					}
				}
			});
		}

		protected void setPercentageEditable(boolean b) {
			if(!agency.isReadOnly()){
				infoValue.setReadOnly(!b);
				if(!b){
					infoValue.setValue(null);
					refreshPercentageValues();
				}
			}
		}

		public boolean isButton() {
				return isButton;
		}
		
	}

	protected void addNewCoInsuranceAgency() {


		CoInsurer coInsurer = new CoInsurer();
		coInsurer.insuranceAgencyId = new String("");
		coInsurer.percent = 0.0;

		CoInsuranceListEntry temp = new CoInsuranceListEntry(coInsurer);
		coInsuranceAgencies.remove(coInsuranceAgencies.size()-1);
		temp.setEditable(true);
		temp.setPercentageEditable(false);
		coInsuranceAgencies.add(temp);

		((TextBox)temp.infoValue.getTextBox()).getElement().getStyle().setColor("red");

		temp.addPercentageValueChangeHandler();
		temp.addAgencyIdValueChangeHandler();


		coInsuranceAgencies.add(new CoInsuranceListEntry(null));
		coInsuranceAgencies.getScrollable().scrollToBottom();

	}


	List<CoInsurer> coInsuranceAgencies;
	CoInsuranceListEntry mainAgency;
	VerticalPanel wrapper = new VerticalPanel();

	public CoInsurerSelection() {

		super();
		initWidget(wrapper);
		wrapper.setSize("350px", "200px");
		coInsuranceAgencies = new List<CoInsurer>();
		coInsuranceAgencies.getScrollable().setSize("100%", "100%");
		coInsuranceAgencies.setSelectableEntries(false);
		wrapper.add(coInsuranceAgencies.getScrollable());


	}

	protected void addCoInsuranceAgency(CoInsuranceListEntry entry){

		coInsuranceAgencies.add(entry);
		entry.addPercentageValueChangeHandler();
		entry.addAgencyIdValueChangeHandler();
	}


	public void setMainCoInsuranceAgency(String insurangeAgencyId){


		if(coInsuranceAgencies.size() == 0){
			CoInsurer temp = new CoInsurer();
			temp.insuranceAgencyId = insurangeAgencyId;
			CoInsuranceListEntry first = new CoInsuranceListEntry(temp);
			first.setEditable(false);
			first.addAgencyIdValueChangeHandler();
			coInsuranceAgencies.add(0, first);
			coInsuranceAgencies.add(new CoInsuranceListEntry(null));
		}
		else{
			((CoInsuranceListEntry)coInsuranceAgencies.get(0)).agency.setValue(insurangeAgencyId);
			((CoInsuranceListEntry)coInsuranceAgencies.get(0)).getValue().insuranceAgencyId = insurangeAgencyId;
		}

		mainAgency = ((CoInsuranceListEntry)coInsuranceAgencies.get(0));
		refreshPercentageValues();

	}


	protected void refreshPercentageValues() {

		float sum = 0;

		for (int i = 1; i<coInsuranceAgencies.size()-1; i++){
			sum += ((CoInsuranceListEntry)coInsuranceAgencies.get(i)).getPercentage();
		}

		((CoInsuranceListEntry)coInsuranceAgencies.get(0)).setPercentage(100-sum);
	}

	protected void removeInsuranceCompany(
			DeleteRequestEvent deleteRequestEvent) {

		for (int i = 1; i<coInsuranceAgencies.size()-1; i++){
			if(coInsuranceAgencies.get(i).getValue() == deleteRequestEvent.getObject()){
				coInsuranceAgencies.remove(i);
				break;
			}
		}
		refreshPercentageValues();

	}


	private boolean readOnly = false;

	@Override
	public void clear() {
		coInsuranceAgencies.clear();
	}

	@Override
	protected void setReadOnlyInternal(boolean readonly) {

		readOnly = readonly;

		for(int i = 1; i < coInsuranceAgencies.size()-1; i++){
			((CoInsuranceListEntry)coInsuranceAgencies.get(i)).setEditable(!readonly);
		}
		if(coInsuranceAgencies.size()>0){
			coInsuranceAgencies.get(coInsuranceAgencies.size()-1).setVisible(!readonly);
		}

	}


	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		return;
	}

	@Override
	public CoInsurer[] getValue(){	
		
		if(coInsuranceAgencies.size() < 3){
			return null;
		}
		
		for(int i = 1; i<coInsuranceAgencies.size()-1; i++){
			if(((CoInsuranceListEntry)coInsuranceAgencies.get(i)).getValue().insuranceAgencyId.isEmpty() || ((CoInsuranceListEntry)coInsuranceAgencies.get(i)).getValue().percent == null){
				coInsuranceAgencies.remove(i);
				i--;
			}
		}
		
		
		CoInsurer[] coInsurers = new CoInsurer[coInsuranceAgencies.size()-2];

		for(int i = 0; i<coInsurers.length; i++){
			coInsurers[i] = coInsuranceAgencies.get(i+1).getValue();
		}

		return coInsurers;
	}

	@Override
	public void setValue(CoInsurer[] coInsurers){

		coInsuranceAgencies.clear();
		setMainCoInsuranceAgency(mainAgency.getValue().insuranceAgencyId);
		coInsuranceAgencies.remove(1);


		for(int i = 0; i<coInsurers.length; i++){

			CoInsurer temp = new CoInsurer();
			temp.insuranceAgencyId = coInsurers[i].insuranceAgencyId;
			temp.percent = coInsurers[i].percent;

			CoInsuranceListEntry entry = new CoInsuranceListEntry(temp);
			addCoInsuranceAgency(entry);
		}

		coInsuranceAgencies.add(new CoInsuranceListEntry(null));
		coInsuranceAgencies.getScrollable().scrollToBottom();

		refreshPercentageValues();
		//setReadOnly(true);
	}

	public List<CoInsurer> getCoInsuranceAgenciesList() {
		return coInsuranceAgencies;
	}
	
	@Override
	public void focus() {
		if(!coInsuranceAgencies.isEmpty()){
			if(((CoInsuranceListEntry) coInsuranceAgencies.get(coInsuranceAgencies.size()-1)).isButton()){
				coInsuranceAgencies.get(coInsuranceAgencies.size()-1).getLeftWidget().getElement().focus();
			}
		}
	}

}
