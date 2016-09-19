package com.ugen.block;


import com.badlogic.gdx.Gdx;

import java.util.stream.IntStream;

final public class Matrix {
    private int M;             // number of rows
    private int N;             // number of columns
    private int[][] data;   // M-by-N array
    private Integer[][] integerData;
    private Matrix downLeft;
    private Matrix upRight;


    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new int[M][N];
        integerData = new Integer[M][N];

        for(int i = 0; i < M; i++)
            for(int j = 0; j < N; j++)
                integerData[i][j] = Integer.valueOf(data[i][j]);

        downLeft = new Matrix(data);
        upRight = new Matrix(data);

        for(int i = 0; i < M - 1; i++){
            upRight.put(1, i, i + 1);
            downLeft.put(1, i + 1, i);
        }
    }

    // create matrix based on 2d array
    public Matrix(int[][] data) {
        M = data.length;
        N = data[0].length;

        this.data = new int[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                this.data[i][j] = data[i][j];


    }

    public Matrix moveUp(){
        Matrix A = this;
        return upRight.times(A);
    }

    public Matrix moveDown(){
        Matrix A = this;
        return downLeft.times(A);
    }

    public Matrix moveRight(){
        Matrix A = this;
        return A.times(upRight);
    }

    public Matrix moveLeft(){
        Matrix A = this;
        return A.times(downLeft);
    }

    public Matrix rotateClockwise(){
        Matrix A;
        Matrix B = this.transpose();
        A = this.transpose();
        boolean b = true;

        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                A.data[i][j] = B.data[M - 1 - i][j];
            }
        }

        while(!contains((A.data[0]), 1))
            A = A.moveUp();

        while(b) {
            for (int i = 0; i < N; i++) {
                if (A.data[i][0] == 1)
                    b = false;
            }

            if(!b)
                break;

            A = A.moveLeft();
        }

        return A;
    }

    public Matrix rotateCounterClockwise(){
        Matrix A;
        Matrix B = this.transpose();
        A = this.transpose();
        boolean b = true;

        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                A.data[i][j] = B.data[i][N - 1 - j];
            }
        }

        while(!contains((A.data[0]), 1))
            A = A.moveUp();

        while(b) {
            for (int i = 0; i < N; i++) {
                if (A.data[i][0] == 1)
                    b = false;
            }

            if(!b)
                break;

            A = A.moveLeft();
        }

        return A;
    }

    public void setData(int[][] data){
        this.data = data;
    }

    public void put(int value, int row, int column){
        data[row][column] = value;
    }

    // copy constructor
    private Matrix(Matrix A) {
        this(A.data);
    }

    // create and return a random M-by-N matrix with values between 0 and 1
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
        for (int j = 0; j < N; j++)
         A.data[i][j] = (int)Math.random();
        return A;
    }

    // create and return the N-by-N identity matrix
    public static Matrix identity(int N) {
        Matrix I = new Matrix(N, N);
        for (int i = 0; i < N; i++)
        I.data[i][i] = 1;
        return I;
    }

    // swap rows i and j
    private void swap(int i, int j) {
        int[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
        for (int j = 0; j < N; j++)
         A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
        for (int j = 0; j < N; j++)
         C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }


    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
        for (int j = 0; j < N; j++)
         C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
        for (int j = 0; j < N; j++)
         if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
        for (int j = 0; j < C.N; j++)
         for (int k = 0; k < A.N; k++)
             C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }


    // return x = A^-1 b, assuming A is square and has full rank
    public Matrix solve(Matrix rhs) {
        if (M != N || rhs.M != N || rhs.N != 1)
        throw new RuntimeException("Illegal matrix dimensions.");

        // create copies of the data
        Matrix A = new Matrix(this);
        Matrix b = new Matrix(rhs);

        // Gaussian elimination with partial pivoting
        for (int i = 0; i < N; i++) {

          // find pivot row and swap
            int max = i;
            for (int j = i + 1; j < N; j++)
             if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
                 max = j;
            A.swap(i, max);
            b.swap(i, max);

            // singular
            if (A.data[i][i] == 0.0) throw new RuntimeException("Matrix is singular.");

            // pivot within b
            for (int j = i + 1; j < N; j++)
             b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];

            // pivot within A
            for (int j = i + 1; j < N; j++) {
             double m = A.data[j][i] / A.data[i][i];
             for (int k = i + 1; k < N; k++) {
                 A.data[j][k] -= A.data[i][k] * m;
             }
             A.data[j][i] = 0;
            }
        }

    // back substitution
        Matrix x = new Matrix(N, 1);
        for (int j = N - 1; j >= 0; j--) {
        double t = 0.0;
        for (int k = j + 1; k < N; k++)
         t += A.data[j][k] * x.data[k][0];
        x.data[j][0] = (int)(b.data[j][0] - t) / A.data[j][j];
        }
        return x;

    }

    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            Gdx.app.log("DEBUG", data[i][0] + " " + data[i][1] + " " + data[i][2] + " " + data[i][3]);

        }
        Gdx.app.log("DEBUG", " ");
    }

    public boolean contains(int[] array, int toContain){
        boolean b = false;

        for(int i = 0; i < array.length; i++)
            if (array[i] == toContain) {
                b = true;
                break;
            }

        return b;
    }

    public int getRows(){
        return M;
    }

    public int[][] getData() {
        return data;
    }
 }
