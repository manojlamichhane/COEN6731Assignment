package com.example.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import com.example.model.Skier;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;

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

	@SuppressWarnings("deprecation")
	@Test
	void testArtistsPost() throws Exception {

		Integer noOfThreads = 32;

		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(noOfThreads);
		String jsonInputString = null;

		ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
		Instant start = Instant.now();
		for (int i = 0; i < noOfThreads; i++) { // create and start threads
			executor.submit(new Worker(startSignal, doneSignal, jsonInputString, 1000));
			startSignal.countDown();
		}
		doneSignal.await();
		System.out.println("Post complete with 10000 request");

		executor.shutdown();

		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Total time: " + timeElapsed.toMillis());
		System.out.println("Total throughtput: " + 10000 / timeElapsed.toSeconds());
	}

	public Integer sendPostRequest(String jsonInputString) throws Exception {
		URL url = new URL("http://localhost:9090/collection/skiers");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		byte[] input = jsonInputString.getBytes("utf-8");
		conn.getOutputStream().write(input);
		Integer statusCode = conn.getResponseCode();
		conn.disconnect();
		return statusCode;
	}

}

class Worker implements Runnable {
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;
	private final String jsonInputString;
	private Integer noOfRequestsPerThread;
	private Integer noOfSuccess = 0;
	private Integer noOfFailure = 0;
	private boolean exit = false;
	ArrayList<Long> latencies = new ArrayList<Long>();

	long mean;
	long median;
	
