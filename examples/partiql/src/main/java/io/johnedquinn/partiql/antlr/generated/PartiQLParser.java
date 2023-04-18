// Generated from PartiQL.g4 by ANTLR 4.10.1
package io.johnedquinn.partiql.antlr.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PartiQLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EPSILON=1, SELECT=2, FROM=3, PAREN_LEFT=4, PAREN_RIGHT=5, SYMBOL=6, WS=7, 
		UNRECOGNIZED=8;
	public static final int
		RULE_statement = 0, RULE_dql = 1, RULE_expr = 2, RULE_expr_select = 3, 
		RULE_expr_atom = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"statement", "dql", "expr", "expr_select", "expr_atom"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'EPSILON'", "'SELECT'", "'FROM'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EPSILON", "SELECT", "FROM", "PAREN_LEFT", "PAREN_RIGHT", "SYMBOL", 
			"WS", "UNRECOGNIZED"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PartiQL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PartiQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StatementDqlContext extends StatementContext {
		public DqlContext dql() {
			return getRuleContext(DqlContext.class,0);
		}
		public StatementDqlContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterStatementDql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitStatementDql(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitStatementDql(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statement);
		try {
			_localctx = new StatementDqlContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			dql();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DqlContext extends ParserRuleContext {
		public DqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dql; }
	 
		public DqlContext() { }
		public void copyFrom(DqlContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DqlExprContext extends DqlContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DqlExprContext(DqlContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterDqlExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitDqlExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitDqlExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DqlContext dql() throws RecognitionException {
		DqlContext _localctx = new DqlContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_dql);
		try {
			_localctx = new DqlExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprSfwContext extends ExprContext {
		public Expr_selectContext expr_select() {
			return getRuleContext(Expr_selectContext.class,0);
		}
		public ExprSfwContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterExprSfw(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitExprSfw(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitExprSfw(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expr);
		try {
			_localctx = new ExprSfwContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			expr_select();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expr_selectContext extends ParserRuleContext {
		public Expr_selectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_select; }
	 
		public Expr_selectContext() { }
		public void copyFrom(Expr_selectContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprSelectFallbackContext extends Expr_selectContext {
		public Expr_atomContext expr_atom() {
			return getRuleContext(Expr_atomContext.class,0);
		}
		public ExprSelectFallbackContext(Expr_selectContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterExprSelectFallback(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitExprSelectFallback(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitExprSelectFallback(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprSelectContext extends Expr_selectContext {
		public TerminalNode SELECT() { return getToken(PartiQLParser.SELECT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode FROM() { return getToken(PartiQLParser.FROM, 0); }
		public ExprSelectContext(Expr_selectContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterExprSelect(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitExprSelect(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitExprSelect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expr_selectContext expr_select() throws RecognitionException {
		Expr_selectContext _localctx = new Expr_selectContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_expr_select);
		try {
			setState(22);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SELECT:
				_localctx = new ExprSelectContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(16);
				match(SELECT);
				setState(17);
				expr();
				setState(18);
				match(FROM);
				setState(19);
				expr();
				}
				break;
			case PAREN_LEFT:
			case SYMBOL:
				_localctx = new ExprSelectFallbackContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(21);
				expr_atom();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expr_atomContext extends ParserRuleContext {
		public Expr_atomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_atom; }
	 
		public Expr_atomContext() { }
		public void copyFrom(Expr_atomContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprWrappedContext extends Expr_atomContext {
		public TerminalNode PAREN_LEFT() { return getToken(PartiQLParser.PAREN_LEFT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode PAREN_RIGHT() { return getToken(PartiQLParser.PAREN_RIGHT, 0); }
		public ExprWrappedContext(Expr_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterExprWrapped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitExprWrapped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitExprWrapped(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprIdentContext extends Expr_atomContext {
		public TerminalNode SYMBOL() { return getToken(PartiQLParser.SYMBOL, 0); }
		public ExprIdentContext(Expr_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).enterExprIdent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PartiQLListener ) ((PartiQLListener)listener).exitExprIdent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PartiQLVisitor ) return ((PartiQLVisitor<? extends T>)visitor).visitExprIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expr_atomContext expr_atom() throws RecognitionException {
		Expr_atomContext _localctx = new Expr_atomContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_expr_atom);
		try {
			setState(29);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SYMBOL:
				_localctx = new ExprIdentContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				match(SYMBOL);
				}
				break;
			case PAREN_LEFT:
				_localctx = new ExprWrappedContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(25);
				match(PAREN_LEFT);
				setState(26);
				expr();
				setState(27);
				match(PAREN_RIGHT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\b \u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003"+
		"\u0003\u0017\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004\u001e\b\u0004\u0001\u0004\u0000\u0000\u0005\u0000\u0002"+
		"\u0004\u0006\b\u0000\u0000\u001c\u0000\n\u0001\u0000\u0000\u0000\u0002"+
		"\f\u0001\u0000\u0000\u0000\u0004\u000e\u0001\u0000\u0000\u0000\u0006\u0016"+
		"\u0001\u0000\u0000\u0000\b\u001d\u0001\u0000\u0000\u0000\n\u000b\u0003"+
		"\u0002\u0001\u0000\u000b\u0001\u0001\u0000\u0000\u0000\f\r\u0003\u0004"+
		"\u0002\u0000\r\u0003\u0001\u0000\u0000\u0000\u000e\u000f\u0003\u0006\u0003"+
		"\u0000\u000f\u0005\u0001\u0000\u0000\u0000\u0010\u0011\u0005\u0002\u0000"+
		"\u0000\u0011\u0012\u0003\u0004\u0002\u0000\u0012\u0013\u0005\u0003\u0000"+
		"\u0000\u0013\u0014\u0003\u0004\u0002\u0000\u0014\u0017\u0001\u0000\u0000"+
		"\u0000\u0015\u0017\u0003\b\u0004\u0000\u0016\u0010\u0001\u0000\u0000\u0000"+
		"\u0016\u0015\u0001\u0000\u0000\u0000\u0017\u0007\u0001\u0000\u0000\u0000"+
		"\u0018\u001e\u0005\u0006\u0000\u0000\u0019\u001a\u0005\u0004\u0000\u0000"+
		"\u001a\u001b\u0003\u0004\u0002\u0000\u001b\u001c\u0005\u0005\u0000\u0000"+
		"\u001c\u001e\u0001\u0000\u0000\u0000\u001d\u0018\u0001\u0000\u0000\u0000"+
		"\u001d\u0019\u0001\u0000\u0000\u0000\u001e\t\u0001\u0000\u0000\u0000\u0002"+
		"\u0016\u001d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}