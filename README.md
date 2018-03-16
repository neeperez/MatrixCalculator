# MatrixCalculator
This is an n x n matrix calculator that exploits the expected sparseness of the matrix's operands. The program is capable of performing 
fast matrix operations, even on very large (sparse) matrices. The program uses object-oriented code to store n x n matrices that are 
created by user input (via a text file). 

The client program (Sparse.java) takes in two command line arguments with the names of the input
and output files. The input file will begin with a single line containing three integers n, a and b, separated by spaces. The second line
will be blank, and the following a lines will specify the non-zero entries of an n x n matrix A. Each of these lines will contain a
space separated list of three numbers: two integers and a double, giving the row, column, and value of the corresponding matrix entry.
After another blank line, will follow b lines specifying the non-zero entries of an n x n matrix B.

The program will read the input file, initialize, and build the array of lists representation of matrices A and B, and then calculate and 
print the following matrices to the output file: A, B, (1.5)A, A + B, A + A, B - A, A - A, A transpose, AB and B * B.
