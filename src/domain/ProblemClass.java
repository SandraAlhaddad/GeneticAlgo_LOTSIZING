package domain;

public enum ProblemClass {

    ONE, TWO, THREE;
         
    public static ProblemClass fromItemCount(int itemCount){
        if(itemCount == 5){
            return ONE;
        }else if(itemCount == 40 || itemCount == 50){
            return TWO;
        }else if(itemCount == 500){
            return THREE;
        }else{
            throw new IllegalArgumentException("No problem class has " + itemCount + " items");
        }
    }
    
}
