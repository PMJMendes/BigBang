package bigBang.module.generalSystemModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.LineList.Entry;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageForm;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.shared.Coverage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;

public class CoverageList extends FilterableList<Coverage> {

	public static class Entry extends ListEntry<Coverage> {
		protected Image editImage;

		public Entry(Coverage coverage){
			super(coverage);
			setLeftWidget(editImage);
		}

		public <I extends Object> void setInfo(I info) {
			Coverage coverage = (Coverage) info;
			setTitle(coverage.name);
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
	private CoverageForm form;
	private PopupPanel popup;
	private boolean readonly;
	private ClickHandler editHandler;
	
	private String parentId;

	public CoverageList(){
		ListHeader header = new ListHeader();
		header.setText("Coberturas");
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
		
		this.form = new CoverageForm();
		form.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Coverage coverage = form.getValue();
				if(coverage.id == null){
					createCoverage(coverage);
				}else{
					saveCoverage(coverage);
				}
			}
		});
		this.popup = new PopupPanel("Cobertura");
		popup.add(form.getNonScrollableContent());
		popup.setWidth("650px");

		setHeaderWidget(header);

		editHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<Coverage> e : entries) {
					if(event.getSource() == ((Entry)e).editImage){
						form.setValue(((Entry)e).getValue());
						showForm(true);
					}
				}
			}
		};
		
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

	private void createCoverage(Coverage coverage) {
		coverage.subLineId = this.parentId;
		CoveragesService.Util.getInstance().createCoverage(coverage, new BigBangAsyncCallback<Coverage>() {

			@Override
			public void onSuccess(Coverage result) {
				Entry entry = new Entry(result);
				add(entry);
				entry.setSelected(true);
				showForm(false);
			}
		});
	}

	private void saveCoverage(Coverage coverage){
		coverage.subLineId = this.parentId;
		CoveragesService.Util.getInstance().saveCoverage(coverage, new BigBangAsyncCallback<Coverage>() {

			@Override
			public void onSuccess(Coverage result) {
				updateEntry(result);
				showForm(false);
			}
		});
	}

	private void updateEntry(Coverage coverage) {
		for(ListEntry<Coverage> e : entries){
			if(e.getValue().id.equals(coverage.id))
				e.setValue(coverage);
		}
	}

	public void setReadOnly(boolean readonly) {
		//TODO
		this.readonly = readonly = false;
		this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<Coverage> e : entries){
			((Entry) e).setEditable(!readonly);
		}
	}
}
