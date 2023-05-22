package com.example.bootIML.interpretator;

import com.example.bootIML.service.ImlParamServiceImpl;

import java.util.Stack;
import java.util.Vector;

public class StatD {

	public static ImlParamServiceImpl imlParamServiceImpl;
	public static Vector<Ident> TID;
	public static Vector<String> restArg;
	
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
