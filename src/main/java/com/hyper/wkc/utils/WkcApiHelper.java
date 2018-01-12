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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyper.wkc.account.domain.AccountInfo;
import com.hyper.wkc.device.domain.Device;
import com.hyper.wkc.device.domain.UsbInfo;
import com.hyper.wkc.income.domain.IncomeHistory;
import com.hyper.wkc.user.domain.LoginInfo;
import com.hyper.wkc.user.domain.User;

/** 
 * 玩客API
 * @ClassName  WkcApiHelper
 * @author  泡面和尚
 * @date  2018年1月6日 上午3:43:18
 */
public class WkcApiHelper {
	
	private static Logger logger = LoggerFactory.getLogger(WkcApiHelper.class);
	
	private static final String DEFAULT_CHARSET = "utf-8";
	
	/** 初始app版本 */
    private static final String appVersion = "1.4.11";
    private static final String apiAccountUrl = "https://account.onethingpcs.com";
    private static final String apiControlUrl = "https://control.onethingpcs.com";
    
    /**
     * 在线设备
     */
    private static Map<String, HttpClient> clients = new HashMap<>();
    
    /**
     * 获取客户端
     * @Title  getClient
     * @author  泡面和尚
     * @param phone
     * @return
     * @return  HttpClient
     */
    public static HttpClient getClient(String phone){
    	if (!clients.containsKey(phone)){
    		clients.put(phone, HttpClients.createDefault());
    	}
    	return clients.get(phone);
	}
    
    /**
     * 登陆
     * @Title  doLogin
     * @author  泡面和尚
     * @param user
     * @return
     * @return  boolean
     */
    public static boolean doLogin(User user){
    	HttpClient client = HttpClients.createDefault();
    	HttpPost post = null;
    	try {
    		String param = WkcUtil.getLoginParams(user.getPhone(), user.getPwd());
    	    post = new HttpPost(apiAccountUrl+"/user/login?appversion="+appVersion);
    	    post.addHeader("cache-control", "no-cache");
    	    HttpClientContext context = HttpClientContext.create();
    	    StringEntity entity = new StringEntity(param, DEFAULT_CHARSET);
    	    entity.setContentType("application/x-www-form-urlencoded");
    	    post.setEntity(entity);
    	    HttpResponse response = client.execute(post, context);
    	    if(response.getStatusLine().getStatusCode() == 200){
    	    	String logindata = okToGet(getEntity(response));
    	    	if(logindata != null){
    	    		//更新用户信息
    	    		user.setSessionid(WkcUtil.getCookie(context, "sessionid"));
    	    		user.setUserid(WkcUtil.getCookie(context, "userid"));
    	    		user.setLastLogintime(new Date());
    	    		user.setLastLoginInfo(JSON.parseObject(logindata, LoginInfo.class));
    	    		//更新客户端
    	    		clients.put(user.getPhone(), client);
    	    		return true;
    	    	}
    	    }
    	} catch (Exception e) {
    		logger.error("登录失败:{}, {}", user.getPhone(), e);
    	    e.printStackTrace();
    	}
		return false;
    }
    
