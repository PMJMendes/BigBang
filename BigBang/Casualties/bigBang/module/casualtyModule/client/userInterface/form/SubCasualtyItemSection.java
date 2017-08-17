package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.Collection;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyItem;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.FormField;
import bigBang.library.client.Session;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.module.insurancePolicyModule.client.dataAccess.PolicyTypifiedListBroker;
import bigBang.module.insurancePolicyModule.client.dataAccess.SubPolicyTypifiedListBroker;

public class SubCasualtyItemSection extends CollapsibleFormViewSection {

	protected SubCasualty.SubCasualtyItem currentItem;

	protected ExpandableListBoxFormField coverage;
	protected ExpandableListBoxFormField damageType;
	protected NumericTextBoxFormField damages;
	protected NumericTextBoxFormField settlement;
	protected NumericTextBoxFormField itemValue;
	protected NumericTextBoxFormField deductible;
	protected ExpandableListBoxFormField injuryCause;
	protected ExpandableListBoxFormField injuryType;
	protected ExpandableListBoxFormField injuredPart;
	protected CheckBoxFormField thirdParty;
	protected TextAreaFormField notes;
	
	protected Button removeButton;

	public SubCasualtyItemSection(SubCasualtyItem item, String referenceTypeId, String referenceId) {
		super("");
		removeButton = new Button("Remover");

		coverage = new ExpandableListBoxFormField("Cobertura");
		damageType = new ExpandableListBoxFormField("Tipologia");
		damages = new NumericTextBoxFormField("Valor dos Danos", true);
		damages.setUnitsLabel(Session.getCurrency());
		damages.setFieldWidth("175px");
		settlement = new NumericTextBoxFormField("Valor da Indemnização", true);
		settlement.setFieldWidth("175px");
		settlement.setUnitsLabel(Session.getCurrency());
		itemValue = new NumericTextBoxFormField("Capital Segurado", true);
		itemValue.setUnitsLabel(Session.getCurrency());
		deductible = new NumericTextBoxFormField("Franquia", true);
		injuryCause = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INJURY_CAUSE, "Causa do Sinistro");
		injuryCause.allowEdition(false);
		injuryType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INJURY_TYPE, "Tipo de Lesão");
		injuryType.allowEdition(false);
		injuredPart = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INJURED_PART, "Parte do Corpo");
		injuredPart.allowEdition(false);
		thirdParty = new CheckBoxFormField("Paga A Terceiros");
		notes = new TextAreaFormField("Notas do Detalhe");

		addFormFieldGroup(new FormField<?>[]{
				coverage,
				itemValue,
				deductible
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				damageType,
				damages,
				settlement,
				thirdParty
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				injuryCause,
				injuryType,
				injuredPart
		}, true);
		
		addFormFieldGroup(new FormField<?>[]{
				notes
		}, true);
				
		SimplePanel buttonWrapper = new SimplePanel();
		buttonWrapper.add(removeButton);
		buttonWrapper.setWidth("100%");

		addWidget(buttonWrapper, false);

		setItem(item, referenceTypeId, referenceId);
	}

	public void setItem(final SubCasualtyItem item, String referenceTypeId, String referenceId){
		this.currentItem = item;
		if(item != null) {
			this.headerLabel.setText("Detalhe");

			setReference(referenceTypeId, referenceId);

			coverage.setTypifiedDataBroker((TypifiedListBroker) (referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ?
					PolicyTypifiedListBroker.Util.getInstance() :
						SubPolicyTypifiedListBroker.Util.getInstance()));
			coverage.setListId((referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ?
					BigBangConstants.TypifiedListIds.POLICY_COVERAGE :
						BigBangConstants.TypifiedListIds.SUB_POLICY_COVERAGE)+"/"+referenceId, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
			
			coverage.setValue(item.coverageId);
			damageType.setValue(item.damageTypeId);
			damages.setValue(item.damages);
			settlement.setValue(item.settlement);
			deductible.setValue(item.deductible);
			itemValue.setValue(item.value);
			injuryCause.setValue(item.injuryCauseId);
			injuryType.setValue(item.injuryTypeId);
			injuredPart.setValue(item.injuredPartId);
			thirdParty.setValue(item.isThirdParty);
			notes.setValue(item.notes);
		}
	}

	public void setReference(String referenceTypeId, String referenceId){
		if(referenceId != null) {
			if(referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
				coverage.setListId(BigBangConstants.TypifiedListIds.POLICY_COVERAGE + "/" + referenceId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
				
				InsurancePolicyBroker policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
				policyBroker.getPolicy(referenceId, new ResponseHandler<InsurancePolicy>() {
					
					@Override
					public void onResponse(InsurancePolicy response) {
						damageType.setListId(BigBangConstants.TypifiedListIds.DAMAGE_TYPE + "/" + response.subLineId, new ResponseHandler<Void>() {

							@Override
							public void onResponse(Void response) {
								damageType.allowEdition(false);
								return;
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});

					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}else{
				coverage.setListId(BigBangConstants.TypifiedListIds.SUB_POLICY_COVERAGE + "/" + referenceId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
				
				InsuranceSubPolicyBroker subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
				subPolicyBroker.getSubPolicy(referenceId, new ResponseHandler<SubPolicy>() {
					
					@Override
					public void onResponse(SubPolicy response) {
						damageType.setListId(BigBangConstants.TypifiedListIds.DAMAGE_TYPE + "/" + response.inheritSubLineId, new ResponseHandler<Void>() {

							@Override
							public void onResponse(Void response) {
								damageType.allowEdition(false);
								return;
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});

					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}
		}else{
			coverage.setListId("", new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
			damageType.setListId("", new ResponseHandler<Void>() {
				
				@Override
				public void onResponse(Void response) {
					damageType.allowEdition(false);
					return;
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}

		coverage.allowEdition(false);
	}

	public SubCasualtyItem getItem(){
		SubCasualtyItem result = this.currentItem;

		if(result != null) {
			result.coverageId = coverage.getValue();
			result.damageTypeId = damageType.getValue();
			result.damages = damages.getValue();
			result.settlement = settlement.getValue();
			result.deductible = deductible.getValue();
			result.value = itemValue.getValue();
			result.injuryCauseId = injuryCause.getValue();
			result.injuryTypeId = injuryType.getValue();
			result.injuredPartId = injuredPart.getValue();
			result.isThirdParty = thirdParty.getValue();
			result.notes = notes.getValue();
		}

		return result;
	}

	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		removeButton.setVisible(!readOnly);
	}
}
