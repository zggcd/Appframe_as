/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asiainfo.appframe.zxinga;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * This object wraps the Camera service object and expects to be the only one talking to it. The
 * implementation encapsulates the steps needed to take preview-sized images, which are used for
 * both preview and decoding.
 *
 */
public final class CameraManager {

  private static final String TAG = CameraManager.class.getSimpleName();

  private static final int MIN_FRAME_WIDTH = 240;
  private static final int MIN_FRAME_HEIGHT = 240;
  private static final int MAX_FRAME_WIDTH = 480;
  private static final int MAX_FRAME_HEIGHT = 360;

  private static CameraManager cameraManager;

  static final int SDK_INT; // Later we can use Build.VERSION.SDK_INT
  static {
    int sdkInt;
    try {
      sdkInt = Integer.parseInt(Build.VERSION.SDK);
    } catch (NumberFormatException nfe) {
      // Just to be safe
      sdkInt = 10000;
    }
    SDK_INT = sdkInt;
  }

  private final Context context;
  private final CameraConfigurationManager configManager;
  private Camera camera;
  private Rect framingRect;
  private Rect framingRectInPreview;
  private boolean initialized;
  private boolean previewing;
  private final boolean useOneShotPreviewCallback;
  /**
   * Preview frames are delivered here, which we pass on to the registered handler. Make sure to
   * clear the handler so it will only receive one message.
   */
  private final PreviewCallback previewCallback;
  /** Autofocus callbacks arrive here, and are dispatched to the Handler which requested them. */
  private final AutoFocusCallback autoFocusCallback;

  /**
   * Initializes this static object with the Context of the calling Activity.
   *
   * @param context The Activity which wants to use the camera.
   */
  public static void init(Context context) {
    if (cameraManager == null) {
      cameraManager = new CameraManager(context);
    }
  }

  /**
   * Gets the CameraManager singleton instance.
   *
   * @return A reference to the CameraManager singleton.
   */
  public static CameraManager get() {
    return cameraManager;
  }

  private CameraManager(Context context) {

    this.context = context;
    this.configManager = new CameraConfigurationManager(context);

    // Camera.setOneShotPreviewCallback() has a race condition in Cupcake, so we use the older
    // Camera.setPreviewCallback() on 1.5 and earlier. For Donut and later, we need to use
    // the more efficient one shot callback, as the older one can swamp the system and cause it
    // to run out of memory. We can't use SDK_INT because it was introduced in the Donut SDK.
    //useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.CUPCAKE;
    useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3; // 3 = Cupcake

    previewCallback = new PreviewCallback(configManager, useOneShotPreviewCallback);
    autoFocusCallback = new AutoFocusCallback();
  }

  SurfaceHolder surfaceHolder;
  SurfaceView surfaceView;
  
  /**
   * Opens the camera driver and initializes the hardware parameters.
   *
   * @param holder The surface object which the camera will draw preview frames into.
   * @throws IOException Indicates the camera driver failed to open.
   */
  public void openDriver(SurfaceHolder holder, SurfaceView surfaceView) throws IOException {
    if (camera == null) {
      camera = Camera.open();
      if (camera == null) {
        throw new IOException();
      }
      camera.setPreviewDisplay(holder);
      surfaceHolder = holder;

      this.surfaceView = surfaceView;

      if (!initialized) {
        initialized = true;
        configManager.initFromCameraParameters(camera);
      }
      configManager.setDesiredCameraParameters(camera);

      //FIXME
 //     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
      //�Ƿ�ʹ��ǰ��
//      if (prefs.getBoolean(PreferencesActivity.KEY_FRONT_LIGHT, false)) {
//        FlashlightManager.enableFlashlight();
//      }
      FlashlightManager.enableFlashlight();
    }
  }

  /**
   * Closes the camera driver if still in use.
   */
  @SuppressWarnings("deprecation")
public void closeDriver() {
    if (camera != null) {
      FlashlightManager.disableFlashlight();
      camera.release();
      camera = null;
    }
  }

