import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class writeHTML {
    private String htmlDiv;

    public writeHTML(String html) {
        this.htmlDiv = html;
    }

    public void createhtmlFile() throws IOException {
    	//Create an object Path
        Path htmlPath = Path.of("template.html");
        //Convert template.html to String
        String htmlString = Files.readString(htmlPath);
        //Replace "$replace" by the html div of this instance
        htmlString = htmlString.replace("$replace", this.htmlDiv);
        //Create a new BufferedWriter who allow to write in a file
        //Create also a new File HTML
        BufferedWriter writer = new BufferedWriter(new FileWriter("newFile.html"));
        //Write our string in the new File
        writer.write(htmlString);
        writer.close();
    }

}
