// This is a SUGGESTED skeleton for a class that represents a single
// Table.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes or adds
// about 100 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain dupliace names. */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }
        // FILL IN
        table_titles = columnTitles;
        // System.out.printf("title length: %d%n", table_titles.length);
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        int numOfColumns = table_titles.length;
        return numOfColumns;  // REPLACE WITH SOLUTION
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        if (k < 0 || k >= this.columns()) {
            throw error("index out of range: %d", k);
        }
        String titleName = table_titles[k];
        return titleName;  // REPLACE WITH SOLUTION
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        int columnIndex = -1;
        for (int i = 0; i < table_titles.length; i++) {
            if (title.toLowerCase().equals(table_titles[i].toLowerCase())) {
                columnIndex = i;
                break;
            }
        }
        return columnIndex;  // REPLACE WITH SOLUTION
    }

    /** Return the number of Rows in this table. */
    public int size() {
        int size;
        size = _rows.size();
        return size;  // REPLACE WITH SOLUTION
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
        if (row.size() != table_titles.length) {
            throw error("The size of row is inconsistant with the number of the columns: %d", row.size());
        } else {
            // apply add method in HashSet class
            return _rows.add(row);
        }
        // REPLACE WITH SOLUTION
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            // input = new BufferedReader(new FileReader(name + ".db"));
            input = new BufferedReader(new FileReader("proj1/testing/" + name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames);  // FILL IN
            String data = input.readLine();
            while (data != null) {
                table.add(new Row(data.split(",")));
                data = input.readLine();
            }
        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            // FILL THIS IN
            // print the title of the table
            for (int i = 0; i < table_titles.length - 1; i++) {
                output.print(table_titles[i]);
                output.print(",");
            }
            output.println(table_titles[table_titles.length - 1]);
            // print the data of the table
            for (Row r : _rows) {
                for (int j = 0; j < this.columns() - 1; j++) {
                    output.print(r.get(j));
                    output.print(",");
                }
                output.println(r.get(this.columns() - 1));
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output. */
    void print() {
        // FILL IN
        int i = 0;
        for (Row r : _rows) {
            System.out.print(" ");
            for (i = 0; i < table_titles.length; i++) {
                System.out.print(" ");
                System.out.print(r.get(i));
            }
            System.out.print("\n");
        }
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
        // FILL IN
        ArrayList<Column> newColumns = new ArrayList<Column>();
        for (String c : columnNames) {
            newColumns.add(new Column(c , this));
        }
        if (conditions == null) {
            for (Row r : _rows) {
                result.add(new Row(newColumns, r));
            }
        } else {
            for (Row r : _rows) {
                if (Condition.test(conditions, r)) {
                    result.add(new Row(newColumns, r));
                } 
            }
        }
        return result;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
        if (table2 == null) {
            result = this.select(columnNames, conditions);
            return result;
        }
        List<String> common_column_name;
        // FILL IN
        // comparing the name of each title in this table and table2, find the common column name
        common_column_name = new ArrayList<String>();
        for (int i = 0; i < table_titles.length; i++) {
            for (int j = 0; j < table2.columns(); j++) {
                if (this.getTitle(i).equals(table2.getTitle(j))) {
                    common_column_name.add(this.getTitle(i));
                    break;
                }
            }
        }
        List<Column> common1, common2;
        common1 = new ArrayList<Column>();
        common2 = new ArrayList<Column>();
        
        for (int k = 0; k < common_column_name.size(); k++) {
            common1.add(new Column(common_column_name.get(k), this));
            common2.add(new Column(common_column_name.get(k), table2));
        }
        
        List<Column> common = new ArrayList<Column>();
        for (String n : columnNames) {
            common.add(new Column(n, this, table2));
        }

        if (conditions == null) {
            for (Row thisRow : this) {
                for (Row table2Row : table2) {
                    if (equijoin(common1, common2, thisRow, table2Row)) {
                        result.add(new Row(common, thisRow, table2Row));
                    }
                }
            }
        } else {
            for (Row thisRow : this) {
                for (Row table2Row : table2) {
                    if (equijoin(common1, common2, thisRow, table2Row) && Condition.test(conditions, thisRow, table2Row)) {
                        result.add(new Row(common, thisRow, table2Row));
                    }
                }
            }
        }
        return result;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     *  from those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
                                    Row row1, Row row2) {
        String str1, str2;
        for (int i = 0; i < common1.size(); i++) {
            str1 = common1.get(i).getFrom(row1);
            str2 = common2.get(i).getFrom(row2);
            if (!str1.equals(str2)) {
                return false;
            }
        }
        return true; // REPLACE WITH SOLUTION
    }

    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
    // FILL IN
    /** My titles */
    private String table_titles[];
}
