import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class Charts {
	
	private JFrame frame;
	/**
	 * Create the application.
	 */
	public Charts() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
     * Method that draw a comparison chart depending on the percentages sent as parameters 
     * @param percentages
     * @return non
     */
	void Comparison(float dbpedia,float wikidata,float quora,float neural)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(dbpedia,"Range","DBpedia");
		dataset.setValue(wikidata,"Range","Wikidata");
		dataset.setValue(quora,"Range","Quora");
		dataset.setValue(neural,"Range","Neural Network");

		JFreeChart chart2 = ChartFactory.createBarChart("Results Comparison", "Data Sources","Ranges(normalization)" ,dataset,PlotOrientation.VERTICAL , false,true,false);
		CategoryPlot p=chart2.getCategoryPlot();
		p.setRangeGridlinePaint(Color.black);
		ChartFrame frame2=new ChartFrame("Bar Chart for Comparison",chart2);
		frame2.setVisible(true);
		frame2.setSize(590,490);
	}
	
	/**
     * Method that draw a pie chart depending on the percentages sent as parameters 
     * @param percentages
     * @return non
     */
	void Malicious_Category(float military,float nuclear,float terrorism,float weapon,float technology,float security,float harm,float suicide,String title)
	{
		// create a dataset...
				DefaultPieDataset data = new DefaultPieDataset();
				data.setValue("Military", military);
				data.setValue("Nuclear", nuclear);
				data.setValue("Terrorism", terrorism);
				data.setValue("Weapon", weapon);
				data.setValue("Technology", technology);
				data.setValue("Security", security);
				data.setValue("Harm", harm);
				data.setValue("Suicide", suicide);
				// create a chart...
				JFreeChart chart = ChartFactory.createPieChart3D(
																"Most Malicious Category"+title,
																data,
																true, // legend?
																true, // tooltips?
																false // URLs?
																);
				final PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {1}");
			/*	final PiePlot plot1 = (PiePlot) chart.getPlot();
				plot1.setLabelGenerator(labelGenerator);
			    ChartFrame frame = new ChartFrame("PieChart", chart);
				frame.pack();
				frame.setVisible(true);	*/
				
			     final PiePlot3D plot3 = (PiePlot3D) chart.getPlot();
			     plot3.setLabelGenerator(labelGenerator);
			     ChartFrame frame = new ChartFrame("PieChart", chart);
					frame.pack();
					frame.setVisible(true);
				
				//create and display a frame...
				/*ChartFrame frame = new ChartFrame("PieChart", chart);
				frame.pack();
				frame.setVisible(true);	*/
	}
}
