// This is a SUGGESTED skeleton for a class that parses and executes database
// statements.  Be sure to read the STRATEGY section, and ask us if you have any
// questions about it.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution adds or changes about 50
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

import static db61b.Utils.*;
import static db61b.Tokenizer.*;

/** An object that reads and interprets a sequence of commands from an
 *  input source.
 *  @author */
class CommandInterpreter {

    /* STRATEGY.
     *
     *   This interpreter parses commands using a technique called
     * "recursive descent." The idea is simple: we convert the BNF grammar,
     * as given in the specification document, into a program.
     *
     * First, we break up the input into "tokens": strings that correspond
     * to the "base case" symbols used in the BNF grammar.  These are
     * keywords, such as "select" or "create"; punctuation and relation
     * symbols such as ";", ",", ">="; and other names (of columns or tables).
     * All whitespace and comments get discarded in this process, so that the
     * rest of the program can deal just with things mentioned in the BNF.
     * The class Tokenizer performs this breaking-up task, known as
     * "tokenizing" or "lexical analysis."
     *
     * The rest of the parser consists of a set of functions that call each
     * other (possibly recursively, although that isn't needed for this
     * particular grammar) to operate on the sequence of tokens, one function
     * for each BNF rule. Consider a rule such as
     *
     *    <create statement> ::= create table <table name> <table definition> ;
     *
     * We can treat this as a definition for a function named (say)
     * createStatement.  The purpose of this function is to consume the
     * tokens for one create statement from the remaining token sequence,
     * to perform the required actions, and to return the resulting value,
     * if any (a create statement has no value, just side-effects, but a
     * select clause is supposed to produce a table, according to the spec.)
     *
     * The body of createStatement is dictated by the right-hand side of the
     * rule.  For each token (like create), we check that the next item in
     * the token stream is "create" (and report an error otherwise), and then
     * advance to the next token.  For a metavariable, like <table definition>,
     * we consume the tokens for <table definition>, and do whatever is
     * appropriate with the resulting value.  We do so by calling the
     * tableDefinition function, which is constructed (as is createStatement)
     * to do exactly this.
     *
     * Thus, the body of createStatement would look like this (_input is
     * the sequence of tokens):
     *
     *    _input.next("create");
     *    _input.next("table");
     *    String name = name();
     *    Table table = tableDefinition();
     *    _input.next(";");
     *
     * plus other code that operates on name and table to perform the function
     * of the create statement.  The .next method of Tokenizer is set up to
     * throw an exception (DBException) if the next token does not match its
     * argument.  Thus, any syntax error will cause an exception, which your
     * program can catch to do error reporting.
     *
     * This leaves the issue of what to do with rules that have alternatives
     * (the "|" symbol in the BNF grammar).  Fortunately, our grammar has
     * been written with this problem in mind.  When there are multiple
     * alternatives, you can always tell which to pick based on the next
     * unconsumed token.  For example, <table definition> has two alternative
     * right-hand sides, one of which starts with "(", and one with "as".
     * So all you have to do is test:
     *
     *     if (_input.nextIs("(")) {
     *         _input.next("(");
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * As a convenience, you can also write this as
     *
     *     if (_input.nextIf("(")) {
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * combining the calls to .nextIs and .next.
     *
     * You can handle the list of <column name>s in the preceding in a number
     * of ways, but personally, I suggest a simple loop:
     *
     *     ... = columnName();
     *     while (_input.nextIs(",")) {
     *         _input.next(",");
     *         ... = columnName();
     *     }
     *
     * or if you prefer even greater concision:
     *
     *     ... = columnName();
     *     while (_input.nextIf(",")) {
     *         ... = columnName();
     *     }
     *
     * (You'll have to figure out what do with the names you accumulate, of
     * course).
     */


    /** A new CommandInterpreter executing commands read from INP, writing
     *  prompts on PROMPTER, if it is non-null. */
 
    CommandInterpreter(Scanner inp, PrintStream prompter) {
        _input = new Tokenizer(inp, prompter);
        _database = new Database();
    }

