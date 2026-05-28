//Li Ming Chun Simon 25017659D
//BlueJ

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Database
{
    private ArrayList<Item> items;

    /**
     * Construct an empty Database.
     */
    public Database()
    {
        items = new ArrayList<Item>();
    }

    /**
     * Add an item to the database.
     */
    public void addItem(Item theItem)
    {
        items.add(theItem);
    }

    /**
     * Print a list of all currently stored CDs and videos to the
     * text terminal.
     */
    public void list()
    {
        for(Iterator iter = items.iterator(); iter.hasNext(); )
        {
            Item item = (Item)iter.next();
            item.print();
            System.out.println();   // empty line between items
        }
    }
    
    //part b
    public Item searchByTitle(String theTitle) 
    {
        if (theTitle == null) {
            return null;
        }
        
        for (Item item : items) {
            if (item.getTitle().equalsIgnoreCase(theTitle)) {
                return item;
            }
        }
        
        return null;
    }
    
    //part c
    public Item searchByTitlePattern(String pat) {
        if (pat == null) {
            return null;
        }
        
        Pattern p = Pattern.compile(Pattern.quote(pat), Pattern.CASE_INSENSITIVE);
        
        for (Item item : items) {
            Matcher m = p.matcher(item.getTitle());
            if (m.find()) {
                return item;
            }
        }
        
        return null;
    }
    
    //part d
    public Item searchByPattern(String pat) {
        if (pat == null) {
            return null;
        }
        
        Pattern p = Pattern.compile(Pattern.quote(pat), Pattern.CASE_INSENSITIVE);
        
        for (Item item : items) {
            String rep = item.toString();
            if (rep == null) continue;
            Matcher m = p.matcher(rep);
            if (m.find()) {
                return item;
            }
        }
        
        return null;
    }
    
    
        
    }
