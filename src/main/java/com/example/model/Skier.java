package com.example.model;


public class Skier {

	public Integer skierID;
	public Integer resortID;
	public Integer liftID;
	public Integer seasonID = 2022; 
	public Integer dayID = 1;
	public Integer time;
	

	public Skier(Integer skierID, Integer resortID, Integer liftID, Integer seasonID, Integer dayID,Integer time) {
		// TODO Auto-generated constructor stub
		this.skierID=skierID;
		this.resortID=resortID;
		this.liftID=liftID;
		this.seasonID=seasonID;
		this.dayID=dayID;
		this.time=time;
	}

	public Integer getSkierID() {
		return skierID;
	}
	public void setSkierID(Integer skierID) {
		this.skierID = skierID;
	}
	public Integer getResortID() {
		return resortID;
	}
	public void setResortID(Integer resortID) {
		this.resortID = resortID;
	}
	public Integer getLiftID() {
		return liftID;
	}
	public void setLiftID(Integer liftID) {
		this.liftID = liftID;
	}
	public Integer getSeasonID() {
		return seasonID;
	}
	public void setSeasonID(Integer seasonID) {
		this.seasonID = seasonID;
	}
	public Integer getDayID() {
		return dayID;
	}
	public void setDayID(Integer dayID) {
		this.dayID = dayID;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}

}
