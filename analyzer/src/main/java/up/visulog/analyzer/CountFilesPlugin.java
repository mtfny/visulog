package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.NumberOfFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountFilesPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountFilesPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }


    static Result processLog(Integer numFiles) {
        var result = new Result();
        result.NumberOfFiles=numFiles;
        return result;
    }

    @Override
    public void run() {
        result =  processLog( NumberOfFiles.parseNumberOfFilesCommand() );
    }

    @Override
    public Result getResult() {
        //If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private Integer NumberOfFiles=null;

        //Method that returns the hashmap that contains the number of commits associated with each author
        Integer getNumberOfFiles() {
            return NumberOfFiles;
        }

        @Override
        //Method that returns the commitsPerAuthor list in String form
        public String getResultAsString() {
            return NumberOfFiles.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the commitsPerAuthor list as an html page
        public String getResultAsHtmlDiv() {
            return "<div class=\"module\" hidden>fileCounter</div><div id=\"data-file-number\" hidden><div data-file-number=" + NumberOfFiles + "></div></div>";
        }
    }


}