  /**
   * Asks the camera hardware to begin drawing preview frames to the screen.
   */
  @SuppressWarnings({ "deprecation", "deprecation", "deprecation", "deprecation", "deprecation", "deprecation", "deprecation" })
public void startPreview() {
    if (camera != null && !previewing) {
    	Camera.Parameters parameters = camera.getParameters();//获取camera的parameter实例  
        List<Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
        Rect rect = surfaceHolder.getSurfaceFrame();
        Size optionSize = getOptimalPreviewSize(sizeList, surfaceHolder.getSurfaceFrame().width(), surfaceHolder.getSurfaceFrame().height());//获取一个最为适配的camera.size
        parameters.setPreviewSize(optionSize.width,optionSize.height);//把camera.size赋值到parameters  
        
        Size pictureSize = getPropPictureSize(parameters.getSupportedPictureSizes(), optionSize.width, optionSize.height);
        
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes != null) {
            for (String mode : focusModes) {
                mode.contains("continuous-video");
                parameters.setFocusMode("continuous-video");
            }
        }
        
        parameters.setPictureSize(pictureSize.width,pictureSize.height);
        camera.setParameters(parameters);//把parameters设置给camera
        
        LayoutParams flp = (LayoutParams) surfaceView.getLayoutParams();
        flp.width = optionSize.height;
        flp.height = optionSize.width;
        surfaceView.setLayoutParams(flp);
        Log.d("optionSize", "width = " + optionSize.width + "  height = " + optionSize.height);
        Log.d("pictureSize", "width = " + pictureSize.width + "  height = " + pictureSize.height);
        camera.startPreview();//开始预览  
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上  
        previewing = true;
    }
  }
  
  private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {  
      final double ASPECT_TOLERANCE = 0.1;  
      double targetRatio = (double) w / h;  
      if (sizes == null) return null;  

      Size optimalSize = null;  
      double minDiff = Double.MAX_VALUE;  

      int targetHeight = h;  

      // Try to find an size match aspect ratio and size  
      for (Size size : sizes) {  
          double ratio = (double) Math.min(size.width, size.height)/ (float)Math.max(size.width, size.height);//(double) size.height /  size.width ;  
          if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;  
          if (Math.abs(size.height - targetHeight) < minDiff) {  
              optimalSize = size;  
              minDiff = Math.abs(size.height - targetHeight);  
          }  
      }  

      // Cannot find the one match the aspect ratio, ignore the requirement  
      if (optimalSize == null) {  
          minDiff = Double.MAX_VALUE;  
          for (Size size : sizes) {  
              if (Math.abs(size.height - targetHeight) < minDiff) {
                  optimalSize = size;  
                  minDiff = Math.abs(size.height - targetHeight);  
              }  
          }  
      }  
      return optimalSize;
  }  
 
  public  Size getPropPreviewSize(List<Size> list, float th, int minWidth){
//      Collections.sort(list, sizeComparator);   
      int i = 0;  
      for(Size s:list){  
	      if((s.width >= minWidth) && equalRate(s, th)){  
	              Log.i(TAG, "PreviewSize:w = " + s.width + ",h = " + s.height);  
	              break;  
	     }  
	     i++;  
      }  
      if(i == list.size()){  
    	  i = 0;//如果没找到，就选最小的size  
      }  
      return list.get(i);  
  }
  
  public Size getPropPictureSize(List<Size> list, int minWidth, int minHeight) {  
//      Collections.sort(list, sizeComparator);  

      int i = 0;  
      for (Size s : list) {  
          Log.i(TAG, "PreviewSize : width = " + s.width + "height = " + s.height);  
          if (s.height == minHeight && s.width == minWidth) {  
              Log.i(TAG, "PreviewSize : w = " + s.width + "h = " + s.height);  
              break;  
          }  
          i++;  
      }  
      if (i == list.size()) {  
          i = list.size() - 1;//如果没找到，就选最大的size  
      }  
      return list.get(i);  
  }
  
  public boolean equalRate(Size s, float rate){  
      float r = (float)(s.width)/(float)(s.height);  
      if(Math.abs(r - rate) <= 0.2)  
      {  
          return true;  
      }  
      else{  
          return false;  
      }  
  } 

  /**
   * Tells the camera to stop drawing preview frames.
   */
  public void stopPreview() {
    if (camera != null && previewing) {
      if (!useOneShotPreviewCallback) {
        camera.setPreviewCallback(null);
      }
      camera.stopPreview();
      previewCallback.setHandler(null, 0);
      autoFocusCallback.setHandler(null, 0);
      previewing = false;
    }
  }

  /**
   * A single preview frame will be returned to the handler supplied. The data will arrive as byte[]
   * in the message.obj field, with width and height encoded as message.arg1 and message.arg2,
   * respectively.
   *
   * @param handler The handler to send the message to.
   * @param message The what field of the message to be sent.
   */
  public void requestPreviewFrame(Handler handler, int message) {
    if (camera != null && previewing) {
      previewCallback.setHandler(handler, message);
//      if (useOneShotPreviewCallback) {
//        camera.setOneShotPreviewCallback(previewCallback);
//      } else {
        camera.setPreviewCallback(previewCallback);
//      }
    }
  }

  /**
   * Asks the camera hardware to perform an autofocus.
   *
   * @param handler The Handler to notify when the autofocus completes.
   * @param message The message to deliver.
   */
  public void requestAutoFocus(Handler handler, int message) {
    if (camera != null && previewing) {
      autoFocusCallback.setHandler(handler, message);
      //Log.d(TAG, "Requesting auto-focus callback");
      camera.autoFocus(autoFocusCallback);
    }
  }

  /**
   * Calculates the framing rect which the UI should draw to show the user where to place the
   * barcode. This target helps with alignment as well as forces the user to hold the device
   * far enough away to ensure the image will be in focus.
   *
   * @return The rectangle to draw on screen in window coordinates.
   */
  public Rect getFramingRect() {
    Point screenResolution = configManager.getScreenResolution();
    if (framingRect == null) {
      if (camera == null) {
        return null;
      }
      int width = screenResolution.x * 3 / 4;
//      if (width < MIN_FRAME_WIDTH) {
//        width = MIN_FRAME_WIDTH;
//      } else if (width > MAX_FRAME_WIDTH) {
//        width = MAX_FRAME_WIDTH;
//      }
//      int height = screenResolution.y * 3 / 4;
      int height = width;
//      if (height < MIN_FRAME_HEIGHT) {
//        height = MIN_FRAME_HEIGHT;
//      } else if (height > MAX_FRAME_HEIGHT) {
//        height = MAX_FRAME_HEIGHT;
//      }
      int leftOffset = (screenResolution.x - width) / 2;
      int topOffset = (screenResolution.y - height) / 2;
      framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
      Log.d(TAG, "Calculated framing rect: " + framingRect);
    }
    return framingRect;
  }

  /**
   * Like {@link #getFramingRect} but coordinates are in terms of the preview frame,
   * not UI / screen.
   */
  public Rect getFramingRectInPreview() {
    if (framingRectInPreview == null) {
      Rect rect = new Rect(getFramingRect());
      Point cameraResolution = configManager.getCameraResolution();
      Point screenResolution = configManager.getScreenResolution();
      //modify here
//      rect.left = rect.left * cameraResolution.x / screenResolution.x;
//      rect.right = rect.right * cameraResolution.x / screenResolution.x;
//      rect.top = rect.top * cameraResolution.y / screenResolution.y;
//      rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
      rect.left = rect.left * cameraResolution.y / screenResolution.x;
      rect.right = rect.right * cameraResolution.y / screenResolution.x;
      rect.top = rect.top * cameraResolution.x / screenResolution.y;
      rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
      framingRectInPreview = rect;
    }
    return framingRectInPreview;
  }

  /**
   * Converts the result points from still resolution coordinates to screen coordinates.
   *
   * @param points The points returned by the Reader subclass through Result.getResultPoints().
   * @return An array of Points scaled to the size of the framing rect and offset appropriately
   *         so they can be drawn in screen coordinates.
   */
  /*
  public Point[] convertResultPoints(ResultPoint[] points) {
    Rect frame = getFramingRectInPreview();
    int count = points.length;
    Point[] output = new Point[count];
    for (int x = 0; x < count; x++) {
      output[x] = new Point();
      output[x].x = frame.left + (int) (points[x].getX() + 0.5f);
      output[x].y = frame.top + (int) (points[x].getY() + 0.5f);
    }
    return output;
  }
   */

  /**
   * A factory method to build the appropriate LuminanceSource object based on the format
   * of the preview buffers, as described by Camera.Parameters.
   *
   * @param data A preview frame.
   * @param width The width of the image.
   * @param height The height of the image.
   * @return A PlanarYUVLuminanceSource instance.
   */
  public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
    Rect rect = getFramingRectInPreview();
    int previewFormat = configManager.getPreviewFormat();
    String previewFormatString = configManager.getPreviewFormatString();
    switch (previewFormat) {
      // This is the standard Android format which all devices are REQUIRED to support.
      // In theory, it's the only one we should ever care about.
      case PixelFormat.YCbCr_420_SP:
      // This format has never been seen in the wild, but is compatible as we only care
      // about the Y channel, so allow it.
      case PixelFormat.YCbCr_422_SP:
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
            rect.width(), rect.height());
      default:
        // The Samsung Moment incorrectly uses this variant instead of the 'sp' version.
        // Fortunately, it too has all the Y data up front, so we can read it.
        if ("yuv420p".equals(previewFormatString)) {
          return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
            rect.width(), rect.height());
        }
    }
    throw new IllegalArgumentException("Unsupported picture format: " +
        previewFormat + '/' + previewFormatString);
  }

	public Context getContext() {
		return context;
	}
	
	/**
	 * 获取闪光灯状态
	 */
	public boolean isLightOn() {
		if (camera != null) {
			Camera.Parameters p = camera.getParameters();
			// 关闭是off
			return "torch".equals(p.getFlashMode()) ? true : false;
		}
		return true;
	}

	/**
	 * 打开闪光灯
	 */
	public void turnLightOn() {
		if (camera != null) {
			FlashlightManager.turnLightOn(camera);
		}
	}

	/**
	 * 打开闪光灯
	 */
	public void turnLightOff() {
		if (camera != null) {
			FlashlightManager.turnLightOff(camera);
		}
	}

}
