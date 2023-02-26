package com.example.controller;
import com.example.model.Audio;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "saveaudio", value = "saveaudio")
public class ResourcePostServlet extends HttpServlet{
	ConcurrentHashMap<String,ConcurrentHashMap> audioDB = new ConcurrentHashMap<String,ConcurrentHashMap>();
	
	ArrayList<Audio> audio_files = new ArrayList<Audio>();
	
	@Override
	public void init() throws ServletException{
		audio_files.add(new Audio("Ed Sheeran","Ed Sheeran - Photograph","x",213950659,2014,33160659,369300));
		audio_files.add(new Audio("Murda","Konum Gizli (feat. Murda)","single",2139506,2022,160659,69300));
		for (int i = 0; i < audio_files.size(); i++) {
			ConcurrentHashMap<String,String> audioParams = new ConcurrentHashMap<String,String>();
			audioParams.put("Artist_Name", audio_files.get(i).Artist_Name);
			audioParams.put("Track_Title", audio_files.get(i).Track_Title);
			audioParams.put("Album_Title", audio_files.get(i).Album_Title);
			audioParams.put("Track_Number", Integer.toString(audio_files.get(i).Track_Number));
			audioParams.put("Year", Integer.toString(audio_files.get(i).Year));
			audioParams.put("Number_Of_Reviews", Integer.toString(audio_files.get(i).Number_Of_Reviews));
			audioParams.put("Number_Of_Copies_Sold",Integer.toString(audio_files.get(i).Number_Of_Copies_Sold));
		
		audioDB.put(audio_files.get(i).Track_Title, audioParams);
		}
		}
		@Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        PrintWriter out = response.getWriter();
			try {
	    	String Artist_Name = request.getParameter("Artist_Name");
	        String Track_Title = request.getParameter("Track_Title");
	        String Album_Title = request.getParameter("Album_Title");
	        Integer Track_Number = Integer.parseInt(request.getParameter("Track_Number"));
	        Integer Year = Integer.parseInt(request.getParameter("Year"));
	        Integer Number_Of_Reviews = Integer.parseInt(request.getParameter("Number_Of_Reviews"));
	        Integer Number_Of_Copies_Sold = Integer.parseInt(request.getParameter("Number_Of_Copies_Sold"));
	        ConcurrentHashMap<String,String> audioParams = new ConcurrentHashMap<String,String>();
	        
	        audioParams.put("Artist_Name", Artist_Name);
	        audioParams.put("Track_Title", Track_Title);
	        audioParams.put("Album_Title", Album_Title);
	        audioParams.put("Track_Number", Integer.toString(Track_Number));
	        audioParams.put("Year", Integer.toString(Year));
	        audioParams.put("Number_Of_Reviews", Integer.toString(Number_Of_Reviews));
	        audioParams.put("Number_Of_Copies_Sold", Integer.toString(Number_Of_Copies_Sold));
	        
	        audioDB.put(Track_Title, audioParams);


	        out.println(Track_Title + " is added to database");
	        response.setStatus(200);   
	    }catch(NullPointerException ex) {
	    	out.println("Invalid Arguments");
	    }
		}
}	

