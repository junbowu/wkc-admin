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

import java.io.Serializable;
import java.util.List;
/**
 * 分页
 * @ClassName Page
 * @author 泡面和尚
 * @date 2018年1月6日 上午6:20:04
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1;
	public static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 每页显示个数
	 */
	private int pageSize;
	/**
	 * 当前页数
	 */
	private int currentPage;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 总记录数
	 */
	private int totalCount;
	/**
	 * 结果列表
	 */
	private List<T> rows;

	public Page() {
		this.currentPage = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
	}

	public Page(int currentPage, int pageSize) {
		this.currentPage = currentPage <= 0 ? 1 : currentPage;
		this.pageSize = pageSize <= 0 ? 1 : pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 设置结果 及总页数
	 * 
	 * @param list
	 */
	public void build(List<T> rows) {
		this.setRows(rows);
		int count = this.getTotalCount();
		int divisor = count / this.getPageSize();
		int remainder = count % this.getPageSize();
		this.setTotalPage(remainder == 0 ? divisor == 0 ? 1 : divisor : divisor + 1);
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
