package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Expense;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class ExpenseSendMessageViewPresenter extends SendMessageViewPresenter<Expense>{

	private ExpenseDataBroker broker;
	protected Expense expense;

	public ExpenseSendMessageViewPresenter(Display<Expense> view) {
		super(view);
		broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
	}

	@Override
	public void setParameters(HasParameters parameterHolder){
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.EXPENSE);
		super.setParameters(parameterHolder);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<Expense> handler) {
		broker.getExpense(ownerId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				expense = response;
				setContacts();
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});

	}

	protected void setContacts() {
		view.addContact("Despesa de Saúde (" + expense.number + ")", expense.id , BigBangConstants.EntityIds.EXPENSE);
		view.addContact("Apólice " + (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(expense.referenceTypeId) ? "Adesão " : "") + "(" + expense.referenceNumber + ")", expense.referenceId, expense.referenceTypeId);
		view.addContact("Seguradora (" + expense.inheritInsurerName + ")", expense.inheritInsurerId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
		if (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(expense.referenceTypeId)) {
			view.addContact("Cliente Subscritor (" + expense.clientName + ")", expense.clientId, BigBangConstants.EntityIds.CLIENT);
			view.addContact("Mediador do Cliente Subscritor (" + expense.inheritMediatorName + ")", expense.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
			view.addContact("Cliente Principal (" + expense.inheritMasterClientName + ")", expense.inheritMasterClientId, BigBangConstants.EntityIds.CLIENT);
			view.addContact("Mediador da Apólice Mãe (" + expense.inheritMasterMediatorName + ")", expense.inheritMasterMediatorId, BigBangConstants.EntityIds.MEDIATOR);
		}
		else {
			view.addContact("Cliente (" + expense.clientName + ")", expense.clientId, BigBangConstants.EntityIds.CLIENT);
			view.addContact("Mediador (" + expense.inheritMediatorName + ")", expense.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
		}

	}

	@Override
	protected void send() {
		broker.sendMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onSendRequestSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendRequestFailed();
			}
		});
	}
}
