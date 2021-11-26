package up.visulog.analyzer;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


import static org.junit.Assert.assertEquals;

public class TestCountPLPercentagePlugin {
    // Let's check percentage of every programming language

    //Tests the number of files
    @Test
    public  void checkSum() throws IOException, URISyntaxException {
        //{Java=38.31325301204819, CSS=20.240963855421686, HTML=41.44578313253012}
        var uri = TestCountFilesPlugin.class.getClassLoader().getResource("fileTest").toURI();
        File file = new File(uri);
        CountPLPercentagePlugin.Result res =CountPLPercentagePlugin.getPL_percentage(file.toPath());
        HashMap<String , Double> expected=new HashMap<>();
        expected.put("Java",38.31325301204819);
        expected.put("CSS",20.240963855421686);
        expected.put("HTML",41.44578313253012);
        assertEquals(expected.size(), res.getPLpercentage().size());
        for (Map.Entry<String, Double> entry : res.getPLpercentage().entrySet()) {
            assertEquals(entry.getValue(),expected.get(entry.getKey()));
        }
    }


}
