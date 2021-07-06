// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package io.qaxh.qr;

import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.annotations.UsesPermissions;

import java.lang.Throwable;
import java.math.BigInteger;
import java.util.Formatter;
import java.security.Security;
import java.security.MessageDigest;
import java.security.SecureRandom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.ErrorMessages;

import android.content.Context;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.Date;

/**
 * Provides access to QRcode functions
 *
 * @author Jose Luu
 */

class QAXH_QR_COMPONENT {
  public static final int VERSION = 501;
  public static final String VERSION_STR = "5.01";
}



@UsesPermissions(permissionNames = "android.permission.READ_EXTERNAL_STORAGE")
@DesignerComponent(version = QAXH_QR_COMPONENT.VERSION,
        description = "A component to create QR codes",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "aiwebres/qr.png")
@SimpleObject(external=true)
public class QAXH_Qr extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "QaxhQrCodeComponent";
  private Context context;
  private final Activity activity;

    /**
     * Creates a QAXH_Qr component.
     *
     * @param container container, component will be placed in
     */
    public QAXH_Qr(ComponentContainer container) {
        super(container.$form());
        context = (Context) container.$context();
        activity = (Activity) container.$context();
    }

    @SimpleFunction(description = "Return the QR extension version.")
    public String getVersion() {
      return String.valueOf(QAXH_QR_COMPONENT.VERSION_STR);
    }
  
    /**
     *Display a QRCore encoding the data passed in argument returning a URI
     *
     *@param data
     */
    @SimpleFunction(
            description = "Creates a QR code picture")
    public String QRCodeGeneratorURI(String data) {
        BitMatrix bitMatrix;
        Bitmap bitmap;
        try {
            bitMatrix  = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 512, 512);
            bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.ARGB_8888);

            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    if (bitMatrix.get(x, y)) {
                        bitmap.setPixel(x, y, Color.BLACK);
                    }
                }
            }

        } catch (WriterException e) {
            Log.w(LOG_TAG, e);
            return "Error Writer Exception "+e.getMessage();
        }
        File file = null;
        try {
          String path = Environment.getExternalStorageDirectory().toString();
          file = new File(path + "/Images/", "QRCodeQaxh.png");

          File dir = new File(path + "/Images/");
          if (!dir.exists()) {
              dir.mkdirs();
          }
          
          FileOutputStream fOut = new FileOutputStream(file);

            final int noCompression=100;// 100 means no compression, the lower you go, the stronger the compression
            if (bitmap.compress(Bitmap.CompressFormat.PNG, noCompression, fOut)) {
                fOut.close();
                return Uri.fromFile(file).toString();
            } else {
                return "Could not compress bitmap to file: "+file.getAbsolutePath();
            }
        } catch (IOException e) {
            if (file != null) {
                Log.e(LOG_TAG, "Could not copy QRcode to temp file " +
                        file.getAbsolutePath());
                file.delete();
            } else {
                Log.e(LOG_TAG, "Could not copy QRcode to temp file.");
            }
            return "IOException writing file "+e.getMessage();
        }
    }

   /**
     *Display a QRCore encoding the data passed in argument
     *
     *@param data
     */
    @SimpleFunction(
            description = "Creates a QR code picture")
    public String QRCodeGenerator(String data) throws IOException, WriterException{
        BitMatrix bitMatrix;
        Bitmap bitmap;
        try {
            bitMatrix  = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 512, 512);
            bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.ARGB_8888);

            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    if (bitMatrix.get(x, y)) {
                        bitmap.setPixel(x, y, Color.BLACK);
                    }
                }
            }

        } catch (WriterException e) {
            Log.w(LOG_TAG, e);
            throw e;
            //return "Error";
        }
        File file = null;
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            file = new File(path + "/Captures/", "QRCodeQaxh.png");

            //file = File.createTempFile("AI_Media_", ".png");
            //file.deleteOnExit();
            File dir = new File(path + "/Captures/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fOut = new FileOutputStream(file);
            // 100 means no compression, the lower you go, the stronger the compression
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)) {
                fOut.close();
                return file.getAbsolutePath();
            } else {
                return "Could not compress bitmap to file: "+file.getAbsolutePath();
            }

        } catch (IOException e) {
            if (file != null) {
                Log.e(LOG_TAG, "Could not copy QRcode to temp file " +
                        file.getAbsolutePath());
                file.delete();
            } else {
                Log.e(LOG_TAG, "Could not copy QRcode to temp file.");
            }
            throw e;
        }
        //return "Error IOException";
    }


    /**
     * Get a 128 bits random number
     *
     * @return a 128 bits random number
     */
    @SimpleFunction(
            description = "Get a 128 bits random number")
    public String getRandom128(){
        SecureRandom rng = new SecureRandom();
        byte[] randomBytes = new byte[16];

        rng.nextBytes(randomBytes);
        return bytesToHex(randomBytes);
    }

    /**
     * Get a 256 bits random number
     *
     * @return a 256 bits random number
     */
    @SimpleFunction(
            description = "Get a 256 bits random number")
    public String getRandom256(){
        SecureRandom rng = new SecureRandom();
        byte[] randomBytes = new byte[32];

        rng.nextBytes(randomBytes);
        return bytesToHex(randomBytes);
    }
  
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Convert from bytes to hexadecimal
     *
     * This function isn't visible from AppInventor.
     *
     * @param byte[] bytes, the array of bits to be translated
     * @return a string containing the transalation in hexadecimal
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

      @SimpleFunction(description = "Convert ASCII text to hexadecimal.")
    public String ASCIItoHex(String text) {
        try { return String.format("0x%x", new BigInteger(1, text.getBytes("UTF-8"))); }
        catch (java.io.UnsupportedEncodingException e) {
            return "Not possible to convert to hex with the current parameters.";
        }
    }

    /**
     * Convert hexadecimal to ASCII
     *
     * @param hex, a String encoded in hexadecimal
     * @return hex translated in ASCII
     */
    @SimpleFunction(description = "Convert from hexadecimal to ASCII")
    public String HextoASCII(String hex) {
        hex = hex.substring(2, hex.length());
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
    }

  
}
