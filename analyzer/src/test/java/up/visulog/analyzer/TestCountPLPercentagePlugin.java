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
        //{Java=41.81818181818182, CSS=19.09090909090909, HTML=39.09090909090909}
        var uri = TestCountFilesPlugin.class.getClassLoader().getResource("fileTest").toURI();
        File file = new File(uri);
        CountPLPercentagePlugin.Result res =CountPLPercentagePlugin.getPL_percentage(file.toPath());
        HashMap<String , Double> expected=new HashMap<>();

        expected.put("Java",41.81818181818182);
        expected.put("CSS",19.09090909090909);
        expected.put("HTML",39.09090909090909);
        assertEquals(expected.size(), res.getPLpercentage().size());
        for (Map.Entry<String, Double> entry : res.getPLpercentage().entrySet()) {
            assertEquals(entry.getValue(),expected.get(entry.getKey()));
        }
    }


}
