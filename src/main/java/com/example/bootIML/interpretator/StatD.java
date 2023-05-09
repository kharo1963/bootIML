package com.example.bootIML.interpretator;

import java.util.Stack;
import java.util.Vector;

public class StatD {	
	static Vector<Ident> TID = new Vector<Ident>();
	
	static int from_st_i(Stack<Integer> st) {
	    int i = st.peek();
		st.pop();
	    return i;
	}
	static Type_of_lex from_st_t(Stack<Type_of_lex> st) {
		Type_of_lex i = st.peek();
		st.pop();
	    return i;
	}
}
