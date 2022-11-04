package com.example.easyweb.dao;

/**
 * @author chenyh-a
 * @version created 2022-10-17
 */
public interface IDao<T, E> {
	/**
	 * execute dao
	 * 
	 * @param request
	 * @return response
	 */
	E execute(T req);

}
