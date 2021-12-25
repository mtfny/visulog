package up.visulog.analyzer;

import java.lang.Runnable;

public interface AnalyzerPlugin extends Runnable {
    public static final String sortAlphabetically = "-a";
    public static final String sortNumerically = "-n";
    public static final String reverse = "-r";
	
    interface Result {
        String getResultAsString();
        String getResultAsHtmlDiv();
    }

    /**
     * run this analyzer plugin
     */
    void run();

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already done.
     */
    Result getResult();
    
    
}
