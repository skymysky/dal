package com.ctrip.platform.dal.dao.sqlbuilder;

import com.ctrip.platform.dal.common.enums.DatabaseCategory;

/**
 * The free sql build for update statement.
 *  
 * @author jhhe
 */
public class FreeUpdateSqlBuilder extends AbstractFreeSqlBuilder {
    public static final String INSERT_INTO = "INSERT INTO";
    public static final String VALUES = "VALUES";

    public static final String DELETE_FROM = "DELETE FROM";
    public static final String UPDATE = "UPDATE";
    public static final String SET = "SET";
    
	public FreeUpdateSqlBuilder(DatabaseCategory dbCategory) {
	    setDbCategory(dbCategory);
	}

	/**
	 * If there is IN parameter, no matter how many values in the IN clause, the IN clause only need to 
	 * contain one "?".
	 * E.g. UPDATE ... WHERE id IN ?
	 * 
	 * This method is the same with super.append(String);
	 * @param updateSqlTemplate
	 * @return
	 */
	public FreeUpdateSqlBuilder setTemplate(String updateSqlTemplate) {
		append(updateSqlTemplate);
		return this;
	}
	
	/**
	 * Append INSERT INTO + table name.
	 * @param tableName
	 * @return
	 */
	public FreeUpdateSqlBuilder insertInto(String tableName) {
	    return insertInto(table(tableName));
	}
	
    /**
     * Append INSERT INTO + table name.
     * @param tableName
     * @return FreeUpdateSqlBuilder instance. Because the following append will be values(...)
     */
	public FreeUpdateSqlBuilder insertInto(Table tableName) {
        append(INSERT_INTO);
        append(tableName);
        return this;
    }
	
	/**
	 * Append column names and "?" for INSERT statement
	 * @param columnNames
	 * @return
	 */
	public FreeUpdateSqlBuilder values(String...columnNames) {
	    append(EMPTY, Expressions.leftBracket);
        StringBuilder valueFields = new StringBuilder();
        
        for (int i = 0; i < columnNames.length; i++) {
            appendColumn(columnNames[i]);
            valueFields.append(PLACE_HOLDER);
            if(i != columnNames.length -1) {
                append(COMMA);
                valueFields.append(", ");
            }
        }
        
        append(Expressions.rightBracket, EMPTY, VALUES, EMPTY, Expressions.bracket(text(valueFields)));
        
        return this;
    }
	
    /**
     * Append DELETE FROM + table name.
     * @param tableName
     * @return
     */
    public FreeUpdateSqlBuilder deleteFrom(String tableName) {
        return deleteFrom(table(tableName));
    }
    
    /**
     * Append DELETE FROM + table name.
     * @param tableName
     * @return
     */
    public FreeUpdateSqlBuilder deleteFrom(Table tableName) {
        append(DELETE_FROM);
        append(tableName);
        return this;
    }
    
    /**
     * Append UPDATE + table name.
     * @param tableName
     * @return
     */
    public FreeUpdateSqlBuilder update(String tableName) {
        return update(table(tableName));
    }

    /**
     * Append UPDATE + table name.
     * @param tableName
     * @return
     */
    public FreeUpdateSqlBuilder update(Table tableName) {
        append(UPDATE);
        append(tableName);
        return this;
    }
    
    /**
     * Append SET name1=?, name2=?... for UPDATE statement.
     * @param columnNames
     * @return
     */
    public FreeUpdateSqlBuilder set(String...columnNames) {
        append(SET);
        for (int i = 0; i < columnNames.length; i++) {
            append(Expressions.createColumnExpression("%s=?", columnNames[i]));
            if(i != columnNames.length -1)
                append(COMMA);
        }
        
        return this;
    }
}
