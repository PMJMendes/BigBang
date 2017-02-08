package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.File;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Company;

/**
 * Class responsible for creating a XML file with the needed info for billing
 * 
 * https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 * https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
 */
public class XMLCreator {

	// Element Names' Definition
	private static String ROOT_ID = "Documento";
	private static String ENTRY_ID = "Entrada";
	private static String COMPANY_NAME_ID = "NomeCompanhia";
	private static String COMPANY_NIF_ID = "NifCompanhia";
	private static String ADDRESS_ID = "MoradaCompanhia";
	private static String STREET_1_ID = "Rua_1";
	private static String STREET_2_ID = "Rua_2";
	private static String ZIP_ID = "CodigoPostal";
	private static String LOCALIDADE_ID = "Localidade";
	private static String COUNTY_ID = "Concelho";
	private static String DISTRICT_ID = "Distrito";
	private static String COUNTRY_ID = "Pais";
	private static String LINE_ID = "Linha";
	private static String DOC_NUMBER_ID = "NumDoc";
	private static String DOC_DATE_ID = "DataDocumento";
	private static String DUE_DATE_ID = "DataVencimento";
	private static String VALUES_ID = "Valores";
	private static String TOTAL_VALUE_ID = "TotalComissoes";
	private static String TAX_ID = "ImpostoSelo";
	private static String LIQUID_VALUE_ID = "ValorLiquido";

	// Class variables
	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;
	private static Document doc;
	private static Element rootElement;

	// This class defines the data to display in one entry
	private class XMLEntry {

		private String companyName;
		private String companyNif;
		private String addressStreet1;
		private String addressStreet2;
		private String addressZipCode;
		private String addressCity;
		private String addressCounty;
		private String addressDistrict;
		private String addressCountry;
		private String documentNumber;
		private String documentDate;
		private String dueDate;
		private String valuesTotalValue;
		private String valuesTax;
		private String valuesNet;

		private String getCompanyName() {
			return companyName;
		}

		private void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		private String getCompanyNif() {
			return companyNif;
		}

		private void setCompanyNif(String companyNif) {
			this.companyNif = companyNif;
		}

		private String getAddressStreet1() {
			return addressStreet1;
		}

		private void setAddressStreet1(String addressStreet1) {
			this.addressStreet1 = addressStreet1;
		}

		private String getAddressStreet2() {
			return addressStreet2;
		}

		private void setAddressStreet2(String addressStreet2) {
			this.addressStreet2 = addressStreet2;
		}

		private String getAddressZipCode() {
			return addressZipCode;
		}

		private void setAddressZipCode(String addressZipCode) {
			this.addressZipCode = addressZipCode;
		}

		private String getAddressCity() {
			return addressCity;
		}

		private void setAddressCity(String addressCity) {
			this.addressCity = addressCity;
		}

		private String getAddressCounty() {
			return addressCounty;
		}

		private void setAddressCounty(String addressCounty) {
			this.addressCounty = addressCounty;
		}

		private String getAddressDistrict() {
			return addressDistrict;
		}

		private void setAddressDistrict(String addressDistrict) {
			this.addressDistrict = addressDistrict;
		}

		private String getAddressCountry() {
			return addressCountry;
		}

		private void setAddressCountry(String addressCountry) {
			this.addressCountry = addressCountry;
		}

		private String getDocumentNumber() {
			return documentNumber;
		}

		private void setDocumentNumber(String documentNumber) {
			this.documentNumber = documentNumber;
		}

		private String getDocumentDate() {
			return documentDate;
		}

		private void setDocumentDate(String documentDate) {
			this.documentDate = documentDate;
		}

		private String getDueDate() {
			return dueDate;
		}

		private void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}

		private String getValuesTotalValue() {
			return valuesTotalValue;
		}

		private void setValuesTotalValue(String valuesTotalValue) {
			this.valuesTotalValue = valuesTotalValue;
		}

		private String getValuesTax() {
			return valuesTax;
		}

		private void setValuesTax(String valuesTax) {
			this.valuesTax = valuesTax;
		}

		private String getValuesNet() {
			return valuesNet;
		}

		private void setValuesNet(String valuesNet) {
			this.valuesNet = valuesNet;
		}

