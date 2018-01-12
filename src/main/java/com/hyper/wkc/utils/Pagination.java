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
package com.hyper.wkc.utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * @ClassName  Pagination
 * @author  泡面和尚
 * @date  2018年1月6日 上午12:30:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {
	
	/** 每页显示条数 */
    private Integer pageSize;

    /** 当前页 */
    private Integer currentPage;

    /** 总页数 */
    private Integer totalPage;

    /** 查询到的总数据量 */
    private Integer totalCount;

    /** 数据集 */
    private List<T> dataList;

    public Pagination(int pageSize ,int currentPage, int totalCount) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalCount = totalCount;
    }

    /**
     * 处理查询得到的结果数据
     */
    public void build(List dataList) {
        this.dataList = dataList;
        //处理总页数
        int divisor = totalCount / pageSize;
        int remainder = totalCount % pageSize;
        if (remainder == 0) {
            this.totalPage = divisor;
        } else {
            this.totalPage = divisor + 1;
        }
    }

}
