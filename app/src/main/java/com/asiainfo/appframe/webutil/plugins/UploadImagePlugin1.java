package com.asiainfo.appframe.webutil.plugins;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.WebView;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.net.PortalClient;
import com.asiainfo.appframe.utils.BitmapUtil;
import com.asiainfo.appframe.utils.Log;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;
import com.asiainfo.appframe.data.Constants;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UploadImagePlugin1 extends BasePlugin {

	/** 相机 */
    public static final int CAMERA = 0;
    /** 图片库 */
    public static final int PHOTOLIBRARY = 1;
    /** 图片库 */
    public static final int SAVEDPHOTOALBUM = 2;
    /** 成功回调 */
    private String mStrSuccess;
    /** 失败回调 */
    private String mStrFail;
    /** 系统ID */
    private String systemId = null;
    /** 图片获取方式 */
    private int srcType;
    /** 默认图片上传地址 */
    private String url = "http://61.160.128.44/ecs_imageserver/imageserver/uploadPicture.do";
    /** 相机拍照图片存储地址 */
    private String cameraPath;
    /** 调取相机和图片库的请求码 */
    private static final int ACTION_CAMERA = 7000;
    /** 调取相机和图片库的请求码（BASE64） **/
    private static final int ACTION_CAMERA_BASE64 = 7001;
    /** 压缩后的图片 */
    private Bitmap uploadImageBitmap;
    /** 上传到服务器的图片本地地址 */
    private String uploadFilePath;
	
	public UploadImagePlugin1(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String action, JSONArray args) {
		// TODO Auto-generated method stub
		if ("Upload".equals(action)) {
			 String success = args.optString(0);
             String fail = args.optString(1);
            try {
                String nullString = args.optString(2);//扩展字段目前为null
                String sourceType = args.optString(3);
                String url = args.getString(4) != null ? args.getString(4) : "";
                String massScale = args.optString(6);			//质量压缩比例
                String vectorScale = args.optString(5);			//矢量压缩
                url = URLDecoder.decode(url, "UTF-8");
                Upload(success, fail, sourceType, url, massScale, vectorScale);
//                Upload(success, fail, systemId, sourceType, url);
            } catch (Exception e) {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", e.getMessage());
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
            }
        }else if("GetBase64EncodeImage".equals(action)){		//将图片转为base编码，直接返回
        	String success = args.optString(0);
            String fail = args.optString(1);
        	try {
                
                String sourceType = args.optString(2);
                String massScale = args.optString(3);			//质量压缩比例
                String vectorScale = args.optString(4);			//矢量压缩
                UploadBase64(success, fail, sourceType, massScale, vectorScale);
//	                Upload(success, fail, systemId, sourceType, url);
            } catch (Exception e) {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", e.getMessage());
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
                e.printStackTrace();
            }
        }
	}
	
	@Override
	public void execute(String action, JSONArray args, String type) {
		// TODO Auto-generated method stub
		String param = "";
		if ("Upload".equals(action)) {
			 String success = args.optString(0);
             String fail = args.optString(1);
            try {
            	
            	param = URLDecoder.decode(args.optString(2), "UTF-8");
				JSONObject jsonObject = new JSONObject(param);
            	
                String sourceType = jsonObject.getString("sourceType");
                String url = jsonObject.getString("uploadPath");
//                String url = args.getString(4) != null ? args.getString(4) : "";
                String massScale = jsonObject.getString("quality");			//质量压缩比例
                String vectorScale = jsonObject.getString("width");			//矢量压缩
                url = URLDecoder.decode(url, "UTF-8");
                Upload(success, fail, sourceType, url, massScale, vectorScale);
//                Upload(success, fail, systemId, sourceType, url);
            } catch (Exception e) {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", e.getMessage());
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
            }
        }else if("GetBase64EncodeImage".equals(action)){		//将图片转为base编码，直接返回
        	String success = args.optString(0);
            String fail = args.optString(1);
        	try {
                
        		param = URLDecoder.decode(args.optString(2), "UTF-8");
				JSONObject jsonObject = new JSONObject(param);
        		
                String sourceType = jsonObject.getString("sourceType");
                String massScale = jsonObject.getString("quality");			//质量压缩比例
                String vectorScale = jsonObject.getString("width");			//矢量压缩
                UploadBase64(success, fail, sourceType, massScale, vectorScale);
//	                Upload(success, fail, systemId, sourceType, url);
            } catch (Exception e) {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", e.getMessage());
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
                e.printStackTrace();
            }
        }
	}
	
	/**
     * 图片上传处理入口
     * 
     * @param success
     * @param fail
     * @param systemId
     * @param sourceType
     * @param url
     */
    public void Upload(final String success, final String fail,
            String systemId, String sourceType, String url) {
        this.mStrSuccess = success;
        this.mStrFail = fail;
        this.systemId = systemId;
        this.srcType = Integer.parseInt(sourceType);
        if (!StringUtil.isEmpty(url)) {
            this.url = url;
        }
        if (StringUtil.isEmpty(systemId)) {
            mPluginManager.callBack("systemId不能为空", this.mStrFail);
        } else {
            try {
                if (srcType == CAMERA || srcType == PHOTOLIBRARY) {
                    takePicture();
                } else {
                	JSONObject resultObj = new JSONObject();
        			try {
        				resultObj.put("resultCode", 0);
        				resultObj.put("resultMsg", "动作类型参数错误");
        			} catch (JSONException e0) {
        				e0.printStackTrace();
        			}
        			callback(resultObj.toString(), fail);
                }
            } catch (IllegalArgumentException e) {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "Illegal Argument Exception");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
            }
        }
    }
    
    /**
     * 图片上传处理入口，之前对图片进行压缩
     * @param success
     * @param fail
     * @param sourceType
     * @param url
     * @param massScale
     * @param vectorScale
     */
    public void Upload(String success, String fail, String sourceType, String url, String massScale, String vectorScale){
    	this.mStrSuccess = success;
        this.mStrFail = fail;
        this.srcType = Integer.parseInt(sourceType);
        this.massScale = (int)(Float.parseFloat(massScale) * 100);
    	this.widthScale = Integer.parseInt(vectorScale);
    	
        if (!StringUtil.isEmpty(url)) {
            this.url = url;
        }
        try {
            if (srcType == CAMERA || srcType == PHOTOLIBRARY) {
                takePicture();
            } else {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "动作类型参数错误");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
            }
        } catch (IllegalArgumentException e) {
        	JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "Illegal Argument Exception");
			} catch (JSONException e0) {
				e0.printStackTrace();
			}
			callback(resultObj.toString(), fail);
        }
    }
    
    /**
     * 调用相机或者图片选择器获取图片
     */
    public void takePicture() {
        // 拍照
        if (srcType == this.CAMERA) {
            File dir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), Constants.PHOTO_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            date = new Date();
            cameraPath = dir + "/" + format.format(date) + ".jpg";
            File picFile = new File(dir, format.format(date) + ".jpg");
            Uri uri = Uri.fromFile(picFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            mECInterface.startActivityForResult(this, intent, ACTION_CAMERA);
            // 相册获取
        } else if (srcType == this.PHOTOLIBRARY) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mECInterface.startActivityForResult(this, intent, ACTION_CAMERA);
        }
    }

    /**
     * 调用相机或者图片选择器获取图片 BASE64编码
     */
    public void takePictureBase64() {
        // 拍照
        if (srcType == this.CAMERA) {
            File dir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), Constants.PHOTO_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            date = new Date();
            cameraPath = dir + "/" + format.format(date) + ".jpg";
            File picFile = new File(dir, format.format(date) + ".jpg");
            Uri uri = Uri.fromFile(picFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            mECInterface.startActivityForResult(this, intent, ACTION_CAMERA_BASE64);
            // 相册获取
        } else if (srcType == this.PHOTOLIBRARY) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mECInterface.startActivityForResult(this, intent, ACTION_CAMERA_BASE64);
        }
    }

    /**
     * 调用相机或者图片选择器获取图片回调
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    	if(resultCode == Activity.RESULT_OK){
    		switch (requestCode) {
			case ACTION_CAMERA:				//获取图片并上传至服务器
				takePickCallBack(data);
				break;
			case ACTION_CAMERA_BASE64:		//获取图片并进行BASE64编码，直接返回
				takePickCallBack_base64(data);
				break;
			default:
				break;
			}
    	} else if(resultCode == Activity.RESULT_CANCELED){
    		JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "Camera cancelled.");
			} catch (JSONException e0) {
				e0.printStackTrace();
			}
			callback(resultObj.toString(), this.mStrFail);
    	} else {
    		JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "Did not complete!");
			} catch (JSONException e0) {
				e0.printStackTrace();
			}
			callback(resultObj.toString(), this.mStrFail);
    	}
    	
    	/*if (requestCode == ACTION_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                takePickCallBack(data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mPluginManager.callBack("Camera cancelled.", this.mStrFail);
            }else {
                mPluginManager.callBack("Did not complete!", this.mStrFail);
            }
        }*/
    }

    /**
     * 图片的压缩处理
     * 
     * @param data
     */
    private void takePickCallBack(Intent data) {
        if (srcType == this.CAMERA) {
            try {
            	
            	// 获取图片宽高
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(cameraPath, options);
                int outwidth = options.outWidth;
                int outheitht = options.outHeight;
                float scaleWidth = (float)widthScale / outwidth;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);
                //尺寸压缩
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
                uploadImageBitmap = BitmapFactory.decodeFile(cameraPath, opts);
                uploadImageBitmap = Bitmap.createBitmap(uploadImageBitmap, 0, 0, outwidth, outheitht, matrix, true);
                uploadImageBitmap = compressBitmap(uploadImageBitmap);
            	
                // 获取图片压缩比例
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(cameraPath, options);
//                int scaleSize = BitmapUtil.computeSampleSize(options,1280 * 720);
//                // 压缩图片
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inPurgeable = true;
//                opts.inSampleSize = scaleSize;
//                opts.inInputShareable = true;
//                uploadImageBitmap = BitmapFactory.decodeFile(cameraPath, opts);
            } catch (Exception e) {
                if (new File(cameraPath).exists())
                    new File(cameraPath).delete();
                e.printStackTrace();
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "图片处理失败");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), this.mStrFail);
            }
        } else {
            Uri uri = data.getData();
            try {
            	

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                ContentResolver resolver = mPluginManager.getContext()
                        .getContentResolver();
                BitmapFactory.decodeStream(resolver.openInputStream(uri), null,
                        options);
                int outwidth = options.outWidth;
                int outheitht = options.outHeight;
                float scaleWidth = (float)widthScale / outwidth;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);
                // 压缩图片
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                uploadImageBitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opts);
                uploadImageBitmap = Bitmap.createBitmap(uploadImageBitmap, 0, 0, outwidth, outheitht, matrix, true);
                uploadImageBitmap = compressBitmap(uploadImageBitmap);
            	
                // 获取图片压缩比例
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                ContentResolver resolver = mPluginManager.getContext()
//                        .getContentResolver();
//                BitmapFactory.decodeStream(resolver.openInputStream(uri), null,
//                        options);
//                int scaleSize = BitmapUtil.computeSampleSize(options,
//                        1280 * 720);
//                // 压缩图片
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inPurgeable = true;
//                opts.inSampleSize = scaleSize;
//                opts.inInputShareable = true;
//                uploadImageBitmap = BitmapFactory.decodeStream(
//                        resolver.openInputStream(uri), null, opts);
            } catch (Exception e) {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "图片处理失败");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), this.mStrFail);
            }

        }
        try {
            uploadImage();
        } catch (Exception e) {
            e.printStackTrace();
            
            JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "图片上传失败");
			} catch (JSONException e0) {
				e0.printStackTrace();
			}
			callback(resultObj.toString(), this.mStrFail);
        }
    }
    
    int massScale; 		//质量压缩比例
    int widthScale;	//所需图片宽度
    
    /**
     * 图片进行压缩，并转换为base64编码直接返回
     * @param success
     * @param fail
     * @param sourceType
     * @param massScale
     * @param vectorScale
     */
    public void UploadBase64(String success, String fail, String sourceType, String massScale, String widthScale){
    	this.mStrSuccess = success;
    	this.mStrFail = fail;
    	this.srcType = Integer.parseInt(sourceType);
    	this.massScale = (int)(Float.parseFloat(massScale) * 100);
    	this.widthScale = Integer.parseInt(widthScale);
    	try {
            if (srcType == CAMERA || srcType == PHOTOLIBRARY) {
                takePictureBase64();
            } else {
            	JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "动作类型参数错误");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), this.mStrFail);
            }
        } catch (IllegalArgumentException e) {
        	JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "Illegal Argument Exception");
			} catch (JSONException e0) {
				e0.printStackTrace();
			}
			callback(resultObj.toString(), this.mStrFail);
        }
    }
    
    /**
     * 图片的压缩处理 base64
     * 
     * @param data
     */
    private void takePickCallBack_base64(Intent data) {
        if (srcType == this.CAMERA) {
            try {
                // 获取图片宽高
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(cameraPath, options);
                int outwidth = options.outWidth;
                int outheitht = options.outHeight;
                float scaleWidth = (float)widthScale / outwidth;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);
                //尺寸压缩
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
                uploadImageBitmap = BitmapFactory.decodeFile(cameraPath, opts);
                uploadImageBitmap = Bitmap.createBitmap(uploadImageBitmap, 0, 0, outwidth, outheitht, matrix, true);
                uploadImageBitmap = compressBitmap(uploadImageBitmap);
                
                String str = "{" + 
                			"\"resultCode\":" +
                			1 + "," +
                			"\"resultMsg\":" + 
                			"\"\"" + "," +
                			"\"object\":" + 
                			"{\"imageBase64\":" + "\"" + 
                			bitmapToBase64(uploadImageBitmap) + "\"" +
                			"}" + 
                			"}";
                
//        		JSONObject object = new JSONObject();
//                try {
//                	object.put("imageBase64", bitmapToBase64(uploadImageBitmap));
//        		} catch (JSONException e0) {
//        			e0.printStackTrace();
//        		}
//                
//                JSONObject resultObj = new JSONObject();
//        		try {
//        			resultObj.put("resultCode", 1);
//        			resultObj.put("resultMsg", "");
//        			resultObj.put("object", object.toString());
//        		} catch (JSONException e0) {
//        			e0.printStackTrace();
//        		}
//        		System.out.println(resultObj.toString());
//        		callback(resultObj.toString(), mStrSuccess);
        		callback(str, mStrSuccess);
            } catch (Exception e) {
                if (new File(cameraPath).exists())
                    new File(cameraPath).delete();
                e.printStackTrace();
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "图片处理失败");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), this.mStrFail);
            }
        } else {
            Uri uri = data.getData();
            try {
                // 获取图片压缩比例
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                ContentResolver resolver = mPluginManager.getContext()
                        .getContentResolver();
                BitmapFactory.decodeStream(resolver.openInputStream(uri), null,
                        options);
                int outwidth = options.outWidth;
                int outheitht = options.outHeight;
                float scaleWidth = (float)widthScale / outwidth;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);
                // 压缩图片
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                uploadImageBitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opts);
                uploadImageBitmap = Bitmap.createBitmap(uploadImageBitmap, 0, 0, outwidth, outheitht, matrix, true);
                uploadImageBitmap = compressBitmap(uploadImageBitmap);
                
                
                String str = "{" + 
            			"\"resultCode\":" +
            			1 + "," +
            			"\"resultMsg\":" + 
            			"\"\"" + "," +
            			"\"object\":" + 
            			"{\"imageBase64\":" + "\"" + 
            			bitmapToBase64(uploadImageBitmap) + "\"" +
            			"}" + 
            			"}";
                
