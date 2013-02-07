package bigBang.module.quoteRequestModule.client.userInterface.form;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class QuoteRequestHeaderForm extends FormView<QuoteRequest>{

	protected NavigationFormField client;
	protected TextBoxFormField number;
	protected ExpandableListBoxFormField clientMediator;
	protected TextBoxFormField status;
	protected ExpandableListBoxFormField policyMediator;
	protected ExpandableListBoxFormField quoteRequestManager;
	protected CheckBoxFormField caseStudy;


	public QuoteRequestHeaderForm() {

		addSection("Cabeçalho da Consulta de Mercado");

		client = new NavigationFormField("Cliente");
		number = new TextBoxFormField("Número");
		number.setEditable(false);
		clientMediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Gestor do Cliente");
		clientMediator.setEditable(false);
		status = new TextBoxFormField("Estado");
		status.setEditable(false);
		policyMediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador das Apólices");
		policyMediator.setEmptyValueString("(O mesmo do Cliente)");
		quoteRequestManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor da Consulta");
		quoteRequestManager.setEmptyValueString("(O mesmo do Cliente)");
		caseStudy = new CheckBoxFormField("Case Study");
		
		addFormField(client);
		
		addFormFieldGroup(new FormField<?>[]{
				number,
				status,
				quoteRequestManager
		}, true);
		
		addFormFieldGroup(new FormField<?>[]{
				clientMediator,
				policyMediator,
		}, true);
		
		addLineBreak();
		
		addFormField(caseStudy);
		
		setValidator(new QuoteRequestHeaderFormValidator(this));
	}

	@Override
	public QuoteRequest getInfo() {
		QuoteRequest toReturn = value;

		toReturn.processNumber = number.getValue();
		toReturn.managerId = quoteRequestManager.getValue();
		toReturn.mediatorId = policyMediator.getValue();
		toReturn.caseStudy = caseStudy.getValue();


		return toReturn;
	}

	@Override
	public void setInfo(QuoteRequest info) {

		if(info.clientId != null){
			ClientProcessBroker clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
			clientBroker.getClient(info.clientId, new ResponseHandler<Client>() {

				@Override
				public void onResponse(Client response) {
					NavigationHistoryItem item = new NavigationHistoryItem();
					item.setParameter("section", "client");
					item.setStackParameter("display");
					item.pushIntoStackParameter("display", "search");
					item.setParameter("clientid", response.id);
					client.setValue(item);

					client.setValueName("#" + response.clientNumber + " - " + response.name);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
		}

		number.setValue(info.processNumber);
		clientMediator.setValue(info.inheritMediatorId);
		status.setValue(info.isOpen ? "Aberta" : "Fechada");
		policyMediator.setValue(info.mediatorId);
		quoteRequestManager.setValue(info.managerId);
		caseStudy.setValue(info.caseStudy);
	}

}
