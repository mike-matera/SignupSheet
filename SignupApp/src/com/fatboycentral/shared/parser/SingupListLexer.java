// $ANTLR 3.4 /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g 2013-07-06 18:18:13

  package com.fatboycentral.shared.parser;
  import com.google.gwt.user.client.Window; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SingupListLexer extends Lexer {
    public static final int EOF=-1;
    public static final int INT=4;
    public static final int JOBID=5;
    public static final int NL=6;
    public static final int OPTION=7;
    public static final int PLUS=8;
    public static final int QUOTED=9;
    public static final int SEP=10;
    public static final int WORK=11;
    public static final int WS=12;
      
      public RecognitionException first_error = null;
      public String error_message = null;
      
      @Override
      public void reportError(RecognitionException e) {
        if (first_error == null) {
          first_error = e;
          error_message = getErrorMessage(e, this.getTokenNames());
        }
      }
      
      private static boolean isWS(char c) {
        return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
      }


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public SingupListLexer() {} 
    public SingupListLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SingupListLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g"; }

    // $ANTLR start "JOBID"
    public final void mJOBID() throws RecognitionException {
        try {
            int _type = JOBID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:56:6: ( '[' (~ ( NL | ']' ) )+ ']' )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:56:8: '[' (~ ( NL | ']' ) )+ ']'
            {
            match('['); 

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:56:12: (~ ( NL | ']' ) )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '\u0000' && LA1_0 <= '\t')||(LA1_0 >= '\u000B' && LA1_0 <= '\\')||(LA1_0 >= '^' && LA1_0 <= '\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\\')||(input.LA(1) >= '^' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            match(']'); 


              setText(getText().substring(1,getText().length()-1));


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "JOBID"

    // $ANTLR start "OPTION"
    public final void mOPTION() throws RecognitionException {
        try {
            int _type = OPTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:60:7: ( '(' (~ ( ')' ) )* ')' )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:60:9: '(' (~ ( ')' ) )* ')'
            {
            match('('); 

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:60:13: (~ ( ')' ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '\u0000' && LA2_0 <= '(')||(LA2_0 >= '*' && LA2_0 <= '\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '(')||(input.LA(1) >= '*' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            match(')'); 


              String text = getText();
              text = text.substring(1,text.length()-1);
              while (text.length() > 0 && isWS(text.charAt(0))) {
                text = text.substring(1);
              }
              setText(text); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OPTION"

    // $ANTLR start "QUOTED"
    public final void mQUOTED() throws RecognitionException {
        try {
            int _type = QUOTED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:69:7: ( '{' (~ ( '}' ) )* '}' )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:69:9: '{' (~ ( '}' ) )* '}'
            {
            match('{'); 

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:69:13: (~ ( '}' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= '\u0000' && LA3_0 <= '|')||(LA3_0 >= '~' && LA3_0 <= '\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '|')||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            match('}'); 


              String text = getText();
              // Kill the { and the }
              text = text.substring(1,text.length()-1);
              // Kill leading whitespace
              while (text.length() > 0 && isWS(text.charAt(0))) {
                text = text.substring(1);
              }
              // Kill trailing whitespace
              while (text.length() > 0 && isWS(text.charAt(text.length()-1))) {
                text = text.substring(0,text.length()-1);
              }  
              setText(text); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "QUOTED"

    // $ANTLR start "SEP"
    public final void mSEP() throws RecognitionException {
        try {
            int _type = SEP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:84:4: ( '=' (~ ( NL ) )+ )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:84:6: '=' (~ ( NL ) )+
            {
            match('='); 

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:84:10: (~ ( NL ) )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '\u0000' && LA4_0 <= '\t')||(LA4_0 >= '\u000B' && LA4_0 <= '\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);



              String text = getText();
              text = text.substring(1);
              while (isWS(text.charAt(0))) {
                text = text.substring(1);
              }
              setText(text);  


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SEP"

    // $ANTLR start "WORK"
    public final void mWORK() throws RecognitionException {
        try {
            int _type = WORK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:93:5: ( ':' (~ ( NL | '{' ) )+ )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:93:7: ':' (~ ( NL | '{' ) )+
            {
            match(':'); 

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:93:11: (~ ( NL | '{' ) )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\t')||(LA5_0 >= '\u000B' && LA5_0 <= 'z')||(LA5_0 >= '|' && LA5_0 <= '\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= 'z')||(input.LA(1) >= '|' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


              
              String text = getText();
              text = text.substring(1);
              while (isWS(text.charAt(0))) {
                text = text.substring(1);
              }
              setText(text);  


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORK"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:102:5: ( '+' )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:102:7: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:104:4: ( ( '0' .. '9' )+ )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:104:6: ( '0' .. '9' )+
            {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:104:6: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "NL"
    public final void mNL() throws RecognitionException {
        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:106:12: ( '\\n' )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:106:14: '\\n'
            {
            match('\n'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NL"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:108:3: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:108:5: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:8: ( JOBID | OPTION | QUOTED | SEP | WORK | PLUS | INT | WS )
        int alt7=8;
        switch ( input.LA(1) ) {
        case '[':
            {
            alt7=1;
            }
            break;
        case '(':
            {
            alt7=2;
            }
            break;
        case '{':
            {
            alt7=3;
            }
            break;
        case '=':
            {
            alt7=4;
            }
            break;
        case ':':
            {
            alt7=5;
            }
            break;
        case '+':
            {
            alt7=6;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt7=7;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt7=8;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 7, 0, input);

            throw nvae;

        }

        switch (alt7) {
            case 1 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:10: JOBID
                {
                mJOBID(); 


                }
                break;
            case 2 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:16: OPTION
                {
                mOPTION(); 


                }
                break;
            case 3 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:23: QUOTED
                {
                mQUOTED(); 


                }
                break;
            case 4 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:30: SEP
                {
                mSEP(); 


                }
                break;
            case 5 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:34: WORK
                {
                mWORK(); 


                }
                break;
            case 6 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:39: PLUS
                {
                mPLUS(); 


                }
                break;
            case 7 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:44: INT
                {
                mINT(); 


                }
                break;
            case 8 :
                // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:1:48: WS
                {
                mWS(); 


                }
                break;

        }

    }


 

}