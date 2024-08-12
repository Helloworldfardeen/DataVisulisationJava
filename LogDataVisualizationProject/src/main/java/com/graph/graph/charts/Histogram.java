package com.graph.graph.charts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Value;

import com.graph.graph.CSVReading.ReadingData;
import com.graph.graph.chart.service.GraphServices;

public class Histogram implements GraphServices {

    @Value("${project.image}")
    String imagePath;

    @Override
    public void generateGraph(String path, String imageName) throws IOException {
        ReadingData data = new ReadingData(path);
        Map<String, Integer> logData = data.getLogData();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : logData.entrySet()) {
            dataset.addValue(entry.getValue(), "Frequency", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Log Frequency Histogram", // Chart title
                "Log Type",                // X-Axis Label
                "Frequency",               // Y-Axis Label
                dataset,                   // Dataset
                PlotOrientation.VERTICAL,  // Orientation
                false,                     // Include legend
                true,                      // Tooltips
                false                      // URLs
        );

        String pathImage = String.format("D:\\PROJECT\\ALL_IMP_To_Project\\image_graph_generated\\%s", imageName);
        try {
            ChartUtilities.saveChartAsJPEG(new File(pathImage), chart, 500, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
