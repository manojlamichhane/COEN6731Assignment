package com.example.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.example.model.Skier;
import com.google.gson.Gson;

public class Client1 implements Runnable{
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;
	private final String jsonInputString;
	private Integer noOfRequestsPerThread;
	private Integer noOfSuccess = 0;
	private Integer noOfFailure = 0;
	private boolean exit = false;
	ArrayList<Long> latencies = new ArrayList<Long>();


	Client1(CountDownLatch startSignal, CountDownLatch doneSignal, String jsonInputString,
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
			while (!exit) {
				for (int i = 1; i <= noOfRequestsPerThread; i++) {

					String jsonInputString = getRandomSkier();
					System.out.println("Working for " + i + " client request");
					Instant startforone = Instant.now();

					Integer statusCode = sendPostRequest(jsonInputString);
					if (i * 32 >= 10000) {
						exit = true;
						break;
					}
					while (statusCode / 100 == 4 | statusCode / 100 == 5) {
						for (int k = 1; k < 5; k--) {
							System.out.println("Sending request for " + k + " time");
						}
						System.out.println("Post request failed");
					}
					if (statusCode == 200) {
						noOfSuccess++;
					} else {
						noOfFailure++;
					}
					Instant endforone = Instant.now();
					Duration timeElapsed = Duration.between(startforone, endforone);
					long latency = timeElapsed.toMillis();
					latencies.add(latency);
					String str = String.valueOf(latency);
//				System.out.println(startforone+","+"POST"+","+str+","+"200");		        
				}

				System.out.println("Mean Value: " + getMean(latencies));
				System.out.println("Median Value: " + getMedian(latencies));
				
				Collections.sort(latencies);
				
				System.out.println("Minimum response time: " + latencies.get(0));
				System.out.println("Maximum response time: " + latencies.get(latencies.size() - 1));
				System.out.println("P99 response time: " + latencies.get(latencies.size() - 100));
			}
			System.out.println("noOfSuccess: " + noOfSuccess * 32);
			System.out.println("noOfFailure: " + noOfFailure * 32);
			doneSignal.countDown();

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		exit = true;
	}

	public Long getMean(ArrayList<Long> input) {
		long sum = 0;
		long mean;
		for (int k = 0; k < input.size(); k++) {
			sum += input.get(k);
		}
		mean = sum / input.size();
		return mean;
	}
	public Long getMedian (ArrayList<Long> input) {
		long median;
		Collections.sort(input);
		if (input.size() % 2 == 0) {
			median = ((long) input.get(input.size() / 2)
					+ (long) input.get(input.size() / 2 - 1)) / 2;
		} else {
			median = (long) input.get(input.size() / 2);
		}
		return median;
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

	public String getRandomSkier() {
		Gson gson = new Gson();
		Random rand = new Random();
		Integer lowbound = 1;  //low bound used so that the value we generate will start from 1 instead of 0
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