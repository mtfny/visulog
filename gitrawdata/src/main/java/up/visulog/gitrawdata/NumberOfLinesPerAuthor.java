package up.visulog.gitrawdata;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NumberOfLinesPerAuthor {
    private final String name;
    private Date date;
    private int lines_added;
    private int lines_removed;

    public NumberOfLinesPerAuthor(String name, Date date, int lines_added, int lines_removed) {
        this.name = name;
        this.date = date;
        this.lines_added = lines_added;
        this.lines_removed = lines_removed;
    }

    public NumberOfLinesPerAuthor(String name,Date date) {
        this.name = name;
        this.date = date;
        this.lines_added = 0;
        this.lines_removed = 0;
    }

    //git log --pretty="%nName :%an %nDate :%ad" --numstat
    public static List<NumberOfLinesPerAuthor> parseLogFromCommand(Path gitPath, List<String> command) {
        command.add("--pretty=%nName :%an %nDate :%ad");
        command.add("--numstat");
        return parseNumberOfLines(Commit.executeGitCommand(gitPath,command));
    }

    public static List<NumberOfLinesPerAuthor> parseNumberOfLines(BufferedReader reader) {
        var result = new ArrayList<NumberOfLinesPerAuthor>();
        Optional<NumberOfLinesPerAuthor> current_Author = parseNumberOfLinesPerAuthor(reader);

        while (current_Author.isPresent()) {
            boolean alreadyPresent=false;
            for (NumberOfLinesPerAuthor n: result) {
                if(n.name.equals(current_Author.get().name)) {
                    n.lines_added += current_Author.get().lines_added;
                    n.lines_removed += current_Author.get().lines_removed;
                    alreadyPresent=true;
                }
            }
            if(!alreadyPresent){
                result.add(current_Author.get());
            }
            current_Author = parseNumberOfLinesPerAuthor(reader);
        }
        for (NumberOfLinesPerAuthor n: result) {
            n.date=null;
        }
        return result;
    }

    public static List<NumberOfLinesPerAuthor> getAllLinesPerAuthor(BufferedReader reader) {
        var result = new ArrayList<NumberOfLinesPerAuthor>();
        Optional<NumberOfLinesPerAuthor> current_Author = parseNumberOfLinesPerAuthor(reader);

        while (current_Author.isPresent()) {
            result.add(current_Author.get());
            current_Author = parseNumberOfLinesPerAuthor(reader);
        }
        return result;
    }


    public static Optional<NumberOfLinesPerAuthor> parseNumberOfLinesPerAuthor(BufferedReader input) {
        try {
            var line = input.readLine();
            // if no line can be read, we are done reading the buffer
            if (line == null) {
                return Optional.empty();
            }
            line = line.trim();
            NumberOfLinesPerAuthor current_Author = null;

            do {
                if (!line.isEmpty()) {
                    if(!line.startsWith("Name :")) parseError();
                    String name=line.substring(line.indexOf(":")+1);
                    line = input.readLine();
                    if (line == null) {
                        return Optional.empty();
                    }
                    line = line.trim();
                    if(!line.startsWith("Date :")) parseError();
                    String datest=line.substring(line.indexOf(":")+1);
                    Date date=setDate(datest);
                    current_Author = new NumberOfLinesPerAuthor(name,date);
                }
                line = input.readLine();
                if (line == null) {
                    return Optional.empty();
                }
                line = line.trim();
            } while (!line.matches("^[0-9-]+\\s+[0-9-]+\\s+.*$"));

            //_____________________________________________

            while (!line.equals("")) {
                var parts = line.split("\\s+");
                current_Author.lines_added += toNumber(parts[0]);
                current_Author.lines_removed += toNumber(parts[1]);

                //prepare next iteration
                line = input.readLine();
                if (line == null) {
                    return Optional.of(current_Author);
                }
                line = line.trim();
            }
            return Optional.of(current_Author);
            //_____________________________________________
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    //a function that tests if a string contains only numbers
    private static int toNumber(String input){
        if(input.equals("-")) return 0;
        else {
            try {
                return Integer.parseInt(input);

            } catch (Exception e) {
                parseError();
            }
        }
        return 0;
    }

    private static Date setDate(String dateSt) { // Convert a String into a Date and change the date attribute of the commit
        SimpleDateFormat s= new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        Date date=null;
        try {
            date=s.parse(dateSt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong result format.");
    }
    @Override
    public String toString() {
        SimpleDateFormat s= new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        String res = "NumberOfLinesPerAuthor{" + "name=" + name ;
        if(date!=null)   res+=" date=" + s.format(date).toString();
        res+=" lines_added=" + lines_added + " lines_removed=" + lines_removed + " total="+( lines_added-lines_removed)+ '}';
        return res;
    }

    public String getName() {
        return name;
    }
    public int getLines_added() {
        return lines_added;
    }
    public int getLines_removed() {
        return lines_removed;
    }
    public Date getDate() {
        return date;
    }

    public static void main(String[] args) {
        Path gitPath = FileSystems.getDefault().getPath(".");
        var res=parseLogFromCommand(gitPath, new ArrayList<String>());
        System.out.println(res.toString());
    }
}
