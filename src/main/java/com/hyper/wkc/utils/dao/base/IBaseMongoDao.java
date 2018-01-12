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
package com.hyper.wkc.utils.dao.base;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.hyper.wkc.utils.Page;
import java.util.List;

/** 
 * moangoDB操作接口
 * @ClassName  IBaseMongoDao
 * @author  泡面和尚
 * @date  2018年1月6日 上午12:33:59
 */
public interface IBaseMongoDao<T> {
	
	/**
     * 通过条件查询实体(集合)
     *
     * @param query
     */
    List<T> find(Query query);
    /**
     * 通过一定的条件查询一个实体
     *
     * @param query
     * @return
     */
    T findOne(Query query);
    /**
     * 通过条件查询更新数据
     *
     * @param query
     * @param update
     * @return
     */
    T update(Query query, Update update);

    /**
     * 保存一个对象到mongodb
     *
     * @param entity
     * @return
     */
    void save(T entity);

    /**
     * 通过ID获取记录
     *
     * @param id
     * @return
     */
    T findById(String id);

    /**
     * 通过ID删除记录
     *
     * @return
     */
    T remove(Query query);

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
//    Page<T> findPage(Page<T> page, Query query);

    /**
     * 求数据总和
     * @param query
     * @return
     */
    long count(Query query);

    /**
     * 获取需要操作的集合collectionName名字
     * @return
     */
    String getCollectionName();

}
