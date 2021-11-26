package up.visulog.analyzer;

import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;

public class TestCountFilesPlugin {
    // Let's check whether the number of files is preserved

    //Tests the number of files
    @Test
    public  void checkSum() throws IOException, URISyntaxException {
        var uri = TestCountFilesPlugin.class.getClassLoader().getResource("fileTest").toURI();
        File file = new File(uri);
        int expected=3;
        int count=CountFilesPlugin.getFilesCount(file);
        assertEquals(count,expected);
    }

    //Tests the number of files including hidden files
    @Test
    public  void checkAllSum() throws IOException, URISyntaxException{
        var uri = TestCountFilesPlugin.class.getClassLoader().getResource("fileTest").toURI();
        File file = new File(uri);
        int expected=4;
        int count=CountFilesPlugin.getAllFilesCount(file);
        assertEquals(count,expected);
    }

}
