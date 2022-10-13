package com.example.easyweb.dao;


public interface IDao<T, E> {
	E execute(T req);

}
