package com.oristartech.cinema.Jpush;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.oristartech.cinema.utils.Cinema365DBUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;

public class Jpush {

	static String appKey = "";
	static String masterSecret = "";
	static Boolean apnsProduction = false;
	static JPushClient jpushClient;
	private static ResultSet rs = null;

	static { // 静态块
		InputStream configFile = Jpush.class.getResourceAsStream("jpush.config.xml");
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(configFile);
			Element config = doc.getRootElement();
			appKey = config.elementTextTrim("appKey");
			masterSecret = config.elementTextTrim("masterSecret");
			apnsProduction = (config.elementTextTrim("apnsProduction").equals("true")) ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param mess
	 *            发送内容
	 * @param UserCode
	 *            用户ID
	 * @param key
	 *            jpush的key
	 * @param secret
	 *            jpush的secret
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 * @throws SQLException
	 */
	public static ScheduleResult sendPushMessage(String mess, String UserCode)
			throws APIConnectionException, APIRequestException, SQLException {

		// //System.out.println("PushMessage:" + mess + ";UserCode:" + UserCode
		// + ";key:" + appKey + ";secret:" + masterSecret);
		ScheduleResult result = null;
		ArrayList<Object> registrationId = new ArrayList<Object>();
		ArrayList<Object> aliasAll = new ArrayList<Object>();

		if (!"".equals(mess) && mess != null && !"".equals(UserCode) && UserCode != null) {
			// 遍历表格获取所有该用户的注册id
			String ksqlID = "select *  from users where fCustomerName = '" + UserCode + "' and type='RegistrationID'";
			System.out.println("遍历表格获取所有该用户的注册id:" + ksqlID);
			// 遍历表格获取所有该用户的别名
			String ksqlalias = "select * from users where fCustomerName = '" + UserCode + "' and type='alias'";
			System.out.println("遍历表格获取所有该用户的别名:" + ksqlalias);
			try {
				java.sql.Connection conn = Cinema365DBUtil.getDBConn();

				// 遍历所有的registrationId
				PreparedStatement pstmt = conn.prepareStatement(ksqlID);
				ResultSet rs = null;
				rs = pstmt.executeQuery();
				while (rs.next()) {
					registrationId.add(rs.getString("fRegistrationId"));
				}
				System.out.println("遍历表格获取所有该用户的注册id的result：" + registrationId);
				result = sendPushMessageID(mess, registrationId, appKey, masterSecret);

				// 遍历所有的别名
				PreparedStatement pstmt1 = conn.prepareStatement(ksqlID);
				ResultSet rs1 = null;
				rs1 = pstmt1.executeQuery();
				while (rs1.next()) {
					aliasAll.add(rs1.getString("fRegistrationId"));
				}
				System.out.println("遍历表格获取所有该用户的别名的result：" + registrationId);
				result = sendPushMessageAlias(mess, aliasAll, appKey, masterSecret);

				rs.close();
				pstmt.close();
				rs1.close();
				pstmt1.close();
				conn.close();
				return result;
			} catch (Exception e) {
				System.out.println("发送消息失败！！！");
				return null;
			}
		} else {
			System.out.println("参数传递错误！！！");
			return null;
		}
	}

	public static ScheduleResult sendPushMessageID(String mess, ArrayList registrationId, String key, String secret)
			throws APIConnectionException, APIRequestException {
		ClientConfig config = ClientConfig.getInstance();
		// //System.out.println("PushMessage:" + mess + ";registrationId:" +
		// registrationId + ";key:" + key + ";secret:" + secret);
		jpushClient = new JPushClient(secret, key, 3, null, config);
		PushPayload payload = null;
		Collection<String> strings = new LinkedList<String>();
		if (registrationId.size() > 0) {
			strings.addAll(registrationId);
			payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(strings))
					.setNotification(Notification.alert(mess)).build();
		}
		// else {
		// payload =
		// PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registrationId)).setNotification(Notification.alert(mess)).build();
		// }
		payload.resetOptionsTimeToLive(1000);
		payload.resetOptionsApnsProduction(apnsProduction);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 1);
		String scheduleTime = sdf.format(nowTime.getTime());
		ScheduleResult result = jpushClient.createSingleSchedule(UUID.randomUUID().toString().replaceAll("-", ""),
				scheduleTime, payload);
		return result;
	}

	public static ScheduleResult sendPushMessageAlias(String mess, ArrayList aliasAll, String key, String secret)
			throws APIConnectionException, APIRequestException {
		ClientConfig config = ClientConfig.getInstance();
		// //System.out.println("PushMessage:" + mess + ";alias:" + aliasAll +
		// ";key:" + key + ";secret:" + secret);
		jpushClient = new JPushClient(secret, key, 3, null, config);
		PushPayload payload = null;
		Collection<String> strings = new LinkedList<String>();
		if (aliasAll.size() > 0) {
			strings.addAll(aliasAll);
			payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(strings))
					.setNotification(Notification.alert(mess)).build();
		} else {
			payload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(aliasAll))
					.setNotification(Notification.alert(mess)).build();
		}
		payload.resetOptionsTimeToLive(1000);
		payload.resetOptionsApnsProduction(apnsProduction);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 1);
		String scheduleTime = sdf.format(nowTime.getTime());
		ScheduleResult result = jpushClient.createSingleSchedule(UUID.randomUUID().toString().replaceAll("-", ""),
				scheduleTime, payload);
		return result;
	}

	public static void main(String[] args) throws APIConnectionException, APIRequestException, SQLException {
		// Push p = new Push();
		// p.sendPushMessage("11111", "18610366411", "9f57131039c88405b8383b43",
		// "dc84cb02ce047b90fb90e103");
	}
}
