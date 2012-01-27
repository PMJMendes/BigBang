package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.shared.TypifiedText;

public interface TypifiedTextClient {

	/**
	 * @return the version of the data held by the client
	 */
	public int getTypifiedTextDataVersionNumber();
	
	/**
	 * @param number the data version number
	 */
	public void setTypifiedTextDataVersionNumber(int number);
	
	/**
	 * @param texts sets the texts
	 */
	public void setTypifiedTexts(List<TypifiedText> texts);
	
	/**
	 * Indicates that the client should remove the text if it is cached.
	 * @param text the text to remove
	 */
	public void removeText(TypifiedText text);
	
	/**
	 * Indicates that the client should take notice of a new text
	 * @param text the text to add
	 */
	public void addText(TypifiedText text);
	
	/**
	 * Indicates that the client should update its cached representation of the text
	 * @param text the text to update
	 */
	public void updateText(TypifiedText text);
	
}
