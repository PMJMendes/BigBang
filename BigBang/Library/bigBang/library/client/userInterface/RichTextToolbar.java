/*
 * This software is published under the Apchae 2.0 licenses.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Author: Erik Scholtz 
 * Web: http://blog.elitecoderz.net
 */

package bigBang.library.client.userInterface;

import java.util.HashMap;

import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;

public class RichTextToolbar extends Composite {
	/** Local CONSTANTS **/
	//ImageMap and CSS related
	private static final String CSS_ROOT_NAME = "RichTextToolbar";
	
	//Color and Fontlists - First Value (key) is the Name to display, Second Value (value) is the HTML-Definition
	public final static HashMap<String,String> GUI_COLORLIST = new HashMap<String,String>();
	static {
		GUI_COLORLIST.put("Branco", "#FFFFFF");
		GUI_COLORLIST.put("Preto", "#000000");
		GUI_COLORLIST.put("Vermelho", "red");
		GUI_COLORLIST.put("Verde", "green");
		GUI_COLORLIST.put("Amarelo", "yellow");
		GUI_COLORLIST.put("Azul", "blue");
	}
	public final static HashMap<String,String> GUI_FONTLIST = new HashMap<String,String>();
	static {
	    GUI_FONTLIST.put("Times New Roman", "Times New Roman");
	    GUI_FONTLIST.put("Arial", "Arial");
	    GUI_FONTLIST.put("Courier New", "Courier New");
	    GUI_FONTLIST.put("Georgia", "Georgia");
	    GUI_FONTLIST.put("Trebuchet", "Trebuchet");
	    GUI_FONTLIST.put("Verdana", "Verdana");
	}
	
	//HTML Related (styles made by SPAN and DIV)
	private static final String HTML_STYLE_CLOSE_SPAN = "</span>";
	private static final String HTML_STYLE_CLOSE_DIV = "</div>";
	private static final String HTML_STYLE_OPEN_BOLD = "<span style=\"font-weight: negrito;\">";
	private static final String HTML_STYLE_OPEN_ITALIC = "<span style=\"font-weight: itálico;\">";
	private static final String HTML_STYLE_OPEN_UNDERLINE = "<span style=\"font-weight: sublinhado;\">";
	private static final String HTML_STYLE_OPEN_LINETHROUGH = "<span style=\"font-weight: rasurado;\">";
	private static final String HTML_STYLE_OPEN_ALIGNLEFT = "<div style=\"text-align: esquerda;\">";
	private static final String HTML_STYLE_OPEN_ALIGNCENTER = "<div style=\"text-align: centro;\">";
	private static final String HTML_STYLE_OPEN_ALIGNRIGHT = "<div style=\"text-align: direita;\">";
	private static final String HTML_STYLE_OPEN_INDENTRIGHT = "<div style=\"margin-left: 40px;\">";
	
	//HTML Related (styles made by custom HTML-Tags)
	private static final String HTML_STYLE_OPEN_SUBSCRIPT = "<sub>";
	private static final String HTML_STYLE_CLOSE_SUBSCRIPT = "</sub>";
	private static final String HTML_STYLE_OPEN_SUPERSCRIPT = "<sup>";
	private static final String HTML_STYLE_CLOSE_SUPERSCRIPT = "</sup>";
	private static final String HTML_STYLE_OPEN_ORDERLIST = "<ol><li>";
	private static final String HTML_STYLE_CLOSE_ORDERLIST = "</ol></li>";
	private static final String HTML_STYLE_OPEN_UNORDERLIST = "<ul><li>";
	private static final String HTML_STYLE_CLOSE_UNORDERLIST = "</ul></li>";
	
	//HTML Related (styles without closing Tag)
	private static final String HTML_STYLE_HLINE = "<hr style=\"width: 100%; height: 2px;\">";
	
	//GUI Related stuff
	private static final String GUI_DIALOG_INSERTURL = "Inserir endereço de um link:";
	private static final String GUI_DIALOG_IMAGEURL = "Inserir endereço de uma imagem:";

	private static final String GUI_LISTNAME_COLORS = "Cores";
	private static final String GUI_LISTNAME_FONTS = "Tipos de letra";
	
