package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;

public class InsurancePolicySearchPanel extends SearchPanel<InsurancePolicyStub> implements InsurancePolicyDataBrokerClient {
	
	/**
	 * An entry in the search panel
	 */
	public static class Entry extends ListEntry<InsurancePolicyStub>{
		public Entry(InsurancePolicyStub policy){
			super(policy);
			setHeight("40px");
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}
		
		public <I extends Object> void setInfo(I info) {
			InsurancePolicyStub value = (InsurancePolicyStub)info;
			if(value.id != null){
				Label numberLabel = new Label(value.number);
				numberLabel.setWordWrap(false);
				numberLabel.setWidth("45px");
				setLeftWidget(numberLabel);
				setText(value.clientNumber + " - " + value.clientName);
				setTitle(value.categoryName+" / "+value.lineName+" / "+value.subLineName);
				this.textLabel.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
				return;
			}
			throw new RuntimeException("The given policy was invalid (InsurancePolicySearchPanel.Entry)");
		};
		
		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(selected){
				this.textLabel.getElement().getStyle().setColor("white");
			}else{
				this.textLabel.getElement().getStyle().setColor("gray");
			}
		}
	}

	protected int insurancePolicyDataVersion = 0;
	protected Map<String, InsurancePolicyStub> policiesToUpdate;
	protected Map<String, Void> policiesToRemove;
	
	public InsurancePolicySearchPanel() {
		super(((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY)).getSearchBroker());
		policiesToUpdate = new HashMap<String, InsurancePolicyStub>();
		policiesToRemove = new HashMap<String, Void>();
	}

	@Override
	public void doSearch() {
		this.policiesToRemove.clear();
		this.policiesToUpdate.clear();
		
		SearchParameter[] parameters = new SearchParameter[]{
				
		};
		
		SortParameter[] sorts = new SortParameter[]{
				
		};
		
		doSearch(parameters, sorts);
	}

	@Override
	public void onResults(Collection<InsurancePolicyStub> results) {
		for(InsurancePolicyStub s : results){
			if(!policiesToRemove.containsKey(s.id)){
				if(policiesToUpdate.containsKey(s.id)){
					s = policiesToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}
	}

	protected Entry addSearchResult(SearchResult r){
		Entry entry = null;
		if(r instanceof InsurancePolicyStub){
			entry = new Entry((InsurancePolicyStub)r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
			this.insurancePolicyDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
			return this.insurancePolicyDataVersion;
		}
		return -1;
	}

	@Override
	public void addInsurancePolicy(InsurancePolicy policy) {
		this.add(0, new Entry(policy));
	}

	@Override
	public void updateInsurancePolicy(InsurancePolicy policy) {
		for(ValueSelectable<InsurancePolicyStub> s : this) {
			InsurancePolicyStub policyStub = s.getValue();
			if(policy.id.equalsIgnoreCase(policyStub.id)){
				s.setValue(policy);
				return;
			}
		}
		this.policiesToUpdate.put(policy.id, policy);
	}

	@Override
	public void removeInsurancePolicy(String policyId) {
		for(ValueSelectable<InsurancePolicyStub> s : this) {
			InsurancePolicyStub policyStub = s.getValue();
			if(policyId.equalsIgnoreCase(policyStub.id)){
				remove(s);
				return;
			}
		}
		this.policiesToUpdate.put(policyId, null);
	}

}
