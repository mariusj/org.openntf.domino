/* Generated By:JJTree: Do not edit this line. ASTFocParam.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.openntf.formula.ast;

import java.util.Set;

import org.openntf.formula.EvaluateException;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaParseException;
import org.openntf.formula.FormulaReturnException;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.parse.AtFormulaParserImpl;

public class ASTFocParam extends SimpleNode {
	String paramName;

	public ASTFocParam(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	public void init(final String image) {
		paramName = image;
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {
		try {
			return ctx.getParam(paramName);
		} catch (FormulaParseException e) {
			return ValueHolder.valueOf(new EvaluateException(codeLine, codeColumn, e));
		}
	}

	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		functions.add("#" + paramName + "#");
	}
}
/* JavaCC - OriginalChecksum=7e31041c5fbbe0cb629c28e308d8594c (do not edit this line) */
