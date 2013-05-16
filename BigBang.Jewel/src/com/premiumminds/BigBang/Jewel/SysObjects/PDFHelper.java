package com.premiumminds.BigBang.Jewel.SysObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class PDFHelper
{
	public static int getPageCount(PDDocument pobjDoc)
	{
		return pobjDoc.getNumberOfPages();
	}

	public static BufferedImage getPage(PDDocument pobjDoc, int plngPage)
		throws BigBangJewelException
	{
		PDPage lobjPage;
		int llngRot;
		BufferedImage lobjImage;
		AffineTransform lobjXForm;
		AffineTransformOp lobjOp;

		lobjPage = (PDPage)pobjDoc.getDocumentCatalog().getAllPages().get(plngPage);
		llngRot = lobjPage.findRotation();

		try
		{
			lobjImage = lobjPage.convertToImage(BufferedImage.TYPE_INT_ARGB, 200);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( llngRot != 0 )
		{
			lobjXForm = new AffineTransform();
			lobjXForm.translate(0.5*lobjImage.getHeight(), 0.5*lobjImage.getWidth());
			lobjXForm.quadrantRotate(llngRot/90);
			lobjXForm.translate(-0.5*lobjImage.getWidth(), -0.5*lobjImage.getHeight());
			lobjOp = new AffineTransformOp(lobjXForm, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			lobjImage = lobjOp.filter(lobjImage, null);
		}

		return lobjImage;
	}
}
