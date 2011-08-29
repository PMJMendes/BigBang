package bigBang.library.client.userInterface;
import bigBang.library.client.Checkable;
import bigBang.library.client.HasMetaData;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.CheckedStateChangedEvent;
import bigBang.library.client.event.CheckedStateChangedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListEntry<T> extends View implements ValueSelectable<T>, HasMetaData<String>, Checkable {


	protected T value;
	//protected boolean even = false;
	protected boolean isSelected = false;
	protected String[] metaData;
	
	//protected final String defaultBackgroundImageUrl = ""; //images/listBackground1Even.png";
	protected final String selectedBackgroundImageUrl = "images/listBackground1Selected.png";
	//protected final String hoverBackgroundImageUrl = "images/listBackground1Hover.png";
	
	protected Image backgroundImage;
	
	
	protected Label titleLabel;
	protected Label textLabel;
	protected HasWidgets leftWidgetContainer;
	protected HasWidgets widgetContainer;
	protected HasWidgets rightWidgetContainer;
	protected CheckBox checkBox;
	protected Widget dragHandle;
	protected boolean valueChangeHandlerInitialized;
	protected boolean selectionStateChangedHandlerInitialized;
	protected boolean selectable = true;
	protected boolean toggleable = false;
	protected boolean checkable;
	
	
	public ListEntry(T value) {
		AbsolutePanel panel = new AbsolutePanel();
		this.widgetContainer = new SimplePanel();
		((Widget)this.widgetContainer).setSize("100%", "100%");
		panel.setSize("100%", "30px");
		panel.setStylePrimaryName("listItem");
		
		backgroundImage = new Image();
		backgroundImage.setSize("100%", "100%");
		
		panel.add(backgroundImage, 0, 0);
		
		HorizontalPanel contentWrapper = new HorizontalPanel();
		contentWrapper.setSize("100%", "100%");
		contentWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		contentWrapper.setSpacing(5);
		
		this.leftWidgetContainer = new SimplePanel();
		contentWrapper.add((Widget) this.leftWidgetContainer);
		
		VerticalPanel textWrapper = new VerticalPanel();
		textWrapper.setSize("100%", "100%");
		textWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		this.titleLabel = new Label();
		this.titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.titleLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		textWrapper.add(this.titleLabel);
		textWrapper.setCellHeight(this.titleLabel, "100%");
		this.titleLabel.getElement().getStyle().setProperty("textOverflow", "ellipsis");
		this.titleLabel.getElement().getStyle().setProperty("whiteSpace", "normal");
		this.titleLabel.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		this.titleLabel.setHeight("1em");
		this.titleLabel.setWordWrap(false);
	
		this.textLabel = new Label();
		this.textLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.textLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		this.textLabel.getElement().getStyle().setProperty("textOverflow", "ellipsis");
		this.textLabel.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		this.textLabel.setWordWrap(false);
		
		textWrapper.add(this.textLabel);		
		
		widgetContainer.add(textWrapper);
		
		contentWrapper.add((Widget)widgetContainer);
		contentWrapper.setCellWidth((Widget)widgetContainer, "100%");
		
		this.rightWidgetContainer = new SimplePanel();
		contentWrapper.add((Widget) this.rightWidgetContainer);
		
		panel.add(contentWrapper, 0 , 0);
		
		initWidget(panel);
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		this.setSelected(false);
		
		this.checkBox = new CheckBox();
		this.checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setCheckable(event.getValue());
			}
		});
		
		panel.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setSelected(toggleable ? !isSelected() : true);
			}
		}, ClickEvent.getType());
		
		disableTextSelection(true);
		setDragHandle(this);
		setValue(value);
	}
	
	public <I extends Object> void setInfo(I info){}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		setValue(value, true);
	}

	public void setValue(T value, boolean fireEvents) {
		if(this.value == value){
			setInfo(value);
			return;
		}
		this.value = value;
		if(fireEvents)
			ValueChangeEvent.fire(this, this.value);
		setInfo(value);
	}
	
	public void setSelected(boolean selected) {
		setSelected(selected, true);
	}
	
	public void setSelected(boolean selected, boolean fireEvents) {
		if(selected && !this.selectable)
			return;
		
		boolean initSelected = this.isSelected;
		if(selected){
			backgroundImage.setUrl(selectedBackgroundImageUrl);
			backgroundImage.setVisible(true);
			this.addStyleName("listItem-selected");
			this.removeStyleName("listItemEven");
			this.isSelected = true;
		}else{
			backgroundImage.setVisible(false); //setUrl(defaultBackgroundImageUrl);
			this.removeStyleName("listItem-selected");
			this.addStyleName("listItemEven");
			this.isSelected = false;
		}
		if(initSelected != selected && fireEvents)
			fireEvent(new SelectedStateChangedEvent(this.isSelected));
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	public void setSelectable(boolean selectable) {
		this.setSelected(false, false);
		this.selectable = selectable;
	}
	
	public void setTitle(String title){
		this.titleLabel.getElement().getStyle().setProperty("whiteSpace", "normal");
		if(title == null){
			this.titleLabel.setText("");
			this.titleLabel.setTitle("");
			this.titleLabel.setVisible(false);
			this.titleLabel.setHeight("0px");
			return;
		}
		this.titleLabel.setHeight("1.2em");
		this.titleLabel.setText(title);
		this.titleLabel.setTitle(title);
		this.titleLabel.setVisible(true);
	}
	
	public String getTitle(){
		return this.titleLabel.isVisible() ? this.titleLabel.getText() : null;
	}
	
	public void setText(String text) {
		this.textLabel.getElement().getStyle().setProperty("whiteSpace", "normal");
		if(text == null){
			this.textLabel.setText("");
			this.textLabel.setTitle("");
			this.textLabel.setVisible(false);
			this.textLabel.setHeight("0px");
			return;
		}
		this.textLabel.setHeight("1.2em");
		this.textLabel.setText(text);
		this.textLabel.setTitle(text);
		this.textLabel.setVisible(true);
	}
	
	public String getText() {
		return this.textLabel.isVisible() ? this.textLabel.getText() : "";
	}
	
	public void setWidget(Widget w){
		widgetContainer.clear();
		widgetContainer.add(w);
	}
	
	public Widget getWidget() {
		return null;
	}
	
	public void setLeftWidget(Widget w){
		if(w == null)
			return;
		this.leftWidgetContainer.clear();
		this.leftWidgetContainer.add(w);
	}
	
	public Widget getLeftWidget(){
		return ((Widget)this.leftWidgetContainer);
	}
	
	public void setRightWidget(Widget w){
		if(w == null)
			return;
		this.rightWidgetContainer.clear();
		this.rightWidgetContainer.add(w);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		if (!valueChangeHandlerInitialized)
			valueChangeHandlerInitialized = true;

		return addHandler(handler, ValueChangeEvent.getType());
	}
	
	@Override
	public HandlerRegistration addCheckedStateChangedEventHandler(
			CheckedStateChangedEventHandler handler) {
		return addHandler(handler, CheckedStateChangedEvent.TYPE);
	}

	public void setCheckable(boolean checkable) {
		setDoubleClickable(checkable);
		if(checkable) {
			this.setLeftWidget(this.checkBox);
			this.checkBox.setEnabled(true);
		}else{
			if(this.isCheckable()){
				this.leftWidgetContainer.clear();
				this.checkBox.setEnabled(false);
				this.checkBox.setValue(false);
			}
		}
		this.checkable = checkable;
	}
	
	public void setChecked(boolean checked) {
		setChecked(checked, true);
	}
	
	public void setChecked(boolean checked, boolean fireEvents) {
		if(checked != this.checkBox.getValue()){
			this.checkBox.setValue(checked);
			if(fireEvents)
				fireEvent(new CheckedStateChangedEvent(checked));
		}
	}
	
	public boolean isCheckable(){
		return checkable;
	}

	public boolean isChecked() {
		return this.isCheckable() && this.checkBox.getValue();
	}
	
	@Override
	public void onDoubleClick(Event event){
		super.onDoubleClick(event);
		if(this.isCheckable()){
			this.setChecked(!this.isChecked());
		}
	}

	@Override
	public void setMetaData(String[] data) {
		this.metaData = data;
	}

	@Override
	public String[] getMetaData() {
		return metaData;
	}

	@Override
	public HandlerRegistration addSelectedStateChangedEventHandler(
			SelectedStateChangedEventHandler handler) {
		if (!selectionStateChangedHandlerInitialized)
			selectionStateChangedHandlerInitialized = true;

		return addHandler(handler, SelectedStateChangedEvent.TYPE);
	}
	
	public void setToggleable(boolean t) {
		this.toggleable = t;
	}
	
	public boolean isToggleable(){
		return this.toggleable;
	}
	
	public Widget getDragHandler(){
		return dragHandle;
	}

	protected void setDragHandle(Widget w){
		this.dragHandle = w;
	}

}
