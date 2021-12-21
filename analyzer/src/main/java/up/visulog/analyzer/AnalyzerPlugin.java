package up.visulog.analyzer;

import java.lang.Runnable;

public interface AnalyzerPlugin extends Runnable {
    public static final String sortAlphabetically = "-Alphabetically";
    public static final String sortNumerically = "-Numerically";
    public static final String reverse = "-Reverse";
	
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
