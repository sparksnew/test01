package com.oristartech.cinema.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.awt.Image;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.utils.Cinema365DBUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

@RequestMapping("/upload")
@Controller
public class UploadImgController extends HttpServlet {
	@RequestMapping(value = "/upload")
	@ResponseBody
	public void getTroubleData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		String bugID = request.getParameter("bugID");
		/*
		 * Long start = System.currentTimeMillis(); SimpleDateFormat df = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String currentDate =
		 * df.format(new Date());
		 */
		System.out.println("uploadPicfile is running...");
		String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
		String filepath = path.substring(0, path.lastIndexOf("webapps/"));
		/*
		 * String docStorePath = filepath + "webapps/pcp/tmp"; File docstoreDir
		 * = new File(docStorePath); if (!(docstoreDir.exists() &&
		 * docstoreDir.isDirectory())) { docstoreDir.mkdirs(); }
		 */
		String storePath = filepath + "webapps/img1";
		File storeDir = new File(storePath);
		if (!(storeDir.exists() && storeDir.isDirectory())) {
			storeDir.mkdirs();
		}
		String fids = "";
		filepath = filepath.substring(1);
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// 1.创建DiskFileItemFactory对象，配置缓存用
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		// 2. 创建 ServletFileUpload对象
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
		// 3. 设置文件名称编码
		servletFileUpload.setHeaderEncoding("utf-8");
		// 4. 开始解析文件
		try {
			List<FileItem> items = servletFileUpload.parseRequest(request);
			for (FileItem fileItem : items) {
				if (fileItem.isFormField()) { // >> 普通数据
					String info = fileItem.getString("utf-8");
					// System.out.println("info:" + info);
					String fieldName = fileItem.getFieldName();
					if ("bugID".equals(fieldName)) {
						bugID = info;
					}
				} else { // >> 文件
					// 1. 获取文件名称
					String name = fileItem.getName();
					String fid = UUID.randomUUID().toString().replaceAll("-", "");
					String storeFileName = fid;
					// 2. 获取文件的实际内容
					InputStream is = fileItem.getInputStream();
					// 3. 保存文件
					// InetAddress inet = InetAddress.getLocalHost();
					// 大图真实的物理地址
					String RealityBigUrl = storePath + "/big" + storeFileName;
					// 小图真实的物理地址
					String RealitySmUrl = storePath + "/small" + storeFileName;
					// 原图所在服务器FTP地址
					String TomcatBigUrl = "http://139.129.232.79:8080/img1/big" + storeFileName;
					// 小图所在服务器FTP地址
					String TomcatSmUrl = "http://139.129.232.79:8080/img1/small" + storeFileName;
					byte[] bytes = new byte[is.available()];
					// 将文件中的内容读入到数组中
					String strBase64 = new BASE64Encoder().encode(bytes);
					FileUtils.copyInputStreamToFile(is, new File(RealityBigUrl));
					// 上传小图
					OperationMan imgCom = new OperationMan(RealityBigUrl);
					imgCom.resizeFix(400, 400, RealitySmUrl);
					System.out.println("上传TechExcel开始。。。。");
					AddNewAttachments("LinkedSystemID", "LinkedProjectID", "1060", bugID, name, "1000", strBase64);
					System.out.println("上传TechExcel结束。。。。");
					String sql = "insert into image(BugID,BigUrl,SmUrl) values(?,?,?)";
					// 执行插入操作
					// System.out.println("插入图片的sql:" + sql);
					Connection conn = null;
					PreparedStatement pstmt = null;
					conn = Cinema365DBUtil.getDBConn();
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, bugID);
					pstmt.setString(2, TomcatBigUrl);
					pstmt.setString(3, TomcatSmUrl);
					boolean falg = pstmt.execute();
					if (fids.equals("") || fids == null)
						fids = fid;
					else
						fids = fids + "," + fid;
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		json.put("fids", fids);
	}

	public static void main(String args[]) throws IOException {
		String pic_base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCAAoACgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDfnmeaQsxPsPSmfjR3oruSseY3fUPxo/GrH2GX7P5uPfb3xUDIyHDDB60KSew3FrcdBM8MgZCfcetFMFFJwT3Q4zklowq7plvHIxdyGK9F/rVL1q1p9yluzmTPzYxgUp35XYdO3Nqan2mHzPL8xd+cY96z9ViQOJA2GbqPWoPOX7b53O3dmnahcpcMhTPy5zkVnCDjJWNZ1FKLuVhRQKK2OYfNC8MhVgfY+tMx7UUUoO6uXOKUmg/CjHtRRVEj4IXmkCqD7n0ooorCpUcXZHRSpRlG7P/Z";
		// String TomcatBigUrl = "http://139.129.232.79:8081/ROOT";
		String TomcatBigUrl = "http://139.129.232.79:8081/ROOT";
		InputStream is = new ByteArrayInputStream(pic_base64.getBytes("UTF-8"));
		FileUtils.copyInputStreamToFile(is, new File(TomcatBigUrl));
	}

	public static JSONObject AddNewAttachments(String LinkedSystemID, String LinkedProjectID, String userID,
			String incidentID, String fileName, String size, String fileContent) {
		JSONObject json = new JSONObject();
		String url = "http://118.190.91.92/LinkPlusWebServiceSetup/WsIncident.asmx";// 提供接口的地址
		String soapaction = "http://www.techexcel.com/"; // 域名，这是在server定义的
		Service service = new Service();
		// //System.out.println("开始上传附件");
		try {
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
			call.setOperationName(new QName(soapaction, "AddNewAttachment")); // 设置要调用哪个方法
			call.addParameter(new QName(soapaction, "LinkedSystemID"), org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapaction, "LinkedProjectID"), org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapaction, "userID"), org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapaction, "incidentID"), org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapaction, "fileName"), org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapaction, "size"), org.apache.axis.encoding.XMLType.XSD_INT,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName(soapaction, "fileContent"), org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapaction + "AddNewAttachment");
			String ret = (String) call.invoke(new Object[] { "LinkedSystemID", "LinkedProjectID", userID, incidentID,
					fileName, size, fileContent });
			System.out.println("这是结果吗：" + ret);
			json.put("code", ret);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}

}
