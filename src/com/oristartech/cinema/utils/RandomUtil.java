package com.oristartech.cinema.utils;

import java.util.Random;

public class RandomUtil {
    //获取6位数字验证码
	public static String getNumString(){
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
	//获取6位数字和字母的验证码
	public static String getNumAndCharacter(){
		String base = "0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(base.charAt(random.nextInt(10)));
		}
		return sb.toString();
	}
}