		/**
		 * This method sets the values related to the company to whom the
		 * invoice will be sent
		 */
		private void setCompanyValues(Company company)
				throws BigBangJewelException {

			if (company.getLabel() != null) {
				setCompanyName(company.getLabel());
			}

			Object val = company.getAt(Company.I.FISCALNUMBER);
			if (val != null) {
				setCompanyNif(val.toString());
			}

			val = company.getAt(Company.I.ADDRESS1);
			if (val != null) {
				setAddressStreet1(val.toString());
			}

			val = company.getAt(Company.I.ADDRESS2);
			if (val != null) {
				setAddressStreet2(val.toString());
			}

			val = company.getAt(Company.I.ZIPCODE);
			if (val != null) {
				ObjectBase zipCode;
				try {
					zipCode = Engine.GetWorkInstance(Engine.FindEntity(
							Engine.getCurrentNameSpace(),
							ObjectGUIDs.O_PostalCode), UUID.fromString(val
							.toString()));
				} catch (Throwable e) {
					throw new BigBangJewelException(
							"Error while getting the ZipCode entity. "
									+ e.getMessage(), e);
				}

				// Zip code values set
				if (zipCode != null) {
					val = zipCode.getAt(0);
					if (val != null) {
						setAddressZipCode(val.toString());
					}

					val = zipCode.getAt(1);
					if (val != null) {
						setAddressCity(val.toString());
					}

					val = zipCode.getAt(2);
					if (val != null) {
						setAddressCounty(val.toString());
					}

					val = zipCode.getAt(3);
					if (val != null) {
						setAddressDistrict(val.toString());
					}

					val = zipCode.getAt(4);
					if (val != null) {
						setAddressCountry(val.toString());
					}
				}
			}
		}

