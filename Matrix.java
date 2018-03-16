/* CMPS 101, Programming Assignment 3
 * Nelson Perez
 * CruzID:neeperez 
 */

class Matrix {
	//Private Entry class that will be stored in the lists
	private class Entry{
		int index;
		double value;

		Entry(int index, double value){
			this.index = index;
			this.value = value;
		}

		Entry(){
			index = -1;
			value = -1.0;
		}

		public boolean equals(Object x){
			//System.out.println("We are in the Entry Equals method");
			boolean eq;
			if (this == x){
				return true;
			}
			if(!(x instanceof Entry)){
				return false;
			}
			Entry that = (Entry) x;
			eq = (that.index == this.index && that.value == this.value);
			return eq;
		}

		public String toString(){
			return "(" + index + ", " + value + ")";
		}
	}

	List[] array;
	int size;

	//Initialize an array of empty lists 
	//Pre: n >= 0
	Matrix(int n){
		if(n < 1){
			throw new RuntimeException("Matrix Error: Given size is less than 1");
		}
		size = n;
		array = new List[n];
		for (int i = 0; i < n; i++){
			array[i] = new List();
		}
	}

	// Access functions

	// Returns n, the number of rows and columns of this Matrix
	int getSize(){
		return size;
	}

	 // Returns the number of non-zero entries in this Matrix
	int getNNZ(){
		int count = 0;
		for (int i = 0; i < size; i++){
			array[i].moveFront();
			while(array[i].get() != null){
				count++;
				array[i].moveNext();
			}
		}

		return count;
	}

	// overrides Object's equals() method
	@Override
	public boolean equals(Object x){
		if(x == this){
			return true;
		}

		if(!(x instanceof Matrix)){
			return false;
		}
		Matrix that = (Matrix) x;
		boolean eq = (size == that.size);
		int c = 0;
		while(eq && c < size){
			eq = (that.array[c].equals(array[c]));
			c++;
		}
		return eq;
	}

	// Manipulation procedures

	// sets this Matrix to the zero state 
	void makeZero(){
		for (int i = 0; i < size; i++){
			array[i].clear();
		}
	}

	// returns a new Matrix having the same entries as this Matrix
	Matrix copy(){
		Matrix m = new Matrix(size);
		for (int i = 0; i < size; i++){
			//List new_list = new List();
			array[i].moveFront();
			Entry e = (Entry) array[i].get();
			while(e != null){
				m.array[i].append(e);
				array[i].moveNext();
				e = (Entry) array[i].get();
			}
		}
		return m;
	}

	// changes ith row, jth column of this Matrix to x
 	// pre: 1<=i<=getSize(), 1<=j<=getSize()
	void changeEntry(int i, int j, double x){
		int row = i - 1;
		if((i >= 1 && i <= size) && (j >= 1 && i <= size)){
			array[row].moveFront();
			
			// If we are changing the value to 0.0, we must remove the entry from the list
			if(x == 0.0){
				while(array[row].get() != null){
					Entry d = (Entry) array[row].get();
					if(d.index == j){
						array[row].delete();
						return;
					}
					array[row].moveNext();
				}
				return;
			}
			//The list is initially empty
			if(array[row].get() == null){
				array[row].append(new Entry(j, x));
			}
			int prev_col = -1;
			while(array[row].get() != null){
				//Entry f = (Entry) array[row].get();
				//array[row].moveNext(); //This may cause an issue; we'll see
				Entry e = (Entry) array[row].get();
				/*if(!array[row].cursor.equals(array[row].front))
					f = (Entry) array[row].cursor.previous.data;*/
				if(e.index == j){
					e.value = x;
					return;
				}

				//This means that we will either insert the new value at the beginning of the list or
				//The col index of j is less than the index at e and greater than the index at f
				if(( ((Entry)array[row].get()).equals(((Entry)array[row].front())) || prev_col < j) &&
					e.index > j){

					array[row].insertBefore(new Entry(j,x));
					return;
				} 

				//we are at the end of the list
				if(((Entry)array[row].get()).equals(((Entry)array[row].back())) && e.index < j){
					array[row].append(new Entry(j,x));
					return;
				}
				prev_col = e.index;
				array[row].moveNext();
			}

		} else {
			throw new RuntimeException("Matrix Error: i|j < 1 or i|j > getSize");
		}
	}

