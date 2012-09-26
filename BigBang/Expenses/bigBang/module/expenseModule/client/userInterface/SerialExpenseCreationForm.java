package bigBang.module.expenseModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.InsuranceSubPolicySelectionView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class SerialExpenseCreationForm extends FormView <ExpensePolicyWrapper>{

	private TextBoxFormField policyNumber;
	private Button verifyPolicyNumber;
	private final static Label policyNumberProblem = new Label("A apólice pretendida não existe");
	private Button markAsInvalid;

	private TextBoxFormField subscriber;
	private CheckBoxFormField noSubPolicy;
	private ExpandableSelectionFormField subPolicyReference;
	private InsuranceSubPolicySelectionViewPresenter subPolicySelectionPanel;

	private ExpandableListBoxFormField manager;
	private NumericTextBoxFormField settlement;
	private Label settleLabel;
	private Button settleButton;
	private TextAreaFormField notes;
	private DatePickerFormField expenseDate;
	private ExpandableListBoxFormField coverageId;
	private NumericTextBoxFormField expenseValue;
	private boolean tempIsManual;

	protected ExpandableListBoxFormField insuredObject;
	protected RadioButtonFormField belongsToPolicy;
	protected TextBoxFormField insuredObjectName; 

	public SerialExpenseCreationForm(){

		addSection("Número da apólice");
		policyNumber = new TextBoxFormField("Número da apólice");
		policyNumber.setFieldWidth("175px");
		policyNumber.setMandatory(true);
		policyNumber.getNativeField().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					onClickVerifyPolicyNumber();
				}
				else if(isEditKey(event.getNativeKeyCode())){
					onChangedPolicyNumber();
				}
			}
		});

		addFormField(policyNumber, true);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verifyPolicyNumber = new Button("Verificar");
		buttonPanel.add(verifyPolicyNumber);
		verifyPolicyNumber.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClickVerifyPolicyNumber();
			}
		});

		markAsInvalid = new Button("Saltar despesa de saúde");
		buttonPanel.setSpacing(5);
		buttonPanel.setHeight("55px");
		buttonPanel.add(markAsInvalid);

		markAsInvalid.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClickMarkAsInvalid();
			}
		});

		policyNumberProblem.getElement().getStyle().setColor("RED");
		buttonPanel.add(policyNumberProblem);
		addWidget(buttonPanel, false);

		addSection("Número da apólice adesão");

		subPolicySelectionPanel = new InsuranceSubPolicySelectionViewPresenter((InsuranceSubPolicySelectionView) GWT.create(InsuranceSubPolicySelectionView.class));
		subPolicySelectionPanel.go();
		subPolicyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, "Apólice Adesão", subPolicySelectionPanel);
		subPolicyReference.setReadOnly(false);

		subPolicyReference.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onSubPolicyValueChanged();

			}
		});

		subscriber = new TextBoxFormField("Beneficiário");

		noSubPolicy = new CheckBoxFormField("Sem apólice adesão (usar apólice principal)");

		noSubPolicy.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onNoSubPolicyChange(event.getValue());
				noSubPolicyChangedState();
			}
		});

		addFormField(subPolicyReference, true);
		addFormField(subscriber, false);
		addFormField(noSubPolicy);

		addSection("Despesa de Saúde");

		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Despesa");
		manager.setMandatory(true);

		settlement = new NumericTextBoxFormField("Indemnização", true);
		settlement.setUnitsLabel("€");
		settlement.setFieldWidth("175px");
		settlement.setMandatory(true);
		settleButton = new Button("Substituir");
		settleButton.setHeight("22px");
		settleLabel = new Label();
		settleLabel.setText("Calculado Automaticamente");

		expenseDate = new DatePickerFormField("Data");
		expenseDate.setMandatory(true);

		insuredObject = new ExpandableListBoxFormField("Unidade de Risco");
		insuredObject.setWidth("250px");
		insuredObject.setFieldWidth("231px");
		insuredObjectName = new TextBoxFormField("Unidade de Risco");
		insuredObjectName.setFieldWidth("250px");
		belongsToPolicy = new RadioButtonFormField(true);
		belongsToPolicy.addOption("true", "Presente na apólice");
		belongsToPolicy.addOption("false", "Outra");

		expenseValue = new NumericTextBoxFormField("Valor", true);
		expenseValue.setFieldWidth("175px");
		expenseValue.setUnitsLabel("€");
		expenseValue.setMandatory(true);

		coverageId = new ExpandableListBoxFormField("Cobertura");

		addFormFieldGroup(new FormField<?>[]{
				expenseValue,
				manager,
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				coverageId,
				expenseDate,	
		}, false);

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

		addWidget(settlementPanel);


		notes = new TextAreaFormField();
		addSection("Notas");
		addFormField(notes);
		notes.setFieldWidth("400px");

		manager.setEditable(false);
		settleButton.setEnabled(false);
	}

	protected abstract void noSubPolicyChangedState();

	protected abstract void onSubPolicyValueChanged();

	protected void onNoSubPolicyChange(boolean b){
		clearSubPolicy();
		setSubPolicyEnabled(!b);
		noSubPolicy.setReadOnly(false);
		clearExpense();
		noSubPolicy.setValue(b, false);
		if(b)
			setExpenseEnabled(true);
	}

	private void setManualSettlement(boolean isManual) {
		tempIsManual = isManual;
		settleButton.setVisible(!isManual);
		settleLabel.setVisible(!isManual);
		settlement.setEditable(isManual);
	}

	protected abstract void onChangedPolicyNumber();

	protected abstract void onClickVerifyPolicyNumber();

	@Override
	public ExpensePolicyWrapper getInfo() {

		ExpensePolicyWrapper newW = value;
		newW.subPolicy.id = noSubPolicy.getValue() ? null : subPolicyReference.getValue();
		newW.expense.managerId = manager.getValue();	
		newW.expense.settlement = settlement.getValue();
		newW.expense.notes = notes.getValue();
		newW.expense.expenseDate = expenseDate.getStringValue();
		newW.expense.value = expenseValue.getValue();
		newW.expense.isManual = tempIsManual;
		newW.expense.coverageId = coverageId.getValue();
		newW.expense.coverageName = coverageId.getSelectedItemText();
		newW.expense.insuredObjectId = insuredObject.getValue();
		newW.expense.insuredObjectName = insuredObjectName.getValue();
		return newW;
	}

	@Override
	public void setInfo(ExpensePolicyWrapper info) {

		if(info != null){
			if(info.policy != null){
				policyNumber.setValue(info.policy.number);
			}
			else{
				policyNumber.setValue(null);
			}
			subPolicyReference.setValue(info.subPolicy.id, false);
			subscriber.setValue(info.subPolicy.clientName);
			if(info.subPolicy.managerId != null && !noSubPolicy.getValue()){
				manager.setValue(info.subPolicy.managerId);
			}
			else if(info.policy.managerId != null && noSubPolicy.getValue()){
				manager.setValue(info.policy.managerId);
			}else{
				manager.setValue(null);
			}

			if(info.expense.referenceTypeId != null){
				String listId = info.expense.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? 
						BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT+"/"+info.expense.referenceId 
						: BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS +"/"+info.expense.referenceId;


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

				if(info.expense.insuredObjectName != null){
					belongsToPolicy.setValue("false", true);
					insuredObjectName.setValue(info.expense.insuredObjectName);
				}else{
					belongsToPolicy.setValue("true", true);
					insuredObject.setValue(info.expense.insuredObjectId);
				}

				listId = BigBangConstants.EntityIds.COVERAGE + "/" + info.expense.referenceId;

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
			}
			else{
				clearExpense();
			}

		}
	}

	protected boolean isEditKey(int nativeKeyCode) {

		switch(nativeKeyCode){

		case KeyCodes.KEY_DOWN:
		case KeyCodes.KEY_UP:
		case KeyCodes.KEY_LEFT:
		case KeyCodes.KEY_RIGHT:
		case KeyCodes.KEY_SHIFT:
		case KeyCodes.KEY_CTRL:
		case KeyCodes.KEY_ALT:
		case KeyCodes.KEY_HOME:
		case KeyCodes.KEY_END:
			return false;
		default:
			return true;
		}
	}

	protected abstract void onClickMarkAsInvalid();

	public void setSubPolicies(String id) {
		HasParameters param = new HasParameters();
		param.setParameter("ownerid", id);
		subPolicySelectionPanel.setParameters(param);
	}

	public void showPolicyProblemLabel(boolean b) {
		policyNumberProblem.setVisible(b);
	}

	public void enablePolicy(boolean b) {
		policyNumber.setReadOnly(!b);
		verifyPolicyNumber.setEnabled(b);

		if(b){
			policyNumber.focus();
		}
	}

	public void enableMarkAsInvalid(boolean b){
		markAsInvalid.setEnabled(b);
	}

	public String getPolicyNumber() {
		return policyNumber.getValue();
	}

	public void setSubPolicyEnabled(boolean b) {
		noSubPolicy.setReadOnly(!b);
		subPolicyReference.setReadOnly(!b);
		subPolicySelectionPanel.setReadOnly(!b);
	}

	public String getSubPolicyId(){
		return subPolicySelectionPanel.getValue();
	}

	public void setExpenseEnabled(boolean b) {
		expenseValue.setReadOnly(!b);
		coverageId.setReadOnly(!b);
		expenseDate.setReadOnly(!b);
		insuredObject.setReadOnly(!b);
		insuredObjectName.setReadOnly(!b);
		belongsToPolicy.setReadOnly(!b);
		settleButton.setEnabled(b);
		notes.setReadOnly(!b);
	}

	public void clearExpense() {
		expenseValue.clear();
		manager.setValue(null);
		coverageId.clearValues();
		expenseDate.clear();
		insuredObject.clearValues();
		settlement.clear();
		noSubPolicy.clear();
		settleButton.setVisible(true);
		settleLabel.setVisible(true);
		settlement.setReadOnly(true);
		belongsToPolicy.setValue("true");
	}

	public void clearSubPolicy() {
		subPolicySelectionPanel.setParameters(new HasParameters());
		subPolicyReference.clear();
		subscriber.clear();
	}

	public void setPolicyNumber(String policyNumber2) {
		policyNumber.setValue(policyNumber2);
	}

	public void isPolicyNumberProblem(boolean b) {
		if(b){
			policyNumber.getNativeField().getElement().getStyle().setColor("RED");
		}
		else{
			policyNumber.getNativeField().getElement().getStyle().setColor("BLACK");

		}

	}

	public boolean getSubPolicyDisabled() {
		return noSubPolicy.getValue();
	}

	public boolean isSubPolicy() {
		return !noSubPolicy.getValue();
	}

	public void clearPolicy() {
		policyNumber.setValue(null);
	}
}
