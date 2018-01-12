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
package com.hyper.wkc.account.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Gas信息
 * @ClassName  GasInfo
 * @author  泡面和尚
 * @date  2018年1月6日 上午3:05:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GasInfo {
	
	private String fastGas;
	private String normalGas;
	private Integer isFree;

}
