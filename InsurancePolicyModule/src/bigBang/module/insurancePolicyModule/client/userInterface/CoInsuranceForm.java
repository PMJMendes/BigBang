package bigBang.module.insurancePolicyModule.client.userInterface;

import java.io.Serializable;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CoInsuranceForm extends View{

	public enum Action{
		ADD_NEW_INSURANCE_AGENCY

	}

	public class CoInsuranceAgencyInfo implements Serializable{

		private static final long serialVersionUID = 1L;
		public String typeId = "";
		public String value = "";

		public CoInsuranceAgencyInfo(){
			super();
		}

		public CoInsuranceAgencyInfo(String agencyID, String percentage){
			this.typeId = agencyID;
			this.value = percentage;
		}

	}

	public class CoInsuranceListEntry extends ListEntry<CoInsuranceAgencyInfo>{


		private ExpandableListBoxFormField agency;
		private TextBoxFormField infoValue;
		private Button remove;
		public CoInsuranceListEntry(CoInsuranceAgencyInfo coInsuranceInfo) {
			super(coInsuranceInfo);
			setHeight("40px");
		}

		public TextBoxFormField getInfoValue(){
			return infoValue;
		}

		public CoInsuranceAgencyInfo getValue(){
			return super.getValue();
		}
		
		@Override
		public void setValue(CoInsuranceAgencyInfo coInsuranceAgencyInfo) {

			super.setValue(coInsuranceAgencyInfo);
			if(coInsuranceAgencyInfo == null){
				Button add = new Button("Adicionar Seguradora");
				add.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						addNewInsuranceAgency();

					}
				});
				add.setWidth("180px");
				this.setLeftWidget(add);
				super.setValue(coInsuranceAgencyInfo);
				return;	

			}

			agency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "");
			agency.setFieldWidth("200px");
			infoValue = new TextBoxFormField();
			infoValue.setFieldWidth("25px");
			infoValue.setWidth("25px");
			agency.setValue(coInsuranceAgencyInfo.typeId);
			infoValue.setValue(coInsuranceAgencyInfo.value);
			infoValue.setUnitsLabel("%");

			remove = new Button("X");
			remove.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					fireEvent(new DeleteRequestEvent(getValue()));
				}
			});
			this.setLeftWidget(agency);
			this.setWidget(infoValue);
			this.setRightWidget(remove);


		}

		protected int getPercentage(){
			return infoValue.getValue().length() > 0 ? Integer.parseInt(infoValue.getValue()) : 0;
		}

		protected void setPercentage(int percentage){
			infoValue.setValue(percentage+"");
			if(percentage >= 100 || percentage <= 100){
				infoValue.getElement().getStyle().setColor("red");
			}
		}

		protected void addNewInsuranceAgency() {

			CoInsuranceListEntry temp = new CoInsuranceListEntry(new CoInsuranceAgencyInfo());
			coInsuranceAgencies.remove(coInsuranceAgencies.size()-1);
			temp.setEditable(true);
			coInsuranceAgencies.add(new CoInsuranceListEntry(temp.getValue()));

			((CoInsuranceListEntry)coInsuranceAgencies.get(coInsuranceAgencies.size()-1)).getInfoValue().addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {

					int percentage = Integer.parseInt(event.getValue());
					
					
					if(percentage >= 100 || percentage <= 0){
						((TextBoxFormField)event.getSource()).getElement().getStyle().setColor("red");
					}else
						((TextBoxFormField)event.getSource()).getElement().getStyle().clearColor();
					
					refreshPercentageValues();

				}
			});

			coInsuranceAgencies.add(new CoInsuranceListEntry(null));
			coInsuranceAgencies.getScrollable().scrollToBottom();

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

	}

	List<CoInsuranceAgencyInfo> coInsuranceAgencies;
	VerticalPanel wrapper = new VerticalPanel();



	public CoInsuranceForm() {

		initWidget(wrapper);
		wrapper.setSize("340px", "200px");
		coInsuranceAgencies = new List<CoInsuranceAgencyInfo>();
		coInsuranceAgencies.getScrollable().setSize("340px", "200px");
		coInsuranceAgencies.setSelectableEntries(false);
		CoInsuranceListEntry temp = new CoInsuranceListEntry(null);
		coInsuranceAgencies.add(temp);

		wrapper.add(coInsuranceAgencies.getScrollable());

	}

	public void setMainCoInsuranceAgency(String insurangeAgencyId, String percentage){

		CoInsuranceListEntry first = new CoInsuranceListEntry(new CoInsuranceAgencyInfo(insurangeAgencyId, percentage));
		first.setEditable(false);
		coInsuranceAgencies.add(0, first);

	}


	protected void refreshPercentageValues() {

		int sum = 0;

		for (int i = 1; i<coInsuranceAgencies.size()-1; i++){
			sum += ((CoInsuranceListEntry)coInsuranceAgencies.get(i)).getPercentage();
		}

		((CoInsuranceListEntry)coInsuranceAgencies.get(0)).setPercentage(100-sum);
	}



	@Override
	protected void initializeView() {
		return;
	}



}
