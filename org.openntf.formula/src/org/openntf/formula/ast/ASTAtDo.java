/* Generated By:JJTree: Do not edit this line. ASTAtDo.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.formula.ast;

import java.util.Set;

import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaReturnException;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.parse.AtFormulaParserImpl;

public class ASTAtDo extends SimpleNode {

	private boolean virtual;

	public ASTAtDo(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	/**
	 * AtDo returns the last child's value. This might be a ValueHolder of DataType.ERROR. No additional errorhandling needed
	 */
	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {
		ValueHolder ret = null;
		if (children == null)
			return null;
		for (int i = 0; i < children.length; ++i) {
			ret = children[i].evaluate(ctx);
		}
		return ret;
	}

	/**
	 * add {@literal @}Do to the functions list (if it is not virtual)
	 */
	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		if (!virtual) {
			functions.add("@do");
		}
	}

	/**
	 * Sets the mode to virtual (= function does not appear in function list)
	 * 
	 * @param bool
	 *            virtual
	 */
	public void setVirtual(final boolean bool) {
		virtual = bool;
	}
}
/* JavaCC - OriginalChecksum=28653335c32026ae20324c429d56df0a (do not edit this line) */
