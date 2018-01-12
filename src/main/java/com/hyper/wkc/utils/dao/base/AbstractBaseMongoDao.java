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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.hyper.wkc.utils.Pagination;
import com.hyper.wkc.utils.ReflectionUtils;

import java.util.List;

/** MongoDB操作封装
 * @ClassName  AbstractBaseMongoDao
 * @author  泡面和尚
 * @date  2018年1月6日 上午12:33:02
 */
public abstract class AbstractBaseMongoDao<T> implements IBaseMongoDao<T> {

	@Autowired
	protected MongoTemplate mongoTemplate;

    @Override
    public List<T> find(Query query) {
        System.out.println("--------------"+ this.getEntityClass());
        System.out.println("--------------"+ this.getCollectionName());
        return mongoTemplate.find(query, this.getEntityClass(), this.getCollectionName());
    }

    @Override
    public T findOne(Query query) {
        return mongoTemplate.findOne(query, this.getEntityClass(), this.getCollectionName());
    }

    @Override
    public T update(Query query, Update update) {
        return mongoTemplate.findAndModify(query, update, this.getEntityClass(), this.getCollectionName());
    }
    
    @Override
    public void save(T entity) {
        mongoTemplate.save(entity, this.getCollectionName());
    }

    @Override
    public T findById(String id) {
        return mongoTemplate.findById(id, this.getEntityClass(), this.getCollectionName());
    }

    @Override
    public T remove(Query query) {
         return mongoTemplate.findAndRemove(query, this.getEntityClass(), this.getCollectionName());
    }

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, this.getEntityClass(), this.getCollectionName());
    }
    
    /**
     * 通过条件查询,查询分页结果
     */
    public Pagination<T> getPage(int currentPage, Query query) {
        final int PAGE_SIZE = 2;

        //获取总条数
        int totalCount = (int) this.count(query);

        int skip = (currentPage-1)*PAGE_SIZE;

        // skip相当于从那条记录开始
        query.skip(skip);

        // 从skip开始,取PAGE_SIZE条记录
        query.limit(PAGE_SIZE);

        List<T> dataList = this.find(query);

        Pagination<T> pagination = new Pagination<T>(PAGE_SIZE, currentPage, totalCount);

        pagination.build(dataList);//获取数据

        return pagination;
    }
    
    /** 
     * 获得泛型类 
     */
    private Class<T> getEntityClass() {  
        return ReflectionUtils.getSuperClassGenricType(getClass());  
    }
 
}
