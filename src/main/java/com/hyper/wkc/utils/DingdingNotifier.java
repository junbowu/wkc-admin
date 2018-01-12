package com.hyper.wkc.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hyper.wkc.notify.domain.AlertMessage;

/** 
 * @ClassName  DingdingNotifier
 * @author  泡面和尚
 * @date  2018年1月6日 下午10:34:54
 */
@Component
public class DingdingNotifier {
	
	private static final String DINGDING_ROBOT_SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";
	
	@Value("${dingding.token}")
	private String accessToken;
	
	private static HttpHeaders httpHeaders;
	private RestTemplate restTemplate = new RestTemplate();
	
	private static final String TEXT_MESSAGE = "{\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"}}";
	private static final String MARKDOWN_MESSAGE = "{\"msgtype\":\"markdown\",\"markdown\":{\"title\":\"%s\",\"text\":\"%s\"}}";
	
	public DingdingNotifier() {
	    httpHeaders = new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	}
	
	public void doNotify(AlertMessage message){
		restTemplate.postForEntity(DINGDING_ROBOT_SERVER_URL + accessToken, createMessage(message), Void.class);
	}
	
	private HttpEntity<String> createMessage(AlertMessage message){
		return new HttpEntity<String>(String.format(MARKDOWN_MESSAGE, StringEscapeUtils.escapeJava(message.getTitle()),
				StringEscapeUtils.escapeJava(message.getContent())), httpHeaders);
	}
	
}
