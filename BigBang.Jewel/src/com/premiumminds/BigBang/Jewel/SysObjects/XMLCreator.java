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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Company;

/**
 * Class responsible for creating a XML file with the needed info for billing
 * 
 * https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 */
public class XMLCreator {

	// Element Names' Definition
	private static String ROOT_ID = "Factura";
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
	private static String DOC_DATE_ID = "DataEmissao";
	private static String VALUES_ID = "Valores";
	private static String TOTAL_VALUE_ID = "TotalComissoes";
	private static String TAX_ID = "ImpostoSelo";
	private static String LIQUID_VALUE_ID = "ValorLiquido";
	
	// Other constants
	private static String EMPTY_VAL = ""; 

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
		private String documentDate;
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

		private String getDocumentDate() {
			return documentDate;
		}

		private void setDocumentDate(String documentDate) {
			this.documentDate = documentDate;
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
			} else {
				setCompanyName(EMPTY_VAL);
			}

			Object val = company.getAt(Company.I.FISCALNUMBER);
			if (val != null) {
				setCompanyNif(val.toString());
			} else {
				setCompanyNif(EMPTY_VAL);
			}

			val = company.getAt(Company.I.ADDRESS1);
			if (val != null) {
				setAddressStreet1(val.toString());
			} else {
				setAddressStreet1(EMPTY_VAL);
			}

			val = company.getAt(Company.I.ADDRESS2);
			if (val != null) {
				setAddressStreet2(val.toString());
			} else {
				setAddressStreet2(EMPTY_VAL);
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
					} else {
						setAddressZipCode(EMPTY_VAL);
					}

					val = zipCode.getAt(1);
					if (val != null) {
						setAddressCity(val.toString());
					} else {
						setAddressCity(EMPTY_VAL);
					}

					val = zipCode.getAt(2);
					if (val != null) {
						setAddressCounty(val.toString());
					} else {
						setAddressCounty(EMPTY_VAL);
					}

					val = zipCode.getAt(3);
					if (val != null) {
						setAddressDistrict(val.toString());
					} else {
						setAddressDistrict(EMPTY_VAL);
					}

					val = zipCode.getAt(4);
					if (val != null) {
						setAddressCountry(val.toString());
					} else {
						setAddressCountry(EMPTY_VAL);
					}
				} else {
					setAddressZipCode(EMPTY_VAL);
					setAddressCity(EMPTY_VAL);
					setAddressCounty(EMPTY_VAL);
					setAddressDistrict(EMPTY_VAL);
					setAddressCountry(EMPTY_VAL);
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

			if (docDate != null) {
				setDocumentDate(docDate);
			} else {
				setDocumentDate(EMPTY_VAL);
			}

			if (totalValue != null) {
				setValuesTotalValue(totalValue);
			} else {
				setValuesTotalValue(EMPTY_VAL);
			}

			if (taxValue != null) {
				setValuesTax(taxValue);
			} else {
				setValuesTax(EMPTY_VAL);
			}

			if (netValue != null) {
				setValuesNet(netValue);
			} else {
				setValuesNet(EMPTY_VAL);
			}
		}
	}

	/**
	 * This method writes the xml file, storing it in the given URL
	 */
	private static void writeXmlFile(String url, Document docToWrite)
			throws BigBangJewelException {

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
		source = new DOMSource(docToWrite);

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
	public void createXML(Company company, String docNumber, String docDate,
			String dueDate, String totalValue, String taxValue,
			String netValue, String filePath) throws BigBangJewelException {

		XMLEntry entry = new XMLEntry(company, docNumber, docDate, dueDate,
				totalValue, taxValue, netValue);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (Throwable e) {
			throw new BigBangJewelException(
					"Error while getting the document builder. " + e.getMessage(), e);
		}
		
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(ROOT_ID);
		doc.appendChild(rootElement);

		// Company's Name
		Element companyNameEl = doc.createElement(COMPANY_NAME_ID);
		companyNameEl.appendChild(doc.createTextNode(entry.getCompanyName()));
		rootElement.appendChild(companyNameEl);
		
		// Company's Nif
		Element companyNifEl = doc.createElement(COMPANY_NIF_ID);
		companyNifEl.appendChild(doc.createTextNode(entry.getCompanyNif()));
		rootElement.appendChild(companyNifEl);
		
		// Company's Address
		Element addressEl = doc.createElement(ADDRESS_ID);
		rootElement.appendChild(addressEl);
		
		// Address' fields
		Element street1El = doc.createElement(STREET_1_ID);
		street1El.appendChild(doc.createTextNode(entry.getAddressStreet1()));
		addressEl.appendChild(street1El);
		Element street2El = doc.createElement(STREET_2_ID);
		street2El.appendChild(doc.createTextNode(entry.getAddressStreet2()));
		addressEl.appendChild(street2El);
		Element zipEl = doc.createElement(ZIP_ID);
		zipEl.appendChild(doc.createTextNode(entry.getAddressZipCode()));
		addressEl.appendChild(zipEl);
		Element cityEl = doc.createElement(LOCALIDADE_ID);
		cityEl.appendChild(doc.createTextNode(entry.getAddressCity()));
		addressEl.appendChild(cityEl);
		Element countyEl = doc.createElement(COUNTY_ID);
		countyEl.appendChild(doc.createTextNode(entry.getAddressCounty()));
		addressEl.appendChild(countyEl);
		Element districtEl = doc.createElement(DISTRICT_ID);
		districtEl.appendChild(doc.createTextNode(entry.getAddressDistrict()));
		addressEl.appendChild(districtEl);
		Element countryEl = doc.createElement(COUNTRY_ID);
		countryEl.appendChild(doc.createTextNode(entry.getAddressCountry()));
		addressEl.appendChild(countryEl);
		
		// The date the document was created
		Element docCreationDateEl = doc.createElement(DOC_DATE_ID);
		docCreationDateEl.appendChild(doc.createTextNode(entry.getDocumentDate()));
		rootElement.appendChild(docCreationDateEl);
		
		// Invoice's values
		Element valuesEl = doc.createElement(VALUES_ID);
		rootElement.appendChild(valuesEl);
		
		// Values' fields
		Element totalValueEl = doc.createElement(TOTAL_VALUE_ID);
		totalValueEl.appendChild(doc.createTextNode(entry.getValuesTotalValue()));
		valuesEl.appendChild(totalValueEl);
		Element taxEl = doc.createElement(TAX_ID);
		taxEl.appendChild(doc.createTextNode(entry.getValuesTax()));
		valuesEl.appendChild(taxEl);
		Element netValueEl = doc.createElement(LIQUID_VALUE_ID);
		netValueEl.appendChild(doc.createTextNode(entry.getValuesNet()));
		valuesEl.appendChild(netValueEl);
		
		writeXmlFile(filePath, doc);
	}
}