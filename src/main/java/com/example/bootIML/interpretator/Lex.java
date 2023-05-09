package com.example.bootIML.interpretator;

public class Lex {
    Type_of_lex   t_lex;
    int           v_lex;
    public
    Lex () {
    	t_lex = Type_of_lex.LEX_NULL;
    	v_lex = 0;
    }
    Lex(Type_of_lex t) {
    	t_lex = t;
    	v_lex = 0;
    }   
    Lex(Type_of_lex t, int v) {
    	t_lex = t;
    	v_lex = v;
    }      
    Type_of_lex  get_type() {
        return t_lex;
    }
    int get_value() {
        return v_lex;
    }
 }
