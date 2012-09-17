package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.insurancePolicyModule.client.PolicyWorkSpace;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;

public class InsurancePolicyProcessBrokerImpl extends DataBroker<InsurancePolicy> implements InsurancePolicyBroker {

	protected InsurancePolicyServiceAsync service;
	protected SearchDataBroker<InsurancePolicyStub> searchBroker;
	protected ClientProcessBroker clientBroker;
	protected PolicyObjectServiceAsync insuredObjectService;
	protected PolicyWorkSpace workspace;
	public boolean requiresRefresh;

	public InsurancePolicyProcessBrokerImpl(){
		this(InsurancePolicyService.Util.getInstance(), PolicyObjectService.Util.getInstance());
	}

	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service, PolicyObjectServiceAsync insuredObjectService) {
		this.service = service;
		this.insuredObjectService = insuredObjectService;
		this.clientBroker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		this.searchBroker = new InsurancePolicySearchDataBroker(this.service);
		this.workspace = new PolicyWorkSpace();
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getPolicy(itemId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).addInsurancePolicy(response);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		incrementDataVersion();
		for(DataBrokerClient<InsurancePolicy> bc : getClients()){
			((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(itemId);
			((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getPolicy(itemId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(response);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getPolicy(String insurancePolicyId,
			final ResponseHandler<InsurancePolicy> handler) {
		this.service.getPolicy(insurancePolicyId, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				workspace.loadPolicy(result);

				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(result);
				requiresRefresh = false;
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the requested policy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getEmptyPolicy(String subLineId, String clientId,
			final ResponseHandler<InsurancePolicy> handler) {
		this.service.getEmptyPolicy(subLineId, clientId, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				workspace.loadPolicy(result);

				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(result);
				requiresRefresh = false;
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the empty policy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public InsurancePolicy getPolicyHeader(String policyId) {
		return workspace.getPolicyHeader(policyId);
	}

	@Override
	public InsurancePolicy updatePolicyHeader(InsurancePolicy policy) {
		return workspace.updatePolicyHeader(policy);
	}

	@Override
	public void persistPolicy(String policyId,
			final ResponseHandler<InsurancePolicy> handler) {
		InsurancePolicy policy;

		policy = workspace.getWholePolicy(policyId);

		if(policy != null) {
			if ( policy.id == null) {
				clientBroker.createPolicy(policy, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy response) {
						workspace.loadPolicy(response);

						incrementDataVersion();
						for(DataBrokerClient<InsurancePolicy> bc : getClients()) {
							((InsurancePolicyDataBrokerClient) bc).addInsurancePolicy(response);
							((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
						}
						handler.onResponse(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						handler.onError(errors);
					}
				});
			} else {
				this.service.editPolicy(policy, new BigBangAsyncCallback<InsurancePolicy>() {
	
					@Override
					public void onResponseSuccess(InsurancePolicy result) {
						workspace.loadPolicy(result);

						incrementDataVersion();
						for(DataBrokerClient<InsurancePolicy> bc : getClients()){
							((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
							((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
						}
						handler.onResponse(result);
						requiresRefresh = false;
					}
	
					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[]{
								"Could not update the policy"	
						});
						super.onResponseFailure(caught);
					}
				});
			}
		} else {
			handler.onError(new String[]{
					"Could not update the policy"	
			});
		}
	}

	@Override
	public InsurancePolicy discardEditData(String policyId) {
		return workspace.reset(policyId);
	}

	@Override
	public void removePolicy(final String policyId, final ResponseHandler<String> handler) {
		this.service.deletePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				workspace.loadPolicy(null);

				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(policyId);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.DELETE_POLICY, policyId));
				handler.onResponse(policyId);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not remove the insurance policy")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public ExerciseData createExercise(String policyId) {
		return workspace.createExercise(policyId);
	}

	@Override
	public ExerciseData updateExercise(String policyId, ExerciseData exercise) {
		return workspace.updateExerciseHeader(policyId, exercise);
	}

	@Override
	public InsuredObjectStub[] getAlteredObjects(String policyId) {
		return workspace.getLocalObjects(policyId);
	}

	@Override
	public void getInsuredObject(final String policyId, String objectId,
			final ResponseHandler<InsuredObject> handler) {
		InsuredObject object = workspace.getObjectHeader(policyId, objectId);

		if(object != null) {
			handler.onResponse(object);
		} else {
			insuredObjectService.getObject(objectId, new BigBangAsyncCallback<InsuredObject>() {

				@Override
				public void onResponseSuccess(InsuredObject result) {
					handler.onResponse(workspace.loadExistingObject(policyId, result));
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the object")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public InsuredObject createInsuredObject(String policyId) {
		return workspace.createLocalObject(policyId);
	}

	@Override
	public InsuredObject updateInsuredObject(String policyId, InsuredObject objectId) {
		return workspace.updateObject(policyId, objectId);
	}

	@Override
	public void removeInsuredObject(String policyId, String objectId) {
		workspace.deleteObject(policyId, objectId);
	}

	@Override
	public FieldContainer getContextForPolicy(String policyId, String exerciseId) {
		return workspace.getContext(policyId, null, exerciseId);
	}

	@Override
	public void saveContextForPolicy(String policyId, String exerciseId,
			FieldContainer contents) {
		workspace.updateContext(policyId, null, exerciseId, contents);
		
	}

	@Override
	public FieldContainer getContextForInsuredObject(String policyId,
			String objectId, String exerciseId) {
		return workspace.getContext(policyId, objectId, exerciseId);
	}

	@Override
	public void saveContextForInsuredObject(String policyId, String objectId,
			String exerciseId, FieldContainer contents) {
		workspace.updateContext(policyId, objectId, exerciseId, contents);
	}


	//Other operations

	@Override
	public void getClientPolicies(String clientid,
			final ResponseHandler<Collection<InsurancePolicyStub>> policies) {
		InsurancePolicySearchParameter parameter = new InsurancePolicySearchParameter();
		parameter.ownerId = clientid;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SortParameter sort = new InsurancePolicySortParameter(InsurancePolicySortParameter.SortableField.NUMBER, SortOrder.DESC);		
		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		this.searchBroker.search(parameters, sorts, -1, new ResponseHandler<Search<InsurancePolicyStub>>() {

			@Override
			public void onResponse(Search<InsurancePolicyStub> response) {
				policies.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				policies.onError(new String[]{
						new String("Could not get the client policies")	
				});
			}
		}, true);
	}

	@Override
	public void createSubPolicy(SubPolicy subPolicy,
			final ResponseHandler<SubPolicy> handler) {
		service.createSubPolicy(subPolicy, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				//TODO
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUB_POLICY, result.mainPolicyId));

				handler.onResponse(result);
				
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create new SubPolicy")	
				});
				super.onResponseFailure(caught);			
			}
		});
	}

	@Override
	public void createReceipt(String policyId, Receipt receipt,
			final ResponseHandler<Receipt> handler) {
		this.service.createReceipt(policyId, receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT, result.id));
				handler.onResponse(result);
				DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT).notifyItemCreation(result.id);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create receipt")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void validatePolicy(final String policyId, final ResponseHandler<Void> handler) {
		service.validatePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.VALIDATE_POLICY, policyId));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						caught.getMessage()	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void executeDetailedCalculations(final String policyId, final ResponseHandler<InsurancePolicy> handler){
		service.performCalculations(policyId, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.EXECUTE_DETAILED_CALCULATIONS, policyId));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not perform detailed calculations")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void voidPolicy(PolicyVoiding voiding, final ResponseHandler<InsurancePolicy> handler) {
		service.voidPolicy(voiding, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.VOID_POLICY, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not void the policy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void issueDebitNote(final String policyId, DebitNote note, final ResponseHandler<Void> handler) {
		service.createDebitNote(policyId, note, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_DEBIT_NOTE, policyId));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not issue debit note")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createNegotiation(Negotiation negotiation, final ResponseHandler<Negotiation> handler) {
		service.createNegotiation(negotiation, new BigBangAsyncCallback<Negotiation>() {

			@Override
			public void onResponseSuccess(Negotiation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_NEGOTIATION, result.id));
				handler.onResponse(result);
				DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.NEGOTIATION).notifyItemCreation(result.id);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not save the negotiation")
				});
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void transferToClient(String policyId, String newClientId, final ResponseHandler<InsurancePolicy> handler){
		service.transferToClient(policyId, newClientId, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_TO_CLIENT, result.id));
				handler.onResponse(result);				
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not transfer to client")
				});
				super.onResponseFailure(caught);
			}


		});
	}

	@Override
	public void createManagerTransfer(String[] objectIds, String newManagerId, final ResponseHandler<Void> handler) {
		ManagerTransfer transfer = new ManagerTransfer();
		transfer.dataObjectIds = objectIds;
		transfer.newManagerId = newManagerId;

		if(objectIds.length == 1) {
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER, result.id));
					handler.onResponse(null);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not transfer the process")
					});
					super.onResponseFailure(caught);
				}
			});
		}else if(objectIds.length > 1){
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER, result.id));
					handler.onResponse(null);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not transfer the processes")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Cannot transfer 0 processes")	
			});
		}
	}

	@Override
	public void getInsurancePoliciesWithNumber(String label, final ResponseHandler<Collection<InsurancePolicyStub>> handler){
		service.getExactResults(label, new BigBangAsyncCallback<SearchResult[]>() {

			@Override
			public void onResponseSuccess(SearchResult[] result) {
				ArrayList<InsurancePolicyStub> stubs = new ArrayList<InsurancePolicyStub>();

				for(int i = 0; i<result.length; i++){
					stubs.add((InsurancePolicyStub)result[i]);
				}
				handler.onResponse(stubs);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not find exact results")
				});
				super.onResponseFailure(caught);
			}


		});
	}

	@Override
	public void createExpense(Expense expense, final ResponseHandler<Expense> handler){
		service.createExpense(expense, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_HEALTH_EXPENSE, result.id));

				handler.onResponse(result);
				((ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE)).notifyItemCreation(result.id);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the new Expense")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createCompanyInfoRequest(InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> responseHandler) {
		service.createInfoOrDocumentRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_COMPANY_INFO_REQUEST, result.id));

				responseHandler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not create de info request")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createClientInfoRequest(InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> responseHandler) {
		service.createInfoOrDocumentRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_CLIENT_INFO_REQUEST, result.id));

				responseHandler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not create de info request")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

}
