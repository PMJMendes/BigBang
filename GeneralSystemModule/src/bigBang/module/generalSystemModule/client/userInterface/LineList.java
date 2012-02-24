package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.dataAccess.CoverageDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.SubLineList.Entry;
import bigBang.module.generalSystemModule.client.userInterface.view.LineForm;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class LineList extends FilterableList<Line> implements CoverageDataBrokerClient{

	public static class Entry extends NavigationListEntry<Line> {
		protected Image editImage;

		public Entry(Line line){
			super(line);
			this.textLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			this.textLabel.getElement().getStyle().setFontSize(14, Unit.PX);

			this.titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			setLeftWidget(editImage);
			setHeight("40px");
		}

		public <I extends Object> void setInfo(I info) {
			Line line = (Line) info;
			setTitle(line.categoryName);
			setText(line.name);
			setNavigatable(line.subLines != null && line.subLines.length > 0);
		};

		public void setEditable(boolean editable) {
			editImage.setVisible(editable);
		}

		@Override
		public void setSelected(boolean selected, boolean fireEvents) {
			if(editImage == null)
				editImage = new Image();
			Resources r = GWT.create(Resources.class);
			super.setSelected(selected, fireEvents);
			editImage.setResource(selected ? r.listEditIconSmallWhite() : r.listEditIconSmallBlack());
		}
	}

	private ToolButton newButton;
	private LineForm form;
	private PopupPanel popup;
	private boolean readonly;
	private ClickHandler editHandler;
	private DoubleClickHandler doubleClickHandler;
	private CoverageBroker broker;
	private String lineId;

	public LineList(){
		broker = (CoverageBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		broker.registerClient(this);
		ListHeader header = new ListHeader();
		header.setText("Ramos");
		header.showNewButton("Novo");
		this.showFilterField(false);
		this.newButton = header.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.clearInfo();
				form.setReadOnly(false);
				showForm(true);
			}
		});

		this.form = new LineForm();
		form.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Line line = form.getValue();
				if(line.id == null){
					createLine(line);
				}else{
					saveLine(line);
				}
			}
		});
		this.popup = new PopupPanel("Ramo");
		Widget formContent = form.getNonScrollableContent();
		formContent.setHeight("120px");
		formContent.setWidth("650px");
		popup.add(formContent);
		popup.setWidth("650px");

		header.showRefreshButton();
		header.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		});

		setHeaderWidget(header);

		editHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<Line> e : entries) {
					if(event.getSource() == ((Entry)e).editImage){
						form.setValue(((Entry)e).getValue());
						showForm(true);
					}
				}
			}
		};
		doubleClickHandler = new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				for(ListEntry<Line> e : entries) {
					if(event.getSource() == e){
						form.setValue(((Entry)e).getValue());
						showForm(true);
					}
				}
			}
		};

		setDoubleClickable(true);
		setReadOnly(true);
	}

	public boolean add(Entry e) {
		e.setEditable(!readonly);
		e.editImage.addClickHandler(this.editHandler);
		e.setDoubleClickable(true);
		e.addHandler(doubleClickHandler, DoubleClickEvent.getType());
		return super.add(e);
	}

	public HasClickHandlers getNewButton(){
		return this.newButton;
	}

	private void showForm(boolean show) {
		if(!show){
			popup.hidePopup();
			return;
		}
		popup.center();
	}

	private void createLine(Line line) {
		//TODO
		CoveragesService.Util.getInstance().createLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onResponseSuccess(Line result) {
				Entry entry = new Entry(result);
				add(entry);
				entry.setSelected(true);
				showForm(false);
			}
		});
	}

	private void saveLine(Line line){
		//TODO
		CoveragesService.Util.getInstance().saveLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onResponseSuccess(Line result) {
				updateEntry(result);
				showForm(false);
			}
		});
	}

	public void refresh() {
		broker.getLines(new ResponseHandler<Line[]>() {

			@Override
			public void onResponse(Line[] response) {
				setLines(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de ramos."), TYPE.ALERT_NOTIFICATION));

			}
		});
	}

	private void updateEntry(Line line) {
		//TODO
		for(ListEntry<Line> e : entries){
			if(e.getValue().id.equals(line.id))
				e.setValue(line);
		}
	}

	public void setReadOnly(boolean readonly) {

		this.readonly = readonly;
		this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<Line> e : entries){
			((Entry) e).setEditable(!readonly);
		}
	}


	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLines(Line[] lines) {
		clear();
		if(this.lineId != null){
			for(int i = 0; i<lines.length; i++){
				add(new Entry(lines[i]));
				if(this.lineId.equalsIgnoreCase(lines[i].id)){
					get(i).setSelected(true, false);
				}
			}
		}
		else{
			for(int i = 0; i<lines.length; i++){
				add(new Entry(lines[i]));
			}
		}

	}

	@Override
	public void addLine(Line line) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLine(Line line) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLine(String lineId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSubLines(SubLine[] subLines) {
		return;

	}

	@Override
	public void addSubLine(String parentLineId, SubLine subLine) {
		return;

	}

	@Override
	public void updateSubLine(String parentLineId, SubLine subLine) {
		return;

	}

	@Override
	public void removeSubLine(String parentLineId, String subLineId) {
		return;

	}

	@Override
	public void setCoverages(Coverage[] coverages) {
		return;

	}

	@Override
	public void addCoverage(String parentSubLineId, Coverage coverage) {
		return;

	}

	@Override
	public void updateCoverage(String parentSubLineId, Coverage coverage) {
		return;
	}

	@Override
	public void removeCoverage(String parentSubLineId, String coverageId) {
		return;

	}

	@Override
	public void setTaxes( Tax[] taxes) {
		return;

	}

	@Override
	public void addTax(String parentCoverageId, Tax tax) {
		return;

	}

	@Override
	public void updateTax(String parentCoverageId, Tax tax) {
		return;

	}

	@Override
	public void removeTax(String parentCoverageId, String taxId) {

		return;

	}

	public void setId(String lineId) {
		this.lineId = lineId;

	}

	public void setLines(final String lineId) {
		
		broker.getLines(new ResponseHandler<Line[]>() {
			@Override
			public void onResponse(Line[] response) {
				if(lineId != null){
					for(int i = 0; i<response.length; i++){
						add(new Entry(response[i]));
						if(response[i].id.equalsIgnoreCase(lineId)){
							get(i).setSelected(true, false);
						}
					}
				}
				else{
					for(int i = 0; i<response.length; i++){
						add(new Entry(response[i]));
					}

				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de modalidades."), TYPE.ALERT_NOTIFICATION));

			}


		});
	}
}
