package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.server.InsurancePolicyServiceImpl;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectService;
import bigBang.module.quoteRequestModule.shared.QuoteRequestObjectSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestObjectSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;

public class QuoteRequestObjectServiceImpl
	extends SearchServiceBase
	implements QuoteRequestObjectService
{
	private static final long serialVersionUID = 1L;

	public QuoteRequestObject getObject(String objectId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidObject;
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject lobjObject;
		ObjectBase lobjZipCode;
		ObjectBase lobjType;
		QuoteRequestObject lobjResult;
		QuoteRequest lobjRequest;
		QuoteRequestSubLine[] larrLocalSubLines;
		QuoteRequestCoverage[][] larrAllCoverages;
		QuoteRequestValue[][] larrValues;
		int i, j, k;
		ArrayList<QuoteRequestObject.SubLineData> larrSubLines;
		QuoteRequestObject.SubLineData lobjSubLine;
		ArrayList<QuoteRequestObject.ColumnHeader> larrOutColumns;
		ArrayList<QuoteRequestCoverage> larrLocalCoverages;
		QuoteRequestCoverage[] larrCoverages;
		QuoteRequestObject.ColumnHeader lobjColumnHeader;
		QuoteRequestCoverage lobjHeaderCoverage;
		ArrayList<QuoteRequestObject.CoverageData.FixedField> larrAuxFixed;
		QuoteRequestObject.CoverageData.FixedField lobjFixed;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidObject = UUID.fromString(objectId);

		try
		{
			lobjObject = com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(),
					lidObject);
			if ( lobjObject.getAt(5) == null )
				lobjZipCode = null;
			else
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjObject.getAt(5));
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ObjectType),
					(UUID)lobjObject.getAt(2));
			lobjRequest = QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjObject.getAt(1));

			larrLocalSubLines = lobjRequest.GetCurrentSubLines();

			larrAllCoverages = new QuoteRequestCoverage[larrLocalSubLines.length][];
			larrValues = new QuoteRequestValue[larrLocalSubLines.length][];
			for ( i = 0; i < larrLocalSubLines.length; i++ )
			{
				larrAllCoverages[i] = larrLocalSubLines[i].GetCurrentCoverages();
				larrValues[i] = larrLocalSubLines[i].GetCurrentKeyedValues(lidObject);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new QuoteRequestObject();
		lobjResult.id = lobjObject.getKey().toString();
		lobjResult.ownerId = ((UUID)lobjObject.getAt(1)).toString();
		lobjResult.unitIdentification = lobjObject.getLabel();
		if ( (lobjObject.getAt(3) != null) || (lobjObject.getAt(4) != null) || (lobjZipCode != null) )
		{
			lobjResult.address = new Address();
			lobjResult.address.street1 = (String)lobjObject.getAt(3);
			lobjResult.address.street2 = (String)lobjObject.getAt(4);
			if ( lobjZipCode != null )
			{
				lobjResult.address.zipCode = new ZipCode();
				lobjResult.address.zipCode.code = (String)lobjZipCode.getAt(0);
				lobjResult.address.zipCode.city = (String)lobjZipCode.getAt(1);
				lobjResult.address.zipCode.county = (String)lobjZipCode.getAt(2);
				lobjResult.address.zipCode.district = (String)lobjZipCode.getAt(3);
				lobjResult.address.zipCode.country = (String)lobjZipCode.getAt(4);
			}
			else
				lobjResult.address.zipCode = null;
		}
		else
			lobjResult.address = null;
		lobjResult.typeId = lobjType.getKey().toString();
		lobjResult.typeText = lobjType.getLabel();

		if ( Constants.ObjTypeID_Person.equals(lobjType.getKey()) )
		{
			lobjResult.taxNumberPerson = (String)lobjObject.getAt(8);
			lobjResult.genderId = ( lobjObject.getAt(9) == null ? null : ((UUID)lobjObject.getAt(9)).toString() );
			lobjResult.birthDate = ( lobjObject.getAt(10) == null ? null :
					((Timestamp)lobjObject.getAt(10)).toString().substring(0, 10) );
			lobjResult.clientNumberPerson = ( lobjObject.getAt(11) == null ? null : ((Integer)lobjObject.getAt(11)).toString() );
			lobjResult.insuranceCompanyInternalIdPerson = (String)lobjObject.getAt(12);
		}

		if ( Constants.ObjTypeID_Group.equals(lobjType.getKey()) )
		{
			lobjResult.taxNumberCompany = (String)lobjObject.getAt(13);
			lobjResult.caeId = ( lobjObject.getAt(14) == null ? null : ((UUID)lobjObject.getAt(14)).toString() );
			lobjResult.grievousCaeId = ( lobjObject.getAt(15) == null ? null : ((UUID)lobjObject.getAt(15)).toString() );
			lobjResult.activityNotes = (String)lobjObject.getAt(16);
			lobjResult.productNotes = (String)lobjObject.getAt(17);
			lobjResult.businessVolumeId = ( lobjObject.getAt(18) == null ? null : ((UUID)lobjObject.getAt(18)).toString() );
			lobjResult.europeanUnionEntity = (String)lobjObject.getAt(19);
			lobjResult.clientNumberGroup = ( lobjObject.getAt(20) == null ? null : ((Integer)lobjObject.getAt(20)).toString() );
		}

		if ( Constants.ObjTypeID_Equipment.equals(lobjType.getKey()) )
		{
			lobjResult.makeAndModel = (String)lobjObject.getAt(21);
			lobjResult.equipmentDescription = (String)lobjObject.getAt(22);
			lobjResult.firstRegistryDate = ( lobjObject.getAt(23) == null ? null :
				((Timestamp)lobjObject.getAt(23)).toString().substring(0, 10) );
			lobjResult.manufactureYear = ( lobjObject.getAt(24) == null ? null : ((Integer)lobjObject.getAt(24)).toString() );
			lobjResult.clientInternalId = (String)lobjObject.getAt(25);
			lobjResult.insuranceCompanyInternalIdVehicle = (String)lobjObject.getAt(26);
		}

		if ( Constants.ObjTypeID_Site.equals(lobjType.getKey()) )
		{
			lobjResult.siteDescription = (String)lobjObject.getAt(27);
		}

		if ( Constants.ObjTypeID_Animal.equals(lobjType.getKey()) )
		{
			lobjResult.species = (String)lobjObject.getAt(28);
			lobjResult.race = (String)lobjObject.getAt(29);
			lobjResult.birthYear = ( lobjObject.getAt(30) == null ? null : ((Integer)lobjObject.getAt(30)).toString() );
			lobjResult.cityRegistryNumber = (String)lobjObject.getAt(31);
			lobjResult.electronicIdTag = (String)lobjObject.getAt(32);
		}

		larrSubLines = new ArrayList<QuoteRequestObject.SubLineData>();
		for ( i = 0; i < larrLocalSubLines.length; i++ )
		{
			if ( !lobjType.getKey().equals((UUID)larrLocalSubLines[i].GetSubLine().getAt(2)) )
				continue;

			lobjSubLine = new QuoteRequestObject.SubLineData();
			lobjSubLine.subLineId = larrLocalSubLines[i].getKey().toString();
			lobjSubLine.headerText = larrLocalSubLines[i].GetSubLine().getLine().getCategory().getLabel() + " / " +
					larrLocalSubLines[i].GetSubLine().getLine().getLabel() + " / " + larrLocalSubLines[i].GetSubLine().getLabel();

			lobjHeaderCoverage = null;
			larrLocalCoverages = new ArrayList<QuoteRequestCoverage>();
			larrOutColumns = new ArrayList<QuoteRequestObject.ColumnHeader>();
			for ( j = 0; j < larrAllCoverages[i].length; j++ )
			{
				if ( larrAllCoverages[i][j].GetCoverage().IsHeader() )
					lobjHeaderCoverage = larrAllCoverages[i][j];
				else
					larrLocalCoverages.add(larrAllCoverages[i][j]);
			}
			larrCoverages = larrLocalCoverages.toArray(new QuoteRequestCoverage[larrLocalCoverages.size()]);
			java.util.Arrays.sort(larrCoverages, new Comparator<QuoteRequestCoverage>()
			{
				public int compare(QuoteRequestCoverage o1, QuoteRequestCoverage o2)
				{
					if ( o1.GetCoverage().IsMandatory() == o2.GetCoverage().IsMandatory() )
						return o1.GetCoverage().getLabel().compareTo(o2.GetCoverage().getLabel());
					if ( o1.GetCoverage().IsMandatory() )
						return -1;
					return 1;
				}
			});
			lobjSubLine.coverageData = new QuoteRequestObject.CoverageData[larrCoverages.length];
			for ( j = 0; j < larrCoverages.length; j++ )
			{
				lobjSubLine.coverageData[j] = new QuoteRequestObject.CoverageData();
				lobjSubLine.coverageData[j].coverageId = larrCoverages[j].GetCoverage().getKey().toString();
				lobjSubLine.coverageData[j].coverageLabel = larrCoverages[j].GetCoverage().getLabel();
				larrAuxFixed = new ArrayList<QuoteRequestObject.CoverageData.FixedField>();
				for ( k = 0; k < larrValues[i].length; k++ )
				{
					if ( !larrValues[i][k].GetTax().GetCoverage().getKey().equals(larrCoverages[j].GetCoverage().getKey()) )
						continue;

					if ( larrValues[i][k].GetTax().GetColumnOrder() >= 0 )
					{
						while ( larrOutColumns.size() <= larrValues[i][k].GetTax().GetColumnOrder() )
							larrOutColumns.add(null);
						lobjColumnHeader = new QuoteRequestObject.ColumnHeader();
						lobjColumnHeader.index = larrValues[i][k].GetTax().GetColumnOrder();
						lobjColumnHeader.label = larrValues[i][k].GetTax().getLabel();
						lobjColumnHeader.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)larrValues[i][k].GetTax().getAt(2));
						lobjColumnHeader.unitsLabel = (String)larrValues[i][k].GetTax().getAt(3);
						lobjColumnHeader.refersToId = ( larrValues[i][k].GetTax().getAt(7) == null ? null :
								((UUID)larrValues[i][k].GetTax().getAt(7)).toString() );
						larrOutColumns.set(lobjColumnHeader.index, lobjColumnHeader);
					}

					lobjFixed = new QuoteRequestObject.CoverageData.FixedField();
					lobjFixed.fieldId = larrValues[i][k].GetTax().getKey().toString();
					lobjFixed.fieldName = larrValues[i][k].GetTax().getLabel();
					lobjFixed.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrValues[i][k].GetTax().GetFieldType());
					lobjFixed.unitsLabel = larrValues[i][k].GetTax().GetUnitsLabel();
					lobjFixed.refersToId = ( larrValues[i][k].GetTax().GetRefersToID() == null ? null :
						larrValues[i][k].GetTax().GetRefersToID().toString() );
					lobjFixed.columnIndex = larrValues[i][k].GetTax().GetColumnOrder();
					lobjFixed.value = larrValues[i][k].getLabel();
					larrAuxFixed.add(lobjFixed);
				}
				lobjSubLine.coverageData[j].fixedFields =
						larrAuxFixed.toArray(new QuoteRequestObject.CoverageData.FixedField[larrAuxFixed.size()]);
				java.util.Arrays.sort(lobjSubLine.coverageData[j].fixedFields,
						new Comparator<QuoteRequestObject.CoverageData.FixedField>()
				{
					public int compare(QuoteRequestObject.CoverageData.FixedField o1, QuoteRequestObject.CoverageData.FixedField o2)
					{
						if ( (o1.type == o2.type) && (o1.refersToId == o1.refersToId) )
							return o1.fieldName.compareTo(o2.fieldName);
						if ( o1.type == o2.type )
							return o1.refersToId.compareTo(o2.refersToId);
						return o1.type.compareTo(o2.type);
					}
				});
			}
			for ( j = larrOutColumns.size() - 1; j >= 0; j-- )
				if ( larrOutColumns.get(j) == null )
					larrOutColumns.remove(j);
			lobjSubLine.columnHeaders = larrOutColumns.toArray(new QuoteRequestObject.ColumnHeader[larrOutColumns.size()]);
			if ( lobjHeaderCoverage != null )
			{
				lobjSubLine.headerData = new QuoteRequestObject.CoverageData();
				larrAuxFixed = new ArrayList<QuoteRequestObject.CoverageData.FixedField>();
				for ( k = 0; k < larrValues[i].length; k++ )
				{
					if ( !larrValues[i][k].GetTax().GetCoverage().getKey().equals(lobjHeaderCoverage.GetCoverage().getKey()) )
						continue;

					lobjFixed = new QuoteRequestObject.CoverageData.FixedField();
					lobjFixed.fieldId = larrValues[i][k].GetTax().getKey().toString();
					lobjFixed.fieldName = larrValues[i][k].GetTax().getLabel();
					lobjFixed.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrValues[i][k].GetTax().GetFieldType());
					lobjFixed.unitsLabel = larrValues[i][k].GetTax().GetUnitsLabel();
					lobjFixed.refersToId = ( larrValues[i][k].GetTax().GetRefersToID() == null ? null :
						larrValues[i][k].GetTax().GetRefersToID().toString() );
					lobjFixed.columnIndex = larrValues[i][k].GetTax().GetColumnOrder();
					lobjFixed.value = larrValues[i][k].getLabel();
					larrAuxFixed.add(lobjFixed);
				}
				lobjSubLine.headerData.fixedFields =
						larrAuxFixed.toArray(new QuoteRequestObject.CoverageData.FixedField[larrAuxFixed.size()]);
				java.util.Arrays.sort(lobjSubLine.headerData.fixedFields,
						new Comparator<QuoteRequestObject.CoverageData.FixedField>()
				{
					public int compare(QuoteRequestObject.CoverageData.FixedField o1, QuoteRequestObject.CoverageData.FixedField o2)
					{
						if ( (o1.type == o2.type) && (o1.refersToId == o1.refersToId) )
							return o1.fieldName.compareTo(o2.fieldName);
						if ( o1.type == o2.type )
							return o1.refersToId.compareTo(o2.refersToId);
						return o1.type.compareTo(o2.type);
					}
				});
			}

			larrSubLines.add(lobjSubLine);
		}

		lobjResult.objectData = larrSubLines.toArray(new QuoteRequestObject.SubLineData[larrSubLines.size()]);
		java.util.Arrays.sort(lobjResult.objectData, new Comparator<QuoteRequestObject.SubLineData>()
		{
			public int compare(QuoteRequestObject.SubLineData o1, QuoteRequestObject.SubLineData o2)
			{
				return o1.headerText.compareTo(o2.headerText);
			}
		});

		return lobjResult;
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_QuoteRequestObject;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Name]", "[:Quote Request]", "[:Type]", "[:Type:Type]", "[:Address 1]", "[:Address 2]", "[:Zip Code]",
				"[:Zip Code:Code]", "[:Zip Code:City]", "[:Zip Code:County]", "[:Zip Code:District]", "[:Zip Code:Country]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		QuoteRequestObjectSearchParameter lParam;
		String lstrAux;

		if ( !(pParam instanceof QuoteRequestObjectSearchParameter) )
			return false;
		lParam = (QuoteRequestObjectSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Name] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.requestId != null )
		{
			pstrBuffer.append(" AND [:Quote Request] = '").append(lParam.requestId).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		QuoteRequestObjectSortParameter lParam;

		if ( !(pParam instanceof QuoteRequestObjectSortParameter) )
			return false;
		lParam = (QuoteRequestObjectSortParameter)pParam;

		if ( lParam.field == QuoteRequestObjectSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == QuoteRequestObjectSortParameter.SortableField.NAME )
			pstrBuffer.append("[:Name]");

		if ( lParam.field == QuoteRequestObjectSortParameter.SortableField.TYPE )
			pstrBuffer.append("[:Type]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		QuoteRequestObjectStub lobjResult;

		lobjResult = new QuoteRequestObjectStub();
		lobjResult.id = pid.toString();
		lobjResult.unitIdentification = (String)parrValues[0];
		lobjResult.ownerId = ((UUID)parrValues[1]).toString();
		lobjResult.typeId = ((UUID)parrValues[2]).toString();
		lobjResult.typeText = (String)parrValues[3];
		if ( (parrValues[4] != null) || (parrValues[5] != null) || (parrValues[6] != null) )
		{
			lobjResult.address = new Address();
			lobjResult.address.street1 = (String)parrValues[4];
			lobjResult.address.street2 = (String)parrValues[5];
			if ( parrValues[6] != null )
			{
				lobjResult.address.zipCode = new ZipCode();
				lobjResult.address.zipCode.code = (String)parrValues[7];
				lobjResult.address.zipCode.city = (String)parrValues[8];
				lobjResult.address.zipCode.county = (String)parrValues[9];
				lobjResult.address.zipCode.district = (String)parrValues[10];
				lobjResult.address.zipCode.country = (String)parrValues[11];
			}
			else
				lobjResult.address.zipCode = null;
		}
		else
			lobjResult.address = null;
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		QuoteRequestObjectSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof QuoteRequestObjectSearchParameter) )
				continue;
			lParam = (QuoteRequestObjectSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Name])");
		}

		return lbFound;
	}
}
