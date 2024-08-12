package com.graph.graph.charts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYZDataset;
import org.springframework.beans.factory.annotation.Value;

import com.graph.graph.CSVReading.ReadingData;
import com.graph.graph.chart.service.GraphServices;

public class BubbleChart implements GraphServices {

    @Value("${project.image}") // Ye value properties file se read ki jaayegi jahan pe images ko save karna hai
    String imagePath;

    @Override
    public void generateGraph(String path, String imageName) throws IOException {
        // ReadingData class ka use karke CSV file se data read kar rahe hain
        ReadingData data = new ReadingData(path);
        
        // Bubble chart ke liye data ko retrieve kar rahe hain
        Map<String, List<Integer>> bubbleData = data.getBubbleData();

        // Labels initialize kar rahe hain jo chart ke axis labels ke liye use honge
        String labels[] = new String[3];
        int i = 0;
        int n = 0;

        // Data ke size ko determine kar rahe hain
        for (Map.Entry<String, List<Integer>> iter : bubbleData.entrySet()) {
            n = iter.getValue().size();
            break;
        }

        List<double[]> arr = new ArrayList<>();
        // Bubble chart ke data ko process kar rahe hain
        for (Map.Entry<String, List<Integer>> e : bubbleData.entrySet()) {
            labels[i] = e.getKey(); // Labels ko set kar rahe hain
            int col = 0;
            double a[] = new double[n];
            for (int num : e.getValue()) {
                a[col] = (double) num; // Integer values ko double mein convert kar rahe hain
                col++;
                System.out.print(num + " ");
            }
            arr.add(a); // Processed data ko array list mein add kar rahe hain
            System.out.println();
            i++;
        }

        // DefaultXYZDataset ko initialize kar rahe hain jo JFreeChart ke liye data hold karega
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        double values[][] = { arr.get(0), arr.get(1), arr.get(2) }; // XYZ values ko set kar rahe hain
        dataset.addSeries("Series 1", values); // Dataset mein series add kar rahe hain

        // JFreeChart ka use karke Bubble chart create kar rahe hain
        JFreeChart chart = ChartFactory.createBubbleChart(
            "BubbleChart", // Chart ka title //WEIGHT vs AGE vs WORK
            labels[0], // X-axis label
            labels[1], // Y-axis label
            dataset // Data
        );

        // Chart ko save karne ke liye path create kar rahe hain
        String pathImage = String.format("D:\\PROJECT\\ALL_IMP_To_Project\\image_graph_generated\\%s", imageName);

        try {
            // Chart ko JPEG file ke roop mein save kar rahe hain
            ChartUtilities.saveChartAsJPEG(new File(pathImage), chart, 500, 300);
        } catch (IOException e) {
            // Agar koi error aata hai to uska stack trace print karenge
            e.printStackTrace();
        }
    }
}
