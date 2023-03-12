package com.example.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
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
			executor.submit(new Client1(startSignal, doneSignal, jsonInputString, 1000));
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