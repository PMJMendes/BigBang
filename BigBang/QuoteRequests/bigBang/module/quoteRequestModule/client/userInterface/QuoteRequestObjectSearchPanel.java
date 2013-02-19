package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBrokerClient;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.quoteRequestModule.shared.QuoteRequestObjectSearchParameter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class QuoteRequestObjectSearchPanel extends SearchPanel<QuoteRequestObjectStub> implements QuoteRequestObjectDataBrokerClient{

	protected ToolButton createNew;

	public class Entry extends ListEntry<QuoteRequestObjectStub>{

		protected Label name;
		protected Image invalidIcon;
		
		public Entry(QuoteRequestObjectStub value) {
			super(value);
			setHeight("25px");		
		}

		public <I extends Object> void setInfo(I info){
			QuoteRequestObjectStub value = (QuoteRequestObjectStub) info;

			if(invalidIcon == null){
				Resources resources = GWT.create(Resources.class);
				invalidIcon = new Image(resources.invalidEntry());
				setLeftWidget(invalidIcon);
				setInvalid(false);
			}

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
						getElement().getStyle().setBackgroundColor("#ff7e71");
						break;
					case MODIFIED:
						getElement().getStyle().setBackgroundColor("#a9ff63");
						break;
					}
				}
			}

		}

		private void setInvalid(boolean b) {
			invalidIcon.setVisible(b);
		}

	}

	protected int compositeObjectDataVersion = 0;

	private int quoteRequestDataVersion = 0;
	private Map<String, QuoteRequestObjectStub> localObjects;


	public QuoteRequestObjectSearchPanel(){
		super(((QuoteRequestObjectDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT)).getSearchBroker());

		Resources r = GWT.create(Resources.class);
		createNew = new ToolButton(ButtonHelper.createButtonLabel(
				AbstractImagePrototype.create(r.listNewIcon()), "Nova Unidade de Risco",
				ButtonLabelType.TEXT_ON_LEFT));

		getHeaderWidget().insert(createNew, 0);
		createNew.getElement().getStyle().setMarginRight(5, Unit.PX);
		createNew.getElement().getStyle().setMarginTop(5, Unit.PX);
		getHeaderWidget().setCellHorizontalAlignment(createNew, HasHorizontalAlignment.ALIGN_RIGHT);

		localObjects = new HashMap<String, QuoteRequestObjectStub>();

		QuoteRequestObjectDataBroker broker = (QuoteRequestObjectDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT);
		broker.registerClient(this);
		lockSearchButton(true);
	}

	@Override
	public void doSearch(boolean keepSelected) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		QuoteRequestObjectSearchParameter parameter = new QuoteRequestObjectSearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		doSearch(parameters, null, keepSelected);
	}

	@Override
	public void onResults(Collection<QuoteRequestObjectStub> results) {
		for(QuoteRequestObjectStub s : results){

			if(!localObjects.containsKey(s.id)){

				addSearchResult(s);

			}
		}

	}

	public HasClickHandlers getNewObjectButton(){
		return createNew;
	}
	
	public void allowCreateNew(boolean allow){
		createNew.setEnabled(allow);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT)) {
			this.quoteRequestDataVersion  = number;
		}

	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT)){
			return this.quoteRequestDataVersion;
		}
		return -1;	
	}

	@Override
	public void addQuoteRequestObject(QuoteRequestObject object) {
		this.add(0, new Entry(object));
	}

	@Override
	public void updateQuoteRequestObject(QuoteRequestObject object) {
		for(ValueSelectable<QuoteRequestObjectStub> s : this){
			QuoteRequestObjectStub objectStub = s.getValue();
			if(object.id.equalsIgnoreCase(objectStub.id)){
				s.setValue(object);
				return;
			}
		}

	}

	@Override
	public void removeQuoteRequestObject(String id) {
		for(ValueSelectable<QuoteRequestObjectStub> s : this){
			QuoteRequestObjectStub objectStub = s.getValue();
			if(id.equalsIgnoreCase(objectStub.id)){
				addSearchResult(objectStub);
				return;
			}
		}

	}

	protected Entry addSearchResult(SearchResult r) {
		Entry entry = null;
		if(r instanceof QuoteRequestObjectStub){
			entry = new Entry((QuoteRequestObjectStub)r);
			add(entry);
		}
		return entry;		
	}

	@Override
	public void remapItemId(String newId, String oldId) {
		return;		
	}

	public void dealWithObject(QuoteRequestObjectStub info) {
		localObjects.put(info.id, info);

		for(ListEntry<QuoteRequestObjectStub> s : this){
			if(s.getValue().id.equalsIgnoreCase(info.id)){
				s.setValue(info);
				return;
			}
		}
		clearSelection();
		Entry entry = new Entry(info);
		add(0, entry);
		entry.setSelected(true, false);
	}

	public void setSelected(String id){
		for(ListEntry<QuoteRequestObjectStub> s : this){
			s.setSelected(s.getValue().id.equalsIgnoreCase(id), false);
		}
	}
}