    /**
     * 获取账号信息
     * @Title  getAccountInfo
     * @author  泡面和尚
     * @param user
     * @return
     * @return  AccountInfo
     */
    public static AccountInfo getAccountInfo(User user) {
    	HttpClient client = getClient(user.getPhone());
    	HttpPost post = null;
    	try {
    		JSONObject json = new JSONObject();
    		json.put("appversion", appVersion);
    		String param = WkcUtil.getParams(json, user.getSessionid(), true);
    		post = new HttpPost(apiAccountUrl+"/wkb/account-info");
    		post.addHeader("cache-control", "no-cache");
    		StringEntity entity = new StringEntity(param, DEFAULT_CHARSET);
    		entity.setContentType("application/x-www-form-urlencoded");
    	    post.setEntity(entity);
    	    HttpResponse response = client.execute(post);
    	    if(response.getStatusLine().getStatusCode() == 200){
    	    	return JSON.parseObject(okToGet(getEntity(response)), AccountInfo.class);
    	    }
		} catch (Exception e) {
			logger.error("获取账号信息失败:{}, {}", user.getPhone(), e);
    	    e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 获取历史收益
     * @Title  getIncomeHistory
     * @author  泡面和尚
     * @param user
     * @return
     * @return  IncomeHistory
     */
    public static IncomeHistory getIncomeHistory(User user) {
    	HttpClient client = getClient(user.getPhone());
    	HttpPost post = null;
    	try {
    		JSONObject json = new JSONObject();
    		json.put("appversion", appVersion);
    		json.put("page", "0");
    		String param = WkcUtil.getParams(json, user.getSessionid(), true);
    		post = new HttpPost(apiAccountUrl+"/wkb/income-history");
    	    post.addHeader("cache-control", "no-cache");
    	    HttpClientContext context = HttpClientContext.create();
    	    StringEntity entity = new StringEntity(param, DEFAULT_CHARSET);
    	    entity.setContentType("application/x-www-form-urlencoded");
    	    post.setEntity(entity);
    	    HttpResponse response = client.execute(post, context);
    	    if(response.getStatusLine().getStatusCode() == 200){
    	    	return JSON.parseObject(okToGet(getEntity(response)), IncomeHistory.class);
    	    }
    	} catch (Exception e) {
    		logger.error("获取历史收入信息失败:{}, {}", user.getPhone(), e);
    		e.printStackTrace();
    	}
		return null;
	}
    
    /**
     * 获取设备信息
     * @Title  getDeviceInfo
     * @author  泡面和尚
     * @param user
     * @return
     * @return  Device
     */
    public static Device getDeviceInfo(User user) {
    	HttpClient client = getClient(user.getPhone());
    	HttpGet get = null;
    	try {
    		JSONObject json = new JSONObject();
    		json.put("appversion", appVersion);
    		json.put("v", "1");
    		json.put("ct", "1");
    		String gstr = WkcUtil.getParams(json, user.getSessionid(), true);
    		get = new HttpGet(apiControlUrl+"/listPeer?" + gstr);
    		get.addHeader("cache-control", "no-cache");
    	    HttpResponse response = client.execute(get);
    	    if(response.getStatusLine().getStatusCode() == 200){
    	    	json = JSON.parseObject(getEntity(response));
    	    	if(json.getInteger("rtn") == 0){
    	    		JSONArray jsonArray = json.getJSONArray("result").getJSONObject(1).getJSONArray("devices");
    	    		List<Device> devices = JSONArray.parseArray(jsonArray.toString(), Device.class);
    	    		if(!devices.isEmpty()){
    	    			return devices.get(0);
    	    		}
    	    	}else{ //参数有问题
    	    		logger.error("获取设备信息失败:{}, {}", user.getPhone(), json);
    	    	}
    	    }
		} catch (Exception e) {
			logger.error("获取设备信息失败:{}, {}", user.getPhone(), e);
    	    e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 获取USB挂载信息
     * @Title  getUsbInfo
     * @author  泡面和尚
     * @param user
     * @return
     * @return  UsbInfo
     */
    public static UsbInfo getUsbInfo(User user) {
    	HttpClient client = getClient(user.getPhone());
    	HttpGet get = null;
    	try {
    		JSONObject json = new JSONObject();
    		json.put("appversion", appVersion);
    		json.put("deviceid", user.getDeviceid());
    		json.put("v", "1");
    		json.put("ct", "1");
    		String gstr = WkcUtil.getParams(json, user.getSessionid(), true);
    		get = new HttpGet(apiControlUrl+"/getUSBInfo?" + gstr);
    		get.addHeader("cache-control", "no-cache");
    	    HttpResponse response = client.execute(get);
    	    if(response.getStatusLine().getStatusCode() == 200){
    	    	json = JSON.parseObject(getEntity(response));
    	    	if(json.getInteger("rtn") == 0){
    	    		JSONArray jsonArray = json.getJSONArray("result").getJSONObject(1).getJSONArray("partitions");
    	    		List<UsbInfo> usbInfos = JSONArray.parseArray(jsonArray.toString(), UsbInfo.class);
    	    		if(!usbInfos.isEmpty()){
    	    			return usbInfos.get(0);
    	    		}
    	    	}
    	    }
		} catch (Exception e) {
			logger.error("获取USB信息失败:{}, {}", user.getPhone(), e);
    	    e.printStackTrace();
		}
		return null;
	}
    
    
    public static String drawCoin(User user) {
    	HttpClient client = getClient(user.getPhone());
    	HttpPost post = null;
    	try {
    		JSONObject json = new JSONObject();
    		json.put("appversion", appVersion);
    		json.put("gasType", "2");
    		String param = WkcUtil.getParams(json, user.getSessionid(), true);
    		post = new HttpPost(apiControlUrl+"/wkb/draw");
    		post.addHeader("cache-control", "no-cache");
    		StringEntity entity = new StringEntity(param, DEFAULT_CHARSET);
    	    entity.setContentType("application/x-www-form-urlencoded");
    	    post.setEntity(entity);
    	    HttpResponse response = client.execute(post);
    	    if(response.getStatusLine().getStatusCode() == 200){
    	    	RetData data = JSON.parseObject(getEntity(response), RetData.class);
    	    	if(data.getIRet() == 0){
    	    		 return "";
    	    	}else{
    	    	}
    	    }
		} catch (Exception e) {
			logger.error("提币失败:{}, {}", user.getPhone(), e);
    	    e.printStackTrace();
		}
		return null;
	}
    
    
    /**
     * 响应转换
     * @Title  okToGet
     * @author  泡面和尚
     * @param result
     * @return
     * @return  String
     */
    private static String okToGet(String result){
    	if(result == null){
    		return null;
    	}
    	RetData data = JSON.parseObject(result, RetData.class);
		if(data.getIRet() != null && data.getIRet() == 0){ // 成功
			if(data.getData() != null){
				return data.getData();
			}else{
				return data.getResult();
			}
		}else{
			return null;
		}
	}
    
    /**
     * 获取接口返回数据
     * @Title  getEntity
     * @author  泡面和尚
     * @param response
     * @return
     * @return  String
     */
    private static String getEntity(HttpResponse response){
    	try {
    		String data = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
    		logger.info("接口调用响应——>：{}",  data);
    		return data;
		} catch (Exception e) {
			logger.error("获取响应数据失败", e);
			e.printStackTrace();
		}
		return null;
	}
	
}
