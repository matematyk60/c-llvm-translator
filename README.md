## generate antlr grammar source

To generate ANTLR gramar sources use gradle task `generateGrammarSource`. This will generate proper
java classes into `build/generated-src/antlr/main` directory.

Source grammars (`*.g4`) reside in `src/main/antlr` directory.

## Main program

I implemented main program which looks for C language file `src/main/resources/example.c` and prints
its "textual representation". Output for original example file looks like this:

`example.c`
```
int f(int arg1, char arg2) {
	a1(arg1);
	a2(arg1, arg2);
	a3();
}
```

`output`
```
compilationUnit
  translationUnit
    externalDeclaration
      functionDefinition
        declarationSpecifiers
          declarationSpecifier
            typeSpecifier
        declarator
          directDeclarator
            directDeclarator
            parameterTypeList
              parameterList
                parameterList
                  parameterDeclaration
                    declarationSpecifiers
                      declarationSpecifier
                        typeSpecifier
                    declarator
                      directDeclarator
                parameterDeclaration
                  declarationSpecifiers
                    declarationSpecifier
                      typeSpecifier
                  declarator
                    directDeclarator
        compoundStatement
          blockItemList
            blockItemList
              blockItemList
                blockItem
                  statement
                    expressionStatement
                      expression
                        assignmentExpression
                          conditionalExpression
                            logicalOrExpression
                              logicalAndExpression
                                inclusiveOrExpression
                                  exclusiveOrExpression
                                    andExpression
                                      equalityExpression
                                        relationalExpression
                                          shiftExpression
                                            additiveExpression
                                              multiplicativeExpression
                                                castExpression
                                                  unaryExpression
                                                    postfixExpression
                                                      postfixExpression
                                                        primaryExpression
                                                      argumentExpressionList
                                                        assignmentExpression
                                                          conditionalExpression
                                                            logicalOrExpression
                                                              logicalAndExpression
                                                                inclusiveOrExpression
                                                                  exclusiveOrExpression
                                                                    andExpression
                                                                      equalityExpression
                                                                        relationalExpression
                                                                          shiftExpression
                                                                            additiveExpression
                                                                              multiplicativeExpression
                                                                                castExpression
                                                                                  unaryExpression
                                                                                    postfixExpression
                                                                                      primaryExpression
              blockItem
                statement
                  expressionStatement
                    expression
                      assignmentExpression
                        conditionalExpression
                          logicalOrExpression
                            logicalAndExpression
                              inclusiveOrExpression
                                exclusiveOrExpression
                                  andExpression
                                    equalityExpression
                                      relationalExpression
                                        shiftExpression
                                          additiveExpression
                                            multiplicativeExpression
                                              castExpression
                                                unaryExpression
                                                  postfixExpression
                                                    postfixExpression
                                                      primaryExpression
                                                    argumentExpressionList
                                                      argumentExpressionList
                                                        assignmentExpression
                                                          conditionalExpression
                                                            logicalOrExpression
                                                              logicalAndExpression
                                                                inclusiveOrExpression
                                                                  exclusiveOrExpression
                                                                    andExpression
                                                                      equalityExpression
                                                                        relationalExpression
                                                                          shiftExpression
                                                                            additiveExpression
                                                                              multiplicativeExpression
                                                                                castExpression
                                                                                  unaryExpression
                                                                                    postfixExpression
                                                                                      primaryExpression
                                                      assignmentExpression
                                                        conditionalExpression
                                                          logicalOrExpression
                                                            logicalAndExpression
                                                              inclusiveOrExpression
                                                                exclusiveOrExpression
                                                                  andExpression
                                                                    equalityExpression
                                                                      relationalExpression
                                                                        shiftExpression
                                                                          additiveExpression
                                                                            multiplicativeExpression
                                                                              castExpression
                                                                                unaryExpression
                                                                                  postfixExpression
                                                                                    primaryExpression
            blockItem
              statement
                expressionStatement
                  expression
                    assignmentExpression
                      conditionalExpression
                        logicalOrExpression
                          logicalAndExpression
                            inclusiveOrExpression
                              exclusiveOrExpression
                                andExpression
                                  equalityExpression
                                    relationalExpression
                                      shiftExpression
                                        additiveExpression
                                          multiplicativeExpression
                                            castExpression
                                              unaryExpression
                                                postfixExpression
                                                  postfixExpression
                                                    primaryExpression
``` 

### Acknowledgements

I also added simpler grammar for Matlab as an example in resources directory.