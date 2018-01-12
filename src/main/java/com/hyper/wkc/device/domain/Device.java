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
package com.hyper.wkc.device.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * 设备
 * @ClassName  Device
 * @author  泡面和尚
 * @date  2018年1月6日 上午2:30:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
	
	@Id
	private String phone;
	
	private Long bind_time;
	private String features;
	private Ipinfo ip_info;
	private String ip;
	private Integer coturn_online;
	private Boolean paused;
	private String dcdn_upnp_message;
	private String device_sn;
	private String broker_id;
	private String exception_message;
	private String account_name;
	private Boolean hibernated;
	private String imported;
	private String exception_name;
	private String hardware_model;
	private String mac_address;
	private String status;
	private String lan_ip;
	private String account_type;
	private String account_id;
	private Boolean upgradeable;
	private Long dcdn_download_speed;
	private Long dcdn_upload_speed;
	private Long disk_quota;
	private String peerid;
	private String licence;
	private String dcdn_id;
	private String system_version;
	private String device_id;
	private String system_name;
	private String dcdn_upnp_status;
	private String product_id;
	private String device_name;
	
	private Date updateTime;

}
