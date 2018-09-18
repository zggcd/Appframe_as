package com.asiainfo.appframe.bean;

import java.io.Serializable;

public class Createtime implements Serializable {
	
//	{
//      "date": 20,
//      "day": 1,
//      "hours": 0,
//      "minutes": 0,
//      "month": 1,
//      "seconds": 0,
//      "time": 1487520000000,
//      "timezoneOffset": -480,
//      "year": 117
//  }
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int date;

	private int day;

	private int hours;

	private int minutes;

	private int month;

	private int seconds;

	private int time;

	private int timezoneOffset;

	private int year;

	public void setDate(int date){
	this.date = date;
	}
	public int getDate(){
	return this.date;
	}
	public void setDay(int day){
	this.day = day;
	}
	public int getDay(){
	return this.day;
	}
	public void setHours(int hours){
	this.hours = hours;
	}
	public int getHours(){
	return this.hours;
	}
	public void setMinutes(int minutes){
	this.minutes = minutes;
	}
	public int getMinutes(){
	return this.minutes;
	}
	public void setMonth(int month){
	this.month = month;
	}
	public int getMonth(){
	return this.month;
	}
	public void setSeconds(int seconds){
	this.seconds = seconds;
	}
	public int getSeconds(){
	return this.seconds;
	}
	public void setTime(int time){
	this.time = time;
	}
	public int getTime(){
	return this.time;
	}
	public void setTimezoneOffset(int timezoneOffset){
	this.timezoneOffset = timezoneOffset;
	}
	public int getTimezoneOffset(){
	return this.timezoneOffset;
	}
	public void setYear(int year){
	this.year = year;
	}
	public int getYear(){
	return this.year;
	}
}
