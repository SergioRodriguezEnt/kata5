package software.ulpgc.kata5.application;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.*;
import software.ulpgc.kata5.architecture.viewmodel.Histogram;

import javax.swing.*;
import java.awt.*;

public class Desktop extends JFrame {
    public static Desktop create() {
        return new Desktop();
    }

    private Desktop() {
        setTitle("Movie Histogram Display");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Desktop display(Histogram histogram) {
        add(chartPanelFrom(histogram));
        return this;
    }

    private static ChartPanel chartPanelFrom(Histogram histogram) {
        return new ChartPanel(chartFrom(histogram));
    }

    private static JFreeChart chartFrom(Histogram histogram) {
        return ChartFactory.createHistogram(
                histogram.title(),
                histogram.x(),
                histogram.y(),
                datasetFrom(histogram)
        );
    }

    private static IntervalXYDataset datasetFrom(Histogram histogram) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesOf(histogram));
        return dataset;
    }

    private static XYSeries seriesOf(Histogram histogram) {
        XYSeries series = new XYSeries(histogram.legend());
        histogram.forEach(bin -> series.add(itemFor(bin, histogram)));
        return series;
    }

    private static XYDataItem itemFor(int bin, Histogram histogram) {
        return new XYDataItem(bin, histogram.count(bin));
    }
}
