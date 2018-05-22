
package intervale_project02;

import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
/**
 *
 * @author Andrew
 */
public class Intervale_project02 {

  
    public static void main(String[] args) throws Exception{
     
     Locale locale = new Locale("en");
        Locale.setDefault(locale);

     String sIn, sIn2, line;
     Double sIn3;
    
      FileInputStream fstream = new 
        FileInputStream("D:/input_2.txt");
      
      FileOutputStream outstream = new
        FileOutputStream("D:/output_2.txt");
        
      BufferedReader br = new 
        BufferedReader(new InputStreamReader(fstream));
      
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outstream)); 
 
      DecimalFormat bf = new DecimalFormat("#0.00000");
      DecimalFormat af = new DecimalFormat("#0");
 
        try {
            while ((sIn = br.readLine()) != null){
            sIn2 = sIn;
            sIn = opn(sIn);
            sIn3 = calculate(sIn);
            if (sIn3 <= 1.7976931348623157E+308){
                if(sIn3 % 1 ==0){
                    sIn = af.format(sIn3);
                    line = sIn2 + "=" + sIn + "\n";
                    System.out.println(line);
                    bw.write(line);
                    }
                else{
            sIn = bf.format(sIn3);
            line = sIn2 + "=" + sIn + "\n";
            System.out.println(line);
            bw.write(line);
            }
            }
            else { 
                line = "Division by zero"+ "\n";
            System.out.println(line);
            bw.write(line);
            }  

            }
            br.close();
            bw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
  
    }
     
     
    
    /**
     * Преобразовать строку в обратную польскую нотацию
     */
    private static String opn(String sIn) throws Exception {
        StringBuilder sbStack = new StringBuilder(""), sbOut = new StringBuilder("");
        char cIn, cTmp;

        for (int i = 0; i < sIn.length(); i++) {
            cIn = sIn.charAt(i);
            if (isOp(cIn)) {
                while (sbStack.length() > 0) {
                    cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                    if (isOp(cTmp) && (opPrior(cIn) <= opPrior(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        sbStack.setLength(sbStack.length()-1);
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                sbStack.append(cIn);
            } else if ('(' == cIn) {
                sbStack.append(cIn);
            } else if (')' == cIn) {
                cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                while ('(' != cTmp) {
                    if (sbStack.length() < 1) {
                        throw new Exception("Ошибка разбора скобок. Проверьте правильность выражения.");
                    }
                    sbOut.append(" ").append(cTmp);
                    sbStack.setLength(sbStack.length()-1);
                    cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                }
                sbStack.setLength(sbStack.length()-1);
            } else {
             
                sbOut.append(cIn);
            }
        }

       
        while (sbStack.length() > 0) {
            sbOut.append(" ").append(sbStack.substring(sbStack.length()-1));
            sbStack.setLength(sbStack.length()-1);
        }

        return  sbOut.toString();
    }
    
    

    /**
     * Функция проверяет оператор
     */
    private static boolean isOp(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
            case '^':
            case '%': 
                return true;
        }
        return false;
    }

    /**
     * Возвращает приоритет операции
     */
    private static byte opPrior(char op) {
        switch (op) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 1; // Тут остается + и -
    }

    /**
     * Считаем выражение в обратной польской нотации
     */
    private static double calculate(String sIn) throws Exception {
        double dA = 0, dB = 0;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<Double>();
        StringTokenizer st = new StringTokenizer(sIn);
        while(st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && isOp(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("Неверное количество данных в стеке для операции " + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    switch (sTmp.charAt(0)) {
                        case '+':
                            dA += dB;
                            break;
                        case '-':
                            dA -= dB;
                            break;
                        case '/':
                            dA /= dB;
                            break;
                        case '*':
                            dA *= dB;
                            break;
                        case '%':
                            dA = dA/100*dB;
                            break;
                        case '^':
                            dA = Math.pow(dA, dB);
                            break;
                        default:
                            throw new Exception("Недопустимая операция " + sTmp);
                    }
                    stack.push(dA);
                } else {
                    dA = Double.parseDouble(sTmp);
                    stack.push(dA);
                }
            } catch (Exception e) {
                throw new Exception("Недопустимый символ в выражении");
            }
        }
 
        if (stack.size() > 1) {
            throw new Exception("Количество операторов не соответствует количеству операндов");
        }

        return stack.pop();

    }

}
