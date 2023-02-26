package com.example.model;

public class Audio {
	public String Artist_Name;
	public String Track_Title;
	public String Album_Title;
	public int Track_Number;
	public int Year;
	public int Number_Of_Reviews;
	public int Number_Of_Copies_Sold;
	
	public Audio(String Artist_Name, String Track_Title, String Album_Title, int Track_Number, int Year,
			int Number_Of_Reviews, int Number_Of_Copies_Sold) {
		super();
		this.Artist_Name = Artist_Name;
	    this.Track_Title = Track_Title;
	    this.Album_Title = Album_Title;
	    this.Track_Number = Track_Number;
	    this.Year = Year;
		this.Number_Of_Reviews = Number_Of_Reviews;
		this.Number_Of_Copies_Sold = Number_Of_Copies_Sold;
	}

	
	public String getArtist_Name() {
		return Artist_Name;
	}
	public void setArtist_Name(String artist_Name) {
		Artist_Name = artist_Name;
	}
	public String getTrack_Title() {
		return Track_Title;
	}
	public void setTrack_Title(String track_Title) {
		Track_Title = track_Title;
	}
	public String getAlbum_Title() {
		return Album_Title;
	}
	public void setAlbum_Title(String album_Title) {
		Album_Title = album_Title;
	}
	public int getTrack_Number() {
		return Track_Number;
	}
	public void setTrack_Number(int track_Number) {
		Track_Number = track_Number;
	}
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	public int getNumber_Of_Reviews() {
		return Number_Of_Reviews;
	}
	public void setNumber_Of_Reviews(int number_Of_Reviews) {
		Number_Of_Reviews = number_Of_Reviews;
	}
	public int getNumber_Of_Copies_Sold() {
		return Number_Of_Copies_Sold;
	}
	public void setNumber_Of_Copies_Sold(int number_Of_Copies_Sold) {
		Number_Of_Copies_Sold = number_Of_Copies_Sold;
	}
	
}

