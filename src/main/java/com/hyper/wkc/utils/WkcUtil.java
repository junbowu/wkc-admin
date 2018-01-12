/*
    GPL(GNU Public Library) is a Library Management System.
    Copyright (C) 2018-2020  Huang Jie
    This file is part of GPL.
    GPL is a free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.hyper.wkc.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import com.alibaba.fastjson.JSONObject;

/** 
 * 玩客AP辅助工具类
 * @ClassName  WkcUtil
 * @author  泡面和尚
 * @date  2017年12月23日 上午1:17:55
 */
public class WkcUtil {

	public static void main(String[] args) {
		String phone = "17190083574";
		String pass = "adminpass@123";
		//75324c73de1bd2c336efc766fb734a0b
		System.out.println(getLoginParams(phone,pass));
		
	}
	
	private static String getDeviceid(String phone) {
		return DigestUtils.md5Hex(phone).substring(0, 16).toUpperCase();
	}
	
	private static String getImeiid(String phone) {
		BigDecimal decimal = new BigDecimal(phone);
		return String.valueOf(decimal.multiply(decimal)).substring(0, 14).toLowerCase();
	}
	
	private static String getSignPassword(String pass) {
		StringBuilder sb = new StringBuilder();
		sb.append(DigestUtils.md5Hex(pass));
		char c1 = sb.charAt(2);
        char c2 = sb.charAt(8);
        char c3 = sb.charAt(17);
        char c4 = sb.charAt(27);
        sb.deleteCharAt(27);
        sb.deleteCharAt(17);
        sb.deleteCharAt(8);
        sb.deleteCharAt(2);
        sb.insert(2, c2);
        sb.insert(8, c1);
        sb.insert(17, c4);
        sb.insert(27, c3);
		return DigestUtils.md5Hex(sb.toString());
	}
	
	public static String getLoginParams(String phone, String pwd) {
		JSONObject json = new JSONObject();
		json.put("deviceid", getDeviceid(phone));
		json.put("imeiid", getImeiid(phone));
		json.put("phone", phone);
		json.put("pwd", pwd);
		json.put("account_type", "4"); //账户类型(默认4)
		return getParams(json, "", false);
	}
	
	public static String getCookie(HttpClientContext context, String key) {
		List<Cookie> cookies = context.getCookieStore().getCookies();
		for(Cookie cookie:cookies){
			if(cookie.getName().equals(key)){
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public static String getParams(JSONObject jsonObj, String sessionid, boolean isGet) {
		List<String> list = new ArrayList<String>();
		Set<String> keys = jsonObj.keySet();
		for(String key:keys){
			if (key.equals("pwd")){
				list.add(key+"="+getSignPassword(jsonObj.getString(key)));
			}else{
				list.add(key+"="+jsonObj.getString(key));
			}
		}
		String[] str = new String[list.size()];
		list.toArray(str);
		Arrays.sort(str);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<str.length; i++){
			sb.append(str[i]);
			if (i != str.length - 1){
                sb.append("&");
            }
		}
		String gstr = sb.toString();//如果get，不加key参数
		sb.append("&");
		sb.append("key=").append(sessionid);
		String ss = sb.toString(); //这是sign的原文
		String sign = DigestUtils.md5Hex(ss); //这是最终的sign
        return isGet ? gstr+"&sign="+sign : ss+"&sign="+sign;
	}
	

}
