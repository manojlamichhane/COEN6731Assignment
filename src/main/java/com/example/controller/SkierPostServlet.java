package com.example.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


import com.example.model.Skier;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "/skiers", value = "/skiers")
public class SkierPostServlet extends HttpServlet{
	ConcurrentHashMap<String, ConcurrentHashMap> skierDB = new ConcurrentHashMap<String, ConcurrentHashMap>();
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		PrintWriter out = response.getWriter();		
		Gson gson = new Gson();
		try {
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
            }
			Skier skier = (Skier) gson.fromJson(sb.toString(), Skier.class);
			
			Integer skierID = skier.skierID;
			Integer resortID = skier.resortID;
			Integer liftID = skier.liftID;
			Integer seasonID = 2022;
			Integer dayID = 1;
			Integer time = skier.time;
			
			ConcurrentHashMap<String, Integer> skierparams = new ConcurrentHashMap<String, Integer>();
			
			if(skierID >=1 & skierID <=100000) { 
				skierparams.put("skierID",skierID);
				if(resortID >=1 & resortID <=10) {
					skierparams.put("resortID",resortID);
					if(liftID >=1 & liftID <=40) {
						skierparams.put("liftID",liftID);
						if(time >=1 & time <=360) {
							skierparams.put("time",time);
							if(skierparams.size() == 4) {
								skierparams.put("seasonID",seasonID);
								skierparams.put("dayID",dayID);
								skierDB.put(Integer.toString(skier.skierID) ,skierparams );
								out.println("Added Skier: "+skierparams);
								out.println("All skiers: "+skierDB);
								response.setStatus(200);
							}
						}else {
							out.println("Invalid value for time, must be between 1 and 360");
						}
					}else {
						out.println("Invalid value for liftID, must be between 1 and 10");
					}
				}else {
					out.println("Invalid value for resortID, must be between 1 and 10");
				} 
				
			}else {
				out.println("Invalid value for skierID, must be between 1 and 100000");
			}
			
					
		}catch(Exception e ) {
			e.printStackTrace();
	}

	}
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		PrintWriter out = response.getWriter();		
		out.println(skierDB);


	}
	}
