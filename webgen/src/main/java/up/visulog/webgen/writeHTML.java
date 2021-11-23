package up.visulog.webgen;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;


import up.visulog.analyzer.*;
import java.io.InputStream;
import java.io.InputStreamReader;

public class writeHTML {
    private String htmlDiv;
    private final static String resultPath = "../resultat/";

    public writeHTML(AnalyzerResult result) {
        String html = "";
        List<AnalyzerPlugin.Result> subResult = result.getSubResults();
        for (int i = 0; i < subResult.size(); i++) {
            html += subResult.get(i).getResultAsHtmlDiv() + "\n";
        }
        this.htmlDiv = html;
    }

    //Creates an HTML file that contains the result of the analysis by using a template file (template.html)
    public void createhtmlFile() throws IOException, URISyntaxException {
    	//Get the HTML template as an inputStream and turns it into a string
    	InputStream template = writeHTML.class.getClassLoader().getResourceAsStream("template.html");
    	String htmlString = InputStreamToString(template);
        
        //Replace "$replace" by the html div of this instance
        htmlString = htmlString.replace("$replace", this.htmlDiv);
        
        //Create a new BufferedWriter who allow to write in a file
        //Create also a new File HTML
        BufferedWriter writer = new BufferedWriter(new FileWriter(getResultFolder()  + "/newFile.html"));
        //Write our string in the new File
        writer.write(htmlString);
        writer.close();

    }
    
    //returns the path to the result folder where the file will be written
    private String getResultFolder() {
    	try {
    		File f =  new File(resultPath);
    		String path = f.getCanonicalPath();
    		return path;
    	}
    	
    	catch(IOException e) {
    		return null;
    	}
    }
    
    
    //Returns a string that contains the contents of an inputStream
    private String InputStreamToString(InputStream input) {
        String text = new BufferedReader(
          new InputStreamReader(input, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));

        return text;
    }

    //Open an htlm file in the default browser
    public void openHtlmFile() {
        String s=getResultFolder()+"/newFile.html";
        URI u = new File(s).toURI();

        try {

            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(u);
            }

        }
        catch (Exception e){
            e.printStackTrace();
            // Notify the user of the failure
            System.out.println("Failed to open the webpage on your default browser.");
            System.out.println("Webpage: " + u);
        }
    }


}