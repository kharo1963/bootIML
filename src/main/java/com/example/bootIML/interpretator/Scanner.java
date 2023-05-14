package com.example.bootIML.interpretator;

import java.io.RandomAccessFile;

public class Scanner {
    RandomAccessFile fp;
    
    char   c;
    long pos;
    
    int look(String buf, String [] list) {
        int i = 0;
        while (i < list.length) {
        	if (buf.equals(list[i]))
                return i;
            ++i;
        }
        return 0;
    }
    
    void gc() {
    	byte   b;
        String tB;
        byte[] bytes = new byte[1];
    	try {
    		b = fp.readByte();    		
     		bytes[0] = b;
     		tB = new String(bytes);
      		c = tB.charAt(0);
       	}
    	catch (Exception e) {
    		throw new RuntimeException ("can’t readChar from file");
    	}      		
    }
    
    void ungetc (RandomAccessFile fp) {
       	try {
     		fp.seek(fp.getFilePointer() - 1); 		
    	}	
    	catch (Exception e) {
    		throw new RuntimeException ("can’t ungetc to file");
    	}      	
    }    
    
    int put(String buf) {
        int k = 0;
        for (Ident l : StatD.TID) {
        	if (buf.equals(l.name))
        		return k;
        	++k;
        }
        StatD.TID.addElement(new Ident(buf));
        return StatD.TID.size() - 1;
    }
    
    Type_of_lex GetTypeOfOrd (int ord) {
    	int k = 0;
		for (Type_of_lex e: Type_of_lex.values()) {
			if (k == ord)
				return e;
			++k;
		}
		return Type_of_lex.LEX_NULL;
    }
    
    public
    static String TW[] = { "", "and", "begin", "bool", "do", "else", "end", "if", "false", "int", "not", "or", "program",
        "read", "then", "true", "var", "while", "write", "get"};
    static String TD[] = { "@", ";", ",", ":", ":=", "(", ")", "=", "<", ">", "+", "-", "*", "/", "<=", "!=", ">="};
   
    Scanner(String program) {
    	try {
    		fp = new RandomAccessFile(program, "r");
    	}	
    	catch (Exception e) {
    		throw new RuntimeException ("can’t open file");
    	}    
    }
    
    void store_pos() {
    	try {
    		pos = fp.getFilePointer();
    	}	
    	catch (Exception e) {
    		throw new RuntimeException ("can’t getFilePointer");
    	}         
    }
    
    void restore_pos() {
    	try {
         fp.seek(pos);
		}	
		catch (Exception e) {
			throw new RuntimeException ("can’t getFilePointer");
		}           
    }
    
    Lex get_lex() {
        int         d = 1, j;
        String      buf = "";
        State_lex   CS = State_lex.H;
        do {
            gc();
            switch (CS) {
            case H:
                if (c == ' ' || c == '\n' || c == '\r' || c == '\t');
                else if (Character.isLetter(c)) {
                    buf = buf + c;
                    CS = State_lex.IDENT;
                }
                else if (Character.isDigit(c)) {
                    d = c - '0';
                    CS = State_lex.NUMB;
                }
                else if (c == '{') {
                    CS = State_lex.COM;
                }
                else if (c == ':' || c == '<' || c == '>') {
                    buf = buf + c;
                    CS = State_lex.ALE;
                }
                else if (c == '@')
                    return new Lex(Type_of_lex.LEX_FIN);
                else if (c == '!') {
                    buf = buf + c;
                    CS = State_lex.NEQ;
                }
                else {
                    buf = buf + c;                  
                    j = look (buf, TD);                  
                    if (j > 0) {      
                        return new Lex(GetTypeOfOrd(j + Type_of_lex.LEX_FIN.ordinal()), j);
                    }
                    else
                         throw new RuntimeException (String.valueOf(c));
                }
                break;
            case IDENT:
                if (Character.isLetterOrDigit(c)) {
                    buf = buf + c;
                }
                else {
                    ungetc(fp);
                    j = look(buf, TW);
                    if (j > 0) {
                          return new Lex(GetTypeOfOrd(j), j);
                    }
                    else {
                        j = put(buf);
                        return new Lex(Type_of_lex.LEX_ID, j);
                    }
                }
                break;
            case NUMB:
                if (Character.isDigit(c)) {
                    d = d * 10 + (c - '0');
                }
                else {
                    ungetc(fp);
                    return new Lex(Type_of_lex.LEX_NUM, d);
                }
                break;
            case COM:
                if (c == '}') {
                    CS = State_lex.H;
                }
                else if (c == '@' || c == '{')
                	throw new RuntimeException (String.valueOf(c));
                break;
            case ALE:
                if (c == '=') {
                    buf = buf + c;
                }
                else {
                    ungetc(fp);
                }
                j = look(buf, TD);
                return new Lex(GetTypeOfOrd(j + Type_of_lex.LEX_FIN.ordinal()), j);
            case NEQ:
                if (c == '=') {
                    buf = buf + c;
                    j = look(buf, TD);
                    return new Lex(Type_of_lex.LEX_NEQ, j);
                }
                else
                	throw new RuntimeException (String.valueOf('!'));
             } //end switch
        } while (true);
    }
    int getRestArg() {
        String      buf = "";
         do {
            gc();
            if (c == ')') {
                System.out.println("getRestArg");
                System.out.println(buf);
                ungetc(fp);
                StatD.restArg.addElement(buf);
                return StatD.restArg.size() - 1;
              }
            else {
                buf = buf + c;
            }
        } while (true);
    }
}

