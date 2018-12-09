/*package com.oristartech.cinema.controller;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oristartech.cinema.service.FilmInfoService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.PNGDecodeParam;

import net.sf.json.JSONObject;

@RequestMapping("/Imgs")
@Controller
public class FetchImgsController {
	@Autowired
	private FilmInfoService filmInfoService;
	@RequestMapping(value = "/ImgsDownload")
	@ResponseBody
	public void getFilmBox() throws IOException {
		// 全国累计票房、今日票房占比、首周票房
		final String ImgUrl = "http://theater.mtime.com/China_Beijing/";
		// String weekUrl =
		// "http://www.cbooo.cn/boxOffice/GetYearInfo_f?year=2017";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 7); // 控制时
		calendar.set(Calendar.MINUTE, 0); // 控制分
		calendar.set(Calendar.SECOND, 0); // 控制秒
		Date time = calendar.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer = new Timer();
		try{
			//获取请求连接
			Connection con = Jsoup.connect(ImgUrl).ignoreContentType(true);
			//遍历生成参数
	        Document doc = con.post();
	        timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					FetchImgs();
				}
			}, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@RequestMapping(value = "/FetchImgs")
	@ResponseBody
	public void FetchImgs(){
		// 利用Jsoup获得连接
		
		 * "http://maoyan.com/films"; 有问题
		 * "http://www.komovie.cn/movie"; 
		 * "http://theater.mtime.com/China_Beijing/"; 
		 * "http://www.komovie.cn/movie/coming"; 
		 * "https://movie.douban.com/cinema/nowplaying/beijing/";
		 
		String downloadUrl = "E:/img/";
		File file = new File(downloadUrl);
		file.mkdir();
		deleteFile(file);
		int height = 140;
		int width = 100;
		String ftype = "jpg";
		String url = "http://theater.mtime.com/China_Beijing/";
		//String url = "http://theater.mtime.com/China_Beijing/";
		//String url = "https://movie.douban.com/cinema/nowplaying/beijing/";
		Connection connect = Jsoup.connect(url);
		try {
			// 得到Document对象
			Document document = connect.get();
			// 查找所有img标签
			Elements imgs = document.getElementsByTag("img");
			System.out.println("共检测到下列图片URL：");
			System.out.println("开始下载");
			Map<String, String> picInfo = new HashMap<String, String>();
			Map<Object, Object> pictureInfo = new HashMap<Object, Object>();
			// 遍历img标签并获得src的属性
			for (Element element : imgs) {
				// 获取每个img标签URL "abs:"表示绝对路径
				String imgSrc = element.attr("abs:data-src");
				String[] imgUrls = imgSrc.split("@");
				String imgUrl = imgUrls[0].toString();
				String alt = element.attr("alt");
				// 打印URL
				System.out.println(imgUrl);
				// 下载图片到本地
				if (!imgUrl.equals("")) {
					Map downImages = downImages(downloadUrl, imgUrl, alt);
					picInfo.putAll(downImages);
				} else {

				}
			   if(url.contains("komovie")){
			   }else{
				   String filmName = alt;
				   if(alt.contains("/")){
					   filmName = alt.substring(0, alt.indexOf('/'));  
				   }
				   String imgSrc1 = element.attr("abs:src"); // 打印URL 
				   String[] imgUrls1 = imgSrc1.split("@"); 
				   String imgUrl1 = imgUrls1[0].toString(); 
				   System.out.println(imgUrl1);
				   //下载图片到本地 
				   if(!imgUrl1.equals("")){ 
					   if(alt.contains("《")||alt.contains("APP购票")){
						   continue;
					   }
					  File[] filelist = file.listFiles();
					  Boolean flag = true;
						for (File f : filelist) {
							String picName = (String) picInfo.get(f.getName());
							if(filmName.equals(picName.trim())){
								flag=false;
								break;
							}
						}
						if(flag){
							Map downImages = downImages(downloadUrl, imgUrl1, filmName);
							picInfo.putAll(downImages);
						}
				   }else{		  
				    }  
			   }
				String imgSrc2 = element.attr("abs:data-original");
				String[] imgUrls2 = imgSrc2.split("@");
				String imgUrl2 = imgUrls2[0].toString();
				String alt1 = element.attr("alt");
				// 打印URL
				System.out.println(imgUrl2);
				// 下载图片到本地
				if (!imgUrl2.equals("")) {
					Map downImages = downImages(downloadUrl, imgUrl2, alt1);
					picInfo.putAll(downImages);
				} else {

				}
			}
			System.out.println("下载完成");
			File out = new File("E:/img2/");
			out.mkdir();
			deleteFile(out);
			File[] filelist = file.listFiles();
			for (File f : filelist) {
				//if (f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".jpg"))) {
				if (f.isFile()) {
					String picName = f.getName();
					File p_in = new File("E:\\img\\" + picName);
					File p_out = new File("E:\\img2\\" + picName);
					uploadImage(p_in, p_out, height, width, ftype);
				}
			}
			int i = 0;
			for (File f2 : filelist) {
				if (f2.isFile() && (f2.getName().endsWith(".jpg") || f2.getName().endsWith(".jpg"))) {
					String picName = (String) picInfo.get(f2.getName());
					if(picName.contains("院线")||picName.contains("影城")||picName.contains("影院")){
						System.out.println(picName);
						continue;
					}
					String picShort = null;
					if(picName.contains("（")&&(picName.contains("数字")||picName.contains("中国巨幕")||picName.contains("IMAX"))){
					    picShort = picName.substring(0, picName.indexOf("（"));
					}else if(picName.contains("_海报")){
						picShort = picName.substring(0, picName.indexOf("_"));
					}else if(picName.contains("(20")){
						 picShort = picName.substring(0, picName.indexOf("("));
					}else{
						picShort=picName;
					}
					// String fileName = (String) picInfo.get("fileName");
					JSONObject picFile = uploadPicfile(picShort,f2.getName());
					String shortUrl = picFile.getString("shortUrl");
					pictureInfo.put("picName", picName);
					pictureInfo.put("shortUrl", shortUrl);
					//System.out.println(picShort);
					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
					List list = filmInfoService.getMovieCode(picShort);
					DataSourceContextHolder.clearDbType();
					if(list.size()>0){
						for(int k=0;k<list.size();k++){
							Map codeMap = (Map) list.get(k);
							String movieCode = (String) codeMap.get("movieCode");
							String movieUrl = (String) pictureInfo.get("shortUrl");
							//System.out.println("movieCode="+movieCode+" movieUrl="+movieUrl+movieUrl.length());
							DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
							String movieCode1 = movieCode.substring(movieCode.length()-8, movieCode.length());
							Integer imgNum = filmInfoService.searchMovieInfo(movieCode1);
							int kk = 0;
							if(imgNum>0){
								System.out.println(kk++);
							}else{
								filmInfoService.insertMovieInfo(movieCode1,movieUrl);
							}
							DataSourceContextHolder.clearDbType();
						}
					}
					
					//System.out.println(pictureInfo);
					i++;
				}
			}
			//System.out.println(pictureInfo);
			deleteFile(file);
			deleteFile(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void deleteFile(File file) {
		if (file.exists()) {// 判断文件是否存在
			if (file.isFile()) {// 判断是否是文件
				file.delete();// 删除文件
			} else if (file.isDirectory()) {// 否则如果它是一个目录
				File[] files = file.listFiles();// 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
					this.deleteFile(files[i]);// 把每个文件用这个方法进行迭代
				}
				// file.delete();//删除文件夹
			}
		} else {
			System.out.println("所删除的文件不存在");
		}
	}

	*//**
	 * 下载图片到指定目录
	 *
	 * @param filePath
	 *            文件路径
	 * @param imgUrl
	 *            图片URL
	 *//*
	public static Map downImages(String filePath, String imgUrl, String alt) {
		// 若指定文件夹没有，则先创建
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 截取图片文件名
		String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
		String fileName1 = System.currentTimeMillis() + "" + new Random().nextInt(10)
				+ imgUrl.substring(imgUrl.lastIndexOf('.'), imgUrl.length());
		Map<String, String> map = new HashMap<String, String>();
		map.put(fileName1, alt);
		// map.put("fileName", fileName1);
		try {
			// 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
			String urlTail = URLEncoder.encode(fileName, "UTF-8");
			// 因此要将加号转化为UTF-8格式的%20
			imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 写出的路径
		File file = new File(filePath + File.separator + fileName1);

		try {
			// 获取图片URL
			URL url = new URL(imgUrl);
			// 获得连接
			URLConnection connection = url.openConnection();
			// 设置10秒的相应时间
			connection.setConnectTimeout(10 * 1000);
			// 获得输入流
			InputStream in = connection.getInputStream();
			// 获得输出流
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			// 构建缓冲区
			byte[] buf = new byte[1024];
			int size;
			// 写入到文件
			while (-1 != (size = in.read(buf))) {
				out.write(buf, 0, size);
			}
			out.close();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	// 按比例裁剪图片
	public static void uploadImage(File p_in, File p_out, int height, int width, String ftype)
			throws FileNotFoundException, IOException {
		// 取得图片处理
		InputStream l_in = new FileInputStream(p_in);
		OutputStream l_out = new FileOutputStream(p_out);
		chgPic(l_in, l_out, width, height, ftype);
	}

	// 按比例压缩图片
	public static boolean chgPic(InputStream in, OutputStream out, int newWidth, int newHeight, String ftype) {
		BufferedImage img = null;
		FileInputStream newin = null;
		File tempfile = null;
		try {
			if (ftype.compareToIgnoreCase("bmp") == 0) {
				PNGDecodeParam decodeParam = new PNGDecodeParam();
				String l_tempfile = "jpg";
				tempfile = new File(l_tempfile);
				JPEGEncodeParam encodeParam = new JPEGEncodeParam();
				// 根据路径打开输出流
				FileOutputStream tempout;
				tempout = new FileOutputStream(tempfile);
				ImageDecoder decoder = ImageCodec.createImageDecoder("BMP", in, decodeParam);
				RenderedImage image = decoder.decodeAsRenderedImage();
				ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", tempout, encodeParam);
				encoder.encode(image);
				tempout.close();

				newin = new FileInputStream(tempfile);
				img = ImageIO.read(newin);
			} else {
				img = ImageIO.read(in);
			}

			int width = img.getWidth(null);
			int height = img.getHeight(null);

			if (newWidth >= width) {
				if (newHeight < height) {
					width = (int) (width * newHeight / height);
					height = newHeight;
				}
			} else {
				if (newHeight >= height) {
					height = (int) (height * newWidth / width);
					width = newWidth;
				} else {
					if (height > width) {
						width = (int) (width * newHeight / height);
						height = newHeight;
					} else {
						height = (int) (height * newWidth / width);
						width = newWidth;
					}
				}
			}

			BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			img2.getGraphics().drawImage(img, 0, 0, width, height, null);

			if (ftype.compareToIgnoreCase("jpg") == 0 || ftype.compareToIgnoreCase("jpeg") == 0) {
				ImageIO.write(img2, "jpg", out);
			} else
				ImageIO.write(img2, "png", out);

			if (ftype.compareToIgnoreCase("bmp") == 0) {
				ImageIO.write(img2, "jpg", out);
				newin.close();
				tempfile.delete();
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {

			}
		}
	}

	@RequestMapping(value = "/uploadPicfile")
	@ResponseBody
	public JSONObject uploadPicfile(String picShort,String fileName) throws IOException {
		String storeUrl = "E:\\img2\\";
		String imgUrl = storeUrl + fileName;
		String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
		String filepath = path.substring(0, path.lastIndexOf("lib/"));
		String docStorePath = filepath + "webapps/pcp/tmp";
		File docstoreDir = new File(docStorePath);
		if (!(docstoreDir.exists() && docstoreDir.isDirectory())) {
			docstoreDir.mkdirs();
		}
		//String storePath = filepath + "webapps/pcp/tmp/movieImg";
		String storePath = filepath + "webapps/pcp/tmp/movieImg4";
		//String storePath = filepath + "webapps/pcp/tmp/movieImg2";
		if (i == 0) {
			deleteFile(new File(storePath));
		}
		File storeDir = new File(storePath);
		if (!(storeDir.exists() && storeDir.isDirectory())) {
			storeDir.mkdirs();
		}
		JSONObject json = new JSONObject();
		InetAddress inet = InetAddress.getLocalHost();
		String RealityUrl = storePath + "/" + fileName;
		String TomcatUrl = "http://" + inet.getHostAddress() + ":8080/pcp/tmp";
		//String shortUrl = "/movieImg/"+fileName;
		String shortUrl = "/movieImg4/"+fileName;
		//String shortUrl = "/movieImg2/"+fileName;
		File file = new File(imgUrl);
		if (!file.exists())
			throw new RuntimeException("文件不存在..");
		
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List list = filmInfoService.getMovieCode(picShort);
		DataSourceContextHolder.clearDbType();
		if(list.size()>0){
			Map map = (Map) list.get(0);
			String movieCode = (String) map.get("movieCode");
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			String movieCode1 = movieCode.substring(movieCode.length()-8, movieCode.length());
			Integer imgNum = filmInfoService.searchMovieInfo(movieCode1);
			int kk = 0;
			if(imgNum>0){
				System.out.println(kk++);
			}else{
				FileInputStream fis = new FileInputStream(file);
				byte[] b = new byte[1024];
				int len = 0;
				FileOutputStream fos = new FileOutputStream(RealityUrl);
				while ((len = fis.read(b)) != -1) {
					fos.write(b, 0, len);
				}
				fos.close();
				fis.close();
			}
			DataSourceContextHolder.clearDbType();
		}
		// json.put("RealityUrl", RealityUrl);
		json.put("TomcatUrl", TomcatUrl);
		json.put("shortUrl", shortUrl);
		return json;
	}
}
*/