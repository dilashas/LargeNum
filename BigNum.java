import org.w3c.dom.Node;

public class BigNum {
    private Node node;
    private Node tailNode = null;
    private boolean isPositive;
    private int len = 0;

    // Construct an empty BigNum. This operation should be O(1),
    // and it results in a BigNum whose toString() method returns the empty string "".
    public BigNum() {
        node = null;
        isPositive = true;
    }

    //Private class for a linked list node
    private class Node {
        public int data;
        public Node next;
        public Node prev;

        public Node() {
            data = 0;
            next = null;
            prev = null;
        }

        public Node(int num, Node next) {
            this.data = num;
            this.next = next;
            prev = null;
        }
    }

    //Helper method to convert string to linked list
    private Node buildList(String s) {
        Node llist = null;
        if(s.length() > 0){
            tailNode = new Node(Character.getNumericValue(s.charAt(0)), null);
            llist = tailNode;
        }
        for (int i = 1; i < s.length(); i++) {
            int digit = Character.getNumericValue(s.charAt(i));
            if (digit < 0 || digit > 9) {
                throw new IllegalArgumentException("Please enter a valid number (0-9)!");
            }
            Node temp = llist;
            llist = new Node(digit, llist);
            temp.prev = llist;
        }
        len = s.length();
        return llist;
    }

    // Construct a BigNum from an int, which can be positive or negative. (Do not throw an
    // IllegalArgumentException if n < 0.)
    public BigNum(int n) {
        if (n >= 0) {
            isPositive = true;
        }
        //If int is negative, take the '-' out and then convert to linked list
        if (!isPositive) {
            node = buildList((Integer.toString(n)).substring(1));
        }
        //If int is positive, convert to linked list
        else {
            node = buildList(Integer.toString(n));
        }
    }

    // Construct a BigNum from a String, which must contain only
    // characters between 0 and 9. It may begin with the character "-".
    // Throw an IllegalArgumentException for any other character.
    public BigNum(String s) {
        int i = 0;
        //If string is negative, take the '-' out and then convert to linked list
        if (s.charAt(i) == '-') {
            isPositive = false;
            i++;
            while (s.charAt(i) == '0' && s.length() > i+1){
                i++;
            }
            s = s.substring(i);
        }
            //Else if string is positive, just convert to linked list
        else {
            isPositive = true;
            while (s.charAt(i) == '0' && s.length() > i+1){
                i++;
            }
            s = s.substring(i);
        }
        node = buildList(s);
    }

    //Add '-' sign to negative numbers
    private String addSign(Node n) {
        if (!isPositive && n == null) {
            return "-";
        } else if (n == null)
            return "";

        else
            return addSign(n.next) + n.data;
    }

    // Convert this BigNum to a String
    public String toString() {
        String ans = addSign(node);
        while (ans.startsWith("0") && ans.length() > 1) {
            ans = ans.replaceFirst("0", "");
        }
        while (ans.startsWith("-0") && ans.length() > 2) {
            ans = ans.replaceFirst("-0", "-");
        }
        if (ans.equals("-0")) {
            return "0";
        }

        return ans;
    }

    //Compares the length of this and other
    private int compareToHelper(BigNum other) {
        if (other.len == this.len) {
            Node otherReverse = other.tailNode;
            Node curReverse = this.tailNode;

            //If the lengths of two numbers are equal, iterate
            //through and compare each digit of the numbers
            while(otherReverse!=null && curReverse!=null){
                if (otherReverse.data == curReverse.data) {
                    otherReverse = otherReverse.prev;
                    curReverse = curReverse.prev;
                } else if (otherReverse.data > curReverse.data) {
                    //returns -1 if this is greater than other
                    return -1;
                } else {
                    //returns 1 if this is greater than other
                    return 1;
                }
            }
            //return 0 if both numbers are equal
            return 0;
        } else if (other.len > this.len) {
            return -1;
        } else {
            return 1;
        }
    }

