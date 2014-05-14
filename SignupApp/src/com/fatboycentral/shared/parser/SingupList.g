grammar SingupList;

options {
  language = Java;
  output = AST;
}

@header {
  package com.fatboycentral.shared.parser;
  import com.fatboycentral.shared.SignupDatabase; 
  import com.fatboycentral.shared.SignupData.SignupEntry; 
  import com.google.gwt.user.client.Window; 
}

@lexer::header {
  package com.fatboycentral.shared.parser;
  import com.google.gwt.user.client.Window; 
}

@members {  
  public RecognitionException first_error = null;
  public String error_message = null;
  
  @Override
  public void reportError(RecognitionException e) {
    if (first_error == null) {
      first_error = e;
      error_message = getErrorMessage(e, this.getTokenNames());
    }
  }
}

@lexer::members {  
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
}

sheet: job+ EOF ;

job: JOBID^ OPTION? QUOTED (SEP | work)* ;

work: OPTION? PLUS? INT WORK^ QUOTED? ;

JOBID: '[' ~(NL | ']')+ ']' {
  setText(getText().substring(1,getText().length()-1));
};

OPTION: '(' ~(')')* ')' {
  String text = getText();
  text = text.substring(1,text.length()-1);
  while (text.length() > 0 && isWS(text.charAt(0))) {
    text = text.substring(1);
  }
  setText(text); 
};

QUOTED: '{' ~('}')* '}' {
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
}; 

SEP: '=' ~(NL)+ {
  String text = getText();
  text = text.substring(1);
  while (isWS(text.charAt(0))) {
    text = text.substring(1);
  }
  setText(text);  
};

WORK: ':' ~(NL | '{')+ {  
  String text = getText();
  text = text.substring(1);
  while (isWS(text.charAt(0))) {
    text = text.substring(1);
  }
  setText(text);  
};

PLUS: '+' ;

INT: ('0' .. '9')+;

fragment NL: '\n';

WS: (' ' | '\t' | '\r' | '\n') {$channel=HIDDEN;}; 
