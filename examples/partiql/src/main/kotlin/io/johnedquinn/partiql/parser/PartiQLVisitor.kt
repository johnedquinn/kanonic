package io.johnedquinn.partiql.parser

import com.amazon.ionelement.api.ionNull
import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.partiql.generated.PartiQLBaseVisitor
import io.johnedquinn.partiql.generated.PartiQLNode
import org.partiql.lang.domains.PartiqlAst

object PartiQLVisitor : PartiQLBaseVisitor<PartiqlAst.PartiqlAstNode, PartiQLVisitor.Context>() {

    class Context(
        val builder: PartiqlAst.Builder
    )

    public fun visit(node: Node): PartiqlAst.Statement {
        val ctx = Context(PartiqlAst.BUILDER())
        return visit(node, ctx) as PartiqlAst.Statement
    }

    override fun visitDqlExpr(node: PartiQLNode.DqlNode.DqlExprNode, ctx: Context): PartiqlAst.Statement.Query {
        return ctx.builder.query(
            visitExpr(node.expr()[0], ctx)
        )
    }

    override fun visitExprSelect(
        node: PartiQLNode.ExprSelectNode.ExprSelectNode,
        ctx: Context
    ): PartiqlAst.Expr.Select {
        return ctx.builder.select(
            project = PartiqlAst.Projection.ProjectList(
                listOf(
                    ctx.builder.projectExpr(
                        visitExpr(node.expr()[0], ctx),
                    )
                )
            ),
            from = ctx.builder.scan(
                visitExpr(node.expr()[1], ctx)
            )
        )
    }

    override fun visitExprIdent(node: PartiQLNode.ExprAtomNode.ExprIdentNode, ctx: Context): PartiqlAst.PartiqlAstNode {
        return ctx.builder.id(node.SYMBOL()[0].token.content, PartiqlAst.CaseSensitivity.CaseInsensitive(), PartiqlAst.ScopeQualifier.Unqualified())
    }

    override fun visitExprWrapped(
        node: PartiQLNode.ExprAtomNode.ExprWrappedNode,
        ctx: Context
    ): PartiqlAst.Expr {
        return visitExpr(node.expr()[0], ctx)
    }

    override fun visitExpr(node: PartiQLNode.ExprNode, ctx: Context): PartiqlAst.Expr {
        return super.visitExpr(node, ctx) as PartiqlAst.Expr
    }

    override fun defaultReturn(node: PartiQLNode, ctx: Context): PartiqlAst.PartiqlAstNode = TODO("Not yet implemented")

    override fun defaultVisit(node: PartiQLNode, ctx: Context): PartiqlAst.PartiqlAstNode {
        var result: PartiqlAst.PartiqlAstNode? = null
        for (child in node.children) {
            result = child.accept(this, ctx)
        }
        return result ?: ctx.builder.lit(ionNull())
    }

    override fun visitTerminal(node: TerminalNode, ctx: Context): PartiqlAst.PartiqlAstNode {
        return ctx.builder.lit(ionNull())
    }
}