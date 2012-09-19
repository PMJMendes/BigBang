package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.insurancePolicyModule.client.SubPolicyWorkSpace;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.SubPolicySortParameter;

public class InsuranceSubPolicyBrokerImpl extends DataBroker<SubPolicy>
implements InsuranceSubPolicyBroker {

	protected SubPolicyServiceAsync service;
	protected InsurancePolicyServiceAsync policyService;
	protected SearchDataBroker<SubPolicyStub> searchBroker;
	protected SubPolicyObjectServiceAsync insuredObjectService;
	protected InsuredObjectDataBroker insuredObjectsBroker;
	protected SubPolicyWorkSpace workspace;

	public InsuranceSubPolicyBrokerImpl(){
		this(SubPolicyService.Util.getInstance(), SubPolicyObjectService.Util.getInstance());
	}

	public InsuranceSubPolicyBrokerImpl(SubPolicyServiceAsync service, SubPolicyObjectServiceAsync insuredObjectService) {
		this.service = service;
		this.insuredObjectService = insuredObjectService;
		policyService = InsurancePolicyService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_SUB_POLICY;
		this.searchBroker = new InsuranceSubPolicySearchDataBroker();
		this.workspace = new SubPolicyWorkSpace();
	}

	@Override
	public void requireDataRefresh() {
		return; //No cache usage, so no need to signal refresh
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getSubPolicy(itemId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()) {
					((InsuranceSubPolicyDataBrokerClient) bc).addInsuranceSubPolicy(response);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		incrementDataVersion();
		for(DataBrokerClient<SubPolicy> bc : getClients()) {
			((InsuranceSubPolicyDataBrokerClient) bc).removeInsuranceSubPolicy(itemId);
			((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getSubPolicy(itemId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()) {
					((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(response);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void getSubPolicy(String subPolicyId,
			final ResponseHandler<SubPolicy> handler) {
		this.service.getSubPolicy(subPolicyId, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				workspace.loadSubPolicy(result);

				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()){
					((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the requested subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getEmptySubPolicy(String policyId,
			final ResponseHandler<SubPolicy> handler) {
		this.service.getEmptySubPolicy(policyId, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				workspace.loadSubPolicy(result);

				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()){
					((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the empty subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public SubPolicy getSubPolicyHeader(String subPolicyId) {
		return workspace.getSubPolicyHeader(subPolicyId);
	}

	@Override
	public SubPolicy updateSubPolicyHeader(SubPolicy subPolicy) {
		return workspace.updateSubPolicyHeader(subPolicy);
	}

	@Override
	public void updateCoverages(Coverage[] coverages) {
		workspace.updateCoverages(coverages);
	}

	@Override
	public void persistSubPolicy(String subPolicyId,
			final ResponseHandler<SubPolicy> handler) {
		SubPolicy subPolicy;

		subPolicy = workspace.getWholeSubPolicy(subPolicyId);

		if(subPolicy != null) {
			if ( subPolicy.id == null) {
				policyService.createSubPolicy(subPolicy, new BigBangAsyncCallback<SubPolicy>() {
					
					@Override
					public void onResponseSuccess(SubPolicy result) {
						workspace.loadSubPolicy(result);

						incrementDataVersion();
						for(DataBrokerClient<SubPolicy> bc : getClients()) {
							((InsuranceSubPolicyDataBrokerClient) bc).addInsuranceSubPolicy(result);
							((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
						}
						handler.onResponse(result);
					}
					
					@Override
					public void onResponseFailure(Throwable caught) {
						super.onResponseFailure(caught);
						handler.onError(new String[]{"Could not create subpolicy"});
					}
				
				});
			} else {
				this.service.editSubPolicy(subPolicy, new BigBangAsyncCallback<SubPolicy>() {
	
					@Override
					public void onResponseSuccess(SubPolicy result) {
						workspace.loadSubPolicy(result);

						incrementDataVersion();
						for(DataBrokerClient<SubPolicy> bc : getClients()){
							((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
							((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
						}
						handler.onResponse(result);
					}
	
					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[]{
								"Could not update the subpolicy"	
						});
						super.onResponseFailure(caught);
					}
				});
			}
		} else {
			handler.onError(new String[]{
					"Could not update the subpolicy"	
			});
		}
	}

	@Override
	public SubPolicy discardEditData(String subPolicyId) {
		return workspace.reset(subPolicyId);
	}

	@Override
	public void removeSubPolicy(final String subPolicyId, String reason, final ResponseHandler<String> handler) {
		this.service.deleteSubPolicy(subPolicyId, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				workspace.loadSubPolicy(null);

				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()){
					((InsuranceSubPolicyDataBrokerClient) bc).removeInsuranceSubPolicy(subPolicyId);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY, subPolicyId));
				handler.onResponse(subPolicyId);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not remove the insurance subpolicy")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public InsuredObjectStub[] getAlteredObjects(String policyId) {
		return workspace.getLocalObjects(policyId);
	}

	@Override
	public void getInsuredObject(final String subPolicyId, String objectId,
			final ResponseHandler<InsuredObject> handler) {
		InsuredObject object = workspace.getObjectHeader(subPolicyId, objectId);

		if(object != null) {
			handler.onResponse(object);
		} else {
			insuredObjectService.getObject(objectId, new BigBangAsyncCallback<InsuredObject>() {

				@Override
				public void onResponseSuccess(InsuredObject result) {
					handler.onResponse(workspace.loadExistingObject(subPolicyId, result));
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
	public InsuredObject createInsuredObject(String subPolicyId) {
		return workspace.createLocalObject(subPolicyId);
	}

	@Override
	public InsuredObject updateInsuredObject(String subPolicyId, InsuredObject objectId) {
		return workspace.updateObject(subPolicyId, objectId);
	}

	@Override
	public void removeInsuredObject(String subPolicyId, String objectId) {
		workspace.deleteObject(subPolicyId, objectId);
	}

	@Override
	public FieldContainer getContextForSubPolicy(String subPolicyId, String exerciseId) {
		return workspace.getContext(subPolicyId, null, exerciseId);
	}

	@Override
	public void saveContextForSubPolicy(String subPolicyId, String exerciseId,
			FieldContainer contents) {
		workspace.updateContext(subPolicyId, null, exerciseId, contents);
		
	}

	@Override
	public FieldContainer getContextForInsuredObject(String subPolicyId,
			String objectId, String exerciseId) {
		return workspace.getContext(subPolicyId, objectId, exerciseId);
	}

	@Override
	public void saveContextForInsuredObject(String subPolicyId, String objectId,
			String exerciseId, FieldContainer contents) {
		workspace.updateContext(subPolicyId, objectId, exerciseId, contents);
	}


	//Other operations

	@Override
	public void createReceipt(final String subPolicyId, Receipt receipt,
			final ResponseHandler<Receipt> handler) {
		this.service.createReceipt(subPolicyId, receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT, subPolicyId));
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
	public SearchDataBroker<SubPolicyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void validateSubPolicy(final String subPolicyId,
			final ResponseHandler<Void> handler) {
		service.validateSubPolicy(subPolicyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VALIDATE, subPolicyId));
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
	public void executeDetailedCalculations(String subPolicyId,
			final ResponseHandler<SubPolicy> handler) {
		service.performCalculations(subPolicyId, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.PERFORM_CALCULATIONS, result.id));
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
	public void voidSubPolicy(PolicyVoiding voiding,
			final ResponseHandler<SubPolicy> handler) {
		service.voidSubPolicy(voiding, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VOID, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not void the subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void includeInsuredObject(String subPolicyId, InsuredObject object,
			final ResponseHandler<InsuredObject> handler) {
		service.includeObject(subPolicyId, object, new BigBangAsyncCallback<InsuredObject>() {

			@Override
			public void onResponseSuccess(InsuredObject result) {
				insuredObjectsBroker.notifyItemCreation(result.id);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not include insured object in the subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void includeObjectFromClient(final String subPolicyId,
			final ResponseHandler<InsuredObject> handler) {
		service.includeObjectFromClient(subPolicyId, new BigBangAsyncCallback<InsuredObject>() {

			@Override
			public void onResponseSuccess(InsuredObject result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.INCLUDE_OBJECT_FROM_CLIENT, subPolicyId));
				insuredObjectsBroker.notifyItemCreation(result.id);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not include insured object from client in the subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void excludeObject(final String subPolicyId, final String objectId,
			final ResponseHandler<Void> handler) {
		service.excludeObject(subPolicyId, objectId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EXCLUDE_OBJECT, subPolicyId));
				insuredObjectsBroker.notifyItemDeletion(objectId);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not exclude insured object from the subpolicy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void transferToInsurancePolicy(String subPolicyId,
			String newPolicyId, final ResponseHandler<SubPolicy> handler) {
		service.transferToPolicy(subPolicyId, newPolicyId, new BigBangAsyncCallback<SubPolicy>() {

			@Override
			public void onResponseSuccess(SubPolicy result) {
				incrementDataVersion();
				for(DataBrokerClient<SubPolicy> bc : getClients()){
					((InsuranceSubPolicyDataBrokerClient) bc).updateInsuranceSubPolicy(result);
					((InsuranceSubPolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.TRANSFER_TO_POLICY, result.id));
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not transfer subpolicy to insurance policy")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createInfoOrDocumentRequest(InfoOrDocumentRequest request,
			final ResponseHandler<InfoOrDocumentRequest> handler) {
		service.createInfoOrDocumentRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_INFO_OR_DOCUMENT_REQUEST, result.id));
				handler.onResponse(result);

			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create info or document request")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void createReceipt(Receipt receipt, final ResponseHandler<Receipt> handler) {
		service.createReceipt(receipt.policyId, receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onResponseSuccess(Receipt result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT, result.id));
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not create the new Receipt")	
				});
				super.onResponseFailure(caught);
			}
		});
	}
	
	@Override
	public void getSubPoliciesForPolicy(String ownerId,
			final ResponseHandler<Collection<SubPolicyStub>> responseHandler) {
		SubPolicySearchParameter parameter = new SubPolicySearchParameter();
		parameter.ownerId = ownerId;
		SubPolicySearchParameter[] parameters = new SubPolicySearchParameter[]{
				parameter
		};

		SubPolicySortParameter sort = new SubPolicySortParameter();
		sort.order = SortOrder.DESC;
		sort.field = SubPolicySortParameter.SortableField.NUMBER;
		SubPolicySortParameter[] sorts = new SubPolicySortParameter[]{
				sort	
		};

		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<SubPolicyStub>>() {

			@Override
			public void onResponse(Search<SubPolicyStub> response) {
				responseHandler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				responseHandler.onError(new String[]{
						new String("Could not get the subpolicies")
				});
			}
		}, true);
	}

	@Override
	public void createExpense(Expense expense, final ResponseHandler<Expense> handler){
		service.createExpense(expense, new BigBangAsyncCallback<Expense>() {

			@Override
			public void onResponseSuccess(Expense result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT, result.id));
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

}
