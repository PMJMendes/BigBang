package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class QuoteRequestObjectData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrName;
	public UUID midOwner;
	public UUID midType;
	public String mstrAddress1;
	public String mstrAddress2;
	public UUID midZipCode;

	public String mstrFiscalI;
	public UUID midSex;
	public Timestamp mdtDateOfBirth;
	public Integer mlngClientNumberI;
	public String mstrInsurerIDI;

	public String mstrFiscalC;
	public UUID midPredomCAE;
	public UUID midGrievousCAE;
	public String mstrActivityNotes;
	public String mstrProductNotes;
	public UUID midSales;
	public String mstrEUEntity;
	public Integer mlngClientNumberC;

	public String mstrMakeAndModel;
	public String mstrEquipmentNotes;
	public Timestamp mdtFirstRegistry;
	public Integer mlngManufactureYear;
	public String mstrClientIDE;
	public String mstrInsurerIDE;

	public String mstrSiteNotes;

	public String mstrSpecies;
	public String mstrRace;
	public Integer mlngAge;
	public String mstrCityNumber;
	public String mstrElectronicIDTag;

	public boolean mbNew;
	public boolean mbDeleted;

	public QuoteRequestObjectData mobjPrevValues;

	public void Clone(QuoteRequestObjectData pobjSource)
	{
		mid = pobjSource.mid;
		mstrName = pobjSource.mstrName;
		midOwner = pobjSource.midOwner;
		midType = pobjSource.midType;
		mstrAddress1 = pobjSource.mstrAddress1;
		mstrAddress2 = pobjSource.mstrAddress2;
		midZipCode = pobjSource.midZipCode;
		mstrFiscalI = pobjSource.mstrFiscalI;
		midSex = pobjSource.midSex;
		mdtDateOfBirth = pobjSource.mdtDateOfBirth;
		mlngClientNumberI = pobjSource.mlngClientNumberI;
		mstrInsurerIDI = pobjSource.mstrInsurerIDI;
		mstrFiscalC = pobjSource.mstrFiscalC;
		midPredomCAE = pobjSource.midPredomCAE;
		midGrievousCAE = pobjSource.midGrievousCAE;
		mstrActivityNotes = pobjSource.mstrActivityNotes;
		mstrProductNotes = pobjSource.mstrProductNotes;
		midSales = pobjSource.midSales;
		mstrEUEntity = pobjSource.mstrEUEntity;
		mlngClientNumberC = pobjSource.mlngClientNumberC;
		mstrMakeAndModel = pobjSource.mstrMakeAndModel;
		mstrEquipmentNotes = pobjSource.mstrEquipmentNotes;
		mdtFirstRegistry = pobjSource.mdtFirstRegistry;
		mlngManufactureYear = pobjSource.mlngManufactureYear;
		mstrClientIDE = pobjSource.mstrClientIDE;
		mstrInsurerIDE = pobjSource.mstrInsurerIDE;
		mstrSiteNotes = pobjSource.mstrSiteNotes;
		mstrSpecies = pobjSource.mstrSpecies;
		mstrRace = pobjSource.mstrRace;
		mlngAge = pobjSource.mlngAge;
		mstrCityNumber = pobjSource.mstrCityNumber;
		mstrElectronicIDTag = pobjSource.mstrElectronicIDTag;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrName = (String)pobjSource.getAt(0);
		midOwner = (UUID)pobjSource.getAt(1);
		midType = (UUID)pobjSource.getAt(2);
		mstrAddress1 = (String)pobjSource.getAt(3);
		mstrAddress2 = (String)pobjSource.getAt(4);
		midZipCode = (UUID)pobjSource.getAt(5);
		mstrFiscalI = (String)pobjSource.getAt(6);
		midSex = (UUID)pobjSource.getAt(7);
		mdtDateOfBirth = (Timestamp)pobjSource.getAt(8);
		mlngClientNumberI = (Integer)pobjSource.getAt(9);
		mstrInsurerIDI = (String)pobjSource.getAt(10);
		mstrFiscalC = (String)pobjSource.getAt(11);
		midPredomCAE = (UUID)pobjSource.getAt(12);
		midGrievousCAE = (UUID)pobjSource.getAt(13);
		mstrActivityNotes = (String)pobjSource.getAt(14);
		mstrProductNotes = (String)pobjSource.getAt(15);
		midSales = (UUID)pobjSource.getAt(16);
		mstrEUEntity = (String)pobjSource.getAt(17);
		mlngClientNumberC = (Integer)pobjSource.getAt(18);
		mstrMakeAndModel = (String)pobjSource.getAt(19);
		mstrEquipmentNotes = (String)pobjSource.getAt(20);
		mdtFirstRegistry = (Timestamp)pobjSource.getAt(21);
		mlngManufactureYear = (Integer)pobjSource.getAt(22);
		mstrClientIDE = (String)pobjSource.getAt(23);
		mstrInsurerIDE = (String)pobjSource.getAt(24);
		mstrSiteNotes = (String)pobjSource.getAt(25);
		mstrSpecies = (String)pobjSource.getAt(26);
		mstrRace = (String)pobjSource.getAt(27);
		mlngAge = (Integer)pobjSource.getAt(28);
		mstrCityNumber = (String)pobjSource.getAt(29);
		mstrElectronicIDTag = (String)pobjSource.getAt(30);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt( 0, mstrName);
			pobjDest.setAt( 1, midOwner);
			pobjDest.setAt( 2, midType);
			pobjDest.setAt( 3, mstrAddress1);
			pobjDest.setAt( 4, mstrAddress2);
			pobjDest.setAt( 5, midZipCode);
			pobjDest.setAt( 6, mstrFiscalI);
			pobjDest.setAt( 7, midSex);
			pobjDest.setAt( 8, mdtDateOfBirth);
			pobjDest.setAt( 9, mlngClientNumberI);
			pobjDest.setAt(10, mstrInsurerIDI);
			pobjDest.setAt(11, mstrFiscalC);
			pobjDest.setAt(12, midPredomCAE);
			pobjDest.setAt(13, midGrievousCAE);
			pobjDest.setAt(14, mstrActivityNotes);
			pobjDest.setAt(15, mstrProductNotes);
			pobjDest.setAt(16, midSales);
			pobjDest.setAt(17, mstrEUEntity);
			pobjDest.setAt(18, mlngClientNumberC);
			pobjDest.setAt(19, mstrMakeAndModel);
			pobjDest.setAt(20, mstrEquipmentNotes);
			pobjDest.setAt(21, mdtFirstRegistry);
			pobjDest.setAt(22, mlngManufactureYear);
			pobjDest.setAt(23, mstrClientIDE);
			pobjDest.setAt(24, mstrInsurerIDE);
			pobjDest.setAt(25, mstrSiteNotes);
			pobjDest.setAt(26, mstrSpecies);
			pobjDest.setAt(27, mstrRace);
			pobjDest.setAt(28, mlngAge);
			pobjDest.setAt(29, mstrCityNumber);
			pobjDest.setAt(30, mstrElectronicIDTag);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		pstrBuilder.append("Identificação: ").append(mstrName).append(pstrLineBreak);
		pstrBuilder.append("Morada de Risco:");
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		if ( mstrAddress1 != null )
			pstrBuilder.append(mstrAddress1);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		if ( mstrAddress2 != null )
			pstrBuilder.append(mstrAddress2);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		if ( midZipCode != null )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						midZipCode);
				pstrBuilder.append((String)lobjAux.getAt(0));
				pstrBuilder.append(" ");
				pstrBuilder.append((String)lobjAux.getAt(1));
				pstrBuilder.append(pstrLineBreak);
				pstrBuilder.append("- ");
	        	pstrBuilder.append((String)lobjAux.getAt(4));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o código postal.)");
			}
		}
		pstrBuilder.append(pstrLineBreak);

		if ( Constants.ObjTypeID_Person.equals(midType) )
		{
			if ( mstrFiscalI != null )
				pstrBuilder.append("NIF: ").append(mstrFiscalI).append(pstrLineBreak);
			if ( midSex != null )
			{
				pstrBuilder.append("Sexo: ");
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Sex),
							midSex);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o sexo do cliente.)");
				}
				pstrBuilder.append(pstrLineBreak);
			}
			if ( mdtDateOfBirth != null )
				pstrBuilder.append("Data de Nascimento: ").append(mdtDateOfBirth.toString().substring(0, 10)).append(pstrLineBreak);
			if ( mlngClientNumberI != null )
				pstrBuilder.append("Número Interno de Cliente: ").append(mlngClientNumberI).append(pstrLineBreak);
			if ( mstrInsurerIDI != null )
				pstrBuilder.append("Identificação na Seguradora: ").append(mstrInsurerIDI).append(pstrLineBreak);
		}

		if ( Constants.ObjTypeID_Group.equals(midType) )
		{
			if ( mstrFiscalC != null )
				pstrBuilder.append("NIPC: ").append(mstrFiscalC).append(pstrLineBreak);
			if ( midPredomCAE != null )
			{
				pstrBuilder.append("CAE Predominante: ");
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CAE),
							midPredomCAE);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o CAE predominante do cliente.)");
				}
				pstrBuilder.append(pstrLineBreak);
			}
			if ( midGrievousCAE != null )
			{
				pstrBuilder.append("CAE Mais Gravoso: ");
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CAE),
							midGrievousCAE);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o CAE mais gravoso do cliente.)");
				}
				pstrBuilder.append(pstrLineBreak);
			}
			if ( mstrActivityNotes != null )
				pstrBuilder.append("Obs. Actividade: ").append(mstrActivityNotes).append(pstrLineBreak);
			if ( mstrProductNotes != null )
				pstrBuilder.append("Obs. Produtos: ").append(mstrProductNotes).append(pstrLineBreak);
			if ( midSales != null )
			{
				pstrBuilder.append("Facturação: ");
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SalesVolume),
							midSales);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter a facturação do cliente.)");
				}
				pstrBuilder.append(pstrLineBreak);
			}
			if ( mstrEUEntity != null )
				pstrBuilder.append("Entidade Responsável na UE: ").append(mstrEUEntity).append(pstrLineBreak);
			if ( mlngClientNumberC != null )
				pstrBuilder.append("Número Interno de Cliente: ").append(mlngClientNumberC).append(pstrLineBreak);
		}

		if ( Constants.ObjTypeID_Equipment.equals(midType) )
		{
			if ( mstrMakeAndModel != null )
				pstrBuilder.append("Marca e Modelo: ").append(mstrMakeAndModel).append(pstrLineBreak);
			if ( mstrEquipmentNotes != null )
				pstrBuilder.append("Notas Descritivas: ").append(mstrEquipmentNotes).append(pstrLineBreak);
			if ( mdtFirstRegistry != null )
				pstrBuilder.append("Data da Primeira Matrícula: ").append(mdtFirstRegistry.toString().substring(0, 10)).append(pstrLineBreak);
			if ( mlngManufactureYear != null )
				pstrBuilder.append("Ano de Fabrico: ").append(mlngManufactureYear).append(pstrLineBreak);
			if ( mstrClientIDE != null )
				pstrBuilder.append("Identificação no Cliente: ").append(mstrClientIDE).append(pstrLineBreak);
			if ( mstrInsurerIDE != null )
				pstrBuilder.append("Identificação na Seguradora: ").append(mstrInsurerIDE).append(pstrLineBreak);
		}

		if ( Constants.ObjTypeID_Site.equals(midType) )
		{
			if ( mstrSiteNotes != null )
				pstrBuilder.append("Notas Descritivas: ").append(mstrSiteNotes).append(pstrLineBreak);
		}

		if ( Constants.ObjTypeID_Animal.equals(midType) )
		{
			if ( mstrSpecies != null )
				pstrBuilder.append("Espécie: ").append(mstrSpecies).append(pstrLineBreak);
			if ( mstrRace != null )
				pstrBuilder.append("Raça: ").append(mstrRace).append(pstrLineBreak);
			if ( mlngAge != null )
				pstrBuilder.append("Idade: ").append(mlngAge).append(pstrLineBreak);
			if ( mstrCityNumber != null )
				pstrBuilder.append("Inscrição no Município: ").append(mstrCityNumber).append(pstrLineBreak);
			if ( mstrElectronicIDTag != null )
				pstrBuilder.append("Identificação Electrónica: ").append(mstrElectronicIDTag).append(pstrLineBreak);
		}
	}
}
