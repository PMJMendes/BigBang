package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedTextBroker;
import bigBang.library.client.dataAccess.TypifiedTextClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ExpandableListBoxFormField.ManagementPanelType;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TypifiedTextSelector extends FormField<TypifiedText> implements TypifiedTextClient{

	private TypifiedTextBroker broker;
	private ExpandableListBoxFormField labels;
	public TextBoxFormField subject = new TextBoxFormField("Assunto");
	private RichTextAreaFormField textBody = new RichTextAreaFormField();
	private VerticalPanel wrapper = new VerticalPanel();
	private HorizontalPanel labelPanel = new HorizontalPanel();
	private String tag;

	public TypifiedTextSelector(){

		labels  = new ExpandableListBoxFormField(null,"Modelo", ManagementPanelType.TYPIFIED_TEXT);
		broker = (TypifiedTextBroker)DataBrokerManager.staticGetBroker(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT);
		labels.addValueChangeHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {

				String id = event.getValue();
				if(id == null || id.length() == 0){
					clear();
					return;
				}

				broker.getText(tag, id, new ResponseHandler<TypifiedText>() {

					@Override
					public void onResponse(TypifiedText response) {
						subject.setValue(response.subject);
						buildText(response.text);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o texto pedido."), TYPE.ALERT_NOTIFICATION));
					}
				});



			}
		});

		initWidget(wrapper);

		labelPanel.add(labels);
		labels.setWidth("100%");

		wrapper.add(labelPanel);
		subject.setWidth("100%");
		subject.setFieldWidth("100%");
		wrapper.add(subject);

		textBody.setLabelWidth("0px");
		textBody.setFieldWidth("100%");
		wrapper.add(textBody);
		setReadOnlyInternal(false);
	}

	public void setTypifiedTexts(String tag){
		if(this.tag != null && this.tag.length() > 0){
			broker.unregisterClient(this.tag, this);
		}
		tag = tag != null && tag.isEmpty() ? null : "@" + tag;
		this.tag = tag;
		if(this.tag != null){
			broker.registerClient(tag, this);
			labels.setListId(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT+"/"+tag, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
		else{
			labels.setListId(null, null);
		}
	}

	@Override
	public void setValue(TypifiedText value, boolean fireEvents) {

		this.labels.setValue(value.label);
		this.subject.setValue(value.subject);
		buildText(value.text);
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}

	@Override
	public TypifiedText getValue() {
		TypifiedText result = new TypifiedText();
		result.subject = this.subject.getValue();
		result.text = this.textBody.getValue().replaceAll("\\</bigbang:.*?:bb\\>",  "");
		return result;
	}

	@Override
	public void clear() {
		labels.clear();
		subject.clear();
		clearText();
	}

	@Override
	protected void setReadOnlyInternal(boolean readonly) {
		subject.setReadOnlyInternal(readonly);
		textBody.setReadOnlyInternal(readonly);
		labels.setReadOnlyInternal(readonly);

	}

	@Override
	public boolean isReadOnly() {
		return textBody.isReadOnly() || subject.isReadOnly();
	}

	@Override
	public void setLabelWidth(String width) {
		return;
	}

	@Override
	public void setTypifiedTexts(List<TypifiedText> texts) {
		return;
	}


	@Override
	public void removeText(TypifiedText text) {
		return;
	}


	@Override
	public void addText(TypifiedText text) {
		return;
	}


	@Override
	public void updateText(TypifiedText text) {
		return;
	}

	@Override
	public int getTypifiedTextDataVersionNumber() {
		return 0;
	}


	@Override
	public void setTypifiedTextDataVersionNumber(int number) {
		return;
	}


	@Override
	public void focus() {
		subject.focus();		
	}

	private void clearText() {
		textBody.setValue(deletePreKey(textBody.getValue().replaceAll("\\</bigbang:.*?:bb\\>",  "")));
	}

	private void buildText(String newText) {
		String result = getPreKey(newText) + deletePreKey(textBody.getValue().replaceAll("\\</bigbang:.*?:bb\\>",  ""));

		String key = nextKey(newText);
		while ( key != null )
		{
			result = replaceSection(result, key, getSection(newText, key));
			newText = deleteSection(newText, key);
			key = nextKey(newText);
		}

		textBody.setValue(result);
	}

	private String nextKey(String source) {
		int i, j;

		i = source.indexOf("<bigbang:");
		if ( i < 0 )
			return null;

		j = source.indexOf(":bb>", i + 9);
		if ( j < 0 )
			return null;

		return source.substring(i + 9, j);
	}

	private String getSection(String source, String key) {
		int i, j;
		
		i = source.indexOf("<bigbang:" + key + ":bb>");
		if ( i < 0 )
			return "";

		j = source.indexOf("<bigbang:", i + 13 + key.length());
		if ( j < 0 )
			return source.substring(i);

		return source.substring(i, j);
	}

	private String replaceSection(String source, String key, String newContent) {
		int i, j;
		
		i = source.indexOf("<bigbang:" + key + ":bb>");
		if ( i < 0 )
			return source + newContent;

		j = source.indexOf("<bigbang:", i + 13 + key.length());
		if ( j < 0 )
			return source.substring(0, i) + newContent;

		return source.substring(0, i) + newContent + source.substring(j);
	}

	private String deleteSection(String source, String key) {
		int i, j;
		
		i = source.indexOf("<bigbang:" + key + ":bb>");
		if ( i < 0 )
			return source;

		j = source.indexOf("<bigbang:", i + 13 + key.length());
		if ( j < 0 )
			return source.substring(0, i);

		return source.substring(0, i) + source.substring(j);
	}

	private String getPreKey(String source) {
		int i;
		
		i = source.indexOf("<bigbang:");
		if ( i < 0 )
			return source;

		return source.substring(0, i);
	}

	private String deletePreKey(String source) {
		int i;
		
		i = source.indexOf("<bigbang:");
		if ( i < 0 )
			return "";

		return source.substring(i);
	}
}
