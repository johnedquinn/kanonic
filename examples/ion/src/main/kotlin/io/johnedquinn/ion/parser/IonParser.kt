package io.johnedquinn.ion.parser

import com.amazon.ionelement.api.IonElement
import com.amazon.ionelement.api.ionNull
import com.amazon.ionelement.api.ionSymbol
import io.johnedquinn.ion.generated.IonBaseVisitor
import io.johnedquinn.ion.generated.IonNode
import io.johnedquinn.ion.generated.IonSpecification
import io.johnedquinn.kanonic.runtime.ast.TerminalNode
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.tool.KanonicNodeFormatter

object IonParser : IonBaseVisitor<IonElement, Unit>() {
    private val parser = KanonicParser.Builder.standard().withSpecification(IonSpecification).build()

    public fun parse(input: String): IonElement {
        val ast = parser.parse(input)
        println(KanonicNodeFormatter.format(ast))
        return visit(ast, Unit)
    }

    override fun defaultVisit(node: IonNode, ctx: Unit): IonElement {
        var element = ionNull()
        for (child in node.children) {
            element = child.accept(this, ctx)
        }
        return element
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
