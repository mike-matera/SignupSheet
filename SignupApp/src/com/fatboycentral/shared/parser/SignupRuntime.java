// $ANTLR 3.4 /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g 2013-07-06 18:18:14

  package com.fatboycentral.shared.parser;
  import com.fatboycentral.shared.SignupDatabase; 
  import com.fatboycentral.shared.SignupData.SignupEntry; 
  import com.fatboycentral.shared.SignupData.SchemaEntry; 
  import com.google.gwt.user.client.Window; 


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SignupRuntime extends TreeParser {
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
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators


    public SignupRuntime(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public SignupRuntime(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return SignupRuntime.tokenNames; }
    public String getGrammarFileName() { return "/home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g"; }


      private String masterJob = "";
      private void setMasterJob(String mstr) {
        masterJob = mstr;
      }
      private String getMasterJob() {
        return masterJob;
      }



    // $ANTLR start "execute"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:27:1: execute[SignupDatabase db] : ^( JOBID ( OPTION )? QUOTED ( SEP | execute_entry[db, $JOBID.getText(), job_separator, admin_only] )* ) ;
    public final void execute(SignupDatabase db) throws RecognitionException {
        CommonTree OPTION1=null;
        CommonTree JOBID2=null;
        CommonTree QUOTED3=null;
        CommonTree SEP4=null;


            String job_separator = "";
            boolean admin_only = false;
          
        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:32:3: ( ^( JOBID ( OPTION )? QUOTED ( SEP | execute_entry[db, $JOBID.getText(), job_separator, admin_only] )* ) )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:32:5: ^( JOBID ( OPTION )? QUOTED ( SEP | execute_entry[db, $JOBID.getText(), job_separator, admin_only] )* )
            {
            JOBID2=(CommonTree)match(input,JOBID,FOLLOW_JOBID_in_execute68); 

            match(input, Token.DOWN, null); 
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:32:13: ( OPTION )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==OPTION) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:32:13: OPTION
                    {
                    OPTION1=(CommonTree)match(input,OPTION,FOLLOW_OPTION_in_execute70); 

                    }
                    break;

            }


            QUOTED3=(CommonTree)match(input,QUOTED,FOLLOW_QUOTED_in_execute73); 


                  if (OPTION1 != null) {
                    admin_only = true;
                  }
                  db.addJob(JOBID2.getText(), QUOTED3.getText());
              

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:37:5: ( SEP | execute_entry[db, $JOBID.getText(), job_separator, admin_only] )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==SEP) ) {
                    alt2=1;
                }
                else if ( (LA2_0==WORK) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:37:6: SEP
            	    {
            	    SEP4=(CommonTree)match(input,SEP,FOLLOW_SEP_in_execute78); 


            	        job_separator = SEP4.getText();
            	        SignupEntry s = new SignupEntry();
            	        s.shift.description = job_separator;
            	        s.shift.job = JOBID2.getText();
            	        s.shift.jid = "";
            	        s.shift.instructions = "";
            	        s.slots = 0;
            	        db.add(s);
            	      

            	    }
            	    break;
            	case 2 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:47:5: execute_entry[db, $JOBID.getText(), job_separator, admin_only]
            	    {
            	    pushFollow(FOLLOW_execute_entry_in_execute86);
            	    execute_entry(db, JOBID2.getText(), job_separator, admin_only);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "execute"



    // $ANTLR start "execute_entry"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:50:10: fragment execute_entry[SignupDatabase db, String jobtitle, String job_separator, boolean admin_only] : ^( WORK ( OPTION )? ( PLUS )? INT ( QUOTED )? ) ;
    public final void execute_entry(SignupDatabase db, String jobtitle, String job_separator, boolean admin_only) throws RecognitionException {
        CommonTree WORK5=null;
        CommonTree OPTION6=null;
        CommonTree PLUS7=null;
        CommonTree INT8=null;
        CommonTree QUOTED9=null;


              SignupEntry s = new SignupEntry();
          
        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:54:3: ( ^( WORK ( OPTION )? ( PLUS )? INT ( QUOTED )? ) )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:54:5: ^( WORK ( OPTION )? ( PLUS )? INT ( QUOTED )? )
            {
            WORK5=(CommonTree)match(input,WORK,FOLLOW_WORK_in_execute_entry118); 

            match(input, Token.DOWN, null); 
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:54:12: ( OPTION )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==OPTION) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:54:12: OPTION
                    {
                    OPTION6=(CommonTree)match(input,OPTION,FOLLOW_OPTION_in_execute_entry120); 

                    }
                    break;

            }


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:54:20: ( PLUS )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==PLUS) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:54:20: PLUS
                    {
                    PLUS7=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_execute_entry123); 

                    }
                    break;

            }


            INT8=(CommonTree)match(input,INT,FOLLOW_INT_in_execute_entry126); 


                  s.shift.description = WORK5.getText();
                  s.shift.adminOnly = admin_only;
                  if (OPTION6 != null) {
                    s.shift.adminOnly = true;
                  }
                  if (PLUS7 == null) {
                    s.shift.master = "";
                    s.shift.jid = job_separator + " " + s.shift.description;
                    setMasterJob(s.shift.jid);
                  }else{
                    s.shift.master = getMasterJob();
                    s.shift.jid = s.shift.master + " " + s.shift.description;
                  }
                  s.shift.job = jobtitle;
                  s.shift.instructions = "";
                  s.slots = Integer.parseInt(INT8.getText());
                  s.shift.slots = s.slots;
              

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:72:5: ( QUOTED )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==QUOTED) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:72:6: QUOTED
                    {
                    QUOTED9=(CommonTree)match(input,QUOTED,FOLLOW_QUOTED_in_execute_entry131); 


                          s.shift.instructions = QUOTED9.getText();
                      

                    }
                    break;

            }



                  db.add(s);
              

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "execute_entry"



    // $ANTLR start "split"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:79:1: split returns [ArrayList<SchemaEntry> jobs] : (newent= split_entry )* ;
    public final ArrayList<SchemaEntry> split() throws RecognitionException {
        ArrayList<SchemaEntry> jobs = null;


        SchemaEntry newent =null;



            int jobno = 0;    
            jobs = new ArrayList<SchemaEntry>();
          
        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:84:3: ( (newent= split_entry )* )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:84:5: (newent= split_entry )*
            {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:84:5: (newent= split_entry )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==JOBID) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:84:6: newent= split_entry
            	    {
            	    pushFollow(FOLLOW_split_entry_in_split167);
            	    newent=split_entry();

            	    state._fsp--;



            	        newent.order = jobno++;
            	        jobs.add(newent);
            	      

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return jobs;
    }
    // $ANTLR end "split"



    // $ANTLR start "split_entry"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:90:1: split_entry returns [SchemaEntry ent] : ^( JOBID ( OPTION )? QUOTED ( split_sep[bldr] | split_slot[bldr] )* ) ;
    public final SchemaEntry split_entry() throws RecognitionException {
        SchemaEntry ent = null;


        CommonTree JOBID10=null;
        CommonTree OPTION11=null;
        CommonTree QUOTED12=null;


            StringBuilder bldr = new StringBuilder();
            ent = new SchemaEntry();
          
        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:98:3: ( ^( JOBID ( OPTION )? QUOTED ( split_sep[bldr] | split_slot[bldr] )* ) )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:98:5: ^( JOBID ( OPTION )? QUOTED ( split_sep[bldr] | split_slot[bldr] )* )
            {
            JOBID10=(CommonTree)match(input,JOBID,FOLLOW_JOBID_in_split_entry203); 

            match(input, Token.DOWN, null); 
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:98:13: ( OPTION )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==OPTION) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:98:13: OPTION
                    {
                    OPTION11=(CommonTree)match(input,OPTION,FOLLOW_OPTION_in_split_entry205); 

                    }
                    break;

            }


            QUOTED12=(CommonTree)match(input,QUOTED,FOLLOW_QUOTED_in_split_entry208); 


                ent.key = JOBID10.getText();
                bldr.append("[" + ent.key + "]");
                if (OPTION11 != null) {
                  bldr.append(" (" + OPTION11.getText() + ")");
                }
                bldr.append(" {\n");
                bldr.append(QUOTED12.getText());
                bldr.append("\n}\n");
              

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:107:5: ( split_sep[bldr] | split_slot[bldr] )*
            loop8:
            do {
                int alt8=3;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==SEP) ) {
                    alt8=1;
                }
                else if ( (LA8_0==WORK) ) {
                    alt8=2;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:107:6: split_sep[bldr]
            	    {
            	    pushFollow(FOLLOW_split_sep_in_split_entry213);
            	    split_sep(bldr);

            	    state._fsp--;


            	    }
            	    break;
            	case 2 :
            	    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:107:24: split_slot[bldr]
            	    {
            	    pushFollow(FOLLOW_split_slot_in_split_entry218);
            	    split_slot(bldr);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            match(input, Token.UP, null); 


            }


                ent.data = bldr.toString();
              
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ent;
    }
    // $ANTLR end "split_entry"



    // $ANTLR start "split_sep"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:110:1: split_sep[StringBuilder bldr] : SEP ;
    public final void split_sep(StringBuilder bldr) throws RecognitionException {
        CommonTree SEP13=null;

        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:111:3: ( SEP )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:111:5: SEP
            {
            SEP13=(CommonTree)match(input,SEP,FOLLOW_SEP_in_split_sep238); 


                bldr.append("= " + SEP13.getText() + "\n");
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "split_sep"



    // $ANTLR start "split_slot"
    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:116:1: split_slot[StringBuilder bldr] : ^( WORK ( OPTION )? ( PLUS )? INT ( QUOTED )? ) ;
    public final void split_slot(StringBuilder bldr) throws RecognitionException {
        CommonTree OPTION14=null;
        CommonTree PLUS15=null;
        CommonTree INT16=null;
        CommonTree WORK17=null;
        CommonTree QUOTED18=null;

        try {
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:3: ( ^( WORK ( OPTION )? ( PLUS )? INT ( QUOTED )? ) )
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:5: ^( WORK ( OPTION )? ( PLUS )? INT ( QUOTED )? )
            {
            WORK17=(CommonTree)match(input,WORK,FOLLOW_WORK_in_split_slot259); 

            match(input, Token.DOWN, null); 
            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:12: ( OPTION )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==OPTION) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:12: OPTION
                    {
                    OPTION14=(CommonTree)match(input,OPTION,FOLLOW_OPTION_in_split_slot261); 

                    }
                    break;

            }


            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:20: ( PLUS )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==PLUS) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:20: PLUS
                    {
                    PLUS15=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_split_slot264); 

                    }
                    break;

            }


            INT16=(CommonTree)match(input,INT,FOLLOW_INT_in_split_slot267); 

            // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:30: ( QUOTED )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==QUOTED) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /home/maximus/GWT/workspace/SignupSheet/src/com/fatboycentral/shared/parser/SignupRuntime.g:117:30: QUOTED
                    {
                    QUOTED18=(CommonTree)match(input,QUOTED,FOLLOW_QUOTED_in_split_slot269); 

                    }
                    break;

            }


            match(input, Token.UP, null); 



                if (OPTION14 != null) {
                  bldr.append("(" + OPTION14 + ")");
                }
                if (PLUS15 != null) {
                  bldr.append("+");
                }
                bldr.append(INT16.getText() + ": " + WORK17);
                if (QUOTED18 != null) {
                  bldr.append("{\n" + QUOTED18.getText() + "\n}");
                }
                bldr.append("\n");
              

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "split_slot"

    // Delegated rules


 

    public static final BitSet FOLLOW_JOBID_in_execute68 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_OPTION_in_execute70 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_QUOTED_in_execute73 = new BitSet(new long[]{0x0000000000000C08L});
    public static final BitSet FOLLOW_SEP_in_execute78 = new BitSet(new long[]{0x0000000000000C08L});
    public static final BitSet FOLLOW_execute_entry_in_execute86 = new BitSet(new long[]{0x0000000000000C08L});
    public static final BitSet FOLLOW_WORK_in_execute_entry118 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_OPTION_in_execute_entry120 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_PLUS_in_execute_entry123 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INT_in_execute_entry126 = new BitSet(new long[]{0x0000000000000208L});
    public static final BitSet FOLLOW_QUOTED_in_execute_entry131 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_split_entry_in_split167 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_JOBID_in_split_entry203 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_OPTION_in_split_entry205 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_QUOTED_in_split_entry208 = new BitSet(new long[]{0x0000000000000C08L});
    public static final BitSet FOLLOW_split_sep_in_split_entry213 = new BitSet(new long[]{0x0000000000000C08L});
    public static final BitSet FOLLOW_split_slot_in_split_entry218 = new BitSet(new long[]{0x0000000000000C08L});
    public static final BitSet FOLLOW_SEP_in_split_sep238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORK_in_split_slot259 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_OPTION_in_split_slot261 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_PLUS_in_split_slot264 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INT_in_split_slot267 = new BitSet(new long[]{0x0000000000000208L});
    public static final BitSet FOLLOW_QUOTED_in_split_slot269 = new BitSet(new long[]{0x0000000000000008L});

}