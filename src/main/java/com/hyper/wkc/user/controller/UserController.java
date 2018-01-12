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
package com.hyper.wkc.user.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyper.wkc.job.WkcAlertJob;
import com.hyper.wkc.job.WkcJob;
import com.hyper.wkc.user.dao.UserDaoImpl;
import com.hyper.wkc.user.domain.User;

/** 
 * @ClassName  UserController
 * @author  泡面和尚
 * @date  2018年1月6日 上午2:29:25
 */
@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserDaoImpl userDao;
	
	@Autowired
	WkcJob job;
	@Autowired
	WkcAlertJob alertJob;
	
	@GetMapping("/add")
	public void add(){
		User user = new User();
		user.setPhone("18*****");
		user.setPwd("****");
		user.setEnable(1);
		user.setStatus(0);
		user.setCreatetime(new Date());
		userDao.save(user);
	}
	
	@GetMapping("/sa")
	public void shuax(){
		job.doLogin();
	}
	
	@GetMapping("/alert/{days}")
	public void shuax(@PathVariable Integer days){
		alertJob.initIncomeMessage(days);
	}


}
