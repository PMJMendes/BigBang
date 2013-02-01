package bigBang.client;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.SignatureRequestBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.ProcessNavigationMapper;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBroker;

public class BigBangProcessNavigationMapper implements ProcessNavigationMapper {

	ResponseHandler<NavigationHistoryItem> handler;
	InsuranceSubPolicyBroker subPolicyBroker;
	NegotiationBroker negotiationBroker;
	SubCasualtyDataBroker subCasualtyBroker;
	SignatureRequestBroker signatureBroker;
	ConversationBroker conversationBroker;
	AssessmentBroker assessmentBroker;
	MedicalFileBroker medicalFileBroker;
	TotalLossFileBroker totalLossFileBroker;

	public BigBangProcessNavigationMapper() {
		subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		subCasualtyBroker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		signatureBroker = (SignatureRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SIGNATURE_REQUEST);
		conversationBroker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
		assessmentBroker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
		medicalFileBroker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
		totalLossFileBroker = (TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE);
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
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)){
			getConversationNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.MEDICAL_FILE)){
			getMedicalFileNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.ASSESSMENT)){
			getAssessmentNavigationProcessItem(instanceId);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.TOTAL_LOSS_FILE)){
			getTotalLossFileNavigationProcessItem(instanceId);
		}	

	}
	
	private void getTotalLossFileNavigationProcessItem(String id) {
		totalLossFileBroker.getTotalLossFile(id, new ResponseHandler<TotalLossFile>() {
			
			@Override
			public void onResponse(TotalLossFile response) {
				final NavigationHistoryItem item = new NavigationHistoryItem();

				item.setStackParameter("display");
				item.setParameter("section", "casualty");
				item.pushIntoStackParameter("display", "search");
				item.pushIntoStackParameter("display", "subcasualty");
				item.pushIntoStackParameter("display", "totallossfile");
				item.setParameter("subcasualtyid", response.subCasualtyId);
				item.setParameter("totallossfileid", response.id);

				handler.onResponse(item);								
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));				
			}
		});
		
	}


	private void getAssessmentNavigationProcessItem(String instanceId) {
		assessmentBroker.getAssessment(instanceId, new ResponseHandler<Assessment>() {

			@Override
			public void onResponse(Assessment response) {
				final NavigationHistoryItem item = new NavigationHistoryItem();

				item.setStackParameter("display");
				item.setParameter("section", "casualty");
				item.pushIntoStackParameter("display", "search");
				item.pushIntoStackParameter("display", "subcasualty");
				item.pushIntoStackParameter("display", "assessment");
				item.setParameter("subcasualtyid", response.subCasualtyId);
				item.setParameter("assessmentid", response.id);

				handler.onResponse(item);				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));

			}
		});
	}

	private void getMedicalFileNavigationProcessItem(String instanceId) {
		medicalFileBroker.getMedicalFile(instanceId, new ResponseHandler<MedicalFile>() {

			@Override
			public void onResponse(MedicalFile response) {

				final NavigationHistoryItem item = new NavigationHistoryItem();

				item.setStackParameter("display");
				item.setParameter("section", "casualty");
				item.pushIntoStackParameter("display", "search");
				item.pushIntoStackParameter("display", "subcasualty");
				item.pushIntoStackParameter("display", "medicalfile");
				item.setParameter("subcasualtyid", response.subCasualtyId);
				item.setParameter("medicalfileid", response.id);

				handler.onResponse(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));

			}
		});
	}

	private void getConversationNavigationProcessItem(String instanceId) {
		conversationBroker.getConversation(instanceId, new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {

				final NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.setParameter("conversationid", response.id);

				if(BigBangConstants.EntityIds.CLIENT.equalsIgnoreCase(response.parentDataTypeId)){
					navigationItem.setParameter("section", "client");
					navigationItem.setParameter("clientid", response.parentDataObjectId);
					navigationItem.pushIntoStackParameter("display","conversation");
					handler.onResponse(navigationItem);

				}else if(BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(response.parentDataTypeId)){
					navigationItem.setParameter("section", "insurancepolicy");
					navigationItem.pushIntoStackParameter("display", "search");

					navigationItem.setParameter("policyid", response.parentDataObjectId);
					navigationItem.pushIntoStackParameter("display","conversation");
					handler.onResponse(navigationItem);

				}else if(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.parentDataTypeId)){
					navigationItem.setParameter("section", "insurancepolicy");
					navigationItem.setParameter("subpolicyid", response.parentDataObjectId);
					navigationItem.pushIntoStackParameter("display", "subpolicy");
					navigationItem.pushIntoStackParameter("display", "subpolicyconversation");
					subPolicyBroker.getSubPolicy(response.parentDataObjectId, new ResponseHandler<SubPolicy>() {

						@Override
						public void onResponse(SubPolicy response) {
							navigationItem.setParameter("policyid", response.mainPolicyId);
							handler.onResponse(navigationItem);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));

						}
					});

				}else if(BigBangConstants.EntityIds.CASUALTY.equalsIgnoreCase(response.parentDataTypeId)){
					navigationItem.setParameter("section", "casualty");
					navigationItem.setParameter("casualtyid", response.parentDataObjectId);
					navigationItem.pushIntoStackParameter("display","conversation");
					handler.onResponse(navigationItem);

				}else if(BigBangConstants.EntityIds.NEGOTIATION.equalsIgnoreCase(response.parentDataTypeId)){

					negotiationBroker.getNegotiation(response.parentDataObjectId, new ResponseHandler<Negotiation>() {

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
							navigationItem.pushIntoStackParameter("display", "negotiationconversation");
							navigationItem.setParameter("negotiationid", response.id);
							handler.onResponse(navigationItem);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));				
						}
					});

				}else if(BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(response.parentDataTypeId)){
					subCasualtyBroker.getSubCasualty(response.parentDataObjectId, new ResponseHandler<SubCasualty>() {

						@Override
						public void onResponse(SubCasualty response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "subcasualtyconversation");
							navigationItem.setParameter("subcasualtyid", response.id);
							navigationItem.setParameter("casualtyid", response.casualtyId);
							handler.onResponse(navigationItem);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));						
						}
					});

				}else if(BigBangConstants.EntityIds.EXPENSE.equalsIgnoreCase(response.parentDataTypeId)){
					navigationItem.setParameter("section", "expense");
					navigationItem.setParameter("expenseid", response.parentDataObjectId);
					navigationItem.pushIntoStackParameter("display","conversation");
					handler.onResponse(navigationItem);

				}else if(BigBangConstants.EntityIds.ASSESSMENT.equalsIgnoreCase(response.parentDataTypeId)){
					assessmentBroker.getAssessment(response.parentDataObjectId, new ResponseHandler<Assessment>() {

						@Override
						public void onResponse(Assessment response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "assessment");
							navigationItem.pushIntoStackParameter("display", "assessmentconversation");
							navigationItem.setParameter("assessmentid", response.id);
							navigationItem.setParameter("subcasualtyid", response.subCasualtyId);
							handler.onResponse(navigationItem);							
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));						
						}
					});

				}
				else if(BigBangConstants.EntityIds.MEDICAL_FILE.equalsIgnoreCase(response.parentDataTypeId)){
					medicalFileBroker.getMedicalFile(response.parentDataObjectId, new ResponseHandler<MedicalFile>() {

						@Override
						public void onResponse(MedicalFile response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "medicalfile");
							navigationItem.pushIntoStackParameter("display", "medicalfileconversation");
							navigationItem.setParameter("medicalfileid", response.id);
							navigationItem.setParameter("subcasualtyid", response.subCasualtyId);
							handler.onResponse(navigationItem);							
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));						
						}
					});

				}
				else if(BigBangConstants.EntityIds.TOTAL_LOSS_FILE.equalsIgnoreCase(response.parentDataTypeId)){
					totalLossFileBroker.getTotalLossFile(response.parentDataObjectId, new ResponseHandler<TotalLossFile>() {

						@Override
						public void onResponse(TotalLossFile response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "totallossfile");
							navigationItem.pushIntoStackParameter("display", "totallossfileconversation");
							navigationItem.setParameter("totallossfileid", response.id);
							navigationItem.setParameter("subcasualtyid", response.subCasualtyId);
							handler.onResponse(navigationItem);							
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));						
						}
					});

				}
				else{
					navigationItem.setParameter("section", "receipt");
					navigationItem.setParameter("receiptid", response.parentDataObjectId);
					navigationItem.pushIntoStackParameter("display","conversation");
					handler.onResponse(navigationItem);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {

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
