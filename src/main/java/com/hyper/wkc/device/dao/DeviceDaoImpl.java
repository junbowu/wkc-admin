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
package com.hyper.wkc.device.dao;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hyper.wkc.constant.CollectionConst;
import com.hyper.wkc.device.domain.Device;
import com.hyper.wkc.user.dao.UserDaoImpl;
import com.hyper.wkc.user.domain.User;
import com.hyper.wkc.utils.WkcApiHelper;
import com.hyper.wkc.utils.dao.base.AbstractBaseMongoDao;

/** 
 * 设备信息Dao实现
 * @ClassName  DeviceDaoImpl
 * @author  泡面和尚
 * @date  2018年1月6日 上午4:52:21
 */
@Repository
public class DeviceDaoImpl extends AbstractBaseMongoDao<Device>{
	
	@Autowired
	private UserDaoImpl userDao;

	@Override
	public String getCollectionName() {
		return CollectionConst.WKC_DEVICE;
	}

	public void loadDeviceInfo(User user) {
		Device device = WkcApiHelper.getDeviceInfo(user);
		if(device != null){
			device.setPhone(user.getPhone());
			device.setUpdateTime(new Date());
			save(device);
			user.setDeviceid(device.getDevice_id());
			userDao.save(user);
		}
	}

}