    // Compare this BigNum to another BigNum, returning 0 if they are equal,
    // a value > 0 if this > other, or a value < 0 if this < other.
    public int compareTo(BigNum other) {
        if (other.isPositive && this.isPositive) {
            return compareToHelper(other);
        }
        else if (!other.isPositive && !this.isPositive) {
            return -compareToHelper(other);
        }
        //if this is negative, other is positive
        else if (!this.isPositive) {
            return -1;
        }
        //If this is positive, other is negative
        else {
            return 1;
        }
    }

    // Add this BigNum to another BigNum, returning a new BigNum which contains the sum of the two
    private BigNum addHelper(BigNum other) {
        Node currentNode = this.node;
        Node otherNode = other.node;
        String sumHead = "";
        int carry = 0;

        while (currentNode != null || otherNode != null) {
            int curNodeDigit = 0;
            int otherNodeDigit = 0;
            if (currentNode != null) {
                curNodeDigit = currentNode.data;
                currentNode = currentNode.next;
            }
            if (otherNode != null) {
                otherNodeDigit = otherNode.data;
                otherNode = otherNode.next;
            }
            int sum = carry + curNodeDigit + otherNodeDigit;
            if(sum>=10){
                carry = 1;
            }else{
                carry = 0;
            }
            sum = sum % 10;
            sumHead = sum + sumHead;
        }

        if(carry!=0){
            sumHead = carry + sumHead;
        }

        // return head of the result list
        return new BigNum(sumHead);
    }

    // Add this BigNum to another BigNum, returning a new BigNum which contains the sum of the two
    public BigNum add(BigNum other) {
        BigNum result = null;
        if (other.isPositive && this.isPositive) {
            result = addHelper(other);
        } else if (!other.isPositive && !this.isPositive) {
            result = addHelper(other);
            result.isPositive = false;
        } else if (other.isPositive) {
            //if this is negative, other is positive then we need to do other-this
            this.isPositive = true;
            result = other.subtract(this);
            this.isPositive = false;
        } else {
            //if this is positive, other is negative then we need to do this-other
            other.isPositive = true;
            result = this.subtract(other);
            other.isPositive = false;
        }
        return result;
    }

    private BigNum subtractHelper(BigNum other) {
        Node otherNode = other.node;
        Node currentNode = this.node;
        boolean borrow = false;
        String result = "";

        while (currentNode != null || otherNode != null) {
            int currentNodeDigit = 0;
            int otherNodeDigit = 0;
            int sum = 0;

            if (currentNode != null) {
                currentNodeDigit = currentNode.data;
                currentNode = currentNode.next;
            }
            if(otherNode != null){
                otherNodeDigit = otherNode.data;
                otherNode = otherNode.next;
            }
            if(borrow){
                if(currentNodeDigit == 0 ){
                    sum = (currentNodeDigit - otherNodeDigit + 9) % 10;
                }else{
                    sum = (currentNodeDigit - otherNodeDigit - 1) % 10;
                    borrow = false;
                }
            }else{
                sum = (currentNodeDigit - otherNodeDigit) % 10;
            }

            if (sum < 0) {
                sum = (sum + 10) % 10;
                borrow = true;
            }
            result = sum + result;
        }
        return new BigNum(result);
    }

     // Subtract BigNum other from this, returning
    // a new BigNum which contains the difference of the two
    public BigNum subtract (BigNum other) {
        BigNum result = new BigNum(0);
        if (other.compareTo(this) == 0) {
            return result;
        }

        if (other.isPositive && this.isPositive) {
            if (this.compareTo(other) == -1) {
                result = other.subtractHelper(this);
                result.isPositive = false;
            } else {
                result = subtractHelper(other);
            }
        } else if (!other.isPositive && !this.isPositive) {
            if (this.compareToHelper(other) == -1) {
                result = other.subtractHelper(this);
            } else {
                result = this.subtractHelper(other);
                result.isPositive = false;
            }
        } else if (other.isPositive) {
            result = addHelper(other);
            result.isPositive = false;
        } else {
            result = addHelper(other);
        }
        return result;
    }


