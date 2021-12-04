package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Date;
import java.util.LinkedList;
import java.math.BigInteger;

public class Commit {
    public final BigInteger id;
    public final Date date;
    public final String author;
    public final String description;
    public final String mergedFrom;

    public Commit(BigInteger id, String author, Date date, String description, String mergedFrom) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
    }
    
    //a function that execute a command
    public static BufferedReader executeGitCommand(Path gitPath,List<String> command) {
    	command.add(0, "git");
        ProcessBuilder builder = new ProcessBuilder(command).directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            String message="";
            for(String s : command){
                message=message+s+" ";
            }
            throw new RuntimeException("Error running \"git "+message+"\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader;
    }
    
    public static List<Commit> parseLogFromCommand(Path gitPath, List<String> args) {
        return parseLog(executeGitCommand(gitPath, args));
    }

    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }

    /**
     * Parses a log item and outputs a commit object. Exceptions will be thrown in case the input does not have the proper format.
     * Returns an empty optional if there is nothing to parse anymore.
     */
    public static Optional<Commit> parseCommit(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");
            if (!idChunks[0].equals("commit")) parseError();
            var builder = new CommitBuilder(idChunks[1]);

            line = input.readLine();

            boolean authorPresent=false;
            boolean mergePresent=false;
            boolean datePresent=false;

            while (!line.isEmpty()) {
                var colonPos = line.indexOf(":");
                var fieldName = line.substring(0, colonPos);
                var fieldContent = line.substring(colonPos + 1).trim();
                switch (fieldName) {
                    case "Author":
                        builder.setAuthor(fieldContent);
                        authorPresent=true;
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        mergePresent=true;
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        datePresent=true;
                        break;
                    default: // TODO: warn the user that some field was ignored
                        if(input.readLine()==null){
                            if (!authorPresent) System.out.println("!WARNING! Author field missing");
                            if (!mergePresent) System.out.println("!WARNING! Merge field missing");
                            if (!datePresent) System.out.println("!WARNING! Date field missing");
                            if (!authorPresent || !datePresent) parseError();
                            break;
                        }
                }
                line = input.readLine(); //prepare next iteration
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }

    @Override
    public String toString() {
    	SimpleDateFormat s= new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        return "Commit{" +
                "id='" + id.toString(16) + '\'' +
                (mergedFrom != null ? ("mergedFrom...='" + mergedFrom + '\'') : "") + //TODO: find out if this is the only optional field
	    ", date='" + s.format(date).toString() + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
