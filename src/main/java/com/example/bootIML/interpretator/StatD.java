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
	static TypeOfLex from_st_t(Stack<TypeOfLex> st) {
		TypeOfLex i = st.peek();
		st.pop();
	    return i;
	}
}
