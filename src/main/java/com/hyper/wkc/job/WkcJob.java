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

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hyper.wkc.account.dao.AccountInfoDaoImpl;
import com.hyper.wkc.device.dao.DeviceDaoImpl;
import com.hyper.wkc.device.dao.UsbInfoDaoImpl;
import com.hyper.wkc.income.dao.IncomeHistoryDaoImpl;
import com.hyper.wkc.user.dao.UserDaoImpl;
import com.hyper.wkc.user.domain.User;

/** 
 * 信息刷新JOB
 * @ClassName  WkcJob
 * @author  泡面和尚
 * @date  2018年1月6日 上午2:25:42
 */
@Component
public class WkcJob {
	
	@Autowired
	private UserDaoImpl userDao;
	
	@Autowired
	private AccountInfoDaoImpl accountInfoDao;
	
	@Autowired
	private IncomeHistoryDaoImpl incomeHistoryDao;
	
	@Autowired
	private DeviceDaoImpl deviceDao;
	
	@Autowired
	private UsbInfoDaoImpl usbInfoDao;
	
	/**
	 * 刷新登陆--启动时自动执行,每小时刷新
	 * @Title  doLogin
	 * @author  泡面和尚
	 * @return  void
	 */
	@Scheduled(cron = "0 0 * * * ?")
	@PostConstruct
	public void doLogin(){
		Query query = Query.query(Criteria.where("status").ne(1).and("enable").is(1));
		List<User> list = userDao.find(query);
		for(User user:list){
			userDao.doLogin(user);
			accountInfoDao.loadAccountInfo(user);
			incomeHistoryDao.loadIncomeHistory(user);
			deviceDao.loadDeviceInfo(user);
			usbInfoDao.loadUsbInfo(user);
		}
	}
	
	/**
	 * 更新收益
	 * @Title  loadIncomeHistory
	 * @author  泡面和尚
	 * @return  void
	 */
	@Scheduled(cron = "0 15 5,6,7,8,9 * * ?")
	public void loadIncome(){
		Query query = Query.query(Criteria.where("status").is(1).and("enable").is(1));
		List<User> list = userDao.find(query);
		for(User user:list){
			accountInfoDao.loadAccountInfo(user);
			incomeHistoryDao.loadIncomeHistory(user);
		}
	}
	
	/**
	 * 更新设备信息
	 * @Title  loadDeviceInfo
	 * @author  泡面和尚
	 * @return  void
	 */
	@Scheduled(cron = "0 30 * * * ?")
	public void loadDeviceInfo(){
		Query query = Query.query(Criteria.where("status").is(1).and("enable").is(1));
		List<User> list = userDao.find(query);
		for(User user:list){
			deviceDao.loadDeviceInfo(user);
			usbInfoDao.loadUsbInfo(user);
		}
	}

}
