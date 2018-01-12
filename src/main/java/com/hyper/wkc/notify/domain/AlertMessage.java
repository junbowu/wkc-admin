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
package com.hyper.wkc.notify.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * 提醒消息
 * @ClassName  AlertMessage
 * @author  泡面和尚
 * @date  2018年1月6日 下午8:07:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertMessage {
	
	@Id
	private String phone;
	
	private String title;
	
	private String content;
	
	/** 状态(1:成功 0:待处理 -1:失败) */
	private Integer status;
	
	/** 类型(1:收益 2:异常) */
	private Integer type;
	
	private Date createtime;
	
	private Date alerttime;

}
