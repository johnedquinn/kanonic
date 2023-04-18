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
		EPSILON=1, SELECT=2, FROM=3, PAREN_LEFT=4, PAREN_RIGHT=5, SYMBOL=6, WS=7, 
		UNRECOGNIZED=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"EPSILON", "SELECT", "FROM", "PAREN_LEFT", "PAREN_RIGHT", "SYMBOL", "WS", 
			"UNRECOGNIZED", "WHITESPACE"
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
		"\u0004\u0000\b=\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0005\u0005.\b"+
		"\u0005\n\u0005\f\u00051\t\u0005\u0001\u0006\u0004\u00064\b\u0006\u000b"+
		"\u0006\f\u00065\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0000\u0000\t\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004"+
		"\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\u0000\u0001\u0000\u000f\u0002"+
		"\u0000EEee\u0002\u0000PPpp\u0002\u0000SSss\u0002\u0000IIii\u0002\u0000"+
		"LLll\u0002\u0000OOoo\u0002\u0000NNnn\u0002\u0000CCcc\u0002\u0000TTtt\u0002"+
		"\u0000FFff\u0002\u0000RRrr\u0002\u0000MMmm\u0004\u0000$$AZ__az\u0005\u0000"+
		"$$09AZ__az\u0003\u0000\t\n\r\r  =\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0001\u0013\u0001\u0000\u0000\u0000\u0003\u001b"+
		"\u0001\u0000\u0000\u0000\u0005\"\u0001\u0000\u0000\u0000\u0007\'\u0001"+
		"\u0000\u0000\u0000\t)\u0001\u0000\u0000\u0000\u000b+\u0001\u0000\u0000"+
		"\u0000\r3\u0001\u0000\u0000\u0000\u000f9\u0001\u0000\u0000\u0000\u0011"+
		";\u0001\u0000\u0000\u0000\u0013\u0014\u0007\u0000\u0000\u0000\u0014\u0015"+
		"\u0007\u0001\u0000\u0000\u0015\u0016\u0007\u0002\u0000\u0000\u0016\u0017"+
		"\u0007\u0003\u0000\u0000\u0017\u0018\u0007\u0004\u0000\u0000\u0018\u0019"+
		"\u0007\u0005\u0000\u0000\u0019\u001a\u0007\u0006\u0000\u0000\u001a\u0002"+
		"\u0001\u0000\u0000\u0000\u001b\u001c\u0007\u0002\u0000\u0000\u001c\u001d"+
		"\u0007\u0000\u0000\u0000\u001d\u001e\u0007\u0004\u0000\u0000\u001e\u001f"+
		"\u0007\u0000\u0000\u0000\u001f \u0007\u0007\u0000\u0000 !\u0007\b\u0000"+
		"\u0000!\u0004\u0001\u0000\u0000\u0000\"#\u0007\t\u0000\u0000#$\u0007\n"+
		"\u0000\u0000$%\u0007\u0005\u0000\u0000%&\u0007\u000b\u0000\u0000&\u0006"+
		"\u0001\u0000\u0000\u0000\'(\u0005(\u0000\u0000(\b\u0001\u0000\u0000\u0000"+
		")*\u0005)\u0000\u0000*\n\u0001\u0000\u0000\u0000+/\u0007\f\u0000\u0000"+
		",.\u0007\r\u0000\u0000-,\u0001\u0000\u0000\u0000.1\u0001\u0000\u0000\u0000"+
		"/-\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u00000\f\u0001\u0000\u0000"+
		"\u00001/\u0001\u0000\u0000\u000024\u0003\u0011\b\u000032\u0001\u0000\u0000"+
		"\u000045\u0001\u0000\u0000\u000053\u0001\u0000\u0000\u000056\u0001\u0000"+
		"\u0000\u000067\u0001\u0000\u0000\u000078\u0006\u0006\u0000\u00008\u000e"+
		"\u0001\u0000\u0000\u00009:\t\u0000\u0000\u0000:\u0010\u0001\u0000\u0000"+
		"\u0000;<\u0007\u000e\u0000\u0000<\u0012\u0001\u0000\u0000\u0000\u0003"+
		"\u0000/5\u0001\u0000\u0001\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}