/* Generated By:JJTree&JavaCC: Do not edit this line. AtFormulaParser.java */
package org.openntf.domino.tests.rpr.formula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.formula.Formulas;
import org.openntf.formula.Function;

public class PrintFunctionSet extends TestRunnerStdIn {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new PrintFunctionSet(), "My thread");
		thread.start();
	}

	public PrintFunctionSet() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		Factory.enableCounters(true, false);
		try {
			System.out.println("Currently supported functions");
			List<Function> funcs = new ArrayList<Function>();
			funcs.addAll(Formulas.getFunctionFactory().getFunctions().values());

			Collections.sort(funcs, new Comparator<Function>() {
				@Override
				public int compare(final Function o1, final Function o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
			for (Function func : funcs) {
				System.out.println(func);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
