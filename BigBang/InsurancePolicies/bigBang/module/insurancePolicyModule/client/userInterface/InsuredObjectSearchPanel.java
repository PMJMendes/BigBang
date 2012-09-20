package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.InsuredObjectStub.Change;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;

public class InsuredObjectSearchPanel extends SearchPanel<InsuredObjectStub> implements InsuredObjectDataBrokerClient{

	protected ToolButton createNew;

	public static class Entry extends ListEntry<InsuredObjectStub>{

		protected Label name;

		public Entry(InsuredObjectStub object){
			super(object);
			setHeight("30px");
		}

		public <I extends Object> void setInfo(I info){
			InsuredObjectStub value = (InsuredObjectStub) info;

			if(value.id != null){
				if(name == null){
					name = getFormatedLabel();
					setWidget(name);
				}
				name.setText(value.unitIdentification);

				if(value != null && value.change != null){
					switch(value.change){
					case CREATED:
						getElement().getStyle().setBackgroundColor("yellow");
						break;
					case DELETED:
						getElement().getStyle().setBackgroundColor("red");
						break;
					case MODIFIED:
						getElement().getStyle().setBackgroundColor("green");
						break;
					}
				}
			}
		}
	}

	protected int insuredObjectDataVersion = 0;
	//protected Map<String, InsuredObjectStub> objectsToUpdate;
	//protected Map<String, Void> objectsToRemove;

	private String ownerId;
	private int insurancePolicyDataVersion = 0;
	private Map<String, InsuredObjectStub> localObjects;

	public InsuredObjectSearchPanel() { 
		super(((InsuredObjectDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT)).getSearchBroker());

		Resources r = GWT.create(Resources.class);
		createNew = new ToolButton(ButtonHelper.createButtonLabel(
				AbstractImagePrototype.create(r.listNewIcon()), "Novo",
				ButtonLabelType.TEXT_ON_LEFT));

		getHeaderWidget().insert(createNew, 0);
		createNew.getElement().getStyle().setMarginRight(5, Unit.PX);
		createNew.getElement().getStyle().setMarginTop(5, Unit.PX);
		getHeaderWidget().setCellHorizontalAlignment(createNew, HasHorizontalAlignment.ALIGN_RIGHT);

		//objectsToRemove = new HashMap<String, Void>();
		//objectsToUpdate = new HashMap<String, InsuredObjectStub>();

		localObjects = new HashMap<String,InsuredObjectStub>();

		InsuredObjectDataBroker broker = (InsuredObjectDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT);
		broker.registerClient(this);
		lockSearchButton(true);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)) {
			this.insurancePolicyDataVersion  = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT)){
			return this.insurancePolicyDataVersion;
		}
		return -1;	
	}

	@Override
	public void removeInsuredObject(String id) {
		for(ValueSelectable<InsuredObjectStub> s : this){
			InsuredObjectStub objectStub = s.getValue();
			if(id.equalsIgnoreCase(objectStub.id)){
				objectStub.change = Change.DELETED;
				addSearchResult(objectStub);
				return;
			}
		}
	}

	@Override
	public void remapItemId(String newId, String Id) {
		return;
	}

	@Override
	public void doSearch() {

		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}
		//this.objectsToRemove.clear();
		//this.objectsToUpdate.clear();

		InsuredObjectSearchParameter parameter = new InsuredObjectSearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();
		parameter.policyId = ownerId;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		doSearch(parameters, null);

	}

	@Override
	public void onResults(Collection<InsuredObjectStub> results) {
		for(InsuredObjectStub s : results){

			if(!localObjects.containsKey(s.id)){
				//	if(!objectsToRemove.containsKey(s.id)){
				//	if(objectsToUpdate.containsKey(s.id)){
				//	s = objectsToUpdate.get(s.id);
				//}
				addSearchResult(s);
				//}
			}
		}

	}

	protected Entry addSearchResult(SearchResult r) {
		Entry entry = null;
		if(r instanceof InsuredObjectStub){
			entry = new Entry((InsuredObjectStub)r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void addInsuredObject(InsuredObject object) {
		this.add(0, new Entry(object));
	}

	@Override
	public void updateInsuredObject(InsuredObject object) {
		for(ValueSelectable<InsuredObjectStub> s : this){
			InsuredObjectStub objectStub = s.getValue();
			if(object.id.equalsIgnoreCase(objectStub.id)){
				s.setValue(object);
				return;
			}
		}
	}

	public void allowCreateInsuredObject(boolean allow) {
		createNew.setEnabled(allow);
	}

	public void setReadOnly(boolean b) {
		createNew.setEnabled(!b);
	}

	public void setOwner(String ownerId) {

		localObjects.clear();

		if(ownerId != null){
			this.ownerId = ownerId;
		}
		else{
			lockSearchButton(true);
		}

		InsuredObjectSearchParameter parameter = new InsuredObjectSearchParameter();
		parameter.policyId = ownerId;

		SearchParameter[] parameters = new SearchParameter[]{parameter};

		doSearch(parameters, null);
		lockSearchButton(false);

	}

	public HasClickHandlers getNewObjectButton(){
		return createNew;
	}

	public void dealWithObject(InsuredObjectStub object) {
		localObjects.put(object.id, object);

		for(ListEntry<InsuredObjectStub> s : this){
			if(s.getValue().id.equalsIgnoreCase(object.id)){
				s.setValue(object);
				return;
			}
		}
		clearSelection();
		addSearchResult(object).setSelected(true, false);
	}

	@Override
	public void clear() {
		super.clear();
		if(localObjects != null){
			for(InsuredObjectStub s : localObjects.values()){
				addSearchResult(s);
			}
		}
	}

	public void setSelected(String id) {
		for(ListEntry<InsuredObjectStub> s: this){
			s.setSelected(s.getValue().id.equalsIgnoreCase(id), false);
		}
	}

}
