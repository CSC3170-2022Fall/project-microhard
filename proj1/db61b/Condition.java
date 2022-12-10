// This is a SUGGESTED skeleton for a class that describes a single
// Condition (such as CCN = '99776').  You can throw this away if you
// want,  but it is a good idea to try to understand it first.
// Our solution changes or adds about 30 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.util.List;

/** Represents a single 'where' condition in a 'select' command.
 *  @author */
class Condition {

    /** A Condition representing COL1 RELATION COL2, where COL1 and COL2
     *  are column designators. and RELATION is one of the
     *  strings "<", ">", "<=", ">=", "=", or "!=". */
    Condition(Column col1, String relation, Column col2) {
        _col1 = col1;
        _col2 = col2;
        _relation = relation;
    }

    /** A Condition representing COL1 RELATION 'VAL2', where COL1 is
     *  a column designator, VAL2 is a literal value (without the
     *  quotes), and RELATION is one of the strings "<", ">", "<=",
     *  ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {
        this(col1, relation, (Column) null);
        _val2 = val2;
    }

    /** Assuming that ROWS are rows from the respective tables from which
     *  my columns are selected, returns the result of performing the test I
     *  denote. */
    boolean test(Row... rows) {
        String value1 = _col1.getFrom(rows);
        String value2;
        if (_val2 == null) 
            value2 = _col2.getFrom(rows);
        else 
            value2 = _val2;
        int compare = value1.compareTo(value2);
        if (compare == 0) 
            return (_relation.equals("=") || _relation.equals(">=") || _relation.equals("<="));    
        else if (compare > 0) 
            return (_relation.equals(">") || _relation.equals(">=") || _relation.equals("!="));
        else if (compare < 0) 
            return (_relation.equals("<") || _relation.equals("<=") || _relation.equals("!="));
        else 
            return false;
    }

    /** AND case: Return true iff ROWS satisfies all CONDITIONS. */
    static boolean test(List<Condition> conditions, String operations, Row... rows) {
        /*Single case
        if (operations.charAt(0) == '0') return testAnd(conditions, rows);
        if (operations.charAt(0) == '1') return testOr(conditions, rows);
         */
        //Complex case
        int n = conditions.size();
        if (n == 0) return true;
        boolean result = conditions.get(0).test(rows);
        for (int i = 0; i < n-1; i++){
            if (operations.charAt(i) == '1'){
                if (result) return true;
                else result = true;
            }
            else{
                result = result && conditions.get(i+1).test(rows);
            }
        }
        return result;
    }

    /* All AND/OR case: Return false iff ROWs satisfies any one CONDITION.
    static boolean testAnd(List<Condition> conditions, Row... rows) {
        for (Condition cond : conditions) {
            if (!cond.test(rows)) {
                return false;
            }
        }
        return true;
    }
    static boolean testOr(List<Condition> conditions, Row... rows) {
        for (Condition cond : conditions) {
            if (cond.test(rows)) {
                return true;
            }
        }
        return false;
    }
     */
    /** The operands of this condition.  _col2 is null if the second operand
     *  is a literal. */
    private Column _col1, _col2;
    /** Second operand, if literal (otherwise null). */
    private String _val2;
    private String _relation;
}
