package com.hp.spp.eservice.el;


import java.io.StringReader;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.commons.el.Coercions;
import org.apache.commons.el.Constants;
import org.apache.commons.el.ExpressionString;
import org.apache.commons.el.FunctionInvocation;
import org.apache.commons.el.Logger;
import org.apache.commons.el.StringLiteral;
import org.apache.commons.el.parser.ELParser;
import org.apache.commons.el.parser.ParseException;
import org.apache.commons.el.parser.Token;
import org.apache.commons.el.parser.TokenMgrError;

   

public class SPPExpressionEvaluator extends ExpressionEvaluator
{
 private class JSTLExpression extends Expression
 {

     public Object evaluate(VariableResolver vResolver)
         throws ELException
     {
         return evaluator.evaluate(expression, expectedType, vResolver, fMapper);
     }

     private SPPExpressionEvaluator evaluator;
     private String expression;
     private Class expectedType;
     private FunctionMapper fMapper;

     public JSTLExpression(SPPExpressionEvaluator evaluator, String expression, Class expectedType, FunctionMapper fMapper)
     {
         this.evaluator = evaluator;
         this.expression = expression;
         this.expectedType = expectedType;
         this.fMapper = fMapper;
     }
 }


 public SPPExpressionEvaluator()
 {
 }

 public SPPExpressionEvaluator(boolean pBypassCache)
 {
     mBypassCache = pBypassCache;
 }

 public Object evaluate(String pExpressionString, Class pExpectedType, VariableResolver pResolver, FunctionMapper functions)
     throws ELException
 {
     return evaluate(pExpressionString, pExpectedType, pResolver, functions, sLogger);
 }

 public Expression parseExpression(String expression, Class expectedType, FunctionMapper fMapper)
     throws ELException
 {
     parseExpressionString(expression);
     return new JSTLExpression(this, expression, expectedType, fMapper);
 }

 Object evaluate(String pExpressionString, Class pExpectedType, VariableResolver pResolver, FunctionMapper functions, Logger pLogger)
     throws ELException
 {
     if(pExpressionString == null)
         throw new ELException(Constants.NULL_EXPRESSION_STRING);
     Object parsedValue = parseExpressionString(pExpressionString);
     if(parsedValue instanceof String)
     {
         String strValue = (String)parsedValue;
         return convertStaticValueToExpectedType(strValue, pExpectedType, pLogger);
     }
     if(parsedValue instanceof FunctionInvocation)
     {
    	 FunctionInvocation expression = (FunctionInvocation)parsedValue;
    	if (expression.getFunctionName().equalsIgnoreCase("spp:InGroup") &&  expression.getArgumentList().size()<2){
//    		HashMap userContext = new HashMap();
//    	
//    	 userContext.put("name","Doe");
    	 List params = expression.getArgumentList();
    	 Object groupList = parseExpressionString("${UserGroups}");
    	 Object result = null;
         if(groupList instanceof org.apache.commons.el.Expression)
         {
        	 org.apache.commons.el.Expression expressionTemp = (org.apache.commons.el.Expression)groupList;
             Object value = expressionTemp.evaluate(pResolver, functions, pLogger);
              result = convertToExpectedType(value, pExpectedType, pLogger);
         }   
         StringLiteral test = StringLiteral.fromLiteralValue((String)result);
    	 params.add(test);
    	 expression.setArgumentList(params);
    	}
    	 Object value = (expression).evaluate(pResolver, functions, pLogger);
         return convertToExpectedType(value, pExpectedType, pLogger);
     }
     if(parsedValue instanceof org.apache.commons.el.Expression)
     {
    	 org.apache.commons.el.Expression expression = (org.apache.commons.el.Expression)parsedValue;
         Object value = (expression).evaluate(pResolver, functions, pLogger);
         return convertToExpectedType(value, pExpectedType, pLogger);
     }
     if(parsedValue instanceof ExpressionString)
     {
         String strValue = ((ExpressionString)parsedValue).evaluate(pResolver, functions, pLogger);
         return convertToExpectedType(strValue, pExpectedType, pLogger);
     } else
     {
         return null;
     }
 }

 public Object parseExpressionString(String pExpressionString)
     throws ELException
 {
     if(pExpressionString.length() == 0)
         return "";
     Object ret = mBypassCache ? null : sCachedExpressionStrings.get(pExpressionString);
     if(ret == null)
     {
         java.io.Reader r = new StringReader(pExpressionString);
         ELParser parser = new ELParser(r);
         try
         {
             ret = parser.ExpressionString();
             sCachedExpressionStrings.put(pExpressionString, ret);
         }
         catch(ParseException exc)
         {
             throw new ELException(formatParseException(pExpressionString, exc));
         }
         catch(TokenMgrError exc)
         {
             throw new ELException(exc.getMessage());
         }
     }
     return ret;
 }

