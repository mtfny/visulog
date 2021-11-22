package up.visulog.analyzer;

import up.visulog.config.Configuration;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CountPLPercentagePlugin implements AnalyzerPlugin{
    private final Configuration configuration;
    private Result result;
    private static final Programming_languages p_languages=new Programming_languages();

    public CountPLPercentagePlugin(Configuration configuration) {
        this.configuration = configuration;
    }

    public static Result getPL_percentage() {
        var result = new Result();
        Path gitPath = FileSystems.getDefault().getPath(".");
        File Root=gitPath.toFile();
        HashMap<String,Integer> res=new HashMap<>();
        PL(Root,res);
        result.PLpercentage=res;
        return result;
    }

    public static void PL(File file,HashMap<String,Integer> res) {
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isHidden();
            }
        });
        if(files==null) parseError();
        for (File f : files) {
            if (!f.isDirectory()) {
                add(f, res);
            }else{
                PL(f, res);
            }
        }
    }

    public static void add(File file,HashMap<String,Integer> res){
        Integer size= (int)file.length();
        String extension= file.getName().substring(file.getName().lastIndexOf("."));
        String pro_lang=p_languages.dictionnary.get(extension);
        if(pro_lang==null) return;
        Integer another_size=res.getOrDefault(pro_lang,0);
        res.put(pro_lang,another_size+size);
    }



    @Override
    public void run() {
        result = getPL_percentage();
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    private static void parseError() {
        throw new RuntimeException("Can't reqad files");
    }

    static class Result implements AnalyzerPlugin.Result {
        private Map<String, Integer> PLpercentage = new HashMap<>();

        //Method that returns the hashmap that contains the percentage of each programmibng language
        public Map<String, Integer> getPLpercentage() {
            return PLpercentage;
        }

        @Override
        //Method that returns the PLpercentage list in String form
        public String getResultAsString() {
            return PLpercentage.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the PLpercentage list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div class=\"module\" hidden>languages</div><div id=\"data-langages-parts\" hidden>");
            for (var item : PLpercentage.entrySet()) {
                html.append("<div data-langages-parts=\"").append(item.getKey()).append("\">").append(item.getValue()).append("</div>");
            }
            html.append("</div>");
            return html.toString();
        }
    }

    private static class Programming_languages{
        private final HashMap<String,String> dictionnary ;
        public Programming_languages() {
            this.dictionnary=new HashMap<>();
            dictionnary.put(".css","CSS");
            dictionnary.put(".java","Java");
            dictionnary.put(".js","JavaScript");
            dictionnary.put("._js","JavaScript");
            dictionnary.put(".bones","JavaScript");
            dictionnary.put(".es","JavaScript");
            dictionnary.put(".es6","JavaScript");
            dictionnary.put(".frag","JavaScript");
            dictionnary.put(".gs","JavaScript");
            dictionnary.put(".jake","JavaScript");
            dictionnary.put(".jsb","JavaScript");
            dictionnary.put(".jscad","JavaScript");
            dictionnary.put(".jsfl","JavaScript");
            dictionnary.put(".jsm","JavaScript");
            dictionnary.put(".jss","JavaScript");
            dictionnary.put(".njs","JavaScript");
            dictionnary.put(".pac","JavaScript");
            dictionnary.put(".sjs","JavaScript");
            dictionnary.put(".ssjs","JavaScript");
            dictionnary.put(".sublime-build","JavaScript");
            dictionnary.put(".sublime-commands","JavaScript");
            dictionnary.put(".sublime-completions","JavaScript");
            dictionnary.put(".sublime-keymap","JavaScript");
            dictionnary.put(".sublime-macro","JavaScript");
            dictionnary.put(".sublime-menu","JavaScript");
            dictionnary.put(".sublime-mousemap","JavaScript");
            dictionnary.put(".sublime-project","JavaScript");
            dictionnary.put(".sublime-settings","JavaScript");
            dictionnary.put(".sublime-theme","JavaScript");
            dictionnary.put(".sublime-workspace","JavaScript");
            dictionnary.put(".sublime_metrics","JavaScript");
            dictionnary.put(".sublime_session","JavaScript");
            dictionnary.put(".xsjs","JavaScript");
            dictionnary.put(".html","HTML");
            dictionnary.put(".htm","HTML");
            dictionnary.put(".html.hl","HTML");
            dictionnary.put(".st","HTML");
            dictionnary.put(".xht","HTML");
            dictionnary.put(".xhtml","HTML");
            dictionnary.put(".php","PHP");
            dictionnary.put(".aw","PHP");
            dictionnary.put(".ctp","PHP");
            dictionnary.put(".fcgi","PHP");
            dictionnary.put(".inc","PHP");
            dictionnary.put(".php3","PHP");
            dictionnary.put(".php4","PHP");
            dictionnary.put(".phps","PHP");
            dictionnary.put(".phpt","PHP");
            dictionnary.put(".kt","Kotlin");
            dictionnary.put(".ktm","Kotlin");
            //incomplete list
        }
    }

}