    /** Parse and execute one statement from the token stream.  Return true
     *  iff the command is something other than quit or exit. */
    boolean statement() {
        switch (_input.peek().toLowerCase()) {
        case "create":
            createStatement();
            break;
        case "load":
            loadStatement();
            break;
        case "exit": case "quit":
            exitStatement();
            return false;
        case "*EOF*":
            return false;
        case "insert":
            insertStatement();
            break;
        case "print":
            printStatement();
            break;
        case "select":
            selectStatement();
            break;
        case "store":
            storeStatement();
            break;
        default:
            throw error("unrecognizable command");
        }
        return true;
    }

    /** Parse and execute a create statement from the token stream. */
    void createStatement() {
        _input.next("create");
        _input.next("table");
        String name = name();
        Table table = tableDefinition();
        _input.next(";");
        _database.put(name, table);

    }

    /** Parse and execute an exit or quit statement. Actually does nothing
     *  except check syntax, since statement() handles the actual exiting. */
    void exitStatement() {
        if (!_input.nextIf("quit")) {
            _input.next("exit");
        }
        _input.next(";");
    }

    /** Parse and execute an insert statement from the token stream. */
    void insertStatement() {
        _input.next("insert");
        _input.next("into");
        Table table = tableName();
        _input.next("values");

        ArrayList<String> values = new ArrayList<>();
        values.add(literal());
        while (_input.nextIf(",")) {
            values.add(literal());
        }

        table.add(new Row(values.toArray(new String[values.size()])));
        _input.next(";");
    }

    /** Parse and execute a load statement from the token stream. */
    void loadStatement() {
        _input.next("load");
        String name = name();
        Table content = Table.readTable(name);
        _database.put(name, content);
        _input.next(";");
        System.out.printf("Loaded %s.db%n", name);
    }

    /** Parse and execute a store statement from the token stream. */
    void storeStatement() {
        _input.next("store");
        String name = _input.peek();
        Table table = tableName();
        table.writeTable(name);
        System.out.printf("Stored %s.db%n", name);
        _input.next(";");
    }

    /** Parse and execute a print statement from the token stream. */
    void printStatement() {
        _input.next("print");
        String name = _input.peek();
        Table table = tableName();
        _input.next(";");
        System.out.printf("Contents of %s:%n", name);
        table.print();
    }

    /** Parse and execute a select statement from the token stream. */
    void selectStatement() {
        Table table = selectClause();
        ArrayList<String> columnNames = new ArrayList<String>();
        if (_input.nextIf("order")) {
            _input.next("by");
            columnNames.add(columnName());
            while (_input.nextIf(",")) {
                columnNames.add(columnName());
            }
            if (_input.nextIf("desc")) {
                table = orderTable_desc(table, columnNames);
            }
            else if (_input.nextIf("asc")) {
                table = orderTable_asc(table, columnNames);
            }
            else {
                table = orderTable_asc(table, columnNames);
            }
        }
        System.out.println("Search results:");
        table.print();
        _input.next(";");
    }

    /* Reorder the table in ascending order. If two rows have the same value, 
    it will keep the original sequence. */
    Table orderTable_asc(Table table, ArrayList<String> columnNames) {
        ArrayList<String> orderedlist = new ArrayList<String>();
        if (columnNames.size() == 1) {
            orderedlist = table.selectcolumn(columnNames.get(0));
            Collections.sort(orderedlist);
            table = table.sort(orderedlist, columnNames.get(0));    
        }
        else {
            orderedlist = table.selectcolumns(columnNames);
            Collections.sort(orderedlist);
            table = table.sort(orderedlist, columnNames);
        }
        return table;
    }

    /* Reorder the table in descending order. If two rows have the same value, 
    it will keep the original sequence. */
    Table orderTable_desc(Table table, ArrayList<String> columnNames) {
        ArrayList<String> orderedlist = new ArrayList<String>();
        if (columnNames.size() == 1) {
            orderedlist = table.selectcolumn(columnNames.get(0));
            Collections.sort(orderedlist);
            Collections.reverse(orderedlist);
            table = table.sort(orderedlist, columnNames.get(0));
        }
        else {
            orderedlist = table.selectcolumns(columnNames);
            Collections.sort(orderedlist);
            Collections.reverse(orderedlist);
            table = table.sort(orderedlist, columnNames);
        }
        return table;
    }

