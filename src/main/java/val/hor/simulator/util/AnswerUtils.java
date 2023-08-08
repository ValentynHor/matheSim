package val.hor.simulator.util;

import val.hor.simulator.entity.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AnswerUtils {

    public static List<Answer> replacePlaceholderWithFormula (List<Answer> answerList){
        List<Answer> answerResultList = new ArrayList<>();
        for (Answer answer:answerList){
            if(answer.getFormula() != null){
                String[] str = answer.getFormula().split(";");
                int count=1;
                count = Integer.parseInt(str[0].trim());
                String x1 ="";
                String x2 ="";
                switch (count) {
                    case 1 -> {
                        x1 = getRandomX(str[1]);
                    }
                    case 2 -> {
                        x1 = getRandomX(str[1]);
                        x2 = getRandomX(str[2]);
                    }
                    default -> x1="ERROR";
                }
                String [] res = answer.getName().split("@@");
                String name = "";
                if(x2.equals("")){
                    name = res[0] + x1 + res[1];
                }else{
                    name = res[0] + x1 + res[1] + x2 + res[2];
                }
                //copy answer with formula
                Answer ans = new Answer(answer.getId(),name,answer.getIsRight(),answer.getFormula(),answer.getParent());
                answerResultList.add(ans);
            }else{
                //copy answer without formula
                answerResultList.add(new Answer(answer.getId(),answer.getName(),answer.getIsRight(),answer.getParent()));
            }
        }
        return answerResultList;
    }

    public static List<Answer> randomlySelectAndShuffleAnswersList(List<Answer> answerList, String variant ){
        List<Answer> resultList = new ArrayList<>();
        List <Answer> rightAnswer = new ArrayList<>();
        List <Answer> falseAnswer = new ArrayList<>();

        for(Answer answer : answerList){
            if (answer.getIsRight()){
                rightAnswer.add(answer);
            }else{
                falseAnswer.add(answer);
            }
        }
        int rightAnswerCount = getRightAnswerCount(variant);
        int maxAnswerCount = getMaxAnswer(variant);
        for (int i=0; i< rightAnswerCount; i++){
            int a= getRandom(0,rightAnswer.size());
            resultList.add(rightAnswer.get(a));
            rightAnswer.remove(a);
        }
        for (int i=rightAnswerCount; i< maxAnswerCount; i++){
            int a= getRandom(0,falseAnswer.size());
            resultList.add(falseAnswer.get(a));
            falseAnswer.remove(a);
        }
        Collections.shuffle(resultList);
        return resultList;
    }
    private static String getRandomX(String str){
        switch (str){
            //square root
            case "SQRT=R+" -> {
                int a = (int) getRandom(1,15,0);
                return Integer.toString(a * (a + 1)) ;
            }
            // floating point number FPN
            case "FPN-" -> {
                StringBuilder result = new StringBuilder();
                double d = getRandom(-9.99, -0.01,2);
                result.append(d);
                int a = (int) getRandom(2,9,0);
                result.append(" \\cdot {10}^{");
                result.append(a);
                result.append("}");
                return result.toString();
            }
            // floating point number FPN
            case "FPN+" -> {
                double d = getRandom(0.01, 9.99, 2);
                int a = (int) getRandom(2,9,0);
                return d+" \\cdot {10}^{-"+a+"}";
            }
            case "SQRT=N" -> {
                int a = getRandom(1,100);
                return (a*a) + "";
            }
            case "Q+" -> {
                int a = getRandom(1,100);
                int b = getRandom(2,100);
                return "\\frac{"+a+"}{"+b+"}";
            }
            case "Q-" -> {
                int a = getRandom(1,100);
                int b = getRandom(2,100);
                return "-\\frac{"+a+"}{"+b+"}";
            }
            case "N" -> {
                return Integer.toString(getRandom(1,9));
            }
            case "Z" -> {
                int a = getRandom(0,1);
                int b = getRandom(1,100);
                if(a==0){
                    return Integer.toString(b);
                }else{
                    return Integer.toString(b * (-1));
                }
            }
            default ->{
                return "";
            }
        }
    }
    private static double getRandom(double min, double max, int decimalPoints){
        Random rn = new Random();
        double d = Math.pow(10, decimalPoints);
        double value = min + rn.nextDouble() * (max - min);
        return Math.rint(value * d) / d;
    }
    private static int getRandom(int min, int max){
        Random rn = new Random();
        return rn.nextInt(max-min) + min;
    }
    private static Integer getMaxAnswer(String variant){
        return Integer.parseInt(variant.split("aus")[1].trim());
    }
    private static Integer getRightAnswerCount(String variant){
        return Integer.parseInt(variant.split("aus")[0].trim());
    }



}
