// Generated from PartiQL.g4 by ANTLR 4.10.1
package io.johnedquinn.partiql.antlr.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PartiQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PartiQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code StatementDql}
	 * labeled alternative in {@link PartiQLParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDql(PartiQLParser.StatementDqlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DqlExpr}
	 * labeled alternative in {@link PartiQLParser#dql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDqlExpr(PartiQLParser.DqlExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprSfw}
	 * labeled alternative in {@link PartiQLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSfw(PartiQLParser.ExprSfwContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprSelect}
	 * labeled alternative in {@link PartiQLParser#expr_select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSelect(PartiQLParser.ExprSelectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprSelectFallback}
	 * labeled alternative in {@link PartiQLParser#expr_select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSelectFallback(PartiQLParser.ExprSelectFallbackContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprIdent}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprIdent(PartiQLParser.ExprIdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprWrapped}
	 * labeled alternative in {@link PartiQLParser#expr_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprWrapped(PartiQLParser.ExprWrappedContext ctx);
}