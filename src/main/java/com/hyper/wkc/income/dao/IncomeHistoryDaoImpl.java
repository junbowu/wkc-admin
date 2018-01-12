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
package com.hyper.wkc.income.dao;

import java.util.Date;
import org.springframework.stereotype.Repository;
import com.hyper.wkc.constant.CollectionConst;
import com.hyper.wkc.income.domain.IncomeHistory;
import com.hyper.wkc.user.domain.User;
import com.hyper.wkc.utils.WkcApiHelper;
import com.hyper.wkc.utils.dao.base.AbstractBaseMongoDao;

/** 收益历史Dao实现
 * @ClassName  IncomeHistoryDaoImpl
 * @author  泡面和尚
 * @date  2018年1月6日 上午4:46:55
 */
@Repository
public class IncomeHistoryDaoImpl extends AbstractBaseMongoDao<IncomeHistory>{

	@Override
	public String getCollectionName() {
		return CollectionConst.WKC_INCOME_HISTORY;
	}

	public void loadIncomeHistory(User user) {
		IncomeHistory history = WkcApiHelper.getIncomeHistory(user);
		if(history != null){
			history.setPhone(user.getPhone());
			history.setUpdateTime(new Date());
			save(history);
		}
	}

}
