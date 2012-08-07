package com.premiumminds.BigBang.Jewel.Objects;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileData;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

public class FileProcesser
	extends ObjectBase
{
    public static FileProcesser GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (FileProcesser)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileProcesser), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileProcesser GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (FileProcesser)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileProcesser), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	FileIOBase mobjProcesser;

	public void Initialize()
		throws JewelEngineException
	{
	    Class<?> lrefClass;
	    Constructor<?> lrefConst;

		try
		{
			lrefClass = Class.forName(((String)getAt(1)).replaceAll("MADDS", "Jewel"));
			lrefConst = lrefClass.getConstructor(new Class<?>[] {});
			mobjProcesser = (FileIOBase)lrefConst.newInstance();
		}
		catch (Throwable e)
		{
	    	throw new JewelEngineException(e.getMessage(), e);
		}
	}

	public void Process(FileData pobjData)
		throws BigBangJewelException
	{
		if ( mobjProcesser == null )
			throw new BigBangJewelException("Erro inesperado a inicializar o processador de ficheiros.");

		mobjProcesser.Parse(pobjData);
	}
}