	private static final String GUI_HOVERTEXT_SWITCHVIEW = "Vista HTML/Texto";
	private static final String GUI_HOVERTEXT_REMOVEFORMAT = "Remover Formatação";
	private static final String GUI_HOVERTEXT_IMAGE = "Inserir Imagem";
	private static final String GUI_HOVERTEXT_HLINE = "Inserir Linha Horizontal";
	private static final String GUI_HOVERTEXT_BREAKLINK = "Quebrar Ligação";
	private static final String GUI_HOVERTEXT_LINK = "Gerar Ligação";
	private static final String GUI_HOVERTEXT_IDENTLEFT = "Identar à Esquerda";
	private static final String GUI_HOVERTEXT_IDENTRIGHT = "Identar à Direita";
	private static final String GUI_HOVERTEXT_UNORDERLIST = "Lista Não Ordenada";
	private static final String GUI_HOVERTEXT_ORDERLIST = "Lista Ordenada";
	private static final String GUI_HOVERTEXT_ALIGNRIGHT = "Alinhar à Direita";
	private static final String GUI_HOVERTEXT_ALIGNCENTER = "Alinhar ao Centro";
	private static final String GUI_HOVERTEXT_ALIGNLEFT = "Alinhar à Esquerda";
	private static final String GUI_HOVERTEXT_SUPERSCRIPT = "Sobrescrito";
	private static final String GUI_HOVERTEXT_SUBSCRIPT = "Subescrito";
	private static final String GUI_HOVERTEXT_STROKE = "Rasura";
	private static final String GUI_HOVERTEXT_UNDERLINE = "Sublinha";
	private static final String GUI_HOVERTEXT_ITALIC = "Itálico";
	private static final String GUI_HOVERTEXT_BOLD = "Negrito";
	
	/** Private Variables **/
	//The main (Vertical)-Panel and the two inner (Horizontal)-Panels
	private VerticalPanel outer;
	private HorizontalPanel topPanel;
	private HorizontalPanel bottomPanel;

	//The RichTextArea this Toolbar referes to and the Interfaces to access the RichTextArea
	private RichTextArea styleText;
	private Formatter styleTextFormatter;

	//We use an internal class of the ClickHandler and the KeyUpHandler to be private to others with these events
	private EventHandler evHandler;

	//The Buttons of the Menubar
	private ToggleButton bold;
	private ToggleButton italic;
	private ToggleButton underline;
	private ToggleButton stroke;
	private ToggleButton subscript;
	private ToggleButton superscript;
	private PushButton alignleft;
	private PushButton alignmiddle;
	private PushButton alignright;
	private PushButton orderlist;
	private PushButton unorderlist;
	private PushButton indentleft;
	private PushButton indentright;
	private PushButton generatelink;
	private PushButton breaklink;
	private PushButton insertline;
	private PushButton insertimage;
	private PushButton removeformatting;
	private ToggleButton texthtml;
	
	private ListBox fontlist;
	private ListBox colorlist;

	/** Constructor of the Toolbar **/
	public RichTextToolbar(RichTextArea richtext) {
		//Initialize the main-panel
		outer = new VerticalPanel();

		//Initialize the two inner panels
		topPanel = new HorizontalPanel();
		bottomPanel = new HorizontalPanel();
		topPanel.setStyleName(CSS_ROOT_NAME);
		bottomPanel.setStyleName(CSS_ROOT_NAME);

		//Save the reference to the RichText area we refer to and get the interfaces to the stylings

		styleText = richtext;
		styleTextFormatter = styleText.getFormatter();

		//Set some graphical options, so this toolbar looks how we like it.
		topPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		bottomPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

		//Add the two inner panels to the main panel
		outer.add(topPanel);
		outer.add(bottomPanel);

		//Some graphical stuff to the main panel and the initialisation of the new widget
		outer.setWidth("100%");
		outer.setStyleName(CSS_ROOT_NAME);
		initWidget(outer);

		//
		evHandler = new EventHandler();

		//Add KeyUp and Click-Handler to the RichText, so that we can actualize the toolbar if neccessary
		styleText.addKeyUpHandler(evHandler);
		styleText.addClickHandler(evHandler);

		//Now lets fill the new toolbar with life
		buildTools();
	}

