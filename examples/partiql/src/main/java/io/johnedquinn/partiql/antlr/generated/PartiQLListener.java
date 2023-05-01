// Generated from PartiQL.g4 by ANTLR 4.10.1
package io.johnedquinn.partiql.antlr.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PartiQLParser}.
 */
public interface PartiQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code StatementDql}
	 * labeled alternative in {@link PartiQLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDql(PartiQLParser.StatementDqlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatementDql}
	 * labeled alternative in {@link PartiQLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDql(PartiQLParser.StatementDqlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DqlExpr}
	 * labeled alternative in {@link PartiQLParser#dql}.
	 * @param ctx the parse tree
	 */
	void enterDqlExpr(PartiQLParser.DqlExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DqlExpr}
	 * labeled alternative in {@link PartiQLParser#dql}.
	 * @param ctx the parse tree
	 */
	void exitDqlExpr(PartiQLParser.DqlExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprSfw}
	 * labeled alternative in {@link PartiQLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprSfw(PartiQLParser.ExprSfwContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprSfw}
	 * labeled alternative in {@link PartiQLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprSfw(PartiQLParser.ExprSfwContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprSelect}
	 * labeled alternative in {@link PartiQLParser#expr_select}.
	 * @param ctx the parse tree
	 */
	void enterExprSelect(PartiQLParser.ExprSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprSelect}
	 * labeled alternative in {@link PartiQLParser#expr_select}.
	 * @param ctx the parse tree
	 */
	void exitExprSelect(PartiQLParser.ExprSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprSelectFallback}
	 * labeled alternative in {@link PartiQLParser#expr_select}.
	 * @param ctx the parse tree
	 */
	void enterExprSelectFallback(PartiQLParser.ExprSelectFallbackContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprSelectFallback}
	 * labeled alternative in {@link PartiQLParser#expr_select}.
	 * @param ctx the parse tree
	 */
	void exitExprSelectFallback(PartiQLParser.ExprSelectFallbackContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprIdent}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void enterExprIdent(PartiQLParser.ExprIdentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprIdent}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void exitExprIdent(PartiQLParser.ExprIdentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprWrapped}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void enterExprWrapped(PartiQLParser.ExprWrappedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprWrapped}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void exitExprWrapped(PartiQLParser.ExprWrappedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void enterExprArray(PartiQLParser.ExprArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 */
	void exitExprArray(PartiQLParser.ExprArrayContext ctx);
}