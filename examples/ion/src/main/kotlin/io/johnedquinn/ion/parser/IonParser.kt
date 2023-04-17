package io.johnedquinn.ion.parser

import com.amazon.ionelement.api.IonElement
import com.amazon.ionelement.api.ionNull
import com.amazon.ionelement.api.ionSexpOf
import com.amazon.ionelement.api.ionSymbol
import io.johnedquinn.ion.generated.IonBaseVisitor
import io.johnedquinn.ion.generated.IonNode
import io.johnedquinn.ion.generated.IonSpecification
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.tool.KanonicNodeFormatter
import java.time.Instant

object IonParser : IonBaseVisitor<IonElement, Unit>() {
    private val parser = KanonicParser.Builder.standard().withSpecification(IonSpecification).build()

    public fun parse(input: String): IonElement {
        val ast = parser.parse(input)
        // println(KanonicNodeFormatter.format(ast))
        return visit(ast, Unit)
    }
    
    public fun timeDiff(older: Instant, newer: Instant): String {
        val secondsInNanos = (newer.epochSecond - older.epochSecond) * 1_000_000_000L + newer.nano - older.nano
        return "$secondsInNanos ns"
    }

    override fun defaultVisit(node: IonNode, ctx: Unit): IonElement {
        var element = ionNull()
        for (child in node.children) {
            element = child.accept(this, ctx)
        }
        return element
    }

    override fun visitAnnotatedExpr(node: IonNode.ExprNode.AnnotatedExprNode, ctx: Unit): IonElement {
        val expr = visitExpr(node.expr()[0], ctx)
        val annotation = visitAnnotation(node.annotation()[0], ctx).asAnyElement().symbolValue
        return expr.copy(
            annotations = listOf(annotation) + expr.annotations
        )
    }

    override fun visitSexp(node: IonNode.ExprNode.SexpNode, ctx: Unit): IonElement {
        return ionSexpOf(node.expr().map { visitExpr(it, ctx) })
    }

    override fun visitAnnotation(node: IonNode.AnnotationNode.AnnotationNode, ctx: Unit): IonElement {
        val content = node.SYMBOL().firstOrNull()?.token?.content ?: node.SYMBOL_QUOTED().firstOrNull()?.let {
            val content = it.token.content
            content.substring(1, content.lastIndex)
        } ?: error("Unexpected empty symbol.")
        return ionSymbol(content)
    }

    override fun visitExpr(node: IonNode.ExprNode, ctx: Unit): IonElement {
        return super.visitExpr(node, ctx)
    }

    override fun visitSymbol(node: IonNode.ExprNode.SymbolNode, ctx: Unit): IonElement {
        val content = node.SYMBOL().firstOrNull()?.token?.content ?: node.SYMBOL_QUOTED().firstOrNull()?.let {
            val content = it.token.content
            content.substring(1, content.lastIndex)
        } ?: error("Unexpected empty symbol.")
        return ionSymbol(content)
    }
    override fun defaultReturn(node: IonNode, ctx: Unit): IonElement = ionNull()

    override fun visitTerminal(node: TerminalNode, ctx: Unit): IonElement = ionNull()
}
