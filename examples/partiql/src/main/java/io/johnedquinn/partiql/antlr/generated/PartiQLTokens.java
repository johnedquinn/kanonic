// Generated from PartiQLTokens.g4 by ANTLR 4.10.1
package io.johnedquinn.partiql.antlr.generated;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PartiQLTokens extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EPSILON=1, AS=2, SELECT=3, FROM=4, PAREN_LEFT=5, PAREN_RIGHT=6, BRACKET_LEFT=7, 
		BRACKET_RIGHT=8, COMMA=9, SYMBOL=10, WS=11, UNRECOGNIZED=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"EPSILON", "AS", "SELECT", "FROM", "PAREN_LEFT", "PAREN_RIGHT", "BRACKET_LEFT", 
			"BRACKET_RIGHT", "COMMA", "SYMBOL", "WS", "UNRECOGNIZED", "WHITESPACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'EPSILON'", "'AS'", "'SELECT'", "'FROM'", "'('", "')'", "'['", 
			"']'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EPSILON", "AS", "SELECT", "FROM", "PAREN_LEFT", "PAREN_RIGHT", 
			"BRACKET_LEFT", "BRACKET_RIGHT", "COMMA", "SYMBOL", "WS", "UNRECOGNIZED"
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


	public PartiQLTokens(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PartiQLTokens.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\fN\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0005\t?\b\t\n\t\f\tB\t\t\u0001\n\u0004\nE\b\n\u000b\n\f\nF\u0001\n"+
		"\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0000\u0000\r\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"+
		"\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\u0000\u0001\u0000\u0010\u0002"+
		"\u0000EEee\u0002\u0000PPpp\u0002\u0000SSss\u0002\u0000IIii\u0002\u0000"+
		"LLll\u0002\u0000OOoo\u0002\u0000NNnn\u0002\u0000AAaa\u0002\u0000CCcc\u0002"+
		"\u0000TTtt\u0002\u0000FFff\u0002\u0000RRrr\u0002\u0000MMmm\u0004\u0000"+
		"$$AZ__az\u0005\u0000$$09AZ__az\u0003\u0000\t\n\r\r  N\u0000\u0001\u0001"+
		"\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0001\u001b\u0001\u0000\u0000"+
		"\u0000\u0003#\u0001\u0000\u0000\u0000\u0005&\u0001\u0000\u0000\u0000\u0007"+
		"-\u0001\u0000\u0000\u0000\t2\u0001\u0000\u0000\u0000\u000b4\u0001\u0000"+
		"\u0000\u0000\r6\u0001\u0000\u0000\u0000\u000f8\u0001\u0000\u0000\u0000"+
		"\u0011:\u0001\u0000\u0000\u0000\u0013<\u0001\u0000\u0000\u0000\u0015D"+
		"\u0001\u0000\u0000\u0000\u0017J\u0001\u0000\u0000\u0000\u0019L\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0007\u0000\u0000\u0000\u001c\u001d\u0007\u0001"+
		"\u0000\u0000\u001d\u001e\u0007\u0002\u0000\u0000\u001e\u001f\u0007\u0003"+
		"\u0000\u0000\u001f \u0007\u0004\u0000\u0000 !\u0007\u0005\u0000\u0000"+
		"!\"\u0007\u0006\u0000\u0000\"\u0002\u0001\u0000\u0000\u0000#$\u0007\u0007"+
		"\u0000\u0000$%\u0007\u0002\u0000\u0000%\u0004\u0001\u0000\u0000\u0000"+
		"&\'\u0007\u0002\u0000\u0000\'(\u0007\u0000\u0000\u0000()\u0007\u0004\u0000"+
		"\u0000)*\u0007\u0000\u0000\u0000*+\u0007\b\u0000\u0000+,\u0007\t\u0000"+
		"\u0000,\u0006\u0001\u0000\u0000\u0000-.\u0007\n\u0000\u0000./\u0007\u000b"+
		"\u0000\u0000/0\u0007\u0005\u0000\u000001\u0007\f\u0000\u00001\b\u0001"+
		"\u0000\u0000\u000023\u0005(\u0000\u00003\n\u0001\u0000\u0000\u000045\u0005"+
		")\u0000\u00005\f\u0001\u0000\u0000\u000067\u0005[\u0000\u00007\u000e\u0001"+
		"\u0000\u0000\u000089\u0005]\u0000\u00009\u0010\u0001\u0000\u0000\u0000"+
		":;\u0005,\u0000\u0000;\u0012\u0001\u0000\u0000\u0000<@\u0007\r\u0000\u0000"+
		"=?\u0007\u000e\u0000\u0000>=\u0001\u0000\u0000\u0000?B\u0001\u0000\u0000"+
		"\u0000@>\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000A\u0014\u0001"+
		"\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000CE\u0003\u0019\f\u0000DC\u0001"+
		"\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000"+
		"FG\u0001\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HI\u0006\n\u0000\u0000"+
		"I\u0016\u0001\u0000\u0000\u0000JK\t\u0000\u0000\u0000K\u0018\u0001\u0000"+
		"\u0000\u0000LM\u0007\u000f\u0000\u0000M\u001a\u0001\u0000\u0000\u0000"+
		"\u0003\u0000@F\u0001\u0000\u0001\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}