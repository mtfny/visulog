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
        Path htmlPath = Path.of("test.html");
        String htmlString = Files.readString(htmlPath);
        htmlString = htmlString.replace("$replace", this.htmlDiv);
        BufferedWriter writer = new BufferedWriter(new FileWriter("template.html"));
        writer.write(htmlString);
        writer.close();
    }

}
