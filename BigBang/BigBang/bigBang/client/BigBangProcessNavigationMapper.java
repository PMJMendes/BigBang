package bigBang.client;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.SignatureRequestBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.ProcessNavigationMapper;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;

public class BigBangProcessNavigationMapper implements ProcessNavigationMapper {

	ResponseHandler<NavigationHistoryItem> handler;
	InsuranceSubPolicyBroker subPolicyBroker;
	NegotiationBroker negotiationBroker;
	SubCasualtyDataBroker subCasualtyBroker;
	SignatureRequestBroker signatureBroker;

	public BigBangProcessNavigationMapper() {
		subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		subCasualtyBroker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		signatureBroker = (SignatureRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SIGNATURE_REQUEST);
	}

	@Override
	public void getProcessNavigationItem(String typeId, String instanceId,
			ResponseHandler<NavigationHistoryItem> handler) {

		this.handler = handler;

		if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
			getNegotiationNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
			getPolicyNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			getSubPolicyNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
			getReceiptNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.SIGNATURE_REQUEST)){
			getSignatureRequestNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
			getCasualtyRequestNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			getSubCasualtyRequestNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.DAS_REQUEST)){
			getDasRequestNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			getClientNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
			getExpenseNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.COMPLAINT)){
			getComplaintNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)){
			getQuoteRequestNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.RISK_ANALISYS)){
			getRiskAnalysisNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)){
			getInfoRequestNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.EXTERNAL_INFO_REQUEST)){
			getExternalInfoRequestNavigationProcessItem(instanceId);
		}

	}

	private void getExternalInfoRequestNavigationProcessItem(String instanceId) {
		ExternRequestServiceAsync service = ExternRequestService.Util.getInstance();
		service.getRequest(instanceId, new BigBangAsyncCallback<ExternalInfoRequest>() {

			@Override
			public void onResponseSuccess(ExternalInfoRequest result) {
				final NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.setParameter("externalrequestid", result.id);
				
				if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
					
					negotiationBroker.getNegotiation(result.parentDataObjectId, new ResponseHandler<Negotiation>() {
						
						@Override
						public void onResponse(Negotiation response) {
							if(response.ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
								navigationItem.setParameter("section", "insurancepolicy");
								navigationItem.setParameter("policyid", response.ownerId);
							}
							else{
								navigationItem.setParameter("section", "quoterequest");
								navigationItem.setParameter("quoterequestid", response.ownerId);
							}
							navigationItem.pushIntoStackParameter("display", "negotiation");
							navigationItem.pushIntoStackParameter("display", "viewnegotiationexternalrequest");
							navigationItem.setParameter("negotiationid", response.id);
							handler.onResponse(navigationItem);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							
						}
					});
					
				}
				
				else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
					
					navigationItem.setParameter("section", "expense");
					navigationItem.pushIntoStackParameter("display", "viewexternalrequest");
					navigationItem.setParameter("expenseid", result.parentDataObjectId);
					handler.onResponse(navigationItem);
					
				}
				else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					
					subCasualtyBroker.getSubCasualty(result.parentDataObjectId, new ResponseHandler<SubCasualty>() {
						
						@Override
						public void onResponse(SubCasualty response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "viewsubcasualtyexternalrequest");
							navigationItem.setParameter("subcasualtyid", response.id);
							navigationItem.setParameter("casualtyid", response.casualtyId);
							handler.onResponse(navigationItem);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));						}
					});
					
				}
			}
			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));
				super.onResponseFailure(caught);
			}
		});
	}

	private void getInfoRequestNavigationProcessItem(String instanceId) {
		InfoOrDocumentRequestServiceAsync service = InfoOrDocumentRequestService.Util.getInstance();
		service.getRequest(instanceId, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				
				final NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.pushIntoStackParameter("display", "viewinforequest");
				navigationItem.setParameter("requestid", result.id);

				if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){					
					navigationItem.setParameter("section", "expense");
					navigationItem.setParameter("expenseid", result.parentDataObjectId);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
					navigationItem.setParameter("section", "client");
					navigationItem.setParameter("clientid", result.parentDataObjectId);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					navigationItem.setParameter("section", "insurancepolicy");
					navigationItem.setParameter("policyid", result.parentDataObjectId);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
					
					InsuranceSubPolicyBroker broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
					broker.getSubPolicy(result.parentDataObjectId, new ResponseHandler<SubPolicy>() {
						
						@Override
						public void onResponse(SubPolicy response) {
							navigationItem.setParameter("section", "insurancepolicy");
							navigationItem.setParameter("policyid", response.mainPolicyId);
							navigationItem.popFromStackParameter("display");
							navigationItem.pushIntoStackParameter("display", "subpolicy");
							navigationItem.pushIntoStackParameter("display", "viewsubpolicyinforequest");
							navigationItem.setParameter("subpolicyid", response.id);
							handler.onResponse(navigationItem);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));
						}
					});
					
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
					navigationItem.setParameter("section", "casualty" );
					navigationItem.setParameter("casualtyid", result.parentDataObjectId);
					NavigationHistoryManager.getInstance().go(navigationItem);
					
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					
					SubCasualtyDataBroker broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
					broker.getSubCasualty(result.parentDataObjectId, new ResponseHandler<SubCasualty>() {
						
						@Override
						public void onResponse(SubCasualty response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.setParameter("casualtyid", response.casualtyId);
							navigationItem.popFromStackParameter("display");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "viewsubcasualtyinforequest");
							navigationItem.setParameter("subcasualtyid", response.id);
							handler.onResponse(navigationItem);						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));
						}
					});
					
					
				}else {
					return;
				}
				
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));
				super.onResponseFailure(caught);
			}
		});		
	}

	private void getRiskAnalysisNavigationProcessItem(String instanceId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "riskanalisys");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("riskanalisysid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getQuoteRequestNavigationProcessItem(String instanceId) {
		
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "quoterequest");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("quoterequestid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getComplaintNavigationProcessItem(String instanceId) {
		
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "complaint");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("complaintid", instanceId);	
		handler.onResponse(navigationItem);
	}

	private void getExpenseNavigationProcessItem(String instanceId) {
	
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "expense");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("expenseid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getClientNavigationProcessItem(String instanceId) {
		
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "client");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("clientid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getDasRequestNavigationProcessItem(String instanceId) {
		
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "receipt");
		navigationItem.pushIntoStackParameter("display", "dasrequest");
		navigationItem.setParameter("dasrequestid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getSubCasualtyRequestNavigationProcessItem(String instanceId) {
		subCasualtyBroker.getSubCasualty(instanceId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {

				NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.setParameter("section","casualty");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.pushIntoStackParameter("display", "subcasualty");
				navigationItem.setParameter("casualtyid", response.casualtyId);
				navigationItem.setParameter("subcasualtyid", response.id);
				handler.onResponse(navigationItem);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível navegar para a apólice adesão"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	private void getCasualtyRequestNavigationProcessItem(String instanceId) {

		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "casualty");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("casualtyid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getSignatureRequestNavigationProcessItem(String instanceId) {
		
		signatureBroker.getRequest(instanceId, new ResponseHandler<SignatureRequest>() {
			
			@Override
			public void onResponse(SignatureRequest response) {
				NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.setParameter("section", "receipt");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.pushIntoStackParameter("display", "signaturerequest");
				navigationItem.setParameter("receiptid", response.receiptId);
				navigationItem.setParameter("signaturerequestid", response.id);
				handler.onResponse(navigationItem);				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível navegar para o pedido de assinatura"), TYPE.ALERT_NOTIFICATION));
			}
		});
		
	}

	private void getReceiptNavigationProcessItem(String instanceId) {

		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "receipt");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("receiptid", instanceId);
		handler.onResponse(navigationItem);
	}

	private void getPolicyNavigationProcessItem(String instanceId) {

		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "insurancepolicy");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("policyid", instanceId);
		handler.onResponse(navigationItem);

	}

	private void getSubPolicyNavigationProcessItem(String instanceId){


		subPolicyBroker.getSubPolicy(instanceId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {

				NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.setParameter("section","insurancepolicy");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.pushIntoStackParameter("display", "subpolicy");
				navigationItem.setParameter("policyid", response.mainPolicyId);
				navigationItem.setParameter("subpolicyid", response.id);
				handler.onResponse(navigationItem);

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível navegar para a apólice adesão"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	private void getNegotiationNavigationProcessItem(final String instanceId) {


		negotiationBroker.getNegotiation(instanceId, new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				if(response.ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					navigationItem.setParameter("section", "insurancepolicy");
				}
				else{
					navigationItem.setParameter("section", "quoterequest");
				}
				navigationItem.pushIntoStackParameter("display", "negotiation");
				navigationItem.setParameter("ownertypeid", response.ownerTypeId);
				navigationItem.setParameter("policyid", response.ownerId);
				navigationItem.setParameter("negotiationid", instanceId);
				handler.onResponse(navigationItem);
			}


			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível navegar para a negociação"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}
}
