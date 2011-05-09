package bigBang.module.generalSystemModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.view.SubLineForm;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class SubLineList extends FilterableList<SubLine> {

	public static class Entry extends NavigationListEntry<SubLine> {
		protected Image editImage;
		
		public Entry(SubLine subLine){
			super(subLine);
			setLeftWidget(editImage);
		}

		public <I extends Object> void setInfo(I info) {
			SubLine subLine = (SubLine) info;
			setTitle(subLine.name);
			setNavigatable(subLine.coverages != null && subLine.coverages.length > 0);
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
	private SubLineForm form;
	private PopupPanel popup;
	private boolean readonly;
	private ClickHandler editHandler;
	private DoubleClickHandler doubleClickHandler; 
	
	private String parentId;
	
	private LineList parentList;

	public SubLineList(LineList parentList){
		this.parentList = parentList;
		ListHeader header = new ListHeader();
		header.setText("Modalidades");
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
		
		this.form = new SubLineForm();
		form.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SubLine subLine = form.getValue();
				if(subLine.id == null){
					createSubLine(subLine);
				}else{
					saveSubLine(subLine);
				}
			}
		});
		this.popup = new PopupPanel("Modalidade");
		Widget formContent = form.getNonScrollableContent();
		formContent.setHeight("80px");
		formContent.setWidth("650px");
		popup.add(formContent);
		popup.setWidth("650px");

		setHeaderWidget(header);

		editHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<SubLine> e : entries) {
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
				for(ListEntry<SubLine> e : entries) {
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
	
	public void setParentId(String id) {
		this.parentId = id;
		if(parentId == null){
			clear();
		}
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

	private void createSubLine(SubLine subLine) {
		subLine.lineId = this.parentId;
		CoveragesService.Util.getInstance().createSubLine(subLine, new BigBangAsyncCallback<SubLine>() {

			@Override
			public void onSuccess(SubLine result) {
				Entry entry = new Entry(result);
				add(entry);
				entry.setSelected(true);
				
				
				for(Selectable s : parentList.getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<Line> vs = (ValueSelectable<Line>) s;
					
					SubLine[] newArray = new SubLine[size()];
					for(int i = 0; i < newArray.length; i++) {
						newArray[i] = get(i).getValue();
					}
					vs.getValue().subLines = newArray;
 					break;
				}
				
				
				showForm(false);
			}
		});
	}

	private void saveSubLine(SubLine subLine){
		subLine.lineId = this.parentId;
		CoveragesService.Util.getInstance().saveSubLine(subLine, new BigBangAsyncCallback<SubLine>() {

			@Override
			public void onSuccess(SubLine result) {
				updateEntry(result);
				showForm(false);
			}
		});
	}

	private void updateEntry(SubLine subLine) {
		for(ListEntry<SubLine> e : entries){
			if(e.getValue().id.equals(subLine.id))
				e.setValue(subLine);
		}
	}

	public void setReadOnly(boolean readonly) {
		//TODO
		this.readonly = readonly = false;
		this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<SubLine> e : entries){
			((Entry) e).setEditable(!readonly);
		}
	}
}
