package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFileStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.MedicalFileBrokerClient;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.client.dataAccess.MedicalFileBroker;
import bigBang.module.casualtyModule.client.resources.Resources;
import bigBang.module.casualtyModule.shared.MedicalFileSearchParameter;
import bigBang.module.casualtyModule.shared.MedicalFileSortParameter;

import com.google.gwt.user.client.ui.UIObject;

public class MedicalFileSearchPanel extends SearchPanel<MedicalFileStub> implements MedicalFileBrokerClient{

	public MedicalFileSearchPanel(SearchDataBroker<MedicalFileStub> broker){
		super(broker);
	}
	
	public static enum Filters{
		INSURED_OBJECT,
		INCLUDE_CLOSED
	}

	public static class Entry extends ListEntry<MedicalFileStub>{

		protected Label reference;
		protected Label scheduledDate;
		protected Label inheritClientName;
		protected Label inheritObjectName;
		protected Image statusIcon;
		protected Label dummyLabel;
		
		protected boolean initialized;


		public Entry(MedicalFileStub value){
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

				dummyLabel = getFormatedLabel();
				
				VerticalPanel rightWrapper = new VerticalPanel();

				rightWrapper.add(scheduledDate);
				rightWrapper.add(statusIcon);
				rightWrapper.add(dummyLabel);

				rightWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				
				rightWrapper.setCellVerticalAlignment(scheduledDate, HasVerticalAlignment.ALIGN_TOP);
				rightWrapper.setCellVerticalAlignment(statusIcon, HasVerticalAlignment.ALIGN_MIDDLE);
				rightWrapper.setCellVerticalAlignment(dummyLabel, HasVerticalAlignment.ALIGN_BOTTOM);
				rightWrapper.setSize("100%", "100%");
				
				rightWrapper.setCellHorizontalAlignment(statusIcon, HasHorizontalAlignment.ALIGN_RIGHT);
				rightWrapper.setCellHorizontalAlignment(scheduledDate, HasHorizontalAlignment.ALIGN_RIGHT);
				rightWrapper.setCellHorizontalAlignment(dummyLabel, HasHorizontalAlignment.ALIGN_RIGHT);

				
				((UIObject) rightWidgetContainer).setSize("100%", "100%");

				setRightWidget(rightWrapper);
			}

			MedicalFileStub file = (MedicalFileStub)info;

			this.reference.setText("#" + (file.reference == null ? "" : file.reference));
			this.reference.setTitle("Número da Ficha Clínica");

			this.inheritClientName.setText(file.inheritClientName);
			this.inheritClientName.setTitle("Cliente");

			this.inheritObjectName.setText(file.inheritObjectName);
			this.inheritObjectName.setTitle("Objecto Seguro");

			scheduledDate.setTitle("Data da próxima consulta");
			scheduledDate.setText(("Data da próxima consulta: "  ) + (file.nextDate != null ? file.nextDate : ""));

			Resources resources = GWT.create(Resources.class);

			statusIcon.setResource(file.isRunning ? resources.activeCasualtyIcon() : resources.inactiveCasualtyIcon());

			setMetaData(new String[]{
					file.reference,
					file.inheritClientName,
					file.inheritObjectName,
					file.nextDate
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
			}else{
				this.inheritClientName.getElement().getStyle().setColor("#0066FF");
				this.inheritObjectName.getElement().getStyle().setColor("black");
				this.reference.getElement().getStyle().setColor("gray");
				this.scheduledDate.getElement().getStyle().setColor("#0066FF");
			}
		}

	}

	private HashMap<String, MedicalFileStub> medicalFilesToUpdate;
	private HashMap<String, Void> medicalFilesToRemove;
	private int medicalFileDataVersion;
	private FiltersPanel filtersPanel;

	public MedicalFileSearchPanel(){
		super(((MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE)).getSearchBroker());
		medicalFilesToRemove = new HashMap<String, Void>();
		medicalFilesToUpdate = new HashMap<String, MedicalFileStub>();

		Map<Enum<?> , String> sortOptions = new TreeMap<Enum<?>, String>();
		sortOptions.put(MedicalFileSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(MedicalFileSortParameter.SortableField.NEXT_DATE, "Data da próxima consulta");
		sortOptions.put(MedicalFileSortParameter.SortableField.REFERENCE, "Referência");

		
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
		
		MedicalFileBroker broker = (MedicalFileBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.MEDICAL_FILE);
		broker.registerClient(this);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.MEDICAL_FILE)){
			medicalFileDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.MEDICAL_FILE)){
			return medicalFileDataVersion;
		}
		return -1;
	}

	@Override
	public void updateMedicalFile(MedicalFile response) {
		for(ValueSelectable<MedicalFileStub> s : this){
			MedicalFileStub med = s.getValue();
			if(med.id.equalsIgnoreCase(response.id)){
				s.setValue(response);
				return;
			}
		}
		this.medicalFilesToUpdate.put(response.id,response);
	}

	@Override
	public void doSearch(boolean keepSelected) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		medicalFilesToRemove.clear();
		medicalFilesToUpdate.clear();
		
		MedicalFileSearchParameter parameter = new MedicalFileSearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();

		parameter.insuredObject = (String) filtersPanel.getFilterValue(Filters.INSURED_OBJECT);
		parameter.includeClosed = (Boolean) filtersPanel.getFilterValue(Filters.INCLUDE_CLOSED);
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		
		SortParameter[] sorts = new SortParameter[]{
				new MedicalFileSortParameter((MedicalFileSortParameter.SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
		};
		
		doSearch(parameters, sorts, keepSelected);
	}

	@Override
	public void onResults(Collection<MedicalFileStub> results) {
		for(MedicalFileStub med : results){
			if(!medicalFilesToRemove.containsKey(med.id)){
				if(medicalFilesToUpdate.containsKey(med.id)){
					med = medicalFilesToUpdate.get(med.id);
				}
				addSearchResult(med);
			}
		}
	}

	private void addSearchResult(MedicalFileStub med) {
		Entry entry = null;
		if(med instanceof MedicalFileStub){
			entry = new Entry((MedicalFileStub)med);
			add(entry);
		}
	}

	
}