 Object convertToExpectedType(Object pValue, Class pExpectedType, Logger pLogger)
     throws ELException
 {
     return Coercions.coerce(pValue, pExpectedType, pLogger);
 }

 Object convertStaticValueToExpectedType(String pValue, Class pExpectedType, Logger pLogger)
     throws ELException
 {
     if(pExpectedType == (java.lang.String.class) || pExpectedType == (java.lang.Object.class))
         return pValue;
     Map valueByString = getOrCreateExpectedTypeMap(pExpectedType);
     if(!mBypassCache && valueByString.containsKey(pValue))
     {
         return valueByString.get(pValue);
     } else
     {
         Object ret = Coercions.coerce(pValue, pExpectedType, pLogger);
         valueByString.put(pValue, ret);
         return ret;
     }
 }

 static Map getOrCreateExpectedTypeMap(Class pExpectedType)
 {
     Map map1;
     synchronized(sCachedExpectedTypes)
     {
         Map ret = (Map)sCachedExpectedTypes.get(pExpectedType);
         if(ret == null)
         {
             ret = Collections.synchronizedMap(new HashMap());
             sCachedExpectedTypes.put(pExpectedType, ret);
         }
         map1 = ret;
     }
     return map1;
 }

 static String formatParseException(String pExpressionString, ParseException pExc)
 {
     StringBuffer expectedBuf = new StringBuffer();
     int maxSize = 0;
     boolean printedOne = false;
     if(pExc.expectedTokenSequences == null)
         return pExc.toString();
     for(int i = 0; i < pExc.expectedTokenSequences.length; i++)
     {
         if(maxSize < pExc.expectedTokenSequences[i].length)
             maxSize = pExc.expectedTokenSequences[i].length;
         for(int j = 0; j < pExc.expectedTokenSequences[i].length; j++)
         {
             if(printedOne)
                 expectedBuf.append(", ");
             expectedBuf.append(pExc.tokenImage[pExc.expectedTokenSequences[i][j]]);
             printedOne = true;
         }

     }

     String expected = expectedBuf.toString();
     StringBuffer encounteredBuf = new StringBuffer();
     Token tok = pExc.currentToken.next;
     for(int i = 0; i < maxSize; i++)
     {
         if(i != 0)
             encounteredBuf.append(" ");
         if(tok.kind == 0)
         {
             encounteredBuf.append(pExc.tokenImage[0]);
             break;
         }
         encounteredBuf.append(addEscapes(tok.image));
         tok = tok.next;
     }

     String encountered = encounteredBuf.toString();
     return MessageFormat.format(Constants.PARSE_EXCEPTION, new Object[] {
         expected, encountered
     });
 }

 static String addEscapes(String str)
 {
     StringBuffer retval = new StringBuffer();
     for(int i = 0; i < str.length(); i++)
     {
         char ch;
         switch(str.charAt(i))
         {
         case 0: // '\0'
             break;

         case 8: // '\b'
             retval.append("\\b");
             break;

         case 9: // '\t'
             retval.append("\\t");
             break;

         case 10: // '\n'
             retval.append("\\n");
             break;

         case 12: // '\f'
             retval.append("\\f");
             break;

         case 13: // '\r'
             retval.append("\\r");
             break;

         case 1: // '\001'
         case 2: // '\002'
         case 3: // '\003'
         case 4: // '\004'
         case 5: // '\005'
         case 6: // '\006'
         case 7: // '\007'
         case 11: // '\013'
         default:
             if((ch = str.charAt(i)) < ' ' || ch > '~')
             {
                 String s = "0000" + Integer.toString(ch, 16);
                 retval.append("\\u" + s.substring(s.length() - 4, s.length()));
             } else
             {
                 retval.append(ch);
             }
             break;
         }
     }

     return retval.toString();
 }

 public String parseAndRender(String pExpressionString)
     throws ELException
 {
     Object val = parseExpressionString(pExpressionString);
     if(val instanceof String)
         return (String)val;
     if(val instanceof org.apache.commons.el.Expression)
         return "${" + ((org.apache.commons.el.Expression)val).getExpressionString() + "}";
     if(val instanceof ExpressionString)
         return ((ExpressionString)val).getExpressionString();
     else
         return "";
 }

 static Map sCachedExpressionStrings = Collections.synchronizedMap(new HashMap());
 static Map sCachedExpectedTypes = new HashMap();
 static Logger sLogger;
 boolean mBypassCache;

 static 
 {
     sLogger = new Logger(System.out);
 }
}