//                JSONObject object = new JSONObject();
//                try {
//                	object.put("imageBase64", bitmapToBase64(uploadImageBitmap));
//        		} catch (JSONException e0) {
//        			e0.printStackTrace();
//        		}
//                
//                JSONObject resultObj = new JSONObject();
//        		try {
//        			resultObj.put("resultCode", 1);
//        			resultObj.put("resultMsg", "");
//        			resultObj.put("object", object.toString());
//        		} catch (JSONException e0) {
//        			e0.printStackTrace();
//        		}
////        		System.out.println(resultObj.toString());
//        		callback(resultObj.toString(), mStrSuccess);
        		callback(str, mStrSuccess);
            } catch (Exception e) {
            	 JSONObject resultObj = new JSONObject();
     			try {
     				resultObj.put("resultCode", 0);
     				resultObj.put("resultMsg", "图片处理失败");
     			} catch (JSONException e0) {
     				e0.printStackTrace();
     			}
     			callback(resultObj.toString(), this.mStrFail);
            }

        }
    }
    
    /** 
     * 质量压缩方法 
     * 
     * @param image 
     * @return 
     */  
    public Bitmap compressBitmap(Bitmap image) {
      
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, massScale, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中    
      
//        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩  
//            baos.reset(); // 重置baos即清空baos  
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中  
//            options -= 10;// 每次都减少10  
//        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片  
        return bitmap;
    }
    
    /** 
     * bitmap转为base64 
     * @param bitmap 
     * @return 
     */  
    public static String bitmapToBase64(Bitmap bitmap) {  
      
        String result = null;  
        ByteArrayOutputStream baos = null;  
        try {  
            if (bitmap != null) {  
                baos = new ByteArrayOutputStream();  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
      
                baos.flush();  
                baos.close();
      
                byte[] bitmapBytes = baos.toByteArray();  
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (baos != null) {  
                    baos.flush();  
                    baos.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  

    /**
     * 本地做成压缩后的图片
     * 
     * @throws Exception
     */
    private void uploadImage() throws Exception {
        if (uploadImageBitmap != null) {
            ByteArrayOutputStream baos = null;
            FileOutputStream fileOutputStream = null;
            // 存储图片
            try {
                baos = new ByteArrayOutputStream();
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath(), Constants.PHOTO_DIR);
                if(!dir.exists()){
                	dir.mkdirs();
                }
                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                date = new Date();
                File photo = new File(dir, format.format(date) + ".jpg");
                uploadFilePath = photo.getPath();
                
                uploadImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        baos);
                fileOutputStream = new FileOutputStream(photo);
                baos.writeTo(fileOutputStream);
                baos.flush();
                upload();
            } catch (Exception e) {
                if (!StringUtil.isEmpty(cameraPath)
                        && new File(cameraPath).exists())
                    new File(cameraPath).delete();
                Log.e("CameraPanelActivity.onClick", "IO异常", e);
                throw e;
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    /**
     * 图片上传到服务器
     * 
     * @throws FileNotFoundException
     */
    private void upload() throws FileNotFoundException {
        RequestParams requestParams = new RequestParams();
        File file = new File(uploadFilePath);
        try {
            requestParams.put("uploadfile", file);
        } catch (FileNotFoundException e) {
            if (file.exists())
                file.delete();
            if (!StringUtil.isEmpty(cameraPath)
                    && new File(cameraPath).exists())
                new File(cameraPath).delete();
            e.printStackTrace();
            throw e;
        }

        final ProgressDialog pdDialog = ProgressDialog.show(
                mPluginManager.getContext(), "", "正在上传中，请稍后", true, true);
        pdDialog.setCanceledOnTouchOutside(false);

        // 设置返回，取消对话框监听，中断后台网络请求
        pdDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                PortalClient.client.cancelRequests(mPluginManager.getContext(),
                        true);
                if (new File(uploadFilePath).exists())
                    new File(uploadFilePath).delete();
                if (!StringUtil.isEmpty(cameraPath)
                        && new File(cameraPath).exists())
                    new File(cameraPath).delete();
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "图片上传取消");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), mStrFail);
            }
        });
        PortalClient.post(mPluginManager.getContext(), url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    byte[] responseBody) {
                if (pdDialog != null && pdDialog.isShowing()) {
                    pdDialog.dismiss();
                }
                if (new File(uploadFilePath).exists())
                    new File(uploadFilePath).delete();
                if (!StringUtil.isEmpty(cameraPath)
                        && new File(cameraPath).exists())
                    new File(cameraPath).delete();
                
                JSONObject object = new JSONObject();
                try {
                	object.put("name", uploadFilePath);
                	object.put("localUrl", uploadFilePath);
                	object.put("remoteUrl", uploadFilePath);
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
                
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 1);
    				resultObj.put("resultMsg", "");
    				resultObj.put("object", object);
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), mStrSuccess);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                    byte[] responseBody, Throwable error) {
                if (pdDialog != null && pdDialog.isShowing()) {
                    pdDialog.dismiss();
                }
                if (new File(uploadFilePath).exists())
                    new File(uploadFilePath).delete();
                if (!StringUtil.isEmpty(cameraPath)
                        && new File(cameraPath).exists())
                    new File(cameraPath).delete();
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", "图片上传失败");
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), mStrFail);
            }

        });
    }
    
	@Override
	public void callback(String result, String type) {
		// TODO Auto-generated method stub
		mPluginManager.callBack(result, type);
	}

}