    // Multiply one digit d times another BigNum other, and return a BigNum
    // containing the product
    public BigNum multOneDigit (int d, BigNum other){

        BigNum oneDigit = new BigNum(d);

        String a = oneDigit.toString();


        return oneDigit.multiply(other);
    }

    // Multiply this BigNum by another BigNum, returning the product as a BigNum
    public BigNum multiply(BigNum other) {
        Node currentNode = this.node;
        Node otherNode = other.node;

        boolean sameSign = true;

        if (this.isPositive != other.isPositive){
            sameSign = false;
        }

        int[] d = new int[this.len + other.len];
        int i = 0;
        while(currentNode!=null){
            otherNode = other.node;
            int j = 0;
            while(otherNode!=null){

                d[i+j] += currentNode.data * otherNode.data;
                j++;
                otherNode = otherNode.next;
            }
            i++;
            currentNode = currentNode.next;
        }

        StringBuilder str = new StringBuilder();
        for(int k=0; k<d.length; k++){
            int mod = d[k]%10;
            int carry = d[k]/10;
            if(k+1<d.length){
                d[k+1] += carry;
            }
            str.insert(0, mod);
        }
        //remove front 0's
        while(str.charAt(0) == '0' && str.length()> 1){
            str.deleteCharAt(0);
        }

        if (!sameSign){
            str.insert(0, '-');
        }
        return new BigNum(str.toString());
    }

    // Use this to test your other BigNum methods
    public static void main(String[] args) {
//        BigNum test1 = new BigNum("123");
//        BigNum test2 = new BigNum("321");
//        System.out.println(test1.compareTo(test2));
//        BigNum test3 = test1.add(test2);
//        String z = test3.toString();
//        System.out.println(z);
//        BigNum test4 = test1.subtract(test2);
//        String w = test4.toString();
//        System.out.println(w);
//        test1 = new BigNum("235");
//        test2 = new BigNum("234");
//        test3 = test1.add(test2);
//        test4 = test1.subtract(test2);
//        w = test4.toString();
//        System.out.println(w);
//        z = test3.toString();
//        System.out.println(z);
//        System.out.println(test1.compareTo(test2));
//        test1 = new BigNum("987");
//        test2 = new BigNum("-987");
//        test3 = test1.add(test2);
//        test4 = test1.subtract(test2);
//        w = test4.toString();
//        System.out.println(w);
//        z = test3.toString();
//        System.out.println(z);
//        System.out.println(test1.compareTo(test2));
//        test1 = new BigNum("-2400");
//        test2 = new BigNum("201000");
//        test3 = test1.add(test2);
//        test4 = test1.subtract(test2);
//        w = test4.toString();
//        System.out.println(w);
//        z = test3.toString();
//        System.out.println(z);
//        System.out.println(test1.compareTo(test2));
//        test1 = new BigNum("-2400");
//        test2 = new BigNum("-4903");
//        test3 = test1.add(test2);
//        test4 = test1.subtract(test2);
//        w = test4.toString();
//        System.out.println(w);
//        z = test3.toString();
//        System.out.println(z);
//        System.out.println(test1.compareTo(test2));
//        test1 = new BigNum("-39038");
//        test2 = new BigNum("-4903");
//        test3 = test1.add(test2);
//        test4 = test1.subtract(test2);
//        w = test4.toString();
//        System.out.println(w);
//        z = test3.toString();
//        System.out.println(z);
//        System.out.println(test1.compareTo(test2));
//        test1 = new BigNum("345");
//        test2 = new BigNum("345");
//        test3 = test1.add(test2);
//        test4 = test1.subtract(test2);
//        w = test4.toString();
//        System.out.println(w);
//        z = test3.toString();
//        System.out.println(z);
//        System.out.println(test1.compareTo(test2));
//
//        BigNum test5 = test1.multiply(test2);
//        String v = test5.toString();
//        System.out.println(v);
//        BigNum test6 = test1.multOneDigit(7,test2);
//        String u = test6.toString();
//        System.out.println(u);
//        BigNum testPositive = new BigNum("650");
//        System.out.println(testPositive.isPositive);
    }
}
