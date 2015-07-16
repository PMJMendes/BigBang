package com.premiumminds.BigBang.Jewel.SysObjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Template;

public class ImageHelper
{
	public static BufferedImage cropAndStamp(BufferedImage pobjInput, int plngX, int plngY, int plngW, int plngH)
		throws BigBangJewelException
	{
		BufferedImage lobjOutput;
		Graphics2D lobjGraphics;
		Template lobjStamp;
		FileXfer lobjFile;
		ByteArrayInputStream lstreamInput;
		BufferedImage lobjStampImg;

		lobjOutput = new BufferedImage(pobjInput.getColorModel(), pobjInput.copyData(null),
				pobjInput.getColorModel().isAlphaPremultiplied(), null);

		lobjGraphics = lobjOutput.createGraphics();
		lobjGraphics.clearRect(0, 0, pobjInput.getWidth(), pobjInput.getHeight());

		lobjGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		lobjGraphics.drawImage(pobjInput, 0, 0, null);

		lobjGraphics.setBackground(Color.WHITE);
		lobjGraphics.clearRect(plngX, plngY, plngW, plngH);

		lobjStamp = Template.GetInstance(Engine.getCurrentNameSpace(), Constants.TID_Stamp);
		lobjFile = lobjStamp.getFile();

		if ( lobjFile != null )
		{
			lstreamInput = new ByteArrayInputStream(lobjFile.getData());
			try
			{
				lobjStampImg = ImageIO.read(lstreamInput);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			lobjGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			lobjGraphics.drawImage(lobjStampImg, (pobjInput.getWidth() - lobjStampImg.getWidth())/2,
					(pobjInput.getHeight() - lobjStampImg.getHeight())/2, null);
		}

		lobjGraphics.dispose();

		return lobjOutput;
	}
}
