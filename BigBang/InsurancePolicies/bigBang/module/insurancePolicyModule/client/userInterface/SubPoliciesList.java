package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.insurancePolicyModule.client.resources.Resources;

public class SubPoliciesList extends FilterableList<SubPolicyStub>  implements InsuranceSubPolicyDataBrokerClient {

	public static class Entry extends ListEntry<SubPolicyStub>{
		protected Label numberLabel;
		protected Label clientLabel;
		protected Label lineLabel;
		protected Image statusIcon;

		public Entry(SubPolicyStub subPolicy){
			
			super(subPolicy);
			setHeight("55px");
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			
			SubPolicyStub value = (SubPolicyStub)info;
			if(value.id != null){
				if(numberLabel == null) {
					numberLabel = getFormatedLabel();
					numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
					numberLabel.setWordWrap(false);
					clientLabel = getFormatedLabel();
					clientLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					lineLabel = getFormatedLabel();
					lineLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					VerticalPanel container = new VerticalPanel();
					container.setSize("100%", "100%");

					container.add(numberLabel);
					container.add(lineLabel);
					container.add(clientLabel);

					setWidget(container);

					statusIcon = new Image();
					statusIcon.setTitle(value.statusText);
					setLeftWidget(statusIcon);
				}

				numberLabel.setText("#" + value.number);
				clientLabel.setText("#" + value.clientNumber + " - " + value.clientName);
				lineLabel.setText(value.inheritCategoryName+" / "+value.inheritLineName+" / "+value.inheritSubLineName);
				lineLabel.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);

				setMetaData(new String[]{
						value.number,
						value.inheritCategoryName,
						value.inheritLineName,
						value.inheritSubLineName,
						value.clientNumber,
						value.clientName
					});
				
				Resources resources = GWT.create(Resources.class);
				switch(value.statusIcon){
				case OBSOLETE:
					statusIcon.setResource(resources.inactivePolicyIcon());
					break;
				case PROVISIONAL:
					statusIcon.setResource(resources.provisionalPolicyIcon());
					break;
				case VALID:
					statusIcon.setResource(resources.activePolicyIcon());
					break;
				default:
					return;
				}

				return;
			}
			
		};
		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(this.clientLabel == null) {return;}
			if(selected){
				this.clientLabel.getElement().getStyle().setColor("white");
			}else{
				this.clientLabel.getElement().getStyle().setColor("gray");
			}
		}
		
		
		
	}

	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected String ownerId;

	public SubPoliciesList(){
		this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		showFilterField(false);

	}

	public void setOwner(String ownerId){
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else{
			if(!subPolicyBroker.isTemp(ownerId)){
				this.subPolicyBroker.getSubPoliciesForPolicy(ownerId, new ResponseHandler<Collection<SubPolicyStub>>() {

					@Override
					public void onResponse(Collection<SubPolicyStub> response) {
						clear();
						for(SubPolicyStub o : response){
							addEntry(o);
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}
		}
	}

	public void addEntry(SubPolicyStub subPolicy){
		this.add(new Entry(subPolicy));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}

	@Override
	public void addInsuranceSubPolicy(SubPolicy subPolicy) {
		if(ownerId != null && ownerId.equalsIgnoreCase(subPolicy.mainPolicyId)) {
			addEntry(subPolicy);
		}
	}

	@Override
	public void updateInsuranceSubPolicy(SubPolicy subPolicy) {
		if(ownerId != null && ownerId.equalsIgnoreCase(subPolicy.mainPolicyId)) {
			for(ListEntry<SubPolicyStub> entry : this) {
				if(entry.getValue().id.equalsIgnoreCase(subPolicy.id)){
					entry.setValue(subPolicy);
					break;
				}
			}
		}
	}

	@Override
	public void removeInsuranceSubPolicy(String subPolicyId) {
		if(ownerId != null && ownerId.equalsIgnoreCase(subPolicyId)) {
			for(ListEntry<SubPolicyStub> entry : this) {
				if(entry.getValue().id.equalsIgnoreCase(subPolicyId)){
					remove(entry);
					break;
				}
			}
		}
	}

	@Override
	public void remapItemId(String oldId, String newId) {
		return;
	}

}
