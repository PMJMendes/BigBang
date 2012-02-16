package bigBang.module.insurancePolicyModule.client.userInterface;

import java.io.Serializable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.client.userInterface.view.ContactView.ContactEntry;

public class CoInsuranceForm extends View{
	
	public enum Action{
		ADD_NEW_INSURANCE_AGENCY
		
	}

	public class CoInsuranceAgencyInfo implements Serializable{
		
		private static final long serialVersionUID = 1L;
		public String typeId = "";
		public String value = "";

	}

	public class CoInsuranceListEntry extends ListEntry<CoInsuranceAgencyInfo>{


		protected ExpandableListBoxFormField agency;
		protected TextBoxFormField infoValue;
		private Button remove;
		public CoInsuranceListEntry(CoInsuranceAgencyInfo contactinfo) {
			super(contactinfo);
		}

		@Override
		public void setValue(CoInsuranceAgencyInfo coInsuranceAgencyInfo) {

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
			super.setValue(coInsuranceAgencyInfo);
		}

		protected void addNewInsuranceAgency() {
			
			CoInsuranceListEntry temp = new CoInsuranceListEntry(new CoInsuranceAgencyInfo());
			temp.setHeight("40px");
			coInsuranceAgencies.remove(coInsuranceAgencies.size()-1);
			temp.setEditable(true);
			coInsuranceAgencies.add(new CoInsuranceListEntry(temp.getValue()));
			coInsuranceAgencies.add(new CoInsuranceListEntry(null));
			
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


		@Override
		public CoInsuranceAgencyInfo getValue() {

			return super.getValue();
		}


	}
	
	private ActionInvokedEventHandler<Action> actionHandler;
	List<CoInsuranceAgencyInfo> coInsuranceAgencies;
	VerticalPanel wrapper = new VerticalPanel();
	
	
	
	public CoInsuranceForm() {

		initWidget(wrapper);
		wrapper.setSize("340px", "200px");
		coInsuranceAgencies = new List<CoInsuranceAgencyInfo>();
		coInsuranceAgencies.getScrollable().setSize("340px", "200px");
		coInsuranceAgencies.setSelectableEntries(false);
		
		CoInsuranceListEntry temp = new CoInsuranceListEntry(null);
		temp.setHeight("40px");
		coInsuranceAgencies.add(temp);
		
		wrapper.add(coInsuranceAgencies.getScrollable());
		
	}
	
	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	protected void initializeView() {
		return;
	}




}
