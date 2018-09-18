package com.asiainfo.appframe.zxinga;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	private static final int OTHER =0xffffffff;
	private static final int PADDING = 4;
	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		
		int minX = widthAndHeight;
		int maxX = 0;
		int minY = widthAndHeight;
		int maxY = 0;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					if(x < minX){
						minX = x;
					}
					if(x > maxX){
						maxX = x;
					}
					if(y < minY){
						minY = y;
					}
					if(y > maxY){
						maxY = y;
					}
				}
			}
		}

		minX = minX - PADDING;
		minY = minY - PADDING;
		maxX = maxX + PADDING;
		maxY = maxY + PADDING;
		
		int newWidth = maxX - minX + 1;
		int newHeight = maxY - minY + 1;
		int[] pixels = new int[newWidth * newHeight];
		
		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				if (matrix.get(x + minX, y + minY)) {
					pixels[y* newWidth + x] = BLACK;
				}else{
					pixels[y* newWidth + x] = OTHER;
				}
			}
		}
		
		Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, newWidth, 0, 0, newWidth, newHeight);
		return bitmap;
	}
}
