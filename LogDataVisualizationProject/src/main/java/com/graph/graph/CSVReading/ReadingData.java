package com.graph.graph.CSVReading;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class ReadingData {

	private CSVReader reader;
	String[] nextline;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	private BufferedReader reader2;

	/**
	 * Constructor to initialize the CSVReader with the given file path.
	 *
	 * @param path The path of the CSV file to be read.
	 */
	
	public ReadingData(String path) {
		try {
			// Constructor me CSV file ka path pass kar rahe ho, aur reader initialize kar
			// rahe ho.
			reader = new CSVReader(new FileReader(path));
			reader2 = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // Log error if the file is not found
		}
	}

	public List<String> getLogData() throws IOException {
		List<String> logData = new ArrayList<>();

		try {
			String[] nextline;

			while ((nextline = reader.readNext()) != null) {
				if (nextline.length >= 3) {
					// Combine timestamp, log level, and message into a single string
					String logEntry = String.join(",", nextline);
					logData.add(logEntry); // Add the combined string to the list
				}
			}
		} catch (Exception e) {
			System.out.println("error if here getLogData ");
		}
		return logData; // Return the list of log entries
	}

	/**
	 * Reads data for PieChart from the CSV file.
	 *
	 * @return A map where keys are labels and values are integer counts.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public Map<String, Integer> getPieData() throws IOException {
		Map<String, Integer> pieData = new LinkedHashMap<>();
		nextline = reader.readNext(); // Read header line
		while ((nextline = reader.readNext()) != null) {
			if (nextline != null) {
				pieData.put(nextline[0], Integer.parseInt(nextline[1]));
			}
		}
		return pieData;
	}

	/**
	 * Reads data for Histogram from the CSV file.
	 *
	 * @return A map where keys are categories and values are lists of integers for
	 *         each category.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public Map<String, List<Integer>> getHistogramData() throws IOException {

		List<Integer> histData = new ArrayList<>();
		nextline = reader.readNext();
		String name = nextline[0];
		while ((nextline = reader.readNext()) != null) {
			if (nextline != null) {
				histData.add(Integer.parseInt(nextline[0]));
			}
		}
		Map<String, List<Integer>> m = new LinkedHashMap<>();
		m.put(name, histData);

		return m;

	}

	/**
	 * Reads data for BubbleChart from the CSV file.
	 *
	 * @return A map where keys are series names and values are lists of integers
	 *         for each series.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public Map<String, List<Integer>> getBubbleData() throws IOException {
		Map<String, List<Integer>> bubbleData = new LinkedHashMap<>();
		String key1, key2, key3;
		nextline = reader.readNext();
		key1 = nextline[0];
		key2 = nextline[1];
		key3 = nextline[2];
		List<Integer> value1 = new ArrayList<>();
		List<Integer> value2 = new ArrayList<>();
		List<Integer> value3 = new ArrayList<>();
		while ((nextline = reader.readNext()) != null) {
			if (nextline != null) {
				value1.add(Integer.parseInt(nextline[0]));
				value2.add(Integer.parseInt(nextline[1]));
				value3.add(Integer.parseInt(nextline[2]));
			}
		}
		bubbleData.put(key1, value1);
		bubbleData.put(key2, value2);
		bubbleData.put(key3, value3);

		return bubbleData;
	}

	/**
	 * Reads data for LineChart from the CSV file.
	 *
	 * @return A map where keys are series names and values are lists of data
	 *         points.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */

	// Read the rest of the data
	// Method to read the CSV file and generate line data for charting
	public Map<String, List<String>> getLineData() throws IOException {
		Map<String, List<String>> lineData = new HashMap<>();
		lineData.put("INFO", new ArrayList<>()); // Initialize list for INFO level logs
		lineData.put("WARN", new ArrayList<>()); // Initialize list for WARN level logs
		lineData.put("ERROR", new ArrayList<>()); // Initialize list for ERROR level logs

		try { // Initialize CSVReader with the file path
			String[] nextline;
			LocalDateTime startTime = null;

			while ((nextline = reader.readNext()) != null) { // Read each line from the CSV
				if (nextline.length >= 2) { // Ensure there are at least two columns (Timestamp and LogLevel)
					String timestampStr = nextline[0]; // Extract timestamp
					String logLevel = nextline[1]; // Extract log level

					LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter); // Parse timestamp

					// Set start time if not already set
					if (startTime == null) {
						startTime = timestamp;
					}

					// Calculate time difference in seconds from start time
					long timeElapsed = java.time.Duration.between(startTime, timestamp).getSeconds();

					// Add the timeElapsed to the appropriate log level series
					if (lineData.containsKey(logLevel)) {
						lineData.get(logLevel).add(timeElapsed + "," + (lineData.get(logLevel).size() + 1));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error if here");
		}
		return lineData; // Return the processed line data
	}

	/**
	 * Reads data for AreaChart from the CSV file.
	 *
	 * @return A map where keys are series names and values are lists of double
	 *         values.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public Map<String, List<Double>> getAreaData() throws IOException {
		Map<String, List<Double>> areaData = new LinkedHashMap<>();
		nextline = reader.readNext(); // Read header line
		for (String col : nextline) {
			if (!col.isEmpty()) {
				areaData.put(col, new ArrayList<>());
			}
		}
		while ((nextline = reader.readNext()) != null) {
			if (nextline != null) {
				int i = 0;
				for (Map.Entry<String, List<Double>> e : areaData.entrySet()) {
					String val = nextline[i];
					e.getValue().add(Double.parseDouble(val));
					i++;
				}
			}
		}
		return areaData;
	}

	/**
	 * Reads data for ScatterChart from the CSV file.
	 *
	 * @return A map where keys are series names and values are lists of data points
	 *         (formatted as "x,y").
	 * @throws IOException If an I/O error occurs while reading the file.
	 */

	// change i have made
	// Assuming this code is in the ReadingData class
	  public Map<String, List<String>> getScatterData() throws IOException {
	        Map<String, List<String>> logData = new LinkedHashMap<>();
	        String headerLine = reader2.readLine();

	        if (headerLine == null) {
	            System.err.println("CSV file is empty.");
	            return logData;
	        }

	        String[] headers = headerLine.split(",");
	        for (String header : headers) {
	            logData.put(header.trim(), new ArrayList<>());
	        }

	        String line;
	        while ((line = reader2.readLine()) != null) {
	            String[] values = line.split(",");
	            for (int i = 0; i < headers.length; i++) {
	                if (i < values.length) {
	                    logData.get(headers[i].trim()).add(values[i].trim());
	                } else {
	                    logData.get(headers[i].trim()).add(""); // Handle missing values
	                }
	            }
	        }

	        reader.close();
	        return logData;
	    }
	
	/**
	 * Reads data for BarChart from the CSV file.
	 *
	 * @return A map where keys are categories and values are maps of sub-categories
	 *         and their corresponding values.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public Map<String, Map<String, Double>> getBarData() throws IOException {
		Map<String, Map<String, Double>> barData = new LinkedHashMap<>();
		nextline = reader.readNext(); // Read header line
		int i = 0;
		for (String col : nextline) {
			if (i == 0) {
				i++;
				continue;
			}
			if (!col.isEmpty()) {
				barData.put(col, new LinkedHashMap<String, Double>());
			}
		}
		while ((nextline = reader.readNext()) != null) {
			if (nextline != null) {
				int index = 1;
				String cat = nextline[0];
				for (Map.Entry<String, Map<String, Double>> e : barData.entrySet()) {
					e.getValue().put(cat, Double.parseDouble(nextline[index]));
					index++;
				}
			}
		}
		return barData;
	}
}
