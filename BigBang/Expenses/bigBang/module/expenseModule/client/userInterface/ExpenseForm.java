package bigBang.module.expenseModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExpenseForm extends FormView<Expense>{

	private ExpandableListBoxFormField manager;
	private NumericTextBoxFormField settlement;
	private Label settleLabel;
	private Button settleButton;
	private TextAreaFormField notes;
	private NavigationFormField client;
	private DatePickerFormField expenseDate;
	private ExpandableListBoxFormField coverageId;
	private NumericTextBoxFormField value;
	private TextBoxFormField isOpen;
	private NavigationFormField reference;
	private TextBoxFormField number;
	private boolean initialized;
	private boolean tempIsManual;
	protected ExpandableListBoxFormField insuredObject;
	protected RadioButtonFormField belongsToPolicy;
	protected TextBoxFormField insuredObjectName; 

	public ExpenseForm() {

		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Despesa");
		settlement = new NumericTextBoxFormField("Indemnização", true);
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
		expenseDate.setMandatory(true);
		value = new NumericTextBoxFormField("Valor", true);
		value.setMandatory(true);
		value.setFieldWidth("175px");
		value.setUnitsLabel("€");
		isOpen = new TextBoxFormField("Estado");
		isOpen.setFieldWidth("175px");

		insuredObject = new ExpandableListBoxFormField("Unidade de Risco");
		insuredObject.setWidth("250px");
		insuredObject.setFieldWidth("231px");
		insuredObjectName = new TextBoxFormField("Unidade de Risco");
		insuredObjectName.setFieldWidth("250px");
		belongsToPolicy = new RadioButtonFormField(true);
		belongsToPolicy.addOption("true", "Presente na apólice");
		belongsToPolicy.addOption("false", "Outra");

		coverageId = new ExpandableListBoxFormField("Cobertura");
		coverageId.setMandatory(true);

		addSection("Despesa de saúde");
		addFormField(client, false);
		addFormField(reference);

		addFormFieldGroup(new FormField<?>[]{
				number,
				isOpen,
				manager,
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				coverageId,
				expenseDate,	
		}, true);


		addFormField(insuredObject, true);
		addFormField(insuredObjectName, true);
		addFormField(belongsToPolicy, true);

		belongsToPolicy.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equalsIgnoreCase("true")){
					insuredObject.setVisible(true);
					insuredObjectName.setVisible(false);
				}else{
					insuredObject.setVisible(false);
					insuredObjectName.setVisible(true);
				}
				insuredObjectName.setValue(null);
				insuredObject.setValue(null);
			}
		});

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

		addLineBreak();

		addFormField(value, true);

		addWidget(settlementPanel, true);


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

		belongsToPolicy.setValue("true");
	}

	@Override
	public Expense getInfo() {

		Expense newExpense = super.value;
		newExpense.managerId = manager.getValue();	
		newExpense.settlement = settlement.getValue();
		newExpense.notes = notes.getValue();
		newExpense.expenseDate = expenseDate.getStringValue();
		newExpense.value = value.getValue();
		newExpense.isManual = tempIsManual;
		newExpense.coverageId = coverageId.getValue();
		newExpense.insuredObjectId = insuredObject.getValue();
		newExpense.insuredObjectName = insuredObjectName.getValue();
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

		if(info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
			referenceItem.pushIntoStackParameter("display", "search");
			referenceItem.setParameter("policyid", info.referenceId);
			reference.setValue(referenceItem);
			reference.setValueName("#"+info.referenceNumber + " - " + info.categoryName + " / " + info.lineName + " / " + info.subLineName);
		}else if(info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			referenceItem.pushIntoStackParameter("display", "subpolicy");
			referenceItem.setParameter("subpolicyid", info.referenceId);
			reference.setValue(referenceItem);
			reference.setValueName("#"+info.referenceNumber + " - " + info.categoryName + " / " + info.lineName + " / " + info.subLineName);
		}

		String listId = info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? 
				BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT+"/"+info.referenceId 
				: BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS +"/"+info.referenceId;

		insuredObject.setListId(listId, new ResponseHandler<Void>() {
			@Override
			public void onResponse(Void response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de unidades de risco."), TYPE.ALERT_NOTIFICATION));
			}
		});

		listId = BigBangConstants.EntityIds.COVERAGE+"/"+info.referenceSubLineId;

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
		coverageId.setValue(info.coverageId);

		settlement.setValue(info.settlement);
		isOpen.setValue(info.isOpen ? "Aberta" : "Fechada");
		value.setValue(info.value);

		if(info.insuredObjectName != null){
			belongsToPolicy.setValue("false", true);
			insuredObjectName.setValue(info.insuredObjectName);
		}else{
			belongsToPolicy.setValue("true", true);
			insuredObject.setValue(info.insuredObjectId);
		}
	}

	private void setManualSettlement(boolean isManual) {
		tempIsManual = isManual;
		settleButton.setVisible(!isManual);
		settleLabel.setVisible(!isManual);
		settlement.setEditable(isManual);
		settlement.setMandatory(isManual);
		if(isReadOnly()){
			settlement.setReadOnly(true);
		}
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
