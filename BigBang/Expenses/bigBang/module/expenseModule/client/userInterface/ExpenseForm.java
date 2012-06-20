package bigBang.module.expenseModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.insurancePolicyModule.client.dataAccess.PolicyTypifiedListBroker;
import bigBang.module.insurancePolicyModule.client.dataAccess.SubPolicyTypifiedListBroker;

public class ExpenseForm extends FormView<Expense>{

	private ExpandableListBoxFormField manager;
	private TextBoxFormField settlement;
	private Label settleLabel;
	private Button settleButton;
	private TextAreaFormField notes;
	private NavigationFormField client;
	private DatePickerFormField expenseDate;
	private ExpandableListBoxFormField insuredObjectId;
	private ExpandableListBoxFormField coverageId;
	private NumericTextBoxFormField value;
	private TextBoxFormField isOpen;
	private NavigationFormField reference;
	private TextBoxFormField number;
	private boolean initialized;
	private boolean tempIsManual;

	public ExpenseForm() {

		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		settlement = new TextBoxFormField("Indemnização");
		settlement.setUnitsLabel("€");
		settlement.setFieldWidth("175px");
		settleButton = new Button("Substituir");
		settleButton.setHeight("22px");
		settleLabel = new Label();
		settleLabel.setText("Calculado Automaticamente");
		reference = new NavigationFormField("Apólice");

		number = new TextBoxFormField("Número");
		number.setFieldWidth("175px");

		client = new NavigationFormField("Cliente"); 
		expenseDate = new DatePickerFormField("Data");
		insuredObjectId = new ExpandableListBoxFormField("Unidade de Risco");
		value = new NumericTextBoxFormField("Valor");
		value.setFieldWidth("175px");
		value.setUnitsLabel("€");
		isOpen = new TextBoxFormField("Estado");
		isOpen.setFieldWidth("175px");

		coverageId = new ExpandableListBoxFormField("Cobertura");

		addSection("Despesa de saúde");
		addFormField(client, false);
		addFormField(reference);

		addFormFieldGroup(new FormField<?>[]{
				number,
				isOpen,
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				manager,
				insuredObjectId,
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				coverageId,
				expenseDate,	
		}, false);



		HorizontalPanel settlementPanel = new HorizontalPanel();
		VerticalPanel settlementVerticalPanel = new VerticalPanel();
		settlementPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		settlementPanel.add(settlement);
		registerFormField(settlement);
		settlementVerticalPanel.add(settleLabel);
		settlementVerticalPanel.add(settleButton);
		settlementPanel.add(settlementVerticalPanel);
		settleButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				settlement.setEditable(true);
				settlement.setReadOnly(false);
				setManualSettlement(true);
			}
		});

		addFormFieldGroup(new FormField<?>[]{
				value	
		}, false);
		addWidget(settlementPanel, false);


		notes = new TextAreaFormField();
		addSection("Notas");
		addFormField(notes);
		notes.setFieldWidth("400px");

		client.setEditable(false);
		reference.setEditable(false);
		number.setEditable(false);
		isOpen.setEditable(false);
		manager.setEditable(false);
		settleButton.setEnabled(false);
		initialized = true;
	}

	@Override
	public Expense getInfo() {

		Expense newExpense = super.value;
		newExpense.managerId = manager.getValue();	
		newExpense.settlement = settlement.getValue();
		newExpense.notes = notes.getValue();
		newExpense.expenseDate = expenseDate.getStringValue();
		newExpense.insuredObjectId = insuredObjectId.getValue();
		newExpense.value = value.getValue();
		newExpense.isManual = tempIsManual;
		newExpense.coverageId = coverageId.getValue();
		return newExpense;

	}

	@Override
	public void setInfo(Expense info) {

		if(info == null) {
			clearInfo();
			return;
		}

		manager.setValue(info.managerId);
		settlement.setValue(info.settlement);
		setManualSettlement(info.isManual);

		number.setValue(info.number);

		notes.setValue(info.notes);

		NavigationHistoryItem clientItem = new NavigationHistoryItem();
		clientItem.setParameter("section", "client");
		clientItem.setStackParameter("display");
		clientItem.pushIntoStackParameter("display", "search");
		clientItem.setParameter("clientid", info.clientId);
		client.setValue(clientItem);
		client.setValueName("#" + info.clientNumber + " - " + info.clientName);
		
		expenseDate.setValue(info.expenseDate);


		NavigationHistoryItem referenceItem = new NavigationHistoryItem();
		referenceItem.setParameter("section", "insurancePolicy");
		referenceItem.setStackParameter("display");
		referenceItem.pushIntoStackParameter("display", "search");
		referenceItem.setParameter("policyid", info.referenceId);
		reference.setValue(referenceItem);
		reference.setValueName("#"+info.referenceNumber + " - " + info.categoryName + " / " + info.lineName + " / " + info.subLineName);

		String listId = info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? 
				BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT+"/"+info.referenceId 
				: BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS +"/"+info.referenceId;


		insuredObjectId.setListId(listId, new ResponseHandler<Void>() {
			@Override
			public void onResponse(Void response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de unidades de risco."), TYPE.ALERT_NOTIFICATION));
			}
		});

		listId = info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? 
				BigBangConstants.TypifiedListIds.POLICY_COVERAGE+"/"+info.referenceId 
				: BigBangConstants.TypifiedListIds.SUB_POLICY_COVERAGE +"/"+info.referenceId;
		
		BigBangTypifiedListBroker listBroker = info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? 
				PolicyTypifiedListBroker.Util.getInstance() : SubPolicyTypifiedListBroker.Util.getInstance();
				
		coverageId.setTypifiedDataBroker(listBroker);
		coverageId.setListId(listId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de coberturas."), TYPE.ALERT_NOTIFICATION));
			}
		});
		settlement.setValue(info.settlement);
		isOpen.setValue(info.isOpen ? "Aberta" : "Fechada");
		value.setValue(info.value);
	}

	private void setManualSettlement(boolean isManual) {
		tempIsManual = isManual;
		settleButton.setVisible(!isManual);
		settleLabel.setVisible(!isManual);
		settlement.setEditable(isManual);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		super.setReadOnly(readonly); 
		if(!initialized)
			return;
		settleButton.setEnabled(!readonly);
	};

	public void setNewExpenseMode(){
		settlement.setEditable(false);
		manager.setEditable(true);
		settleButton.setEnabled(true);
	}

	public void setUpdateMode(){
		manager.setEditable(false);
	}


}
