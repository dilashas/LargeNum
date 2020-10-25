import java.util.Stack;

public class PostFixEvaluator {
    private final BigNum evaluatedExpression;
    // Construct a PostFixEvaluator from String s.
    // Assume s is a legitimate postfix expression.

    public PostFixEvaluator(String s){

        //Use a stack to store the operands
        Stack<BigNum> operandStack = new Stack<>();

        //The expression will have bigNums and operators separated by space
        String[] postFix = s.split(" ");

        // Iterate through postfix expression
        for (String currMember : postFix) {
            if (Character.isDigit(currMember.charAt(currMember.length() - 1))) {
                BigNum num = new BigNum(currMember);
                operandStack.push(num);
            } else  // If the current member is not an operand, it must be an operator so we pop 2 members to apply
            {
                BigNum member1 = operandStack.pop();
                BigNum member2 = operandStack.pop();

                if (currMember.equals("+")) {

                        operandStack.push(member2.add(member1));

                }
                else if(currMember.equals("-")) {

                    operandStack.push(member2.subtract(member1));

                }
                else if(currMember.equals("*")) {

                    operandStack.push(member2.multiply(member1));
                }
            }
        }
        evaluatedExpression = operandStack.pop();
    }

    // Return the evaluation of this postfix expression
    public BigNum evaluate (){
        return this.evaluatedExpression;
    }

    // Use this to test your PostFixEvaluator methods
    public static void main(String[] args){

//        String expression = "8242304283 248234202048422394 -";
//        PostFixEvaluator postEval = new PostFixEvaluator(expression);
//        System.out.println(postEval.evaluate());
//        System.out.println(" Correct: -248234193806118111");
//
//
//        expression = "248234202048422394 8242304283 -";
//        postEval = new PostFixEvaluator(expression);
//        System.out.println(postEval.evaluate());
//        System.out.println(" Correct: 248234193806118111");
//
//
//        expression = "8242304283 248234202048422394 - 37829237400023 72839477234 + *";
//        postEval = new PostFixEvaluator(expression);
//        System.out.println(postEval.evaluate());
//        System.out.println(" Correct: -9408591497203402063872191701527");
//
//
//        expression = "248234202048422394 8242304283 - 37829237400023 72839477234 + *";
//        postEval = new PostFixEvaluator(expression);
//        System.out.println(postEval.evaluate());
//        System.out.println(" Correct: 9408591497203402063872191701527");
    }
}
