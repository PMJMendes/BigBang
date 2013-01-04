package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.client.resources.Resources;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubCasualtySearchPanel extends SearchPanel<SubCasualtyStub> implements SubCasualtyDataBrokerClient{

	public static class Entry extends ListEntry<SubCasualtyStub> {
		protected Label numberLabel;
		protected Label policyLabel;
		protected Label valueLabel;
		protected Image openImage;
		protected boolean initialized;

		public Entry(SubCasualtyStub value) {
			super(value);
			setHeight("40px");
		}

		@Override
		public <I extends Object> void setInfo(I info) {
			if(!initialized){
				numberLabel = getFormatedLabel();
				numberLabel.setWordWrap(false);
				numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
				policyLabel = getFormatedLabel();
				this.policyLabel.getElement().getStyle().setFontSize(10, Unit.PX);
				this.policyLabel.setHeight("1.2em");
				this.policyLabel.setWordWrap(true);

				HorizontalPanel leftContainer = new HorizontalPanel();
				leftContainer.setSize("100%", "100%");

				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");
				container.add(numberLabel);
				container.setCellHeight(numberLabel, "100%");
				container.setCellVerticalAlignment(numberLabel, HasVerticalAlignment.ALIGN_TOP);
				container.add(policyLabel);
				setWidget(container);

				HorizontalPanel leftWidgetContainer = new HorizontalPanel();
				leftWidgetContainer.setSize("100%", "100%");

				VerticalPanel rightWrapper = new VerticalPanel();
				leftWidgetContainer.add(rightWrapper);
				rightWrapper.setSize("100%", "100%");
				rightWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

				openImage = new Image();
				leftWidgetContainer.add(openImage);
				openImage.getElement().getStyle().setMarginLeft(10, Unit.PX);
				leftWidgetContainer.setCellVerticalAlignment(openImage, HasVerticalAlignment.ALIGN_MIDDLE);

				((UIObject) rightWidgetContainer).setSize("100%", "100%");
				setLeftWidget(leftWidgetContainer);

				valueLabel = new Label();
				valueLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

				VerticalPanel rightContainer = new VerticalPanel();
				rightContainer.add(valueLabel);
				rightContainer.setSize("100%", "100%");
				rightContainer.setCellVerticalAlignment(valueLabel, HasVerticalAlignment.ALIGN_BOTTOM);
				setRightWidget(rightContainer);
				container.setCellHorizontalAlignment(rightContainer, HasHorizontalAlignment.ALIGN_RIGHT);
				container.setCellVerticalAlignment(rightContainer, HasVerticalAlignment.ALIGN_BOTTOM);
			}

			SubCasualtyStub c = (SubCasualtyStub) info;

			this.numberLabel.setText("#" + (c.number == null ? "" : c.number));
			this.numberLabel.setTitle("Número e tipo de Recibo");

			String referenceType = c.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? "Apólice #" : "Apólice Adesão #";

			this.policyLabel.setText(referenceType + c.referenceNumber + " " + c.categoryName + "/" + c.lineName + "/" + c.subLineName);

			Resources r = GWT.create(Resources.class);
			openImage.setResource(c.isOpen ? r.activeCasualtyIcon() : r.inactiveCasualtyIcon());

			valueLabel.setText(c.totalDamages != null ?  c.totalDamages + "€" : "-");

			initialized = true;
			setSelected(this.isSelected(), false);
		};

		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(!initialized) {
				return;
			}
			if(selected){
				this.policyLabel.getElement().getStyle().setColor("white");
				this.valueLabel.getElement().getStyle().setColor("white");
			}else{
				this.policyLabel.getElement().getStyle().setColor("gray");
				this.valueLabel.getElement().getStyle().setColor("black");
			}
		}
	}

	private HashMap<String, SubCasualtyStub> subCasualtiesToUpdate;
	private HashMap<String, Void> subCasualtiesToRemove;
	private int subCasualtyDataVersion;

	public SubCasualtySearchPanel() {
		super(((SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY)).getSearchBroker());
		subCasualtiesToUpdate = new HashMap<String, SubCasualtyStub>();
		subCasualtiesToRemove = new HashMap<String, Void>();

		showFilterField(false);

		SubCasualtyDataBroker broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		broker.registerClient(this);
	}

	public SubCasualtySearchPanel(SearchDataBroker<SubCasualtyStub> broker) {
		super(broker);
	}

	@Override
	public void doSearch(boolean keepState) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		subCasualtiesToRemove.clear();
		subCasualtiesToUpdate.clear();

		SubPolicySearchParameter parameter = new SubPolicySearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		doSearch(parameters, null, keepState);		
	}

	@Override
	public void onResults(Collection<SubCasualtyStub> results) {
		for(SubCasualtyStub s : results){
			if(!subCasualtiesToRemove.containsKey(s.id)){
				if(subCasualtiesToUpdate.containsKey(s.id)){
					s = subCasualtiesToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}		
	}

	protected Entry addSearchResult(SearchResult r){
		Entry entry = null;
		if(r instanceof SubCasualtyStub){
			entry = new Entry((SubCasualtyStub)r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			this.subCasualtyDataVersion = number;
		}		
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			return subCasualtyDataVersion;
		}
		return -1;	
	}

	@Override
	public void addSubCasualty(SubCasualty subCasualty) {
		this.add(0, new Entry(subCasualty));
		
	}

	@Override
	public void updateSubCasualty(SubCasualty subCasualty) {
		for(ValueSelectable<SubCasualtyStub> s : this){
			SubCasualtyStub stub = s.getValue();
			if(subCasualty.id.equalsIgnoreCase(stub.id)){
				s.setValue(subCasualty);
				return;
			}
		}
		this.subCasualtiesToUpdate.put(subCasualty.id, subCasualty);		
	}

	@Override
	public void removeSubCasualty(String id) {
		for(ValueSelectable<SubCasualtyStub> s : this){
			SubCasualtyStub stub = s.getValue();
			if(id.equalsIgnoreCase(stub.id)){
				remove(s);
				return;
			}
		}
		this.subCasualtiesToRemove.put(id, null);		
	}

	public void setOwner(String ownerId) {
		SubCasualtySearchParameter parameter = new SubCasualtySearchParameter();
		parameter.ownerId = ownerId;
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		
		doSearch(parameters, null, false);
	}



}
