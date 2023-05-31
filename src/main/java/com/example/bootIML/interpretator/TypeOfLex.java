package com.example.bootIML.interpretator;

public enum TypeOfLex {
    LEX_NULL,                                                                                   /* 0*/
    LEX_AND, LEX_BEGIN, LEX_BOOL, LEX_DO, LEX_ELSE, LEX_END, LEX_IF, LEX_FALSE, LEX_INT,        /* 9*/
    LEX_NOT, LEX_OR, LEX_PROGRAM, LEX_READ, LEX_THEN, LEX_TRUE, LEX_VAR, LEX_WHILE, LEX_WRITE,  /*18*/
    LEX_GET,                                                                                    /*19*/
    LEX_FIN,                                                                                    /*20*/
    LEX_SEMICOLON, LEX_COMMA, LEX_COLON, LEX_ASSIGN, LEX_LPAREN, LEX_RPAREN, LEX_EQ, LEX_LSS,   /*28*/
    LEX_GTR, LEX_PLUS, LEX_MINUS, LEX_TIMES, LEX_SLASH, LEX_LEQ, LEX_NEQ, LEX_GEQ,              /*36*/
    LEX_NUM,                                                                                    /*37*/
    LEX_ID,                                                                                     /*38*/
    POLIZ_LABEL,                                                                                /*39*/
    POLIZ_ADDRESS,                                                                              /*40*/
    POLIZ_GO,                                                                                   /*41*/
    POLIZ_FGO                                                                                   /*42*/
}


