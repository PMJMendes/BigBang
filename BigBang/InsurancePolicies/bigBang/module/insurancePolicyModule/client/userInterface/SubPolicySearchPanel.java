package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyDataBrokerClient;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.insurancePolicyModule.client.resources.Resources;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;

public class SubPolicySearchPanel extends SearchPanel<SubPolicyStub> implements InsuranceSubPolicyDataBrokerClient{

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
	}

	private HashMap<String, SubPolicyStub> subPoliciesToUpdate;
	private HashMap<String, Void> subPoliciesToRemove;
	private int subPolicyDataVersion;
	private FiltersPanel filtersPanel;
	private String clientId;
	private String ownerId;

	public static enum Filters {
		CLIENT_SUB_POLICIES, INSURANCE_POLICY

	}

	public SubPolicySearchPanel() {
		super(((InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)).getSearchBroker());
		subPoliciesToUpdate = new HashMap<String, SubPolicyStub>();
		subPoliciesToRemove = new HashMap<String, Void>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(InsurancePolicySortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(InsurancePolicySortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(InsurancePolicySortParameter.SortableField.CLIENT_NUMBER, "Número de Cliente");
		sortOptions.put(InsurancePolicySortParameter.SortableField.CLIENT_NAME, "Nome de Cliente");

		filtersPanel = new FiltersPanel(sortOptions);

		filtersPanel.addCheckBoxField(Filters.INSURANCE_POLICY, "Apenas as apólices adesão da apólice mãe");
		filtersPanel.setFilterVisible(Filters.INSURANCE_POLICY, false);
		filtersPanel.addCheckBoxField(Filters.CLIENT_SUB_POLICIES, "Apenas as apólices adesão do cliente subscritor");
		filtersPanel.setFilterVisible(Filters.CLIENT_SUB_POLICIES, false);
		
		InsuranceSubPolicyBroker broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		broker.registerClient(this);

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
	}

	@Override
	public void doSearch(boolean keepState) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		subPoliciesToRemove.clear();
		subPoliciesToUpdate.clear();

		SubPolicySearchParameter parameter = new SubPolicySearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();
		if((Boolean)filtersPanel.getFilterValue(Filters.CLIENT_SUB_POLICIES)){
			parameter.clientId = clientId;
		}

		if((Boolean)filtersPanel.getFilterValue(Filters.INSURANCE_POLICY)){
			parameter.ownerId = ownerId;
		}

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		doSearch(parameters, null, keepState);
	}

	@Override
	public void onResults(Collection<SubPolicyStub> results) {
		for(SubPolicyStub s : results){
			if(!subPoliciesToRemove.containsKey(s.id)){
				if(subPoliciesToUpdate.containsKey(s.id)){
					s = subPoliciesToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}
	}

	protected Entry addSearchResult(SearchResult r){
		Entry entry = null;
		if(r instanceof SubPolicyStub){
			entry = new Entry((SubPolicyStub)r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			this.subPolicyDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			return subPolicyDataVersion;
		}
		return -1;
	}

	@Override
	public void addInsuranceSubPolicy(SubPolicy subPolicy) {
		this.add(0, new Entry(subPolicy));
	}

	@Override
	public void updateInsuranceSubPolicy(SubPolicy subPolicy) {
		for(ValueSelectable<SubPolicyStub> s : this){
			SubPolicyStub stub = s.getValue();
			if(subPolicy.id.equalsIgnoreCase(stub.id)){
				s.setValue(subPolicy);
				return;
			}
		}
		this.subPoliciesToUpdate.put(subPolicy.id, subPolicy);
	}

	@Override
	public void removeInsuranceSubPolicy(String subPolicyId) {
		for(ValueSelectable<SubPolicyStub> s : this){
			SubPolicyStub stub = s.getValue();
			if(subPolicyId.equalsIgnoreCase(stub.id)){
				remove(s);
				return;
			}
		}
		this.subPoliciesToRemove.put(subPolicyId, null);
	}

	@Override
	public void remapItemId(String oldId, String newId) {
		return;
	}

	public void setOwner(String ownerId) {

		if(ownerId != null){
			this.ownerId = ownerId;
		}

		SubPolicySearchParameter parameter = new SubPolicySearchParameter();
		parameter.ownerId = ownerId;

		filtersPanel.setFilterValue(Filters.INSURANCE_POLICY, ownerId != null);
		filtersPanel.setFilterVisible(Filters.INSURANCE_POLICY, true);
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		doSearch(parameters, null, false);
	}

	public void setClient(String clientId){

		if(clientId != null){
			this.clientId = clientId;
		}

		SubPolicySearchParameter parameter = new SubPolicySearchParameter();
		parameter.clientId = clientId;
		
		filtersPanel.setFilterValue(Filters.CLIENT_SUB_POLICIES, clientId != null);
		filtersPanel.setFilterVisible(Filters.CLIENT_SUB_POLICIES, true);
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		doSearch(parameters, null, false);
	}

}
