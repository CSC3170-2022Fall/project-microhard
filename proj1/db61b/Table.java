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
import java.util.HashMap;
import java.util.Map;

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
            /*input = new BufferedReader(new FileReader(name + ".db"));*/
            input = new BufferedReader(new FileReader("proj1/testing/" + name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames); 
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
        Map<Row, Boolean> map = new HashMap<>();
        int i = 0;
        for (Row r : _rows) {
            if (map.get(r) == null) {
                map.put(r,true);
                for (i = 0; i < table_titles.length; i++) {
                    System.out.print("  ");
                    System.out.print(r.get(i));
                }
                System.out.print("\n");
            }
        }
    }

    /** Return a list which contains a column of the table. */
    ArrayList<String> selectcolumn(String columnName) {
        ArrayList<String> result = new ArrayList<String>();
        int columnnumber = this.findColumn(columnName);
        for (Row r : _rows) {
            result.add(r.get(columnnumber));
        }
        return result;
    }

    /** Return a list which contains columns of the table. */
    ArrayList<String> selectcolumns(ArrayList<String> columnNames) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Integer> columnnumber = new ArrayList<Integer>();
        for (String c : columnNames) {
            columnnumber.add(this.findColumn(c));
        }
        for (Row r : _rows) {
            String tem = "";
            for (int i = 0; i < columnnumber.size(); i++) {
                tem += r.get(columnnumber.get(i));
            }
            result.add(tem);
        }
        return result;
    }

    /** Reorder the sequence of rows in a table. */
    Table sort(ArrayList<String> order, String columnName) {
        ArrayList<String> columnNames = new ArrayList<String>();
        for (int i = 0; i < this.columns(); i++) {
            columnNames.add(this.getTitle(i));
        }
        Table result = new Table(columnNames);
        ArrayList<Integer> memory = new ArrayList<Integer>();
        ArrayList<Column> newColumns = new ArrayList<Column>();
        int columnnumber = this.findColumn(columnName);
        for (String c : columnNames) {
            newColumns.add(new Column(c, this));
        }
        for (int i = 0; i < order.size(); i++) {
            int count = 0;
            for (Row r : _rows) {
                if ((order.get(i) == r.get(columnnumber)) && (!memory.contains(count))) {
                    result.add(new Row(newColumns, r));
                    memory.add(count);
                    break;
                }
                count++;
            }
        }
        return result;
    }

    /** Reorder the sequence of rows in a table. */
    Table sort(ArrayList<String> order, ArrayList<String> columnName) {
        ArrayList<String> columnNames = new ArrayList<String>();
        for (int i = 0; i < this.columns(); i++) {
            columnNames.add(this.getTitle(i));
        }
        Table result = new Table(columnNames);
        ArrayList<Integer> memory = new ArrayList<Integer>();
        ArrayList<Column> newColumns = new ArrayList<Column>();
        ArrayList<Integer> columnnumber = new ArrayList<Integer>();
        for (String c : columnName) {
            columnnumber.add(this.findColumn(c));
        }
        for (String c : columnNames) {
            newColumns.add(new Column(c, this));
        }
        for (int i = 0; i < order.size(); i++) {
            int count = 0;
            for (Row r : _rows) {
                int start = 0;
                int end = 0;
                int flag = 1;
                for (int j = 0; j < columnnumber.size(); j++) {
                    start += end;
                    end += r.get(columnnumber.get(j)).length();
                    if (memory.contains(count)) {
                        flag = 0;
                        // System.out.println("countbreak");
                        break;
                    }
                    String left = order.get(i).substring(start, end);
                    String right = r.get(columnnumber.get(j));
                    if (!left.equals(right)) {
                        // System.out.printf("start is %d, end is %d, left is %s, right is %s\n", start, end, left, right);
                        flag = 0;
                        // System.out.println("stringbreak");
                        break;
                    }
                }
                if ((flag == 1) && (!memory.contains(count))) {
                    result.add(new Row(newColumns, r));
                    memory.add(count);
                    break;
                }
                count++;
            }
        }
        return result;
    }

    /* Return the aggregate function result based on func type */
    Table aggregate_(String func) {
        Table t;
        String result = null, colName;
        // System.out.printf("test\n");
        if (func.equals("avg")) {
            // System.out.printf("test\n");
            int sum = 0;
            int cnt = 0;
            for (Row r : _rows) {
                try {
                    int i = Integer.parseInt(r.get(0));
                    sum = sum + i;
                    cnt++;
                } catch (Exception e) {
                    throw error("There seem to be problems with the column. Try select another one!");
                }
                // System.out.printf("%d\n", sum);
            }
            int res = sum/cnt;
            // System.out.printf("%.1f\n", res);
            result = Integer.toString(res);
        } else if (func.equals("count")) {
            int cnt = 0;
            for (Row r : _rows) {
                cnt++;
            }
            result = Integer.toString(cnt);
        } else if (func.equals("min")) {
            int min = Integer.MAX_VALUE;
            for (Row r : _rows) {
                try {
                    int i = Integer.parseInt(r.get(0));
                    if (i < min) min = i;
                } catch (Exception e) {
                    throw error("There seem to be problems with the column. Try select another one!");
                }
            }
            result = Integer.toString(min);
        } else if (func.equals("max")) {
            int max = Integer.MIN_VALUE;
            for (Row r : _rows) {
                try {
                    int i = Integer.parseInt(r.get(0));
                    if (i > max) max = i;
                } catch (Exception e) {
                    throw error("There seem to be problems with the column. Try select another one!");
                }
            }
            result = Integer.toString(max);
        }
        colName = func + "(" + this.getTitle(0) + ")";
        String[] col = {colName};
        String[] row = {result};
        t = new Table(col); 
        t.add(new Row(row));
        return t;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions, String operations) {
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
                if (Condition.test(conditions, operations, r)) {
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

        StringBuffer operations = new StringBuffer(conditions.size());
        for (int i = 1; i < conditions.size(); i++) operations.append("0");

        if (table2 == null) {
            result = this.select(columnNames, conditions, operations.toString());
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
                    if (equijoin(common1, common2, thisRow, table2Row) && Condition.test(conditions, operations.toString(), thisRow, table2Row)) {
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
    // private HashSet<Row> _rows = new HashSet<>();
    private ArrayList<Row> _rows = new ArrayList<>();
    // FILL IN
    /** My titles */
    private String table_titles[];
}