		/**
		 * A constructor with all the needed info
		 */
		public XMLEntry(Company company, String docNumber, String docDate,
				String dueDate, String totalValue, String taxValue,
				String netValue) throws BigBangJewelException {

			if (company != null) {
				setCompanyValues(company);
			}

			if (docNumber != null) {
				setDocumentNumber(docNumber);
			}

			if (docDate != null) {
				setDocumentDate(docDate);
			}

			if (dueDate != null) {
				setDueDate(dueDate);
			}

			if (totalValue != null) {
				setValuesTotalValue(totalValue);
			}

			if (taxValue != null) {
				setValuesTax(taxValue);
			}

			if (netValue != null) {
				setValuesNet(netValue);
			}
		}
	}

	/**
	 * This method initializes the document factory, the document builder, and
	 * the document, used to write the XML
	 */
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

		if (doc == null) {
			doc = docBuilder.newDocument();
		}
	}

	/**
	 * This method creates an element with attributes, attributes' values and an
	 * element value
	 */
	private static Element createElement(String elementName,
			String[] attributes, String[] attValues, String elementValue) {

		Element el = doc.createElement(elementName);

		if (attributes != null && attValues != null) {
			for (int i = 0; i < attributes.length && i < attValues.length; i++) {
				Attr attr = doc.createAttribute(attributes[i]);
				attr.setValue(attValues[i]);
				el.setAttributeNode(attr);
			}
		}

		if (elementValue != null) {
			el.appendChild(doc.createTextNode(elementValue));
		}

		return el;
	}

	/**
	 * This method creates an element with an element value
	 */
	private static Element createElementWithValue(String elementName,
			String elementValue) {
		return createElement(elementName, null, null, elementValue);
	}

	/**
	 * This method creates a simple element, without attributes or values
	 */
	private static Element createSimpleElement(String elementName) {
		return createElement(elementName, null, null, null);
	}

	/**
	 * This method creates the root element
	 */
	private static void createRootElement() {
		if (rootElement == null) {
			rootElement = createElement(ROOT_ID, null, null, null);
		}
	}

	/**
	 * This method writes the xml file, storing it in the given URL
	 */
	private static void writeXmlFile(String url, Document docToWrite) throws BigBangJewelException {

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;

		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new BigBangJewelException(
					"Error while getting the transformer used for XML creation. "
							+ e.getMessage(), e);
		}
		DOMSource source;
		if(docToWrite==null) {
			source = new DOMSource(doc);
		} else {
			source = new DOMSource(docToWrite);
		}
		
		StreamResult result = new StreamResult(new File(url));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new BigBangJewelException(
					"Error while writting the XML file. " + e.getMessage(), e);
		}
	}

	/**
	 * This method creates a new XML. Fills the XMLEntry with the info and then
	 * writes the file.
	 */
	private void createXML(Company company, String docNumber, String docDate,
			String dueDate, String totalValue, String taxValue,
			String netValue, String filePath) throws BigBangJewelException {

		XMLEntry entry = new XMLEntry(company, docNumber, docDate, dueDate,
				totalValue, taxValue, netValue);

		initialize();

		createRootElement();

		Element entryEl = createElement(entry);

		// adds it all to the root element
		rootElement.appendChild(entryEl);

		writeXmlFile(filePath, null);
	}
	
	/**
	 * This method fills a XMLEntry with info and writes it to an existing file
	 */
	private void addElementToXML(Company company, String docNumber, String docDate,
			String dueDate, String totalValue, String taxValue,
			String netValue, String filePath) throws BigBangJewelException {

		XMLEntry entry = new XMLEntry(company, docNumber, docDate, dueDate,
				totalValue, taxValue, netValue);
		
		Document existingDoc;

		initialize();

		createRootElement();

		Element entryEl = createElement(entry);
		
		// Parses the file in the path
		try {
			existingDoc = docBuilder.parse(filePath);
		} catch (Throwable e) { 
			throw new BigBangJewelException( "Error while getting the file to add a new element. " + e.getMessage(), e);
		}
		
		// Get the root element
		Node existingRoot = existingDoc.getFirstChild();
		
		// adds it all to the root element
		existingRoot.appendChild(entryEl);
		
		writeXmlFile(filePath, existingDoc);
	}

	/**
	 * This method fills a XMLEntry with info and writes it to an existing file
	 */
	private Element createElement(XMLEntry entry) {
		// Creates the entities with the values
		Element totalValueEl = createElementWithValue(TOTAL_VALUE_ID,
				entry.getValuesTotalValue());
		Element taxEl = createElementWithValue(TAX_ID, entry.getValuesTax());
		Element netValueEl = createElementWithValue(LIQUID_VALUE_ID,
				entry.getValuesNet());
		Element valuesEl = createSimpleElement(VALUES_ID);
		valuesEl.appendChild(totalValueEl);
		valuesEl.appendChild(taxEl);
		valuesEl.appendChild(netValueEl);

		// Creates the entities with the billing info
		Element docNumberEl = createElementWithValue(DOC_NUMBER_ID,
				entry.getDocumentNumber());
		Element docCreationDateEl = createElementWithValue(DOC_DATE_ID,
				entry.getDocumentDate());
		Element docDueDateEl = createElementWithValue(DUE_DATE_ID,
				entry.getDueDate());
		Element lineEl = createSimpleElement(LINE_ID);
		lineEl.appendChild(valuesEl);
		lineEl.appendChild(docNumberEl);
		lineEl.appendChild(docCreationDateEl);
		lineEl.appendChild(docDueDateEl);

		// Creates the entities with the address info
		Element street1El = createElementWithValue(STREET_1_ID,
				entry.getAddressStreet1());
		Element street2El = createElementWithValue(STREET_2_ID,
				entry.getAddressStreet2());
		Element zipEl = createElementWithValue(ZIP_ID,
				entry.getAddressZipCode());
		Element cityEl = createElementWithValue(LOCALIDADE_ID,
				entry.getAddressCity());
		Element countyEl = createElementWithValue(COUNTY_ID,
				entry.getAddressCounty());
		Element districtEl = createElementWithValue(DISTRICT_ID,
				entry.getAddressDistrict());
		Element countryEl = createElementWithValue(COUNTRY_ID,
				entry.getAddressCountry());
		Element addressEl = createSimpleElement(ADDRESS_ID);
		addressEl.appendChild(street1El);
		addressEl.appendChild(street2El);
		addressEl.appendChild(zipEl);
		addressEl.appendChild(cityEl);
		addressEl.appendChild(countyEl);
		addressEl.appendChild(districtEl);
		addressEl.appendChild(countryEl);

		// Creates the entities with the company info
		Element companyNameEl = createElementWithValue(COMPANY_NAME_ID,
				entry.getCompanyName());
		Element companyNifEl = createElementWithValue(COMPANY_NIF_ID,
				entry.getCompanyNif());
		Element entryEl = createSimpleElement(ENTRY_ID);
		entryEl.appendChild(companyNameEl);
		entryEl.appendChild(companyNifEl);
		entryEl.appendChild(addressEl);
		entryEl.appendChild(lineEl);
		return entryEl;
	}
	
	/**
	 * This method creates a new XML or adds a new element to an existing XML.
	 * If the file exists, it adds a new element. If it does not exist, it
	 * creates a new file. However, you may force a new file creation, case in
	 * which a new file will always be created, overwriting any existing file
	 * with the same name.
	 */
	public void createXML(Company company, String docNumber, String docDate,
			String dueDate, String totalValue, String taxValue,
			String netValue, String filePath, boolean overWrite)
			throws BigBangJewelException {

		File f = new File(filePath); // The file used to check if a file exists

		if (f.exists() && !f.isDirectory() && !overWrite) {
			addElementToXML(company, docNumber, docDate, dueDate, totalValue,
					taxValue, netValue, filePath);
		} else {
			createXML(company, docNumber, docDate, dueDate, totalValue,
					taxValue, netValue, filePath);
		}
	}
}