	// returns a matrix that is the scalar product of this matrix with x
	Matrix scalarMult(double x){
		Matrix m = new Matrix(size);
		for(int i = 0; i < size; i++){
			array[i].moveFront();
			Entry e = (Entry) array[i].front();
			double prod; 
			while(e != null){
				prod = e.value * x;
				m.array[i].append(new Entry(e.index,prod));
				array[i].moveNext();
				e = (Entry) array[i].get();
			}
		}
		return m;
	}

	// retuns a new Matrix that is the transpose of this matrix
	Matrix transpose(){
		Matrix m = new Matrix(size);
		int ind;
		double val;
		for (int i = 0; i < size; i++){
			array[i].moveFront();
			Entry e = (Entry) array[i].get();
			while(e != null){
				ind = e.index - 1;
				val = e.value;
				m.array[ind].append(new Entry(i + 1, val));
				array[i].moveNext();
				e = (Entry) array[i].get();
			}
		}
		return m;
	}

	//Returns a new matrix that is the sum of this Matrix with M
	//pre: getSize() == M.getSize()
	Matrix add(Matrix M){
		if(getSize() != M.getSize()){
			throw new RuntimeException("Matrix Error: Called add() with Matrices of different sizes");
		}
		Matrix N = new Matrix(getSize());
		for(int i = 0; i < getSize(); i++){
			N.array[i] = sum(array[i], M.array[i]);
		}
		return N;
	}

	//Returns a new Matrix that is the difference of this Matrix with M
	//pre: getSize() == M.getSize
	Matrix sub(Matrix M){
		if(getSize() != M.getSize()){
			throw new RuntimeException("Matrix error: Called sub() with Matrices of different sizes");
		}
		Matrix N = new Matrix(getSize());
		for(int i = 0; i < getSize(); i++){
			N.array[i] = diff(array[i], M.array[i]);
		}
		return N;
	}

	//Returns a new Matrix that is the product of this matrix with M
	//Pre: getSize() == M.getSize()
	Matrix mult(Matrix M){
		if(getSize() != M.getSize())
			throw new RuntimeException("Matrix error: Called mult() with Matrices of different sizes");
		double e_value;
		Matrix tM = M.transpose();
		Matrix N = new Matrix(getSize());
		for (int i = 0; i < getSize(); i++){
			for(int j = 0; j < getSize(); j++){
				e_value = dot(array[i], tM.array[j]);
				if(e_value == 0.0){
					continue;
				}
				N.changeEntry(i+1,j+1,e_value);
			}
		}
		return N;
	}

	//Computes the dot products of two lists (or in matrix talk, a pair of rows and columns)
	private static double dot(List P, List Q){
		//int max = Math.max(P.length(), Q.length());
		double product = 0.0;
		boolean eq = P.equals(Q);
		P.moveFront();
		Q.moveFront();
		
		while(P.get() != null || Q.get() != null){
			//If the indices are equal to each other in both lists, add their product to the dot product
			if((P.get() != null && Q.get() != null) && ((Entry)P.get()).index == ((Entry)Q.get()).index){
				product = product + (((Entry)P.get()).value * ((Entry)Q.get()).value);
				if(eq){
					P.moveNext();
					continue;
				}
				P.moveNext();
				Q.moveNext();
			//Move along Q
			} else if(((P.get() != null && Q.get()!= null) && ((Entry) P.get()).index > ((Entry)Q.get()).index) || (P.get() == null && Q.get() != null)){
				product = product + 0;
				Q.moveNext();

			//Move cursor along in P
			} else if(((Q.get() != null && P.get() != null) && ((Entry)P.get()).index < ((Entry)Q.get()).index) || (Q.get() == null && P.get() != null)){
				product = product + 0;
				P.moveNext();
			}
		}
		return product;
	}

