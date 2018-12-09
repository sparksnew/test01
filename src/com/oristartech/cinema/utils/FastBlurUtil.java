package com.oristartech.cinema.utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.jhlabs.image.GaussianFilter;

public class FastBlurUtil {
	 public static byte[] blur(byte[] data,String formatName) throws IOException {
	    	ByteArrayInputStream bais = new ByteArrayInputStream(data);
	    	BufferedImage img = ImageIO.read(bais);
	    	GaussianFilter gaussianFilter = new GaussianFilter();
	    	gaussianFilter.setRadius(30);
	    	gaussianFilter.filter(img, img);
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	ImageIO.write(img, formatName, baos);
	    	return baos.toByteArray();
	    }
    public static byte[] blurFile(File file) throws IOException {
    	FileInputStream ips = new FileInputStream(file); 
    	BufferedImage img = ImageIO.read(ips);
    	GaussianFilter gaussianFilter = new GaussianFilter();
    	gaussianFilter.setRadius(30);
    	gaussianFilter.filter(img, img);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(img, "jpg", baos);
    	ips.close();
    	ips=null;
    	return baos.toByteArray();
    }
	public static void main(String[] args) {
		String filepath1 = "D:\\img5";
		File file1 = new File(filepath1);
		File[] listFiles = file1.listFiles();
		for (File file2 : listFiles) {
			String filename = file2.getName();
			String filepath = filepath1+"\\"+filename;
			String newFilePath = filepath1+"\\"+filename.substring(0, filename.indexOf("."))+"_shadow.jpg";
			FileInputStream ips = null; 
			FileOutputStream ops = null;
			try{
				File file=new File(filepath);
				File blurfile=new File(newFilePath);
				ips=new FileInputStream(file);
				byte[] buf =new byte[ips.available()];
				ips.read(buf);
				byte[] blurimg=blurFile(file);
				ops=new FileOutputStream(blurfile);
				ops.write(blurimg);
				ops.flush();
				ops.close();
				ips.close();
			}catch (IOException e){ 
				e.printStackTrace();
			}finally {
			}
		}
			
		
		/*//String filepath="D:\\aaa\\b1.jpg";
		FileInputStream ips = null; 
		FileOutputStream ops = null; 
		try{
			
			File file=new File(filepath);
			//String formatName=filepath.substring(filepath.lastIndexOf(".")+1);
			String newFilePath=filepath.substring(0,filepath.lastIndexOf("."))+"_b"+filepath.substring(filepath.lastIndexOf("."));
			File blurfile=new File(newFilePath);
			ips=new FileInputStream(file);
			byte[] buf =new byte[ips.available()];
			ips.read(buf);
			//byte[] blurimg=blur(buf,formatName);
			byte[] blurimg=blurFile(file);
			ops=new FileOutputStream(blurfile);
			ops.write(blurimg);
			ops.flush();
			ops.close();
			ips.close();


		}catch (IOException e){ 
			e.printStackTrace();
		}finally {

			
		}*/

	}

}
