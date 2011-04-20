package bigBang.module.generalSystemModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.view.LineForm;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.shared.Line;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class LineList extends FilterableList<Line> {

	public static class Entry extends ListEntry<Line> {
		private Label nChildrenLabel;
		protected Image editImage;

		public Entry(Line line){
			super(line);
			setRightWidget(nChildrenLabel);
			setLeftWidget(editImage);
		}

		public <I extends Object> void setInfo(I info) {
			if(nChildrenLabel == null)
				nChildrenLabel = new Label();
			Line line = (Line) info;
			setTitle(line.name);
			nChildrenLabel.setText((line.subLines == null || line.subLines.length == 0) ? "-" : line.subLines.length + "");
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

	public LineList(){
		ListHeader header = new ListHeader();
		header.setText("Ramos");
		header.showNewButton("Novo");
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
		popup.add(form.getNonScrollableContent());
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
		
		setReadOnly(true);
	}
	
	public boolean add(Entry e) {
		e.setEditable(!readonly);
		e.editImage.addClickHandler(this.editHandler);
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
		CoveragesService.Util.getInstance().createLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onSuccess(Line result) {
				add(new Entry(result));
				showForm(false);
			}
		});
	}

	private void saveLine(Line line){
		CoveragesService.Util.getInstance().saveLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onSuccess(Line result) {
				updateEntry(result);
				showForm(false);
			}
		});
	}

	public void refresh() {
		clear();
		CoveragesService.Util.getInstance().getLines(new BigBangAsyncCallback<Line[]>() {

			@Override
			public void onSuccess(Line[] result) {
				clear();
				for(int i = 0; i < result.length; i++) {
					add(new Entry(result[i]));
				}
			}
		});
	}

	private void updateEntry(Line line) {
		for(ListEntry<Line> e : entries){
			if(e.getValue().id.equals(line.id))
				e.setValue(line);
		}
	}

	public void setReadOnly(boolean readonly) {
		//TODO
		this.readonly = readonly = false;
		this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<Line> e : entries){
			((Entry) e).setEditable(!readonly);
		}
	}
}
