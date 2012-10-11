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

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.insurancePolicyModule.client.resources.Resources;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter.SortableField;

public class InsurancePolicySearchPanel extends SearchPanel<InsurancePolicyStub> implements InsurancePolicyDataBrokerClient {

	/**
	 * An entry in the search panel
	 */
	public static class Entry extends ListEntry<InsurancePolicyStub>{
		
		protected Label numberLabel;
		protected Label clientLabel;
		protected Label lineLabel;
		protected Label insuredObjectLabel;
		protected Image statusIcon;

		public Entry(InsurancePolicyStub policy){
			super(policy);
			setHeight("65px");
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			InsurancePolicyStub value = (InsurancePolicyStub)info;
			if(value.id != null){
				if(numberLabel == null) {
					numberLabel = getFormatedLabel();
					numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
					numberLabel.setWordWrap(false);
					clientLabel = getFormatedLabel();
					clientLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					clientLabel.getElement().getStyle().setProperty("whiteSpace", "");
					clientLabel.setHeight("1.2em");
					lineLabel = getFormatedLabel();
					lineLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					insuredObjectLabel = getFormatedLabel();
					insuredObjectLabel.getElement().getStyle().setFontSize(10, Unit.PX);
					insuredObjectLabel.getElement().getStyle().setProperty("whiteSpace", "");
					insuredObjectLabel.setHeight("1.2em");
					VerticalPanel container = new VerticalPanel();
					container.setSize("100%", "100%");

					container.add(numberLabel);
					container.add(lineLabel);
					container.add(clientLabel);
					container.add(insuredObjectLabel);

					setWidget(container);

					statusIcon = new Image();
					statusIcon.setTitle(value.statusText);
					setRightWidget(statusIcon);
				}

				numberLabel.setText(value.number == null ? "Nova Apólice" : "Apólice #" + value.number);
				clientLabel.setText("#" + (value.clientNumber == null ? "" : value.clientNumber) + " - " + (value.clientName == null ? "" : value.clientName));
				lineLabel.setText((value.categoryName == null ? "-" : value.categoryName)+
						" / "+(value.lineName == null ? "-" : value.lineName)+" / "+(value.subLineName == null ? "-" : value.subLineName));
				lineLabel.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);

				insuredObjectLabel.setText(value.insuredObject);
				
				Resources resources = GWT.create(Resources.class);
				if(value.statusIcon == null) {
					statusIcon.setResource(resources.provisionalPolicyIcon());
					statusIcon.setVisible(false);
				}else{
					statusIcon.setVisible(true);
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
				}
				
				setMetaData(new String[]{
					value.number,
					value.categoryName,
					value.lineName,
					value.subLineName,
					value.clientNumber,
					value.clientName
				});

				return;
			}
			throw new RuntimeException("The given policy was invalid (InsurancePolicySearchPanel.Entry)");
		};

		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(this.clientLabel == null) {return;}
			if(selected){
				this.clientLabel.getElement().getStyle().setColor("white");
				this.insuredObjectLabel.getElement().getStyle().setColor("white");
			}else{
				this.clientLabel.getElement().getStyle().setColor("gray");
				this.insuredObjectLabel.getElement().getStyle().setColor("gray");
			}
		}
	}

	public static enum Filters {
		CATEGORY,
		LINE,
		SUBLINE,
		INSURANCE_AGENCY,
		MEDIATOR,
		MANAGER,
		INSURED_OBJECT,
		CASE_STUDY, CLIENT_POLICIES
	}
	protected FiltersPanel filtersPanel;

	protected int insurancePolicyDataVersion = 0;
	protected Map<String, InsurancePolicyStub> policiesToUpdate;
	protected Map<String, Void> policiesToRemove;

	private String ownerId;

	public InsurancePolicySearchPanel() {
		super(((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY)).getSearchBroker());
		
		
		policiesToUpdate = new HashMap<String, InsurancePolicyStub>();
		policiesToRemove = new HashMap<String, Void>();
				
		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(InsurancePolicySortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(InsurancePolicySortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(InsurancePolicySortParameter.SortableField.CATEGORY_LINE_SUBLINE, "Categoria/Ramo/Modalidade");
		sortOptions.put(InsurancePolicySortParameter.SortableField.CLIENT_NUMBER, "Número de Cliente");
		sortOptions.put(InsurancePolicySortParameter.SortableField.CLIENT_NAME, "Nome de Cliente");

		filtersPanel = new FiltersPanel(sortOptions);
		filtersPanel.addCheckBoxField(Filters.CLIENT_POLICIES, "Incluir apenas apólices do cliente");
		filtersPanel.setFilterVisible(Filters.CLIENT_POLICIES, false);
		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor de Apólice");
		filtersPanel.addTypifiedListField(Filters.INSURANCE_AGENCY, BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		filtersPanel.addTypifiedListField(Filters.CATEGORY, BigBangConstants.EntityIds.CATEGORY, "Categoria");
		filtersPanel.addTypifiedListField(Filters.LINE, BigBangConstants.EntityIds.LINE, "Ramo", Filters.CATEGORY);
		filtersPanel.addTypifiedListField(Filters.SUBLINE, BigBangConstants.EntityIds.SUB_LINE, "Modalidade", Filters.LINE);
		filtersPanel.addTextField(Filters.INSURED_OBJECT, "Unidade de Risco");
		filtersPanel.addCheckBoxField(Filters.CASE_STUDY, "Apenas Case Study");

		InsurancePolicyBroker broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
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
		this.policiesToRemove.clear();
		this.policiesToUpdate.clear();

		InsurancePolicySearchParameter parameter = new InsurancePolicySearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();
		parameter.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
		parameter.mediatorId = (String) filtersPanel.getFilterValue(Filters.MEDIATOR);
		parameter.insuranceAgencyId = (String) filtersPanel.getFilterValue(Filters.INSURANCE_AGENCY);
		parameter.categoryId = (String) filtersPanel.getFilterValue(Filters.CATEGORY);
		parameter.lineId = (String) filtersPanel.getFilterValue(Filters.LINE);
		parameter.subLineId = (String) filtersPanel.getFilterValue(Filters.SUBLINE);
		parameter.insuredObject = (String) filtersPanel.getFilterValue(Filters.INSURED_OBJECT);
		boolean caseStudy = (Boolean) filtersPanel.getFilterValue(Filters.CASE_STUDY);
		parameter.caseStudy = caseStudy ? true : null;
		if((Boolean)filtersPanel.getFilterValue(Filters.CLIENT_POLICIES) == false || !filtersPanel.isFilterVisible(Filters.CLIENT_POLICIES)){
			parameter.ownerId = null;
		}else{
			parameter.ownerId = ownerId;
		}

		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		InsurancePolicySortParameter sort = new InsurancePolicySortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder());

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		doSearch(parameters, sorts, keepState);
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
	
	public ValueSelectable<InsurancePolicyStub> addEntry(InsurancePolicyStub policy) {
		Entry entry = new Entry(policy);
		add(0, entry);
		return entry;
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
		this.policiesToRemove.put(policyId, null);
	}

	@Override
	public void remapItemId(String oldId, String newId) {
		return;
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
//		doSearch(); TODO
	}

	public void setOwner(String ownerId) {
		
		if(ownerId != null){
			this.ownerId = ownerId;
		}
		
		InsurancePolicySearchParameter parameter = new InsurancePolicySearchParameter();
		parameter.ownerId = ownerId;
		filtersPanel.setFilterVisible(Filters.CLIENT_POLICIES, true);
		filtersPanel.setFilterValue(Filters.CLIENT_POLICIES, ownerId != null);
		
		SearchParameter[] parameters = new SearchParameter[]{parameter};
		
		doSearch(parameters, null);
		
	}

}
