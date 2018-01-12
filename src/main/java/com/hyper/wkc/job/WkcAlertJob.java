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
package com.hyper.wkc.job;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hyper.wkc.account.dao.AccountInfoDaoImpl;
import com.hyper.wkc.account.domain.AccountInfo;
import com.hyper.wkc.device.dao.DeviceDaoImpl;
import com.hyper.wkc.device.domain.Device;
import com.hyper.wkc.income.dao.IncomeHistoryDaoImpl;
import com.hyper.wkc.income.domain.IncomeHistory;
import com.hyper.wkc.notify.dao.AlertMessageDaoImpl;
import com.hyper.wkc.notify.domain.AlertMessage;
import com.hyper.wkc.utils.DingdingNotifier;

/** 
 * 告警JOB
 * @ClassName  WkcAlertJob
 * @author  泡面和尚
 * @date  2018年1月6日 下午7:44:52
 */
@Component
public class WkcAlertJob {
	
	@Autowired
	private IncomeHistoryDaoImpl incomeHistoryDao;
	
	@Autowired
	private AlertMessageDaoImpl alertMessageDao;
	
	@Autowired
	private DeviceDaoImpl deviceDao;
	
	@Autowired
	private AccountInfoDaoImpl accountInfoDao;
	
	@Autowired
	private DingdingNotifier dingdingNotifier;
	
	@Scheduled(cron = "0 20 5,6,7,8,9 * * ?")
	@PostConstruct
	public void initIncomeMessage(){
		initIncomeMessage(-1);
	}
	
	public void initIncomeMessage(Integer days){
		// 默认 -1
		if(days == null){
			days = -1;
		}
		String lastday = getLastDay(days);
		//获取已更新收益
		Query query = Query.query(Criteria.where("incomeArr").elemMatch(Criteria.where("date").is(lastday)));
		List<IncomeHistory> histories = incomeHistoryDao.find(query);
		if(histories.isEmpty()){
			return;
		}
		sortIncome(histories);
		//获取昨天的收益提醒
		query = Query.query(Criteria.where("type").is(1).and("title").regex(lastday));
		AlertMessage message = alertMessageDao.findOne(query);
		if(message == null){
			message = new AlertMessage();
			message.setTitle("链克收益汇总("+lastday+")");
			StringBuffer sb = new StringBuffer();
			sb.append("### [链克收益汇总("+lastday+")](http://red.xunlei.com/index.php?r=site/coin) \n");
			Device device = null;
			AccountInfo accountInfo = null;
			int deviceCount = 0;
			double lastdayIncome = 0;
			double totalIncome = 0;
			double balance = 0;
			for(IncomeHistory income:histories){
				deviceCount ++;
				lastdayIncome += Double.parseDouble(income.getIncomeArr().get(-1*(days+1)).getNum());
				device = deviceDao.findById(income.getPhone());
				sb.append("> ##### "+device.getDevice_name()+"："+income.getIncomeArr().get(-1*(days+1)).getNum()+" \n");
				totalIncome += income.getTotalIncome();
				accountInfo = accountInfoDao.findById(income.getPhone());
				balance += Double.parseDouble(accountInfo.getBalance());
			}
			sb.append("> ###### 昨日：" + formatCoin(lastdayIncome) + " 平均：" + formatCoin(lastdayIncome/deviceCount) + " \n");
			sb.append("> ###### 累计：" + formatCoin(totalIncome) + " 待提取：" + formatCoin(balance) + " \n");
			sb.append("> ###### "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+" \n");
			message.setContent(sb.toString());
			message.setStatus(0);
			message.setType(1);
			message.setCreatetime(new Date());
			alertMessageDao.save(message);
		}
	}
	
	@Scheduled(cron = "0 * * * * ?")
	public void send(){
		Query query = Query.query(Criteria.where("status").is(0));
		List<AlertMessage> messages = alertMessageDao.find(query);
		for(AlertMessage message:messages){
			dingdingNotifier.doNotify(message);
			message.setStatus(1);
			message.setAlerttime(new Date());
			alertMessageDao.save(message);
		}
	}
	
	 
	/**
	 * 按受益排个序
	 * @Title  sortIncome
	 * @author  泡面和尚
	 * @param histories
	 * @return  void
	 */
	private void sortIncome(List<IncomeHistory> histories){
		Collections.sort(histories, new Comparator<IncomeHistory>() {
            @Override
            public int compare(IncomeHistory b1, IncomeHistory b2) {
                return b2.getIncomeArr().get(0).getNum().compareTo(b1.getIncomeArr().get(0).getNum());
            }
        });
	}
	
	/**
	 * 获取昨天日期
	 * @Title  getLastDay
	 * @author  泡面和尚
	 * @return
	 * @return  String
	 */
	private String getLastDay(Integer d){
		if(d == null || d > 0){
			d = -1;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, d);
		return dateFormat.format(cal.getTime());
	}
	
	private String formatCoin(double d) {  
         DecimalFormat df = new DecimalFormat("#.00000000");  
         return df.format(d);
    }

}
