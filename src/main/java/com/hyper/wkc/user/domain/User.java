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
package com.hyper.wkc.user.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * 玩客用户
 * @ClassName  User
 * @author  泡面和尚
 * @date  2018年1月6日 上午2:27:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Id
	private String phone;
	
	private String pwd;
	
	/** 1:启用 0:禁用*/
	private Integer enable;
	
	/** 1:正常  0:初始化 -1:异常(密码错误,session失效等) */
	private Integer status;
	
	private String userid;
	
	private String sessionid;
	
	private String deviceid;
	
	private Date createtime;
	
	private Date updateTime;
	
	private Date lastLogintime;
	
	private LoginInfo lastLoginInfo;
	
	

}
