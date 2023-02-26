package com.example.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import java.time.Duration;
import java.time.Instant;

public class AudioClientTest {
	Integer numberOfClients = 10;
	@Test
	void testAudioGet() throws Exception {
		String url = "http://localhost:9090/coen6317/audio";
		ArrayList<Long> rtt = new ArrayList<Long>();
		HttpClient client = new HttpClient();
        client.start();

        for(Integer i =0;i<numberOfClients;i++) {
        
        Request request = client.newRequest(url);
        request.param("Track_Title","Ed Sheeran - Photograph");
        request.param("key","Artist_Name");
        
        ContentResponse response = request.send();
        Instant start = Instant.now();
        assertThat(response.getStatus(), equalTo(200));
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
        rtt.add(timeElapsed.toMillis());
        }
        System.out.println("Round trip time for get request");
        System.out.println(rtt);
		client.stop();
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void testArtistsPost() throws Exception {
		
		String url = "http://localhost:9090/coen6317/audio";
		HttpClient client = new HttpClient();
		ArrayList<Long> rtt = new ArrayList<Long>();
        client.start();
        System.out.println("Performing post test");
        
        for(Integer i =0;i<numberOfClients;i=i+10) {
        Request request = client.POST(url);
        
        request.param("Artist_Name","X");
        request.param("Track_Title","Y");
        request.param("Album_Title","Z");
        request.param("Track_Number","1");
        request.param("Year","2");
        request.param("Number_Of_Reviews","3");
        request.param("Number_Of_Copies_Sold","4");

        Instant start = Instant.now();
        ContentResponse response = request.send();
		String res = new String(response.getContent());
		assertThat(response.getStatus(), equalTo(200));
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
        rtt.add(timeElapsed.toMillis());
		
        }
        System.out.println("Round trip time for post request");
        System.out.println(rtt);
		client.stop();
	}
}

