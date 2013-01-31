package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBroker;
import bigBang.module.casualtyModule.client.dataAccess.TotalLossFileBrokerClient;
import bigBang.module.casualtyModule.client.resources.Resources;
import bigBang.module.casualtyModule.shared.TotalLossSearchParameter;
import bigBang.module.casualtyModule.shared.TotalLossSortParameter;

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

public class TotalLossFileSearchPanel extends SearchPanel<TotalLossFileStub> implements TotalLossFileBrokerClient{

	public TotalLossFileSearchPanel(SearchDataBroker<TotalLossFileStub> broker) {
		super(broker);
	}

	public static class Entry extends ListEntry<TotalLossFileStub>	{

		protected Label reference;
		protected Label salvageTypeLabel;
		protected Label inheritClientName;
		protected Label inheritObjectName;
		protected Image statusIcon;

		private boolean initialized;


		public Entry(TotalLossFileStub value) {
			super(value);
			setHeight("65px");
		}

		public <I extends Object> void setInfo(I info){
			if(!initialized){
				reference = getFormatedLabel();
				reference.setWordWrap(false);
				reference.getElement().getStyle().setFontSize(14, Unit.PX);

				salvageTypeLabel = getFormatedLabel();;
				salvageTypeLabel.setWordWrap(false);
				salvageTypeLabel.getElement().getStyle().setFontSize(12, Unit.PX);
				
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
				container.add(salvageTypeLabel);
				container.add(inheritObjectName);
				container.add(inheritClientName);

				setWidget(container);

				statusIcon = new Image();


				VerticalPanel rightWrapper = new VerticalPanel();

				rightWrapper.add(statusIcon);

				rightWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

				rightWrapper.setCellVerticalAlignment(statusIcon, HasVerticalAlignment.ALIGN_MIDDLE);
				rightWrapper.setSize("100%", "100%");

				rightWrapper.setCellHorizontalAlignment(statusIcon, HasHorizontalAlignment.ALIGN_RIGHT);


				((UIObject) rightWidgetContainer).setSize("100%", "100%");

				setRightWidget(rightWrapper);
			}

			TotalLossFileStub file = (TotalLossFileStub) info;

			this.reference.setText("#" + (file.reference == null ? "" : file.reference));
			this.reference.setTitle("Número da Ficha Clínica");

			this.salvageTypeLabel.setText(file.salvageTypeLabel);
			this.salvageTypeLabel.setTitle("Posse do Salvado");

			this.inheritClientName.setText(file.inheritClientName);
			this.inheritClientName.setTitle("Cliente");

			this.inheritObjectName.setText(file.inheritObjectName);
			this.inheritObjectName.setTitle("Objecto Seguro");

			Resources resources = GWT.create(Resources.class);

			statusIcon.setResource(file.isRunning ? resources.activeCasualtyIcon() : resources.inactiveCasualtyIcon());

			setMetaData(new String[]{
					file.reference,
					file.inheritClientName,
					file.inheritObjectName,
					file.salvageTypeLabel
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
				this.salvageTypeLabel.getElement().getStyle().setColor("white");
			}else{
				this.inheritClientName.getElement().getStyle().setColor("#0066FF");
				this.inheritObjectName.getElement().getStyle().setColor("black");
				this.reference.getElement().getStyle().setColor("gray");
				this.salvageTypeLabel.getElement().getStyle().setColor("#0066FF");
			}
		}

	}
	
	private HashMap<String, TotalLossFileStub> totalLossFilesToUpdate;
	private HashMap<String, Void> totalLossFilesToRemove;
	private int totalLossFileDataVersion;
	private FiltersPanel filtersPanel;
	
	public TotalLossFileSearchPanel() {
		super(((TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE)).getSearchBroker());
	
		totalLossFilesToRemove = new HashMap<String, Void>();
		totalLossFilesToUpdate = new HashMap<String, TotalLossFileStub>();
	
		Map<Enum<?> , String> sortOptions = new TreeMap<Enum<?>, String>();
		sortOptions.put(TotalLossSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(TotalLossSortParameter.SortableField.REFERENCE, "Referência");
		sortOptions.put(TotalLossSortParameter.SortableField.CLIENT_NAME, "Nome do Cliente");

		filtersPanel = new FiltersPanel(sortOptions);
		
		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
		
		TotalLossFileBroker broker = (TotalLossFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TOTAL_LOSS_FILE);
		broker.registerClient(this);
	}
	
	
	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.TOTAL_LOSS_FILE)){
			totalLossFileDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.TOTAL_LOSS_FILE)){
			return totalLossFileDataVersion;
		}
		return -1;	}

	@Override
	public void updateTotalLossFile(TotalLossFile result) {
		for(ListEntry<TotalLossFileStub> s : this){
			TotalLossFileStub med = s.getValue();
			if(med.id.equalsIgnoreCase(result.id)){
				s.setValue(result);
				return;
			}
		}
		this.totalLossFilesToUpdate.put(result.id,result);
	}

	@Override
	public void doSearch(boolean keepSelected) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		totalLossFilesToRemove.clear();
		totalLossFilesToUpdate.clear();
		
		TotalLossSearchParameter parameter = new TotalLossSearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		
		SortParameter[] sorts = new SortParameter[]{
				new TotalLossSortParameter((TotalLossSortParameter.SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
		};
		
		doSearch(parameters, sorts, keepSelected);
	}

	@Override
	public void onResults(Collection<TotalLossFileStub> results) {
		for(TotalLossFileStub med : results){
			if(!totalLossFilesToRemove.containsKey(med.id)){
				if(totalLossFilesToUpdate.containsKey(med.id)){
					med = totalLossFilesToUpdate.get(med.id);
				}
				addSearchResult(med);
			}
		}
	}

	private void addSearchResult(TotalLossFileStub med) {
		Entry entry = null;
		if(med instanceof TotalLossFileStub){
			entry = new Entry((TotalLossFileStub)med);
			add(entry);
		}
	}
}
