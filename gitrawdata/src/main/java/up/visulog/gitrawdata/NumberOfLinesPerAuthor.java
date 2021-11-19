package up.visulog.gitrawdata;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NumberOfLinesPerAuthor {
    private final String name;
    private int lines_added;
    private int lines_removed;

    public NumberOfLinesPerAuthor(String name, int lines_added, int lines_removed) {
        this.name = name;
        this.lines_added = lines_added;
        this.lines_removed = lines_removed;
    }

    public NumberOfLinesPerAuthor(String name) {
        this.name = name;
        this.lines_added = 0;
        this.lines_removed = 0;
    }

    //git log --author="_Your_Name_Here_" --pretty=tformat: --numstat | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }' -
    //a function that execute a command
    public static BufferedReader executeCommand(List<String> command) {
        Path gitPath = FileSystems.getDefault().getPath(".");
        ProcessBuilder builder = new ProcessBuilder( command).directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            String message="";
            for(String s : command){
                message+=s+" ";
            }
            throw new RuntimeException("Error running \"git "+message+"\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader;
    }

    public static List<NumberOfLinesPerAuthor> parseLogFromCommand() {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.add("log");
        command.add("--pretty=%n %an");
        command.add("--numstat");
        return parseNumberOfLines( executeCommand(command) );
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
                if (!line.isEmpty()) current_Author = new NumberOfLinesPerAuthor(line);
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
    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong result format.");
    }
    @Override
    public String toString() {
        return "Number Of Lines by" +
                "name='" + name + '\'' +
                ", lines_added=" + lines_added +
                ", lines_removed=" + lines_removed +
                " total="+(lines_added-lines_removed) +
                '}';
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

}