	//Computes the sum of each value in two rows/cols and adds the total to a new list
	private List sum(List P, List Q){
		boolean eq = P.equals(Q);
		double total = 0.0;
		P.moveFront();
		Q.moveFront();
		List L = new List();
		int ix;
		Entry e;
		while(P.get() != null || Q.get() != null){

			//If the two indices are equal to each other, add both of their values together
			//and insert their sum in a new entry in the list
			if((P.get() != null && Q.get() != null) && ((Entry)P.get()).index == ((Entry)Q.get()).index){
				ix = ((Entry) P.get()).index;
				total = ((Entry) P.get()).value + ((Entry) Q.get()).value;
				if(total == 0.0){
					P.moveNext();
					if(!eq)
						Q.moveNext();
					continue;
				}
				e = new Entry(ix,total);
				L.append(e);
				if(eq){
					P.moveNext();
					continue;
				}
				P.moveNext();
				Q.moveNext();

			//If the index of the cursor data at P is greater than the index of Q, then add the 
			//Q's entry to the new list	
			} else if(((P.get() != null && Q.get()!= null) && ((Entry) P.get()).index > ((Entry)Q.get()).index) || (P.get() == null && Q.get() != null)){
				ix = ((Entry)Q.get()).index;
				total = ((Entry)Q.get()).value;
				e = new Entry(ix,total);
				L.append(e);
				Q.moveNext();

			//If the index of the cursor data at Q is greater than the index of P, then add the 
			//P's entry to the new list	
			} else if(((Q.get() != null && P.get() != null) && ((Entry)P.get()).index < ((Entry)Q.get()).index) || (Q.get() == null && P.get() != null)){
				ix = ((Entry)P.get()).index;
				total = ((Entry)P.get()).value;
				e = new Entry(ix,total);
				L.append(e);
				P.moveNext();
			}
			else{
				break;
			}
		}

		return L;
	}

	// Compute the difference between the elements in of P - Q
	private List diff(List P, List Q){
		double difference = 0.0;
		P.moveFront();
		Q.moveFront();
		boolean eq = P.equals(Q);
		List L = new List();
		int ix;
		Entry e;
		while(P.get() != null || Q.get() != null){
			//If the index of the cursor data at P is greater than the index of Q, then add the 
			//Q's entry to the new list
			if((P.get() != null && Q.get() != null) && ((Entry)P.get()).index == ((Entry)Q.get()).index){
				ix = ((Entry)P.get()).index;
				difference = ((Entry)P.get()).value - ((Entry)Q.get()).value;
				if(difference == 0.0){
					P.moveNext();
					if(!eq)
						Q.moveNext();
					continue;
				}
				e = new Entry(ix,difference);
				L.append(e);
				if(eq){
					P.moveNext();
					continue;
				}
				P.moveNext();
				Q.moveNext();

			//If the index of the cursor data at P is greater than the index of Q, then add the 
			//Q's entry to the new list
			} else if(((P.get() != null && Q.get() != null) && ((Entry)P.get()).index > ((Entry)Q.get()).index) || (P.get() == null && Q.get() != null)){
				ix = ((Entry)Q.get()).index;
				difference = -1.0 * ((Entry)Q.get()).value;
				e = new Entry(ix,difference);
				L.append(e);
				Q.moveNext();

			//If the index of the cursor data at Q is greater than the index of P, then add the 
			//P's entry to the new list	
			} else if(((Q.get() != null && P.get() != null) && ((Entry)P.get()).index < ((Entry)Q.get()).index) || (Q.get() == null && P.get() != null)){
				ix = ((Entry)P.get()).index;
				difference = ((Entry)P.get()).value;
				e = new Entry(ix,difference);
				L.append(e);
				P.moveNext();
			}
			else{
				break;
			}
		}


		return L;
	}

	public String toString(){
		StringBuffer matrix = new StringBuffer();
		for(int i = 0; i < size; i++){
			if(array[i].length() > 0)
				matrix.append((i + 1) + ": " + array[i] + "\n");
		}
		return new String(matrix);
	}
}