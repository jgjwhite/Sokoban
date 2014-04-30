
/**
 * COURSE: CCPS 109
 * TERM: Winter 2014
 * STUDENT NAME: Justin White
 * STUDENT ID: 500619747
 *
 * Lab #8 ArrayProblems
 *
 * I certify, under the penalty of failing the course,
* that the code below is fully my own original work.
 */
import java.util.Scanner;


public class ArrayProblems
{


	public static void main (String[] args) {
		
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your grade for conversion here: /n"); 
		
		String usrInput = input.nextLine();
		input.getRyersonEngineeringGrade(usrInput);
		
	}

    //isSorted method:
    public boolean isSorted (int[] a) {
        /*
         * Test: elements of any array are in ascending order
         * as long as the next # in loop is greater, continue the loop
         */
        int counter=0;
        
        for (int y=1; y < a.length; y++) {
            if (a[y-1] > a[y]) {
                counter++;
            } 
        }
        return counter==0;
    }
    
    
	//No Duplicates method:
    public boolean noDuplicates (int[] a) {
        
        for (int i:a) {
            for (int m=i+1; m < a.length; m++) {
                if (a[i] == a[m]) { return false; }
            }
        }
        return true;

   }

    
    //Grade Conversion Method:
    public String getRyersonEngineeringGrade (int pg) {
        
		//set multidim-array
        String[][] letterGrade = {
                {"B","C","D"},
                {"+","","-"}
            };    
        
        int a = 0;
        int b = 0;
        
        
        
        if (0 <= pg && pg <= 100) {
            if (pg <= 49) {
                return "F";
            }
            
            if (pg >= 80) {
                if (pg > 89) {
                    return "A+";
                }
                else {
                    if ((pg%10) <5) { return "A-"; }
                    else { return "A"; }
                }
            }
            
            else { //switch statement to determine array codes used;
                switch(pg/10) {
                    case 5: a=2;
                    break;
                    case 6: a=1;
                    break;
                    case 7: a=0;
                    break;
                }
                
                switch(pg%10) {
                case 0: case 1: case 2: b=2;
                break;
                case 3: case 4: case 5: case 6: b=1;
                break;
                case 7: case 8: case 9: b=0;
                break;
                }
                
				//return string based on multi-dim array:
                return letterGrade[0][a] + letterGrade[1][b];
                
            }
            
            
        }
        
		//upon x<0 or x>100:
        else { return "Enter a percentage grade between 0 -- 100"; }
       
    }
    
    
}