    /** Parse and execute a table definition, returning the specified
     *  table. */
    Table tableDefinition() {
        Table table;
        if (_input.nextIf("(")) {
            ArrayList<String> lst = new ArrayList<String>();
            lst.add(columnName());
            while (_input.nextIf(",")) {
                lst.add(columnName());
            }
            _input.next(")");
            table = new Table(lst);
        } else {
            _input.next("as");
            table = selectClause();
        }
        return table;
    }

    /** Parse and execute a select clause from the token stream, returning the
     *  resulting table. */
    Table selectClause() {
        _input.next("select");
        ArrayList<String> col = new ArrayList<String>();
        ArrayList<Table> tab = new ArrayList<Table>();
        /* Select all */
        if (_input.nextIf("*")) {
            _input.next("from");
            /* We need to get all the columns from the required tables.*/
            Table tab1 = tableName();
            tab.add(tab1);
            while (_input.nextIf(",")) {
                Table tab2 = tableName();
                tab.add(tab2);
            }
            for (int i = 0; i < tab.size(); i++) {
                Table tab_item = tab.get(i);
                ArrayList<String> col_list = getAllCol(tab_item);
                col.addAll(col_list);
            }
            /* Eliminate the duplicate column names */
            ArrayList<String> listTemp = new ArrayList<String>(); 
            for(int j=0; j<col.size(); j++){  
                if(!listTemp.contains(col.get(j))){  
                    listTemp.add(col.get(j));  
                }
            } 
            col = listTemp;
        }
        /* Select by the specified column names*/
        else {
            // col.add(columnName());
            col = select_aux(col);
            while (_input.nextIf(",")) {
                // col.add(columnName());
                col = select_aux(col);
            }
            
            if (test_compatibility(col)) {
                throw error("Aggregate function without Group By does not allow other columns to exist!");
            }

            _input.next("from");
            /* Here we consider the general case */
            Table table1 = tableName();
            tab.add(table1);
            while (_input.nextIf(",")) {
                Table table2 = tableName();
                tab.add(table2);
            }
        }
        Table joined_table = null;
        if (tab.size() == 1) {
            joined_table = tab.get(0);
        } 
        else {
            ArrayList<Condition> con_aux = new ArrayList<Condition>();
            Table t1 = tab.get(0); Table t2 = tab.get(1);
            joined_table = t1.select(t2, rm_dup(getAllCol(t1), getAllCol(t2)), con_aux);
            for (int i = 2; i < tab.size(); i++) {
                joined_table = joined_table.select(tab.get(i), 
                    rm_dup(getAllCol(joined_table), getAllCol(tab.get(i))), con_aux);
            }
        }
        ConditionClause con = new ConditionClause();
        if (_input.nextIf("where")) {
            con = conditionClause(joined_table);
        }

        String aggregate = null;
        if (col.size() == 1) {
            aggregate = get_aggregate(col.get(0));
        }
        if (aggregate == null) {
            // System.out.println("aggregate is null");
            return joined_table.select(col, con.conList, con.operations);
        } else { /* If not null, col must have only one item */
            // System.out.println("aggregate is not null");
            String agg_col = get_aggregate_col(col.get(0));
            if (agg_col.equals("*") && aggregate.equals("count")) {
                agg_col = joined_table.getTitle(0);
            } else if (agg_col.equals("*") && !aggregate.equals("count")) {
                throw error("Aggregate function imcompatible with attribute");
            }
            col.set(0, agg_col);
            Table t = joined_table.select(col, con.conList, con.operations);
            return t.aggregate_(aggregate);
        }
        // return null;
        // return joined_table.select(col, con.conList, con.operations);
    }

    ArrayList<String> select_aux(ArrayList<String> col) {
        String aggregate_func = null;
        if (_input.nextIs("avg")) {
            aggregate_func = "avg";
        } else if (_input.nextIs("count")) {
            aggregate_func = "count";
        } else if (_input.nextIs("min")) {
            aggregate_func = "min";
        } else if (_input.nextIs("max")) {
            aggregate_func = "max";
        }
        if (aggregate_func != null) {
            _input.next(aggregate_func);
            _input.next("(");
            if (_input.nextIf("*")) {
                col.add(aggregate_func + "(*)");
            } else {
                col.add(aggregate_func + "(" + columnName() + ")");
            }
            _input.next(")");
        } else {
            col.add(columnName());
        }
        return col;
    }