	/** Click Handler of the Toolbar **/
	private class EventHandler implements ClickHandler,KeyUpHandler, ChangeHandler {
		public void onClick(ClickEvent event) {
			if (event.getSource().equals(bold)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_BOLD,HTML_STYLE_CLOSE_SPAN);
				} else {
					styleTextFormatter.toggleBold();
				}
			} else if (event.getSource().equals(italic)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_ITALIC,HTML_STYLE_CLOSE_SPAN);
				} else {
					styleTextFormatter.toggleItalic();
				}
			} else if (event.getSource().equals(underline)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_UNDERLINE,HTML_STYLE_CLOSE_SPAN);
				} else {
					styleTextFormatter.toggleUnderline();
				}
			} else if (event.getSource().equals(stroke)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_LINETHROUGH,HTML_STYLE_CLOSE_SPAN);
				} else {
					styleTextFormatter.toggleStrikethrough();
				}
			} else if (event.getSource().equals(subscript)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_SUBSCRIPT,HTML_STYLE_CLOSE_SUBSCRIPT);
				} else {
					styleTextFormatter.toggleSubscript();
				}
			} else if (event.getSource().equals(superscript)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_SUPERSCRIPT,HTML_STYLE_CLOSE_SUPERSCRIPT);
				} else {
					styleTextFormatter.toggleSuperscript();
				}
			} else if (event.getSource().equals(alignleft)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_ALIGNLEFT,HTML_STYLE_CLOSE_DIV);
				} else {
					styleTextFormatter.setJustification(RichTextArea.Justification.LEFT);
				}
			} else if (event.getSource().equals(alignmiddle)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_ALIGNCENTER,HTML_STYLE_CLOSE_DIV);
				} else {
					styleTextFormatter.setJustification(RichTextArea.Justification.CENTER);
				}
			} else if (event.getSource().equals(alignright)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_ALIGNRIGHT,HTML_STYLE_CLOSE_DIV);
				} else {
					styleTextFormatter.setJustification(RichTextArea.Justification.RIGHT);
				}
			} else if (event.getSource().equals(orderlist)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_ORDERLIST,HTML_STYLE_CLOSE_ORDERLIST);
				} else {
					styleTextFormatter.insertOrderedList();
				}
			} else if (event.getSource().equals(unorderlist)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_UNORDERLIST,HTML_STYLE_CLOSE_UNORDERLIST);
				} else {
					styleTextFormatter.insertUnorderedList();
				}
			} else if (event.getSource().equals(indentright)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_OPEN_INDENTRIGHT,HTML_STYLE_CLOSE_DIV);
				} else {
					styleTextFormatter.rightIndent();
				}
			} else if (event.getSource().equals(indentleft)) {
				if (isHTMLMode()) {
					//TODO nothing can be done here at the moment
				} else {
					styleTextFormatter.leftIndent();
				}
			} else if (event.getSource().equals(generatelink)) {
				String url = Window.prompt(GUI_DIALOG_INSERTURL, "http://");
				if (url != null) {
					if (isHTMLMode()) {
						changeHtmlStyle("<a href=\""+url+"\">","</a>");
					} else {
						styleTextFormatter.createLink(url);
					}
				}
			} else if (event.getSource().equals(breaklink)) {
				if (isHTMLMode()) {
					//TODO nothing can be done here at the moment
				} else {
					styleTextFormatter.removeLink();
				}
			} else if (event.getSource().equals(insertimage)) {
				String url = Window.prompt(GUI_DIALOG_IMAGEURL, "http://");
				if (url != null) {
					if (isHTMLMode()) {
						changeHtmlStyle("<img src=\""+url+"\">","");
					} else {
						styleTextFormatter.insertImage(url);
					}
				}
			}  else if (event.getSource().equals(insertline)) {
				if (isHTMLMode()) {
					changeHtmlStyle(HTML_STYLE_HLINE,"");
				} else {
					styleTextFormatter.insertHorizontalRule();
				}
			} else if (event.getSource().equals(removeformatting)) {
				if (isHTMLMode()) {
					//TODO nothing can be done here at the moment
				} else {
					styleTextFormatter.removeFormat();
				}
			} else if (event.getSource().equals(texthtml)) {
				if (texthtml.isDown()) {
					styleText.setText(styleText.getHTML());
				} else {
					styleText.setHTML(styleText.getText());
				}
			} else if (event.getSource().equals(styleText)) {
				//Change invoked by the richtextArea
			}
			updateStatus();
		}

		public void onKeyUp(KeyUpEvent event) {
			updateStatus();
		}

		public void onChange(ChangeEvent event) {
			if (event.getSource().equals(fontlist)) {
				if (isHTMLMode()) {
					changeHtmlStyle("<span style=\"font-family: "+fontlist.getValue(fontlist.getSelectedIndex())+";\">",HTML_STYLE_CLOSE_SPAN);
				} else {
					styleTextFormatter.setFontName(fontlist.getValue(fontlist.getSelectedIndex()));
				}
			} else if (event.getSource().equals(colorlist)) {
				if (isHTMLMode()) {
					changeHtmlStyle("<span style=\"color: "+colorlist.getValue(colorlist.getSelectedIndex())+";\">",HTML_STYLE_CLOSE_SPAN);
				} else {
					styleTextFormatter.setForeColor(colorlist.getValue(colorlist.getSelectedIndex()));
				}
			}
		}
	}

	/** Native JavaScript that returns the selected text and position of the start **/
	public static native JsArrayString getSelection(Element elem) /*-{
		var txt = "";
		var pos = 0;
		var range;
    	var parentElement;
    	var container;

        if (elem.contentWindow.getSelection) {
        	txt = elem.contentWindow.getSelection();
        	pos = elem.contentWindow.getSelection().getRangeAt(0).startOffset;
        } else if (elem.contentWindow.document.getSelection) {
        	txt = elem.contentWindow.document.getSelection();
        	pos = elem.contentWindow.document.getSelection().getRangeAt(0).startOffset;
  		} else if (elem.contentWindow.document.selection) {
  			range = elem.contentWindow.document.selection.createRange();
        	txt = range.text;
        	parentElement = range.parentElement();
        	container = range.duplicate();
        	container.moveToElementText(parentElement);
        	container.setEndPoint('EndToEnd', range);
        	pos = container.text.length - range.text.length;
        }
  		return [""+txt,""+pos];
	}-*/;

	/** Method called to toggle the style in HTML-Mode **/
	private void changeHtmlStyle(String startTag, String stopTag) {
		JsArrayString tx = getSelection(styleText.getElement());
		String txbuffer = styleText.getText();
		Integer startpos = Integer.parseInt(tx.get(1));
		String selectedText = tx.get(0);
		styleText.setText(txbuffer.substring(0, startpos)+startTag+selectedText+stopTag+txbuffer.substring(startpos+selectedText.length()));
	}

	/** Private method with a more understandable name to get if HTML mode is on or not **/
	private Boolean isHTMLMode() {
		return  texthtml.isDown();
	}

	/** Private method to set the toggle buttons and disable/enable buttons which do not work in html-mode **/
	private void updateStatus() {
		if (styleTextFormatter != null) {
			bold.setDown(styleTextFormatter.isBold());
			italic.setDown(styleTextFormatter.isItalic());
			underline.setDown(styleTextFormatter.isUnderlined());
			subscript.setDown(styleTextFormatter.isSubscript());
			superscript.setDown(styleTextFormatter.isSuperscript());
			stroke.setDown(styleTextFormatter.isStrikethrough());
		}
		
		if (isHTMLMode()) {
			removeformatting.setEnabled(false);
			indentleft.setEnabled(false);
			breaklink.setEnabled(false);
		} else {
			removeformatting.setEnabled(true);
			indentleft.setEnabled(true);
			breaklink.setEnabled(true);
		}
	}

	/** Initialize the options on the toolbar **/
	private void buildTools() {
		//Init the TOP Panel forst
		topPanel.add(bold = createToggleButton(1,GUI_HOVERTEXT_BOLD));
		topPanel.add(italic = createToggleButton(2,GUI_HOVERTEXT_ITALIC));
		topPanel.add(underline = createToggleButton(3,GUI_HOVERTEXT_UNDERLINE));
		topPanel.add(stroke = createToggleButton(4,GUI_HOVERTEXT_STROKE));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(subscript = createToggleButton(5,GUI_HOVERTEXT_SUBSCRIPT));
		topPanel.add(superscript = createToggleButton(6,GUI_HOVERTEXT_SUPERSCRIPT));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(alignleft = createPushButton(1,GUI_HOVERTEXT_ALIGNLEFT));
		topPanel.add(alignmiddle = createPushButton(2,GUI_HOVERTEXT_ALIGNCENTER));
		topPanel.add(alignright = createPushButton(3,GUI_HOVERTEXT_ALIGNRIGHT));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(orderlist = createPushButton(4,GUI_HOVERTEXT_ORDERLIST));
		topPanel.add(unorderlist = createPushButton(5,GUI_HOVERTEXT_UNORDERLIST));
		topPanel.add(indentright = createPushButton(6,GUI_HOVERTEXT_IDENTRIGHT));
		topPanel.add(indentleft = createPushButton(7,GUI_HOVERTEXT_IDENTLEFT));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(generatelink = createPushButton(8,GUI_HOVERTEXT_LINK));
		topPanel.add(breaklink = createPushButton(9,GUI_HOVERTEXT_BREAKLINK));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(insertline = createPushButton(10,GUI_HOVERTEXT_HLINE));
		topPanel.add(insertimage = createPushButton(11,GUI_HOVERTEXT_IMAGE));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(removeformatting = createPushButton(12,GUI_HOVERTEXT_REMOVEFORMAT));
		topPanel.add(new HTML("&nbsp;"));
		topPanel.add(texthtml = createToggleButton(13,GUI_HOVERTEXT_SWITCHVIEW));

		//Init the BOTTOM Panel
		bottomPanel.add(fontlist = createFontList());
		bottomPanel.add(new HTML("&nbsp;"));
		bottomPanel.add(colorlist = createColorList());
	}

	/** Method to create a Toggle button for the toolbar **/
	private ToggleButton createToggleButton(int imageId, String tip) {
		Resources resources = GWT.create(Resources.class);
		Image extract = null;
		switch (imageId) {
	        case 1:  extract = new Image(resources.bold());
	                 break;
	        case 2:  extract = new Image(resources.italic());
	                 break;
	        case 3:  extract = new Image(resources.underline());
	                 break;
	        case 4:  extract = new Image(resources.striketrough());
	                 break;
	        case 5:  extract = new Image(resources.subscript());
	                 break;
	        case 6:  extract = new Image(resources.superscript());
	                 break;
	        default: 
	                 break;
	    }
		
		ToggleButton tb = new ToggleButton(extract);
		tb.setHeight("20px");
		tb.setWidth("20px");
		tb.addClickHandler(evHandler);
		if (tip != null) {
			tb.setTitle(tip);
		}
		return tb;
	}

	/** Method to create a Push button for the toolbar **/
	private PushButton createPushButton(int imageId, String tip) {
		Resources resources = GWT.create(Resources.class);
		Image extract = null;
		switch (imageId) {
	        case 1:  extract = new Image(resources.justLeft());
	                 break;
	        case 2:  extract = new Image(resources.justCenter());
	                 break;
	        case 3:  extract = new Image(resources.justRight());
	                 break;
	        case 4:  extract = new Image(resources.listOrdered());
	                 break;
	        case 5:  extract = new Image(resources.listUnordered());
	                 break;
	        case 6:  extract = new Image(resources.listUnordered());
	                 break;
	        case 7:  extract = new Image(resources.indentRight());
            		 break;
	        case 8:  extract = new Image(resources.indentLeft());
   		 			 break;
	        case 9:  extract = new Image(resources.linkAdd());
	 			 	 break;
	        case 10: extract = new Image(resources.linkBreak());
		 	 		 break;
	        case 11: extract = new Image(resources.horizRule());
	 		 		 break;
	        case 12: extract = new Image(resources.image());
	 		 		 break;
	        case 13: extract = new Image(resources.switchImg());
	 		 		 break;
	        default: 
	                 break;
	    }
		
		PushButton tb = new PushButton(extract);
		tb.setHeight("20px");
		tb.setWidth("20px");
		tb.addClickHandler(evHandler);
		if (tip != null) {
			tb.setTitle(tip);
		}
		return tb;
	}
	
	/** Method to create the fontlist for the toolbar **/
	private ListBox createFontList() {
	    ListBox mylistBox = new ListBox();
	    mylistBox.addChangeHandler(evHandler);
	    mylistBox.setVisibleItemCount(1);
	
	    mylistBox.addItem(GUI_LISTNAME_FONTS);
	    for (String name: GUI_FONTLIST.keySet()) {
	    	mylistBox.addItem(name, GUI_FONTLIST.get(name));
	    }
	    
	    return mylistBox;
	}
	
	/** Method to create the colorlist for the toolbar **/
	private ListBox createColorList() {
	    ListBox mylistBox = new ListBox();
	    mylistBox.addChangeHandler(evHandler);
	    mylistBox.setVisibleItemCount(1);
	
	    mylistBox.addItem(GUI_LISTNAME_COLORS);
	    for (String name: GUI_COLORLIST.keySet()) {
	    	mylistBox.addItem(name, GUI_COLORLIST.get(name));
	    }
	    
	    return mylistBox;
	}

}