	Worker(CountDownLatch startSignal, CountDownLatch doneSignal, String jsonInputString,
			Integer noOfRequestsPerThread) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
		this.jsonInputString = jsonInputString;
		this.noOfRequestsPerThread = noOfRequestsPerThread;

	}

	@Override
	public void run() {
		try {
			startSignal.await();
			while(!exit) {
			for (int i = 1; i <= noOfRequestsPerThread; i++) {

				String jsonInputString = getRandomSkier();
//				System.out.println("Working for " + i + " client request");
				Instant startforone= Instant.now();

				Integer statusCode = sendPostRequest(jsonInputString);
				if(i*32>=10000) {
//					System.out.println(exit);	
					exit=true;
					break;
				}
				while (statusCode / 100 == 4 | statusCode / 100 == 5) {
					for (int k = 1; k < 5; k--) {
						System.out.println("Sending request for " + k + " time");
					}
					System.out.println("Post request failed");
				}
				if(statusCode==200) {
					noOfSuccess++;
				}else {
					noOfFailure++;
				}
				Instant endforone= Instant.now();
				Duration timeElapsed = Duration.between(startforone, endforone);
				long latency = timeElapsed.toMillis();
				latencies.add(latency);
				String str = String.valueOf(latency);
			
//		         System.out.println(str);
		        
			    }
			long sum = 0;
            for(int k=0; k<latencies.size(); k++)
            {
           	 sum += latencies.get(k);
            }
            mean = sum / latencies.size();
            System.out.println("Mean Value: " + mean);
            
            Collections.sort(latencies); 
            if (latencies.size() % 2 == 0) {
                median = ((long) latencies.get(latencies.size()/2) + (long) latencies.get(latencies.size()/2 - 1))/2;
             } else {
                median = (long) latencies.get(latencies.size()/2);
             }
            
            System.out.println("Median Value: "+median);
            System.out.println("Minimum response time: "+latencies.get(0));
            System.out.println("Maximum response time: "+latencies.get(latencies.size()-1));
            System.out.println("P99 response time: "+latencies.get(latencies.size()-100));
				}

			System.out.println("noOfSuccess: "+noOfSuccess*32);
			System.out.println("noOfFailure: "+noOfFailure*32);
			doneSignal.countDown();

		}catch(

	InterruptedException ex)
	{
		ex.printStackTrace();
	}catch(
	Exception e)
	{
		e.printStackTrace();
	}
	}

	public void stop() {
		exit = true;
	}

	public Integer sendPostRequest(String jsonInputString) throws Exception {
		URL url = new URL("http://155.248.224.107:8080/skiers");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		byte[] input = jsonInputString.getBytes("utf-8");
		conn.getOutputStream().write(input);
		Integer statusCode = conn.getResponseCode();
		conn.disconnect();
		return statusCode;
	}

	public String getRandomSkier() {
		Gson gson = new Gson();
		Random rand = new Random();
		Integer lowbound = 1;
		int skierID = rand.nextInt(100000 - lowbound) + lowbound;
		int resortID = rand.nextInt(10 - lowbound) + lowbound;
		int liftID = rand.nextInt(40 - lowbound) + lowbound;
		int seasonID = 2022;
		int dayID = 1;
		int time = rand.nextInt(360 - lowbound) + lowbound;
		Skier sk = new Skier(skierID, resortID, liftID, seasonID, dayID, time);
		return gson.toJson(sk);
	}

}
//@Test
//void testAudioGet() throws Exception {
//	String url = "http://localhost:9090/collection/allaudio";
//	int numberOfThreads = 10;
//	ArrayList<Long> rtt = new ArrayList<Long>();
//    ExecutorService service = Executors.newFixedThreadPool(100);
//    CountDownLatch latch = new CountDownLatch(numberOfThreads);
//    
//    for(Integer i=0;i<numberOfClients;i++) {
//    Instant start = Instant.now();
//        for (Integer j = 0;j<numberOfThreads;j++) {
//        	
//        	service.execute(() ->{
//        			try {
//						HttpClient client = new HttpClient();
//			        	client.start();		        	
//			            Request request = client.newRequest(url);
//			            request.param("Track_Title","Ed Sheeran - Photograph");
//			            request.param("key","Artist_Name");
//	
//			            ContentResponse	response = request.send();
//						assertThat(response.getStatus(), equalTo(200));
//						
//						client.stop();
//					} catch (InterruptedException | TimeoutException | ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                        			
//        	});
//        	latch.countDown();
//        }
//	    latch.await();    
//    	Instant end = Instant.now();
//		Duration timeElapsed = Duration.between(start, end);
//		rtt.add(timeElapsed.toMillis());            
//    }
//   
//    System.out.println("Round trip time for get request");
//    System.out.println(rtt);
//	
//}
//@SuppressWarnings("deprecation")
//@Test
//void testArtistsPost() throws Exception {
//	
//	String url = "http://localhost:9090/collection/saveaudio";
//	
//	int numberOfThreads = 10;
//	ArrayList<Long> rtt = new ArrayList<Long>();
//	ExecutorService service = Executors.newFixedThreadPool(100);
//	CountDownLatch latch = new CountDownLatch(numberOfThreads);
//    
//    for(Integer i=0;i<5;i++) {
//    	Instant start = Instant.now();
//    	    for (Integer j = 0;j<numberOfThreads;j++) {
//    	    	service.execute(() ->{
//    	    			try {
//    						HttpClient client = new HttpClient();
//    			        	client.start();		        	
//    			            Request request = client.POST(url);
//    			            request.param("Artist_Name","X");
//    			            request.param("Track_Title","Y");
//    			            request.param("Album_Title","Z");
//    			            request.param("Track_Number","1");
//    			            request.param("Year","2");
//    			            request.param("Number_Of_Reviews","3");
//    			            request.param("Number_Of_Copies_Sold","4");
//
//    			            ContentResponse	response = request.send();
//    						assertThat(response.getStatus(), equalTo(200));
//    						
//    						client.stop();
//    					} catch (InterruptedException | TimeoutException | ExecutionException e) {
//    						// TODO Auto-generated catch block
//    						e.printStackTrace();
//    					} catch (Exception e) {
//    						// TODO Auto-generated catch block
//    						e.printStackTrace();
//    					}                  			
//    	    	});
//    	    	latch.countDown();
//    	    }
//    	    latch.await();    
//    		Instant end = Instant.now();
//    		Duration timeElapsed = Duration.between(start, end);
//    		rtt.add(timeElapsed.toMillis());            
//    	}    
//    System.out.println("Round trip time for post request");
//    System.out.println(rtt);
//}
//}