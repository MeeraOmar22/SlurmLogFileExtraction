import java.io.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GroupProject {
	public static void main(String[] args) throws IOException {
		System.out.println("Number of jobs created/ended within a time range");
		jobstart_ended();
                chartjobstart_ended();
		System.out.println();
                
                System.out.println("Number of jobs by partitions");
                partition();
                chartpartition();
                System.out.println();
                
		System.out.println("Number of job causing error and its corresponding user");
		job_error();
                chartjob_error();
		System.out.println();
                
		System.out.println("Average execution time for jobs submitted to UMHPC");
		average_execution();
                Tableaverage_execution();
		System.out.println();
                
		System.out.println("Job exit status");
                exitstatus();
	}
	
	public static void jobstart_ended(){
	    // HashMap to store month and startJobs and endJobs
            HashMap<String, Integer> startJobs = new HashMap<>();  
            HashMap<String, Integer> endJobs = new HashMap<>();
            // try-catch to handle errors that occur while reading the file
            try (BufferedReader reader = new BufferedReader(new FileReader("/Users/User/Documents/FOP LAB.TUTORIAL/fop.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {    //// when no more line left, method returns null
                if (line.contains("sched: Allocate JobId")) {
                    //// extract the month from the line
                    String[] words = line.split(" ");
                    String date = words[0];
                    String month = date.substring(1, 8);
                    // add 1 to the count for this month in the startJobs map
                    startJobs.put(month, startJobs.getOrDefault(month, 0) + 1);
                    } 
                    else if (line.contains("done")) {
                    String[] words = line.split(" ");
                    String date = words[0];
                    String month = date.substring(1, 8);
                    // add 1 to the count for this month in the endJobs map
                    endJobs.put(month, endJobs.getOrDefault(month, 0) + 1);
                }
            }
        }
        // to catch  IOExceptions that occur while reading file
        catch (IOException e) {

        }
        // ArrayList to store the months with jobStarts or jobEnds
        ArrayList<String> months = new ArrayList<>(startJobs.keySet());
        // sort months in ascending order using collection class
        Collections.sort(months);

 
        System.out.println("----------------------------------------");
        System.out.printf("%-10s %-13s %-15s \n", "|Month", "|Jobs created", "|Jobs ended   |");
        System.out.println("----------------------------------------");
        for (String month : months) {
            System.out.printf("|%-9s |%-12d |%-13d| \n", month, startJobs.get(month), endJobs.getOrDefault(month, 0));
        }
        System.out.println("----------------------------------------");
        }
        
        //chart method for jobstart_ended
        public static void chartjobstart_ended(){
        // Create new dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // add the data to the dataset
        dataset.addValue(2191, "Jobs Created", "2022-06");
        dataset.addValue(2191, "Jobs Created", "2022-06");
        dataset.addValue(1893, "Jobs Ended", "2022-06");
        dataset.addValue(1304, "Jobs Created", "2022-07");
        dataset.addValue(1139, "Jobs Ended", "2022-07");
        dataset.addValue(1232, "Jobs Created", "2022-08");
        dataset.addValue(1186, "Jobs Ended", "2022-08");
        dataset.addValue(1253, "Jobs Created", "2022-09");
        dataset.addValue(1153, "Jobs Ended", "2022-09");
        dataset.addValue(1778, "Jobs Created", "2022-10");
        dataset.addValue(1564, "Jobs Ended", "2022-10");
        dataset.addValue(907, "Jobs Created", "2022-11");
        dataset.addValue(1092, "Jobs Ended", "2022-11");
        dataset.addValue(551, "Jobs Created", "2022-12");
        dataset.addValue(452, "Jobs Ended", "2022-12");

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart("Jobs Created and Ended", "Month", "Number of Jobs", dataset,  PlotOrientation.VERTICAL, true, true, false);

        // Create panel to display chart
        ChartPanel panel = new ChartPanel(chart);

        // Create window to display chart
        JFrame window = new JFrame("Bar Chart");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        window.pack();
        window.setVisible(true);
        }
        
        public static void partition(){
            ArrayList<String> partitions = new ArrayList<>();    //Use to store each partition name
            HashMap<String, Integer> count = new HashMap<>();    //Store the corresponding number of job by partition
            Formatter fmt = new Formatter();                     //use to make table                              
            int total_partition = 0;
            String logFile = "/Users/User/Documents/FOP LAB.TUTORIAL/fop.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                int index = line.indexOf("Partition=");
                int i = index + 10;
                //System.out.println(index);
                if(index > 0){
                   partitions.add(line.substring(i));
                }
                else {
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        for (String partition : partitions) {
            if (count.containsKey(partition)) {
                count.put(partition, count.get(partition) + 1);
            }
            else {
                count.put(partition, 1);
            }
        }
        fmt.format("-------------------------------\n");
        fmt.format("%-15s %15s\n", "Partition", "Number of jobs");
        fmt.format("-------------------------------\n");
        for (String partition : count.keySet()) {
            fmt.format("%-14s  %14s\n",partition,count.get(partition));
            total_partition += count.get(partition);
        }
        fmt.format("-------------------------------\n");
        fmt.format("%-14s  %12s\n", "Total Partition:", total_partition);
        System.out.println(fmt);
        
    }
        
        public static void chartpartition(){
            DefaultPieDataset dataset = new DefaultPieDataset();
            // Create new dataset
            dataset.setValue("gpu-v100s ", 588);
            dataset.setValue("cpu-epyc", 2756);
            dataset.setValue("cpu-opteron", 4509);
            dataset.setValue("gpu-titan", 640);
            dataset.setValue("gpu-k40c", 295);
            dataset.setValue("gpu-k10", 428);
            
            // Create chart
            JFreeChart pieChart = ChartFactory.createPieChart(
                "Number Of Job By Partitions",
                dataset,
                false, true, false);

            // Create panel to display chart
            ChartPanel chartPanel = new ChartPanel(pieChart);
            chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            chartPanel.setBackground(Color.white);
            JFrame window = new JFrame("pie Chart");

            // Create window to display chart
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(chartPanel);
            window.pack();
            window.setVisible(true);
        }
        
        public static void job_error(){
            try {
		    BufferedReader reader = new BufferedReader(new FileReader ("/Users/User/Documents/FOP LAB.TUTORIAL/fop.txt"));
		    String line;
                    int count = 0,x=0;
                    while((line=reader.readLine())!=null)
                    {
                        String[] words=line.split(" ");
                        
                            for(String a : words)
                            {
                                 if(a.equals("error:"))
                                {
                                    //System.out.println(count+" "+line);
                                    for(String b:words)
                                    {
                                        if(b.equals("This"))
                                        {
                                            for(String c : words)
                                            {
                                                if(c.equals("association"))
                                                        {
                                                            String test=words[5];   
                                                            String[] word=test.split("[,]");
                                                            
                                                                    
                                                                    for(String order : word)
                                                                    {
                                                                      
                                                                     System.out.printf("%03d\t",count+1);
                                                                     System.out.print(order);
                                                                     System.out.println();
                                                                     count++;
                                                                    }
                                                                 
                                                        }
                                            }
                                        } 
                                    }
                                    
                                    
                                }
                            }
                       
                    }
                    reader.close();
		    
		} catch(IOException e) {
		    System.out.println("Error reading file");
		}
        }
        
        public static void chartjob_error(){
             DefaultCategoryDataset dataset = new DefaultCategoryDataset();
             //TODO: Extract data from your data source and add it to the dataset
             dataset.addValue( 3.0,"Error","lobbeytan" );
             dataset.addValue( 2.0,"Error", "tingweijing" );
             dataset.addValue( 4.0,"Error", "f4ww4z" );
             dataset.addValue( 1.0,"Error", "xinpeng" );
             dataset.addValue( 2.0,"Error", "aznul" );
             dataset.addValue( 1.0,"Error", "hass" );
             dataset.addValue( 3.0,"Error", "liew.wei.shiung" );
             dataset.addValue( 4.0,"Error", "roland" );
             dataset.addValue( 6.0,"Error", "shahreeza" );
             dataset.addValue( 4.0,"Error", "janvik" );
             dataset.addValue( 4.0,"Error", "lin0618" );
             dataset.addValue( 1.0,"Error", "fahmi8" );
             dataset.addValue( 3.0,"Error", "farhatabjani" );
             dataset.addValue( 1.0,"Error", "kurk" );
             dataset.addValue( 10.0,"Error", "fairus" );
             dataset.addValue( 4.0,"Error", "manoj" );
             dataset.addValue( 11.0,"Error", "han" );
             dataset.addValue( 11.0,"Error", "aah" );
             dataset.addValue( 24.0,"Error", "mk_98" );
             dataset.addValue( 5.0,"Error", "'hva170037" );
             dataset.addValue( 1.0,"Error","yatyuen.lim" );
             dataset.addValue( 1.0,"Error", "ongkuanhung" );
             dataset.addValue( 2.0,"Error", "chiuling" );
             dataset.addValue( 4.0,"Error", "noraini" );
             dataset.addValue( 9.0,"Error", "hongvin" );
             dataset.addValue( 21.0,"Error", "htt_felicia" );
             
             JFreeChart barChart = ChartFactory.createBarChart("Number of jobs causing error and the corresponding user","User","Number of error",dataset,PlotOrientation.HORIZONTAL,true, true, false);
             ChartPanel chartPanel = new ChartPanel( barChart );
             chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 900 ) );
             JFrame window = new JFrame("Bar Chart");
             window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             window.add(chartPanel);
             window.pack();
             window.setVisible(true);
        }
        
        public static void average_execution(){
            String[] index = new String[50000];
       String[] detail = new String[50000];
       String[] b = new String[50000];
       String[] month = new String[50000];
       String[] mon = new String[50000];
       String[] date = new String[50000];
       String[] start = new String[50000];
       String[] end = new String[50000];
       String[] JobID1 = new String[50000];
       String[] JobID2 = new String[50000];
       double[] time = new double[50000];
       String[] total= new String[50000];


       try{


           Scanner input = new Scanner(new FileInputStream("/Users/User/Documents/FOP LAB.TUTORIAL/fop.txt"));


           int i = 0;
           int r = 0;
           int p = 0;
         

           while(input.hasNextLine()){
               String str = input.nextLine();
               index[i]= str;
               String[] temp = str.split(" ");
               detail[i] = temp[1];


               String string = temp[0];
               b = string.replaceAll("[\\[\\]]", "").split("T");
               date[i] = b[0]+" "+b[1];
               month = b[0].split("-");
               mon[i] = month[1];


               if(detail[i].equals("sched:") && temp[2].equals("Allocate")){
                   start[r] = date[i];
                   String ID = temp[3].replaceAll("[^0-9]","");
                   JobID1[r] = ID;
                   r++;}


               if(detail[i].equals("_job_complete:")&&temp[3].equals("done")){
                   end[p] = date[i];
                   String ID2 = temp[2].replaceAll("[^0-9]","");
                   JobID2[p] = ID2;
                   p++;
               }


               i++;
           }
           System.out.println("+--------+-------+-----------------------------+-----------------------------+----------------+-----------+");
           System.out.printf("| %6s | %-5s |    %-24s |    %-24s | %-15s| %-10s|\n", "No.", "JobID", "Start", "End", "Time Taken", "Time(ms)");
           System.out.println("+--------+-------+-----------------------------+-----------------------------+----------------+-----------+");
           {
               int count = 0;
               long sum=0;
               long[] a = new long[50000];
               for(int h=0; h<p; h++){
                   for(int k=0; k<r; k++){
                       if(JobID2[h].equals(JobID1[k])){
                           int cnt = 0;
                           a[cnt] =AverageTime(end[h], start[k]);
                           total[cnt] = Convert(a[cnt]);
                           sum += a[cnt];




                           System.out.printf("|%6d. | %-5s |   %-25s |   %-25s | %-15s| %-10d|\n",count+1, JobID1[k], start[k], end[h], total[cnt], a[cnt]);
                           count++;
                           cnt++;
    break;
                       }
                   }
               }
               System.out.println("+--------+-------+-----------------------------+-----------------------------+----------------+-----------+");


               long average = sum/(count+1);




               System.out.println("+-----------------+------------------------+");
               System.out.printf("|   Total Time    |                        |\n");
               System.out.println("+-----------------+------------------------+");
               System.out.printf("|       ms        |       %-10s     |\n",sum);
               System.out.printf("| DD:HH:mm:ss.SSS |     %-10s    |\n",Convert(sum));
               System.out.println("+-----------------+------------------------+");
               System.out.printf("|     Average     |                        |\n");
               System.out.println("+-----------------+------------------------+");
               System.out.printf("|       ms        |        %-10s      |\n",sum/(count+1));
               System.out.printf("|  HH:mm:ss.SSS   |       %-10s     |\n",Convert(average));
               System.out.println("+-----------------+------------------------+");
               System.out.printf("|       Max       |                        |\n");
               System.out.println("+-----------------+------------------------+");
               System.out.printf("|       ms        |        %-10s      |\n",Max(a));
               System.out.printf("| DD:HH:mm:ss.SSS |      %-10s     |\n",Convert(Max(a)));
               System.out.println("+-----------------+------------------------+");
               Min(a);
               System.out.println("");

           }


           input.close();
       }


       catch(FileNotFoundException e){
           System.out.println("File not found");
       }


   }
  static long AverageTime(String end_date,String start_date){
       SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
       long time_difference = 0;
       try{
           Date d1 = obj.parse(start_date);
           Date d2 = obj.parse(end_date);


           // Calculate time difference
           time_difference = d2.getTime() - d1.getTime();
       }
       catch (Exception e) {
           e.printStackTrace();
       }
       return time_difference;
   }
   static long Max(long[] total){
       long max = 0;
       for(int i = 0;i<total.length; i++){
           max = Math.max(max,total[i]);
       }
       return max;
   }
  static void Min(long[] total){
       long min = total[0];
       for (int i = 1; i < total.length; i++) {
           min = Math.min(min, total[i]);
       }


       System.out.println("Minimum Value: " + min);
   }
   static String Convert(long time_difference){
       String result;
       long days_difference = (time_difference / (1000 * 60 * 60 * 24)) % 365;
       long hours_difference = (time_difference   / (1000 * 60 * 60)) % 24;
       long minutes_difference = (time_difference / (1000 * 60))% 60;
       long seconds_difference = (time_difference / 1000) % 60;
       long milliseconds_difference = time_difference % 1000;


       // Print the date difference in years, days, hours, minutes amd seconds
       result = days_difference+ ":"+hours_difference + ":" + minutes_difference + ":" + seconds_difference+ "." +milliseconds_difference;
       return result;
       
   }
       
       public static void Tableaverage_execution(){
           // frame
	JFrame f;
	// Table
	JTable j;

		// Frame initialization
		f = new JFrame();

		// Frame Title
		f.setTitle("Average execution time for jobs submitted to UMHPC");

		// Data to be displayed in the JTable
		String[][] data = {
			{ "207562420180 ms", "27583045 ms", "595500126 ms" },
			{ "(DD:HH:mm:ss.SSS) 212:8:13:40.180", "(DD:HH:mm:ss.SSS) 0:7:39:43.45", "(DD:HH:mm:ss.SSS) 6:21:25:0.126" }
		};

		// Column Names
		String[] columnNames = { "Total Time", "Average", "Max" };

		// Initializing the JTable
		j = new JTable(data, columnNames);
		j.setBounds(30, 40, 200, 300);

		// adding it to JScrollPane
		JScrollPane sp = new JScrollPane(j);
		f.add(sp);
		// Frame Size
		f.setSize(750, 200);
		// Frame Visible = true
		f.setVisible(true);
	}
            
    

        public static void exitstatus() throws IOException{
            String file = "/Users/User/Documents/FOP LAB.TUTORIAL/fop.txt";
            List<String> allLines = Files.readAllLines(Paths.get(file));
            int exitZero=0, exitOne=0;
            int six=0, seven=0, eight=0, nine=0, ten=0, eleven=0, twelve=0;
            int sixZero=0, sevenZero=0, eightZero=0, nineZero=0, tenZero=0, elevenZero=0, twelveZero=0; 
            for(String line : allLines){
                Pattern pattern3 = Pattern.compile("(\\[)([0-9]+)(-)([0-9]+)(-[0-9]+)+(T)(([+-]?(?=\\.\\d|\\d)(?:\\d+)?(?:\\.?\\d*))(?:[eE]([+-]?\\d+))?(:([+-]?(?=\\.\\d|\\d)(?:\\d+)?(?:\\.?\\d*))(?:[eE]([+-]?\\d+))?)+)(\\])(\\s_[a-zA-Z]+_[a-zA-Z]+:\\s)([a-zA-Z]+=[0-9]+\\s)([WEXITSTATUS^]+\\s)(\\d)");
                Matcher matcher3= pattern3.matcher(line);
    
         
            while(matcher3.find()) { 
              
              if(matcher3.group(4).contains("6") && matcher3.group(17).contains("1")){
                  six++;
              }else if(matcher3.group(4).contains("7") && matcher3.group(17).contains("1")){
                  seven++;
              }else if(matcher3.group(4).contains("8") && matcher3.group(17).contains("1")){
                  seven++;
              }else if(matcher3.group(4).contains("9") && matcher3.group(17).contains("1")){
                  seven++;
              }else if(matcher3.group(4).contains("10") && matcher3.group(17).contains("1")){
                  seven++;
              }else if(matcher3.group(4).contains("11") && matcher3.group(17).contains("1")){
                  seven++;
              }else if(matcher3.group(4).contains("12") && matcher3.group(17).contains("1")){
                  seven++;
              }else if(matcher3.group(4).contains("6") && matcher3.group(17).contains("0")){
                  sixZero++;
              }else if(matcher3.group(4).contains("7") && matcher3.group(17).contains("0")){
                  sevenZero++;
              }else if(matcher3.group(4).contains("8") && matcher3.group(17).contains("0")){
                  eightZero++;
              }else if(matcher3.group(4).contains("9") && matcher3.group(17).contains("0")){
                  nineZero++;
              }else if(matcher3.group(4).contains("10") && matcher3.group(17).contains("0")){
                  tenZero++;
              }else if(matcher3.group(4).contains("11") && matcher3.group(17).contains("0")){
                  elevenZero++;
              }else if(matcher3.group(4).contains("12") && matcher3.group(17).contains("0")){
                  twelveZero++;
            }
        }
   }
        
   
                
        System.out.println("Numbers of Works with Exit 1 by month");
        System.out.println("June: " + six);
        System.out.println("July: " + seven);
        System.out.println("August: " + eight);
        System.out.println("Semptember: " + nine);  
        System.out.println("October: " + ten);
        System.out.println("November: " + eleven);
        System.out.println("December: " + twelve);
        System.out.println("");
            
        System.out.println("Numbers of Works with Exit 0 by month");
        System.out.println("June: " + sixZero);
        System.out.println("July: " + sevenZero);
        System.out.println("August: " + eightZero);
        System.out.println("Semptember: " + nineZero);  
        System.out.println("October: " + tenZero);
        System.out.println("November: " + elevenZero);
        System.out.println("December: " + twelveZero);
        
        exitZero = sixZero + sevenZero + eightZero + nineZero + tenZero + elevenZero + twelveZero;
        exitOne = six + seven + eight + nine + ten + eleven + twelve;
        System.out.println("");
        System.out.println("Total number of works with Exit 0: " + exitZero);
        System.out.println("Total number of works with Exit 1: " + exitOne);
        
        // Create Dataset  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
        // Number of works in June  
    dataset.addValue(513, "Exit Status : 1 ", "June");  
    dataset.addValue(1292, "Exit Status : 0", "June"); 
    
      // Number of works in July
    dataset.addValue(1777, "Exit Status : 1 ", "July");  
    dataset.addValue(779, "Exit Status : 0", "July");  
    
     // Number of works in August
    dataset.addValue(0, "Exit Status : 1 ", "August");  
    dataset.addValue(782, "Exit Status : 0", "August"); 
    
     // Number of works in September
    dataset.addValue(0, "Exit Status : 1 ", "September");  
    dataset.addValue(821, "Exit Status : 0", "September"); 
    
     // Number of works in October
    dataset.addValue(0, "Exit Status : 1 ", "October");  
    dataset.addValue(1087, "Exit Status : 0", "October"); 
    
     // Number of works in November
    dataset.addValue(0, "Exit Status : 1 ", "November");  
    dataset.addValue(687, "Exit Status : 0", "November"); 
    
     // Number of works in December
    dataset.addValue(0, "Exit Status : 1 ", "December");  
    dataset.addValue(331, "Exit Status : 0", "December");  

      
         //Create chart  
        JFreeChart chart = ChartFactory.createBarChart(  
        "Number of Works with Exit Status by month", //Chart Title  
        "Month", // x-axis  
        "Number of works", // y-axis 
        dataset,  
        PlotOrientation.VERTICAL,  
        true,true,false  
       );
        ChartPanel panel = new ChartPanel(chart);

        // Create window to display chart
        JFrame window = new JFrame("Bar Chart");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        window.pack();
        window.setVisible(true);

   }

        }