    /* So far we only support one aggregate function with no other columns
     * Return true if not compatible. Then an error will be generated
     */
    Boolean test_compatibility (ArrayList<String> col) {
        int flag = 0;
        for (int i = 0; i < col.size(); i++) {
            if (get_aggregate(col.get(i)) != null) {
                flag = 1;
                break;
            }
        }
        return (col.size() > 1) && (flag == 1);
    }

    /* get the name of aggregate function */
    String get_aggregate (String s) {
        int i = s.indexOf("(");
        if (i == -1) {
            return null;
        } else {
            return s.substring(0, i);
        }
    }

    String get_aggregate_col (String s) {
        return s.substring(s.indexOf("(")+1, s.indexOf(")"));
    }

    /* Combine two array lists of String type, then remove any duplicated items in the list */
    ArrayList<String> rm_dup(ArrayList<String> l1, ArrayList<String> l2) {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(l1);
        for (int i = 0; i < l2.size(); i++) {
            if (!l1.contains(l2.get(i))) {
                result.add(l2.get(i));
            }
        }
        return result;
    }

    /* Given a table a, return its column names as an Arraylist */
    ArrayList<String> getAllCol(Table t) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < t.columns(); i++) {
            result.add(t.getTitle(i));
        }
        return result;
    }

    /** Parse and return a valid name (identifier) from the token stream. */
    String name() {
        return _input.next(Tokenizer.IDENTIFIER);
    }

    /** Parse and return a valid column name from the token stream. Column
     *  names are simply names; we use a different method name to clarify
     *  the intent of the code. */
    String columnName() {
        return name();
    }

    /** Parse a valid table name from the token stream, and return the Table
     *  that it designates, which must be loaded. */
    Table tableName() {
        String name = name();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    /** Parse a literal and return the string it represents (i.e., without
     *  single quotes). */
    String literal() {
        String lit = _input.next(Tokenizer.LITERAL);
        return lit.substring(1, lit.length() - 1).trim();
    }

    /** Parse and return a list of Conditions that apply to TABLES from the
     *  token stream.  This denotes the conjunction (`and') zero
     *  or more Conditions. */
    public class ConditionClause{
        ArrayList<Condition> conList;
        String operations;
        public ConditionClause() {
            this.conList = null;
            this.operations = "";
        }
        public ConditionClause(ArrayList<Condition> conList, String operations) {
            this.conList = conList;
            this.operations = operations;
        }
    }

    ConditionClause conditionClause(Table... tables) {
        ArrayList<Condition> conList = new ArrayList<Condition>();
        conList.add(condition(tables));
        StringBuffer operation = new StringBuffer("");
        while (_input.nextIs("and") || _input.nextIs("or")){
            if (_input.nextIf("and"))
                operation.append("0");
            if (_input.nextIf("or"))
                operation.append("1");
            conList.add(condition(tables));
        }
        return new ConditionClause(conList, operation.toString());
    }

    /** Parse and return a Condition that applies to TABLES from the
     *  token stream. */
    Condition condition(Table... tables) {
        Column leftCol = new Column(columnName(), tables);
        String relation = _input.next(RELATION);
        if (_input.nextIs(LITERAL)){
            return new Condition(leftCol, relation, literal());
        }
        else{
            Column rightCol = new Column(columnName(), tables);
            return new Condition(leftCol, relation, rightCol);
        }

    }

    /** Advance the input past the next semicolon. */
    void skipCommand() {
        while (true) {
            try {
                while (!_input.nextIf(";") && !_input.nextIf("*EOF*")) {
                    _input.next();
                }
                return;
            } catch (DBException excp) {
                /* No action */
            }
        }
    }

    /** The command input source. */
    private Tokenizer _input;
    /** Database containing all tables. */
    private Database _database;
}
