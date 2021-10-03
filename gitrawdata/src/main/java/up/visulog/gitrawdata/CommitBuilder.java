package up.visulog.gitrawdata;

import java.text.ParseException;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CommitBuilder {
    private final String id;
    private String author;
    private Date date;
    private String description;
    private String mergedFrom;

    public CommitBuilder(String id) {
        this.id = id;
    }

    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }
    
    public CommitBuilder setDate(String dateSt) { // Convert a String into a Date and change the date attribute of the commit
	SimpleDateFormat s= new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
	Date date=null;
	try {
		date=s.parse(dateSt);
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
        this.date = date;
        return this;
    }
    public CommitBuilder setDate(Date date) {
    	this.date=date;
    	return this;
    }

    public CommitBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public CommitBuilder setMergedFrom(String mergedFrom) {
        this.mergedFrom = mergedFrom;
        return this;
    }

    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom);
    }
}
