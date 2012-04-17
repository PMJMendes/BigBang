package bigBang.module.expenseModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HealthExpense;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ExpenseForm extends FormView<HealthExpense>{

	private ExpandableListBoxFormField manager;
	private TextBoxFormField settlement;
	private TextAreaFormField notes;
	private TextBoxFormField clientName;
	private DatePickerFormField expenseDate;
	private ExpandableListBoxFormField insuredObjectId;
	private ExpandableListBoxFormField coverageId;
	private TextBoxFormField value;
	private TextBoxFormField isOpen;
	private TextBoxFormField referenceNumber;
	private TextBoxFormField number;
	private boolean initialized;

	public ExpenseForm() {

		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		settlement = new TextBoxFormField("Indemnização");
		settlement.setUnitsLabel("€");
		settlement.setFieldWidth("175px");
		referenceNumber = new TextBoxFormField("Apólice");
		referenceNumber.setFieldWidth("400px");

		number = new TextBoxFormField("Número");
		number.setFieldWidth("175px");

		clientName = new TextBoxFormField("Cliente"); 
		expenseDate = new DatePickerFormField("Data");
		insuredObjectId = new ExpandableListBoxFormField("Unidade de Risco");
		value = new TextBoxFormField("Valor");
		value.setFieldWidth("175px");
		value.setUnitsLabel("€");
		isOpen = new TextBoxFormField("Estado");
		isOpen.setFieldWidth("175px");

		coverageId = new ExpandableListBoxFormField("Cobertura");

		addSection("Despesa de saúde");
		clientName.setFieldWidth("400px");
		addFormField(clientName, false);
		addFormField(referenceNumber);

		addFormFieldGroup(new FormField<?>[]{
				number,
				isOpen,
				manager,
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				insuredObjectId,	
				coverageId,
				expenseDate,	
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				value,
				settlement,
		}, true);


		notes = new TextAreaFormField();
		addSection("Notas");
		addFormField(notes);
		notes.setFieldWidth("400px");

		clientName.setReadOnly(true);
		referenceNumber.setReadOnly(true);
		number.setReadOnly(true);
		isOpen.setReadOnly(true);
		manager.setReadOnly(true);
		
		initialized = true;

	}

	@Override
	public HealthExpense getInfo() {

		HealthExpense newExpense = super.value;
		newExpense.managerId = manager.getValue();
		newExpense.settlement = settlement.getValue();
		newExpense.notes = notes.getValue();
		newExpense.expenseDate = expenseDate.getStringValue();
		newExpense.insuredObjectId = insuredObjectId.getValue();
		newExpense.value = value.getValue();

		return newExpense;

	}

	@Override
	public void setInfo(HealthExpense info) {

		if(info == null) {
			clearInfo();
			return;
		}

		manager.setValue(info.managerId);
		settlement.setValue(info.settlement);
		notes.setValue(info.notes);
		clientName.setValue("#" + info.clientNumber + " - " + info.clientName);
		expenseDate.setValue(info.expenseDate);

		referenceNumber.setValue("#"+info.referenceNumber + " - " + info.categoryName + "/" + info.lineName + "/" + info.subLineName);

		String listId = info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? 
				BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECTS+"/"+info.referenceId 
				: BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECT +"/"+info.referenceId;


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
		
		listId = BigBangConstants.EntityIds.COVERAGE+"/"+info.referenceId;
		coverageId.setListId(info.referenceId, new ResponseHandler<Void>() {
			
			@Override
			public void onResponse(Void response) {
				return;
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de coberturas."), TYPE.ALERT_NOTIFICATION));
			}
		});

		isOpen.setValue(info.isOpen ? "Aberta" : "Fechada");
		value.setValue(info.value);
	}

	@Override
	public void setReadOnly(boolean readonly) {

		if(!initialized)
			return;


		coverageId.setReadOnly(readonly);
		expenseDate.setReadOnly(readonly);
		value.setReadOnly(readonly);
		settlement.setReadOnly(readonly);
		notes.setReadOnly(readonly);

	};

}
