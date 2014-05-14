tree grammar SignupRuntime;

options {
  language = Java;
  tokenVocab = SingupList;
  ASTLabelType = CommonTree;
}

@header {
  package com.fatboycentral.shared.parser;
  import com.fatboycentral.shared.SignupDatabase; 
  import com.fatboycentral.shared.SignupData.SignupEntry; 
  import com.fatboycentral.shared.SignupData.SchemaEntry; 
  import com.google.gwt.user.client.Window; 
}

@members {
  private String masterJob = "";
  private void setMasterJob(String mstr) {
    masterJob = mstr;
  }
  private String getMasterJob() {
    return masterJob;
  }
}

execute [SignupDatabase db]
  @init {
    String job_separator = "";
    boolean admin_only = false;
  }
  : ^(JOBID OPTION? QUOTED {
      if ($OPTION != null) {
        admin_only = true;
      }
      db.addJob($JOBID.getText(), $QUOTED.getText());
  } (SEP {
    job_separator = $SEP.getText();
    SignupEntry s = new SignupEntry();
    s.shift.description = job_separator;
    s.shift.job = $JOBID.getText();
    s.shift.jid = "";
    s.shift.instructions = "";
    s.slots = 0;
    db.add(s);
  }
  | execute_entry[db, $JOBID.getText(), job_separator, admin_only])*) 
  ;
  
fragment execute_entry[SignupDatabase db, String jobtitle, String job_separator, boolean admin_only] 
  @init {
      SignupEntry s = new SignupEntry();
  }
  : ^(WORK OPTION? PLUS? INT {
      s.shift.description = $WORK.getText();
      s.shift.adminOnly = admin_only;
      if ($OPTION != null) {
        s.shift.adminOnly = true;
      }
      if ($PLUS == null) {
        s.shift.master = "";
        s.shift.jid = job_separator + " " + s.shift.description;
        setMasterJob(s.shift.jid);
      }else{
        s.shift.master = getMasterJob();
        s.shift.jid = s.shift.master + " " + s.shift.description;
      }
      s.shift.job = jobtitle;
      s.shift.instructions = "";
      s.slots = Integer.parseInt($INT.getText());
      s.shift.slots = s.slots;
  } (QUOTED {
      s.shift.instructions = $QUOTED.getText();
  })? {
      db.add(s);
  })
  ;
  
split returns [ArrayList<SchemaEntry> jobs]
  @init {
    int jobno = 0;    
    jobs = new ArrayList<SchemaEntry>();
  }
  : (newent=split_entry {
    newent.order = jobno++;
    jobs.add(newent);
  })*
  ;

split_entry returns [SchemaEntry ent]
  @init {
    StringBuilder bldr = new StringBuilder();
    ent = new SchemaEntry();
  }
  @after {
    ent.data = bldr.toString();
  }
  : ^(JOBID OPTION? QUOTED {
    ent.key = $JOBID.getText();
    bldr.append("[" + ent.key + "]");
    if ($OPTION != null) {
      bldr.append(" (" + $OPTION.getText() + ")");
    }
    bldr.append(" {\n");
    bldr.append($QUOTED.getText());
    bldr.append("\n}\n");
  } (split_sep[bldr] | split_slot[bldr])*)
  ; 

split_sep [StringBuilder bldr]
  : SEP {
    bldr.append("= " + $SEP.getText() + "\n");
  }
  ; 
  
split_slot [StringBuilder bldr]
  : ^(WORK OPTION? PLUS? INT QUOTED?) {
    if ($OPTION != null) {
      bldr.append("(" + $OPTION + ")");
    }
    if ($PLUS != null) {
      bldr.append("+");
    }
    bldr.append($INT.getText() + ": " + $WORK);
    if ($QUOTED != null) {
      bldr.append("{\n" + $QUOTED.getText() + "\n}");
    }
    bldr.append("\n");
  }
  ;
