package com.premiumminds.BigBang.Jewel.SysObjects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

/**
 * Class responsible for creating a XML file with the needed info for billing
 * 
 * https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 */
public class XMLCreator {

	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;

	private static void initialize() throws BigBangJewelException {

		if (docFactory == null) {
			docFactory = DocumentBuilderFactory.newInstance();
		}

		if (docBuilder == null) {
			try {
				docBuilder = docFactory.newDocumentBuilder();
			} catch (Throwable e) {
				throw new BigBangJewelException(
						"Error while initializing the DocumentBuilder used for XML creation. "
								+ e.getMessage(), e);
			}
		}
	}

}
