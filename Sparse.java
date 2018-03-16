/* CMPS 101, Programming Assignment 3
 * Nelson Perez
 * CruzID:neeperez 
 */

import java.util.*;
import java.io.*;

class Sparse {
	public static void main(String[] args) throws IOException{
		Scanner in = null;
		PrintWriter out = null;
		String line = null;
		int size = 0;
		int num_A = 0;
		int num_B = 0;

		if(args.length != 2){
			System.err.println("Usage: Lex infile outfile");
			System.exit(1);
		}

		in = new Scanner(new File(args[0]));
		out = new PrintWriter(new FileWriter(args[1]));

		size = in.nextInt();
		num_A = in.nextInt();
		num_B = in.nextInt();

		Matrix A = new Matrix(size);
		Matrix B = new Matrix(size);

		int row = 0;
		int col = 0;
		double val = 0.0;
		for(int i = 0; i < num_A; i++){
			row = in.nextInt();
			col = in.nextInt();
			val = in.nextDouble();
			A.changeEntry(row,col,val);
		}

		for(int i = 0; i < num_B; i++){
			row = in.nextInt();
			col = in.nextInt();
			val = in.nextDouble();
			B.changeEntry(row,col,val);
		}

		out.println("A has " + num_A + " non-zero entries:");
		out.println(A);
		out.println("B has " + num_B + " non-zero entries:");
		out.println(B);

		Matrix sA = A.scalarMult(1.5);
		out.println("(1.5)*A =");
		out.println(sA);

		Matrix AplusB = A.add(B);
		out.println("A+B =");
		out.println(AplusB);

		Matrix AplusA = A.add(A);
		out.println("A+A =");
		out.println(AplusA);

		Matrix BminusA = B.sub(A);
		out.println("B-A =");
		out.println(BminusA);

		Matrix AminusA = A.sub(A);
		out.println("A-A =");
		out.println(AminusA);

		Matrix tA = A.transpose();
		out.println("Transpose(A) =");
		out.println(tA);

		Matrix AtimesB = A.mult(B);
		out.println("A*B =");
		out.println(AtimesB);

		Matrix BtimesB = B.mult(B);
		out.println("B*B =");
		out.println(BtimesB);

		in.close();
		out.close();
	}
}