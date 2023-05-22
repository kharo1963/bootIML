package com.example.bootIML.interpretator;

import java.util.Stack;
import java.util.Vector;

public class Parser {
    Lex          curr_lex;
    Type_of_lex  c_type;
    int          c_val;
    Scanner      scan;
    Stack<Integer>     st_int = new Stack<Integer>();
    Stack<Type_of_lex> st_lex = new Stack<Type_of_lex>();
    void  P() {
        if (c_type == Type_of_lex.LEX_PROGRAM) {
            gl();
        }
        else
            throw new RuntimeException (curr_lex.toString());
        D1();
        if (c_type == Type_of_lex.LEX_SEMICOLON)
            gl();
        else
        	throw new RuntimeException (curr_lex.toString());
        B();    	
    }
    void  D1() {
        if (c_type == Type_of_lex.LEX_VAR) {
            gl();
            D();
            while (c_type == Type_of_lex.LEX_COMMA) {
                gl();
                D();
            }
        }
        else
        	throw new RuntimeException (curr_lex.toString());   	
    }
    void  D() {
        if (c_type != Type_of_lex.LEX_ID)
        	throw new RuntimeException (curr_lex.toString());
        else {
            st_int.push(c_val);
            gl();
            while (c_type == Type_of_lex.LEX_COMMA) {
                gl();
                if (c_type != Type_of_lex.LEX_ID)
                	throw new RuntimeException (curr_lex.toString());
                else {
                    st_int.push(c_val);
                    gl();
                }
            }
            if (c_type != Type_of_lex.LEX_COLON)
            	throw new RuntimeException (curr_lex.toString());
            else {
                gl();
                if (c_type == Type_of_lex.LEX_INT) {
                    dec(Type_of_lex.LEX_INT);
                    gl();
                }
                else
                    if (c_type == Type_of_lex.LEX_BOOL) {
                        dec(Type_of_lex.LEX_BOOL);
                        gl();
                    }
                    else
                    	throw new RuntimeException (curr_lex.toString());
            }
        }    	
    }
    void  B() {
        if (c_type == Type_of_lex.LEX_BEGIN) {
            gl();
            S();
            while (c_type == Type_of_lex.LEX_SEMICOLON) {
                gl();
                S();
            }
            if (c_type == Type_of_lex.LEX_END) {
                gl();
            }
            else {
            	throw new RuntimeException (curr_lex.toString());
            }
        }
        else
        	throw new RuntimeException (curr_lex.toString());   	
    }
    void  S() {
        int pl0, pl1, pl2, pl3;
        int id_cnt = 0;
        int c_prev_val;
        Type_of_lex  c_prev_type;

        if (c_type == Type_of_lex.LEX_IF) {
            gl();
            E();
            eq_bool();
            pl2 = poliz.size();
            poliz.addElement(new Lex());
            poliz.addElement(new Lex(Type_of_lex.POLIZ_FGO));
            if (c_type == Type_of_lex.LEX_THEN) {
                gl();
                S();
                pl3 = poliz.size();
                poliz.addElement(new Lex());

                poliz.addElement(new Lex(Type_of_lex.POLIZ_GO));
                poliz.setElementAt(new Lex(Type_of_lex.POLIZ_LABEL, poliz.size()),pl2);
  
                if (c_type == Type_of_lex.LEX_ELSE) {
                    gl();
                    S();
                     poliz.setElementAt(new Lex(Type_of_lex.POLIZ_LABEL, poliz.size()),pl3);
                }
                else
                	throw new RuntimeException (curr_lex.toString());
            }
            else
              	throw new RuntimeException (curr_lex.toString());
        }//end if
        else if (c_type == Type_of_lex.LEX_WHILE) {
            pl0 = poliz.size();
            gl();
            E();
            eq_bool();
            pl1 = poliz.size();
            poliz.addElement(new Lex());
            poliz.addElement(new Lex(Type_of_lex.POLIZ_FGO));
            if (c_type == Type_of_lex.LEX_DO) {
                gl();
                S();
                poliz.addElement(new Lex(Type_of_lex.POLIZ_LABEL, pl0));
                poliz.addElement(new Lex(Type_of_lex.POLIZ_GO));
                poliz.setElementAt(new Lex(Type_of_lex.POLIZ_LABEL, poliz.size()),pl1);
            }
            else
            	throw new RuntimeException (curr_lex.toString());
        }//end while
        else if (c_type == Type_of_lex.LEX_READ) {
            gl();
            if (c_type == Type_of_lex.LEX_LPAREN) {
                gl();
                if (c_type == Type_of_lex.LEX_ID) {
                    check_id_in_read();
                    poliz.addElement(new Lex(Type_of_lex.POLIZ_ADDRESS, c_val));
                    gl();
                }
                else
                	throw new RuntimeException (curr_lex.toString());
                if (c_type == Type_of_lex.LEX_RPAREN) {
                    gl();
                    poliz.addElement(new Lex(Type_of_lex.LEX_READ));
                }
                else
                	throw new RuntimeException (curr_lex.toString());
            }
            else
            	throw new RuntimeException (curr_lex.toString());
        }//end read
        else if (c_type == Type_of_lex.LEX_GET) {
            gl();
            if (c_type == Type_of_lex.LEX_LPAREN) {
                gl();
                if (c_type == Type_of_lex.LEX_ID) {
                    check_id_in_read();
                    poliz.addElement(new Lex(Type_of_lex.POLIZ_ADDRESS, c_val));
                    gl();
                }
                else
                    throw new RuntimeException (curr_lex.toString());
                if (c_type == Type_of_lex.LEX_COMMA) {
                    c_val = glRestArg();
                    poliz.addElement(new Lex(Type_of_lex.POLIZ_ADDRESS, c_val));
                    gl();
                }
                else
                    throw new RuntimeException (curr_lex.toString());
                if (c_type == Type_of_lex.LEX_RPAREN) {
                    gl();
                    poliz.addElement(new Lex(Type_of_lex.LEX_GET));
                }
                else
                    throw new RuntimeException (curr_lex.toString());
            }
            else
                throw new RuntimeException (curr_lex.toString());
        }//end get
        else if (c_type == Type_of_lex.LEX_WRITE) {
            gl();
            if (c_type == Type_of_lex.LEX_LPAREN) {
                gl();
                E();
                if (c_type == Type_of_lex.LEX_RPAREN) {
                    gl();
                    poliz.addElement(new Lex(Type_of_lex.LEX_WRITE));
                }
                else
                	throw new RuntimeException (curr_lex.toString());
            }
            else
            	throw new RuntimeException (curr_lex.toString());
        }//end write
        else if (c_type == Type_of_lex.LEX_ID) {
            check_id();
            poliz.addElement(new Lex(Type_of_lex.POLIZ_ADDRESS, c_val));
            gl();
            if (c_type == Type_of_lex.LEX_ASSIGN) {
                ++id_cnt;
                gl();
                while (c_type == Type_of_lex.LEX_ID) {
                    check_id();
                    scan.store_pos();
                    c_prev_val  = c_val;
                    c_prev_type = c_type;
                    gl();
                    if (c_type == Type_of_lex.LEX_ASSIGN) {
                        eq_type();
                        if (StatD.TID.get(c_prev_val).get_declare())
                            st_lex.push(StatD.TID.get(c_prev_val).get_type());
                        else
                        	throw new RuntimeException ("not declared");
                        poliz.addElement(new Lex(Type_of_lex.POLIZ_ADDRESS, c_prev_val));
                        gl();
                        ++id_cnt;
                        continue;
                    }
                    scan.restore_pos();
                    c_val  = c_prev_val;
                    c_type = c_prev_type;
                    break;
                }
                E();
                eq_type();
                while (id_cnt > 0) {
                    --id_cnt;
                    poliz.addElement(new Lex(Type_of_lex.LEX_ASSIGN));
                }
                if (c_type == Type_of_lex.LEX_SEMICOLON)
                    poliz.addElement(new Lex(Type_of_lex.LEX_SEMICOLON));
            }
            else
            	throw new RuntimeException (curr_lex.toString());
        }//assign-end
        else
            B();    	
    }
    void  E() {
        E1();
        if (c_type == Type_of_lex.LEX_EQ  || c_type == Type_of_lex.LEX_LSS || c_type == Type_of_lex.LEX_GTR ||
            c_type == Type_of_lex.LEX_LEQ || c_type == Type_of_lex.LEX_GEQ || c_type == Type_of_lex.LEX_NEQ) {
            st_lex.push(c_type);
            gl();
            E1();
            check_op();
        }   	    	
    }    
    void  E1() {
        T();
        while (c_type == Type_of_lex.LEX_PLUS || c_type == Type_of_lex.LEX_MINUS || c_type == Type_of_lex.LEX_OR) {
            st_lex.push(c_type);
            gl();
            T();
            check_op();
        }    	
    }
    void  T() {
        F();
        while (c_type == Type_of_lex.LEX_TIMES || c_type == Type_of_lex.LEX_SLASH || c_type == Type_of_lex.LEX_AND) {
            st_lex.push(c_type);
            gl();
            F();
            check_op();
        }    	
    }
    void  F() {
        if (c_type == Type_of_lex.LEX_ID) {
            check_id();
            poliz.addElement(new Lex(Type_of_lex.LEX_ID, c_val));
            gl();
        }
        else if (c_type == Type_of_lex.LEX_NUM) {
            st_lex.push(Type_of_lex.LEX_INT);
            poliz.addElement(curr_lex);
            gl();
        }
        else if (c_type == Type_of_lex.LEX_TRUE) {
            st_lex.push(Type_of_lex.LEX_BOOL);
            poliz.addElement(new Lex(Type_of_lex.LEX_TRUE, 1));
            gl();
        }
        else if (c_type == Type_of_lex.LEX_FALSE) {
            st_lex.push(Type_of_lex.LEX_BOOL);
            poliz.addElement(new Lex(Type_of_lex.LEX_FALSE, 0));
            gl();
        }
        else if (c_type == Type_of_lex.LEX_NOT) {
            gl();
            F();
            check_not();
        }
        else if (c_type == Type_of_lex.LEX_LPAREN) {
            gl();
            E();
            if (c_type == Type_of_lex.LEX_RPAREN)
                gl();
            else
            	throw new RuntimeException (curr_lex.toString());
        }
        else
        	throw new RuntimeException (curr_lex.toString());    	
    }
    void  dec(Type_of_lex type) {
        int i;              
        while (!st_int.empty()) {
        	i = StatD.from_st_i (st_int);
            if (StatD.TID.get(i).get_declare())
            	throw new RuntimeException ("twice");  
            else {
            	StatD.TID.get(i).put_declare();
            	StatD.TID.get(i).put_type(type);
            }
        }    	
    }
    void  check_id() {
        if (StatD.TID.get(c_val).get_declare())
            st_lex.push(StatD.TID.get(c_val).get_type());
        else
        	throw new RuntimeException ("not declared");	
    }
    void  check_op() {
        Type_of_lex t1, t2, op, t = Type_of_lex.LEX_INT, r = Type_of_lex.LEX_BOOL;

        t2 = StatD.from_st_t (st_lex);
        op = StatD.from_st_t (st_lex);
        t1 = StatD.from_st_t (st_lex);

        if (op == Type_of_lex.LEX_PLUS || op == Type_of_lex.LEX_MINUS || op == Type_of_lex.LEX_TIMES || op == Type_of_lex.LEX_SLASH)
            r = Type_of_lex.LEX_INT;
        if (op == Type_of_lex.LEX_OR || op == Type_of_lex.LEX_AND)
            t = Type_of_lex.LEX_BOOL;
        if (t1 == t2 && t1 == t)
            st_lex.push(r);
        else
            throw new RuntimeException ("wrong types are in operation");       
        poliz.addElement(new Lex(op));  
    }
    void  check_not() {
        if (st_lex.peek() != Type_of_lex.LEX_BOOL)
        	throw new RuntimeException ("wrong type is in not");
        else
            poliz.addElement(new Lex(Type_of_lex.LEX_NOT));
    }
    void  eq_type() {
        Type_of_lex t;
        t = StatD.from_st_t (st_lex);
        if (t != st_lex.peek())
        	throw new RuntimeException ("wrong types are in :=");
        st_lex.pop();    	
    }
    void  eq_bool() {
        if (st_lex.peek() != Type_of_lex.LEX_BOOL)
        	throw new RuntimeException ("expression is not boolean");
        st_lex.pop();  	
    }
    void  check_id_in_read() {
        if (!StatD.TID.get(c_val).get_declare())
        	throw new RuntimeException ("not declared");
    }
    void  gl() {
        curr_lex = scan.get_lex();
        c_type   = curr_lex.get_type();
        c_val    = curr_lex.get_value();
        System.out.println("gl");
        System.out.println(c_type);
        System.out.println(c_val);
    }
    int  glRestArg() {
        int restArg = scan.getRestArg();
        return restArg;
    }

    public
    Vector<Lex> poliz = new Vector<Lex>();
    Parser(String program) {
         scan = new Scanner(program);
    }
    void  analyze() {
        gl();
        P();
        if (c_type != Type_of_lex.LEX_FIN)
        	throw new RuntimeException (curr_lex.toString());
        /* */
        System.out.println("poliz");
        for (Lex l : poliz) {
        	System.out.println(l.toString());
        	System.out.println(l.t_lex);
        	System.out.println(l.v_lex);
        }
        scan.freeResourse();
        /* */
        System.out.println();
        System.out.println("Yes!!!");
    }
}

