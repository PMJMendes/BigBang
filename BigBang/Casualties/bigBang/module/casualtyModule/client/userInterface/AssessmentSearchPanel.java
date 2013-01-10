package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.AssessmentStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.AssessmentBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.client.resources.Resources;
import bigBang.module.casualtyModule.shared.AssessmentSearchParameter;
import bigBang.module.casualtyModule.shared.AssessmentSortParameter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AssessmentSearchPanel extends SearchPanel<AssessmentStub> implements AssessmentBrokerClient{

	public AssessmentSearchPanel(SearchDataBroker<AssessmentStub> broker) {
		super(broker);
	}

	public static enum Filters{
		INSURED_OBJECT,
		INCLUDE_CLOSED
	}

	public static class Entry extends ListEntry<AssessmentStub>{

		protected Label reference;
		protected Label scheduledDate;
		protected Label effectiveDate;
		protected Label inheritClientName;
		protected Label inheritObjectName;
		protected Image statusIcon;

		protected boolean initialized;

		public Entry(AssessmentStub value) {
			super(value);
			setHeight("65px");
		}

		public <I extends Object> void setInfo(I info){
			if(!initialized){
				reference = getFormatedLabel();
				reference.setWordWrap(false);
				reference.getElement().getStyle().setFontSize(14, Unit.PX);

				inheritClientName = getFormatedLabel();
				inheritClientName.getElement().getStyle().setFontSize(11, Unit.PX);
				inheritClientName.getElement().getStyle().setProperty("whiteSpace", "");
				inheritClientName.setHeight("1.2em");

				inheritObjectName = getFormatedLabel();
				inheritObjectName.getElement().getStyle().setFontSize(10, Unit.PX);
				inheritObjectName.getElement().getStyle().setProperty("whiteSpace", "");
				inheritObjectName.setHeight("1.2em");

				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");

				container.add(reference);
				container.add(inheritObjectName);
				container.add(inheritClientName);

				setWidget(container);

				statusIcon = new Image();

				scheduledDate = getFormatedLabel();

				effectiveDate = getFormatedLabel();

				VerticalPanel rightWrapper = new VerticalPanel();

				rightWrapper.add(scheduledDate);
				rightWrapper.add(statusIcon);
				rightWrapper.add(effectiveDate);

				rightWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				rightWrapper.setCellHorizontalAlignment(statusIcon, HasHorizontalAlignment.ALIGN_RIGHT);
				rightWrapper.setCellVerticalAlignment(scheduledDate, HasVerticalAlignment.ALIGN_TOP);
				rightWrapper.setCellVerticalAlignment(statusIcon, HasVerticalAlignment.ALIGN_MIDDLE);
				rightWrapper.setCellVerticalAlignment(effectiveDate, HasVerticalAlignment.ALIGN_BOTTOM);
				rightWrapper.setSize("100%", "100%");
				
				rightWrapper.setCellHorizontalAlignment(statusIcon, HasHorizontalAlignment.ALIGN_RIGHT);
				rightWrapper.setCellHorizontalAlignment(scheduledDate, HasHorizontalAlignment.ALIGN_RIGHT);
				rightWrapper.setCellHorizontalAlignment(effectiveDate, HasHorizontalAlignment.ALIGN_RIGHT);

				((UIObject) rightWidgetContainer).setSize("100%", "100%");

				setRightWidget(rightWrapper);
			}

			AssessmentStub ass = (AssessmentStub)info;

			this.reference.setText("#" + (ass.reference == null ? "" : ass.reference));
			this.reference.setTitle("Número da Peritagem ou Averiguação");

			this.inheritClientName.setText(ass.inheritClientName);
			this.inheritClientName.setTitle("Cliente");

			this.inheritObjectName.setText(ass.inheritObjectName);
			this.inheritObjectName.setTitle("Objecto Seguro");

			scheduledDate.setTitle("Data agendada");

			effectiveDate.setTitle("Data efectiva");

			if(ass.scheduledDate == null){
				if(ass.effectiveDate == null){
					scheduledDate.setText("Não agendada");
					effectiveDate.setText("");
				}
				else{
					scheduledDate.setText("");
					effectiveDate.setText("Data: " + ass.effectiveDate);
				}
			}else{
				scheduledDate.setText("Agendada para: " + ass.scheduledDate);
				effectiveDate.setText(ass.effectiveDate == null ? "" : "Data: " + ass.effectiveDate);
			}

			Resources resources = GWT.create(Resources.class);

			statusIcon.setResource(ass.isRunning ? resources.activeCasualtyIcon() : resources.inactiveCasualtyIcon());

			setMetaData(new String[]{
				ass.effectiveDate,
				ass.reference,
				ass.inheritClientName,
				ass.inheritObjectName,
				ass.scheduledDate
			});
			
			initialized = true;
			setSelected(this.isSelected(), false);
		}

		@Override
		public void setSelected(boolean selected) {
			super.setSelected(selected);
			if(!initialized){
				return;
			}
			
			if(selected){
				this.inheritClientName.getElement().getStyle().setColor("white");
				this.inheritObjectName.getElement().getStyle().setColor("white");
				this.reference.getElement().getStyle().setColor("white");
				this.scheduledDate.getElement().getStyle().setColor("white");
				this.effectiveDate.getElement().getStyle().setColor("white");
			}else{
				this.inheritClientName.getElement().getStyle().setColor("#0066FF");
				this.inheritObjectName.getElement().getStyle().setColor("black");
				this.reference.getElement().getStyle().setColor("gray");
				this.scheduledDate.getElement().getStyle().setColor("#0066FF");
				this.effectiveDate.getElement().getStyle().setColor("#0066FF");
			}
		}

	}

	private HashMap<String, AssessmentStub> assessmentsToUpdate;
	private HashMap<String, Void> assessmentsToRemove;
	private int assessmentDataVersion;
	private FiltersPanel filtersPanel;

	public AssessmentSearchPanel() {
		super(((AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT)).getSearchBroker());
		assessmentsToRemove = new HashMap<String, Void>();
		assessmentsToUpdate = new HashMap<String, AssessmentStub>();

		Map<Enum<?> , String> sortOptions = new TreeMap<Enum<?>, String>();
		sortOptions.put(AssessmentSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(AssessmentSortParameter.SortableField.CLIENT_NAME, "Nome do Cliente");
		sortOptions.put(AssessmentSortParameter.SortableField.REFERENCE, "Referência");

		filtersPanel = new FiltersPanel(sortOptions);

		filtersPanel.addTextField(Filters.INSURED_OBJECT, "Objecto Seguro");
		filtersPanel.addCheckBoxField(Filters.INCLUDE_CLOSED, "Incluir Encerrados");

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);

		AssessmentBroker broker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
		broker.registerClient(this);
	}


	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.ASSESSMENT)){
			this.assessmentDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.ASSESSMENT)){
			return assessmentDataVersion;
		}
		return -1;
	}

	@Override
	public void updateAssessment(Assessment response) {
		for(ValueSelectable<AssessmentStub> s : this){
			AssessmentStub ass = s.getValue();
			if(ass.id.equalsIgnoreCase(response.id)){
				s.setValue(response);
				return;
			}
		}
		this.assessmentsToUpdate.put(response.id, response);
	}

	@Override
	public void doSearch(boolean keepState) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		assessmentsToRemove.clear();
		assessmentsToUpdate.clear();

		AssessmentSearchParameter parameter = new AssessmentSearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();

		parameter.insuredObject = (String) filtersPanel.getFilterValue(Filters.INSURED_OBJECT);
		parameter.includeClosed = (Boolean) filtersPanel.getFilterValue(Filters.INCLUDE_CLOSED);

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SortParameter[] sorts = new SortParameter[]{
				new AssessmentSortParameter((AssessmentSortParameter.SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
		};

		doSearch(parameters, sorts, keepState);

	}

	@Override
	public void onResults(Collection<AssessmentStub> results) {
		for(AssessmentStub ass : results){
			if(!assessmentsToRemove.containsKey(ass.id)){
				if(assessmentsToUpdate.containsKey(ass.id)){
					ass = assessmentsToUpdate.get(ass.id);
				}
				addSearchResult(ass);
			}
		}
	}

	private Entry addSearchResult(SearchResult ass) {
		Entry entry = null;
		if(ass instanceof AssessmentStub){
			entry = new Entry((AssessmentStub)ass);
			add(entry);
		}
		return entry;
	}		
}
