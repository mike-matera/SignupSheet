// $ANTLR 3.4 /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g 2013-07-06 18:18:13

  package com.fatboycentral.shared.parser;
  import com.fatboycentral.shared.SignupDatabase; 
  import com.fatboycentral.shared.SignupData.SignupEntry; 
  import com.google.gwt.user.client.Window; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class SingupListParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "INT", "JOBID", "NL", "OPTION", "PLUS", "QUOTED", "SEP", "WORK", "WS"
    };

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

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public SingupListParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public SingupListParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return SingupListParser.tokenNames; }
    public String getGrammarFileName() { return "/home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g"; }

      
      public RecognitionException first_error = null;
      public String error_message = null;
      
      @Override
      public void reportError(RecognitionException e) {
        if (first_error == null) {
          first_error = e;
          error_message = getErrorMessage(e, this.getTokenNames());
        }
      }


    public static class sheet_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "sheet"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:50:1: sheet : ( job )+ EOF ;
    public final SingupListParser.sheet_return sheet() throws RecognitionException {
        SingupListParser.sheet_return retval = new SingupListParser.sheet_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token EOF2=null;
        SingupListParser.job_return job1 =null;


        Object EOF2_tree=null;

        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:50:6: ( ( job )+ EOF )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:50:8: ( job )+ EOF
            {
            root_0 = (Object)adaptor.nil();


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:50:8: ( job )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==JOBID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:50:8: job
            	    {
            	    pushFollow(FOLLOW_job_in_sheet62);
            	    job1=job();

            	    state._fsp--;

            	    adaptor.addChild(root_0, job1.getTree());

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


            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_sheet65); 
            EOF2_tree = 
            (Object)adaptor.create(EOF2)
            ;
            adaptor.addChild(root_0, EOF2_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "sheet"


    public static class job_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "job"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:1: job : JOBID ^ ( OPTION )? QUOTED ( SEP | work )* ;
    public final SingupListParser.job_return job() throws RecognitionException {
        SingupListParser.job_return retval = new SingupListParser.job_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token JOBID3=null;
        Token OPTION4=null;
        Token QUOTED5=null;
        Token SEP6=null;
        SingupListParser.work_return work7 =null;


        Object JOBID3_tree=null;
        Object OPTION4_tree=null;
        Object QUOTED5_tree=null;
        Object SEP6_tree=null;

        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:4: ( JOBID ^ ( OPTION )? QUOTED ( SEP | work )* )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:6: JOBID ^ ( OPTION )? QUOTED ( SEP | work )*
            {
            root_0 = (Object)adaptor.nil();


            JOBID3=(Token)match(input,JOBID,FOLLOW_JOBID_in_job73); 
            JOBID3_tree = 
            (Object)adaptor.create(JOBID3)
            ;
            root_0 = (Object)adaptor.becomeRoot(JOBID3_tree, root_0);


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:13: ( OPTION )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==OPTION) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:13: OPTION
                    {
                    OPTION4=(Token)match(input,OPTION,FOLLOW_OPTION_in_job76); 
                    OPTION4_tree = 
                    (Object)adaptor.create(OPTION4)
                    ;
                    adaptor.addChild(root_0, OPTION4_tree);


                    }
                    break;

            }


            QUOTED5=(Token)match(input,QUOTED,FOLLOW_QUOTED_in_job79); 
            QUOTED5_tree = 
            (Object)adaptor.create(QUOTED5)
            ;
            adaptor.addChild(root_0, QUOTED5_tree);


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:28: ( SEP | work )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==SEP) ) {
                    alt3=1;
                }
                else if ( (LA3_0==INT||(LA3_0 >= OPTION && LA3_0 <= PLUS)) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:29: SEP
            	    {
            	    SEP6=(Token)match(input,SEP,FOLLOW_SEP_in_job82); 
            	    SEP6_tree = 
            	    (Object)adaptor.create(SEP6)
            	    ;
            	    adaptor.addChild(root_0, SEP6_tree);


            	    }
            	    break;
            	case 2 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:52:35: work
            	    {
            	    pushFollow(FOLLOW_work_in_job86);
            	    work7=work();

            	    state._fsp--;

            	    adaptor.addChild(root_0, work7.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "job"


    public static class work_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "work"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:1: work : ( OPTION )? ( PLUS )? INT WORK ^ ( QUOTED )? ;
    public final SingupListParser.work_return work() throws RecognitionException {
        SingupListParser.work_return retval = new SingupListParser.work_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPTION8=null;
        Token PLUS9=null;
        Token INT10=null;
        Token WORK11=null;
        Token QUOTED12=null;

        Object OPTION8_tree=null;
        Object PLUS9_tree=null;
        Object INT10_tree=null;
        Object WORK11_tree=null;
        Object QUOTED12_tree=null;

        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:5: ( ( OPTION )? ( PLUS )? INT WORK ^ ( QUOTED )? )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:7: ( OPTION )? ( PLUS )? INT WORK ^ ( QUOTED )?
            {
            root_0 = (Object)adaptor.nil();


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:7: ( OPTION )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==OPTION) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:7: OPTION
                    {
                    OPTION8=(Token)match(input,OPTION,FOLLOW_OPTION_in_work96); 
                    OPTION8_tree = 
                    (Object)adaptor.create(OPTION8)
                    ;
                    adaptor.addChild(root_0, OPTION8_tree);


                    }
                    break;

            }


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:15: ( PLUS )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==PLUS) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:15: PLUS
                    {
                    PLUS9=(Token)match(input,PLUS,FOLLOW_PLUS_in_work99); 
                    PLUS9_tree = 
                    (Object)adaptor.create(PLUS9)
                    ;
                    adaptor.addChild(root_0, PLUS9_tree);


                    }
                    break;

            }


            INT10=(Token)match(input,INT,FOLLOW_INT_in_work102); 
            INT10_tree = 
            (Object)adaptor.create(INT10)
            ;
            adaptor.addChild(root_0, INT10_tree);


            WORK11=(Token)match(input,WORK,FOLLOW_WORK_in_work104); 
            WORK11_tree = 
            (Object)adaptor.create(WORK11)
            ;
            root_0 = (Object)adaptor.becomeRoot(WORK11_tree, root_0);


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:31: ( QUOTED )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==QUOTED) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SingupList.g:54:31: QUOTED
                    {
                    QUOTED12=(Token)match(input,QUOTED,FOLLOW_QUOTED_in_work107); 
                    QUOTED12_tree = 
                    (Object)adaptor.create(QUOTED12)
                    ;
                    adaptor.addChild(root_0, QUOTED12_tree);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "work"

    // Delegated rules


 

    public static final BitSet FOLLOW_job_in_sheet62 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_EOF_in_sheet65 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_JOBID_in_job73 = new BitSet(new long[]{0x0000000000000280L});
    public static final BitSet FOLLOW_OPTION_in_job76 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_QUOTED_in_job79 = new BitSet(new long[]{0x0000000000000592L});
    public static final BitSet FOLLOW_SEP_in_job82 = new BitSet(new long[]{0x0000000000000592L});
    public static final BitSet FOLLOW_work_in_job86 = new BitSet(new long[]{0x0000000000000592L});
    public static final BitSet FOLLOW_OPTION_in_work96 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_PLUS_in_work99 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INT_in_work102 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_WORK_in_work104 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_QUOTED_in_work107 = new BitSet(new long[]{0x0000000000000002L});

}