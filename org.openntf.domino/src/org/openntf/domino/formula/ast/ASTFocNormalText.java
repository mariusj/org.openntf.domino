/* Generated By:JJTree: Do not edit this line. ASTFocNormalText.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.openntf.domino.formula.ast;

import java.util.Set;
import org.openntf.domino.formula.parse.AtFormulaParserImpl;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaReturnException;
import org.openntf.domino.formula.ValueHolder;

public class ASTFocNormalText extends SimpleNode {

	private ValueHolder value;
	public ASTFocNormalText(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	public void toFormula(final StringBuilder sb) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {
		return value;
	}

	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		// TODO Auto-generated method stub

	}

	public void init(final String image) {
		value = ValueHolder.valueOf(image);

	}
}
/* JavaCC - OriginalChecksum=db01481a03cc644b31e074fd8f9465f6 (do not edit this line) */