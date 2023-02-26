package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.example.model.Audio;
import com.example.model.RespObj;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet(name = "audio", value = "audio")
public class ResourceServlet extends HttpServlet{

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
		try {
		String titleresp = request.getParameter("Track_Title");
		String keyresp = request.getParameter("key");
	    Gson gson = new Gson();
	    

	    response.setContentType("application/json");
	    
		ConcurrentHashMap resp = audioDB.get(titleresp);
		
		RespObj respobj = new RespObj();
		respobj.setRkey(keyresp);			
		respobj.setRvalue(resp.get(keyresp).toString());
		
		out.println(gson.toJson(respobj));
		}catch(NullPointerException ex) {
			System.out.println(ex);
			out.println("Invalid Arugments");
		}
    }

	@Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String putresp = request.getParameter("Track_Title");
		ConcurrentHashMap<String,String> resp = new ConcurrentHashMap<String, String>();
		resp = audioDB.get(putresp);
	

		Integer currentcopies = Integer.parseInt(resp.get("Number_Of_Copies_Sold"));
		Integer newcurrentcopies = currentcopies+1;
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		Gson gson = new Gson();
		
		RespObj respobj = new RespObj();
		respobj.setRkey("Number_Of_Copies_Sold");
		respobj.setRvalue(newcurrentcopies.toString());
		out.println(gson.toJson(respobj));
	}	
}
