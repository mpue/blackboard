/*
 * WorkEnv.java
 *
 * Created on May 2, 2006, 11:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice;

import java.util.Arrays;

import org.pmedv.blackboard.spice.sim.node.ComplexNode;
import org.pmedv.blackboard.spice.sim.output.Statistics;
import org.pmedv.blackboard.spice.sim.output.StopWatch;

import javolution.util.FastTable;
import ktb.math.matrix.DenseMatrix;
import ktb.math.matrix.Matrix;
import ktb.math.matrix.MatrixData;
import ktb.math.matrix.MatrixException;
import ktb.math.matrix.SingularException;
import ktb.math.matrix.VectoredMatrixData;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Numeric;

/**
 *
 * @author Kristopher T. Beck
 */
public class WorkEnv {

    private Matrix<Complex> matrix;
    public Matrix<Complex> work;
    private Matrix<Complex> factored;
    private int[] colSwaps;
    private Complex[] rhs;
    private Complex[] rhsOld;
    private double[][] predSol;
    private double[] pred;
    //? move factors to ckt
    private double srcFact = 1;
    private double analogRampFactor = 1;
    //Xprivate double[] rhsOp;
    private int size;
    private MatrixData<Complex> matrixData = new VectoredMatrixData<Complex>();
    public static final String SOLVE = "Solve";
    public static final String FACTOR = "Factor";
    public static final String DECOMP = "Decomp";
    public static final String ORDER = "Order";
    private StopWatch solveTime;
    private StopWatch factorTime;
    private StopWatch decompTime;
    private StopWatch orderTime;
    private boolean ordered;
    private double relThreshold;
    private double absThreshold;

    /** Creates a new instance of WorkEnv */
    public WorkEnv() {
        solveTime = Statistics.addTime(SOLVE);
        factorTime = Statistics.addTime(FACTOR);
        decompTime = Statistics.addTime(DECOMP);
        orderTime = Statistics.addTime(ORDER);
        matrix = DenseMatrix.newInstance(matrixData);
//        sparseData.add(size, size, Complex.zero());
    }

    public Matrix<Complex> getMatrix() {
        return matrix;
    }

    public void init() {
        int _size = Math.max(matrix.getRowSize(), matrix.getColSize());
        setSize(_size);
    }

    public void setSize(int size) {
        this.size = size;
//TODO redo setSize in wrk
        if (rhs != null) {
            rhs = Arrays.copyOf(rhs, size);
        } else {
            rhs = new Complex[size];
        }
        zeroArray(rhs);
        if (rhsOld != null) {
            rhsOld = Arrays.copyOf(rhsOld, size);
        } else {
            rhsOld = new Complex[size];
        }
        zeroArray(rhsOld);
        //X  rhsOp = new double[size];
        pred = new double[size];
        Arrays.fill(pred, 0);
        predSol = new double[8][size];
        for (double[] array : predSol) {
            Arrays.fill(array, 0);
        }
    }

    public void clear() {
        clearRhs();
        clearRhsOld();
        //XrhsOp = null;
        matrix.clear();
    }

    public Complex aquireNode(int row, int col) {
        Complex c = null;
        if (!matrix.containsAt(row, col)) {
            c = new ComplexNode(row, col);
            matrix.add(row, col, c);
        } else {
            c = matrix.getAt(row, col);
        }
        /*matrixData.getAt(row, col);
        if (c == null) {
        c = Complex.zero();
        matrixData.setAt(row, col, c);
        }
         *
         */
        return c;
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public void solve() {
        solveTime.start();
        Complex[] tmp1 = new Complex[rhs.length - 1];//Arrays.copyOfRange(rhs, 1, rhs.length);
        Complex[] tmp2 = new Complex[tmp1.length];
//        System.out.append("rhs = {");
        for (int i = 0; i < tmp1.length; i++) {
            Complex d = rhs[i + 1];
//            System.out.append(d.toString() + " ");
            tmp1[i] = d;//Complex.valueOf(d);
            tmp2[i] = d.copy();// Complex.valueOf(d);
        }
//        System.out.append("}\nwork =\n");
//        System.out.println(factored);

        factored.solve(tmp1, tmp2);
//        System.out.append("sol = {");
        for (int i = 0; i < tmp2.length; i++) {
            Complex c = tmp2[i];
            rhs[colSwaps[i] + 1] = c;
        }
        for (int i = 0; i < rhs.length; i++) {
            Complex c = rhs[i];
//            System.out.append(c.toString() + " ");
        }
        //      System.out.append("}\n");
        //rhs = tmp2;
        solveTime.stop();
    }

    public boolean factor() throws MatrixException {
        factorTime.start();
//        System.out.println("before factor");
//        System.out.println(work.toString());
        factored = work.factor();
//        System.out.println("after factor");
//        System.out.println(factored.toString());
        factorTime.stop();
        if (factored == null) {
            return false;
        }
        return true;
    }

    public boolean factor(double pivTol) throws SingularException, MatrixException {
        decompTime.start();
        factored = work.factor();//luFac(pivTol);
        decompTime.stop();
        if (factored == null) {
            return false;
        }
        return true;
    }

    public boolean factor(double pivTol, double gMin) throws SingularException, MatrixException {
        decompTime.start();
        loadGMin(gMin);
        factored = work.factor();//luFac(pivTol, gMin);
        decompTime.stop();
        if (factored == null) {
            return false;
        }
        return true;
    }

    public boolean reorder(double pivTol, double pivRel) throws SingularException, MatrixException {
        orderTime.start();
        factored = orderAndFactor(pivTol, pivRel, true);
        orderTime.stop();
        if (factored == null) {
            return false;
        }
        return true;
    }

    public boolean reorder(double pivTol, double pivRel, double gMin) throws SingularException, MatrixException {
        orderTime.start();
        loadGMin(gMin);
        factored = orderAndFactor(pivTol, pivRel, true);
        orderTime.stop();
        if (factored == null) {
            return false;
        }
        return true;
    }

    public void loadGMin(double gMin) {
        if (gMin != 0) {
            for (int i = 0; i < work.getColSize(); i++) {
                work.getAt(i, i).plus(gMin);
            }
        }
    }

    public void clearMatrix() {
        matrix.clear();
    }

    public Complex getElement(int row, int col) {
        return matrix.getAt(row, col);
    }

    public int getSize() {
        return size;
    }

    public Complex getRhsAt(int i) {
        return rhs[i];
    }

    public void setRhsAt(int i, Complex c) {
        rhs[i].set(c);
    }

    public double getRhsRealAt(int i) {
        return rhs[i].getReal();
    }

    public void setRhsRealAt(int i, double rValue) {
        this.rhs[i].setReal(rValue);
    }

    public void clearRhs() {
        clear(rhs);
    }

    public Complex getRhsOldAt(int i) {
        return rhsOld[i];
    }

    public void setRhsOldAt(int i, Complex c) {
        rhsOld[i].set(c);
    }

    public double getRhsOldRealAt(int i) {
        return rhsOld[i].getReal();
    }

    public void setRhsOldRealAt(int i, double rhsOld) {
        this.rhsOld[i].setReal(rhsOld);
    }

    public void clearRhsOld() {
        clear(rhsOld);
    }

    public void advanceRhs() {
        Complex[] tmp = rhsOld;
        rhsOld = rhs;
        rhs = tmp;
//? should clear rhs here instead of ckt.load
    }

    public void zeroArray(Complex[] array) {
        for (int i = 0; i < array.length; i++) {
            Complex c = array[i];
            if (c == null) {
                c = Complex.zero();
                array[i] = c;
            } else {
                c.reset();
            }
        }
    }
    /*X
    public double[] getRhsOp() {
    return rhsOp;
    }

    public void setRhsOp(double[] array) {
    rhsOp = array;
    }

    public double getOpAt(int i) {
    return rhsOp[i];
    }

    public void setOpAt(int i, double rhsOp) {
    this.rhsOp[i] = rhsOp;
    }
     */

    public static void clear(double[] array) {
        Arrays.fill(array, 0);
    }

    public static void clear(int[] array) {
        Arrays.fill(array, 0);
    }

    public static void clear(FastTable<? extends Numeric> array) {
        for (int i = 0; i < array.size(); i++) {
            array.get(i).setZero();
        }
    }

    public static void clear(Complex[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i].reset();
        }
    }

    public double getPredAt(int i) {
        return pred[i];
    }

    public void setPredAt(int i, double pred) {
        this.pred[i] = pred;
    }

    public double getPredSolAt(int row, int col) {
        return predSol[row][col];
    }

    public void setPredSolAt(int row, int col, double predSol) {
        this.predSol[row][col] = predSol;
    }

    public double getSrcFact() {
        return srcFact;
    }

    public void setSrcFact(double srcFact) {
        this.srcFact = srcFact;
    }

    public double getAnalogRampFactor() {
        return analogRampFactor;
    }

    public void setAnalogRampFactor(double analogRampFactor) {
        this.analogRampFactor = analogRampFactor;
    }

    public boolean preOrder() {
        int nsize = size - 1;
        colSwaps = new int[nsize];
        work = DenseMatrix.newInstance(nsize, Complex.zero());
        for (int i = 0; i < nsize; i++) {
            colSwaps[i] = i;
            for (int j = 0; j < nsize; j++) {
                if (matrix.containsAt(i + 1, j + 1)) {
//                    double d =
                    Complex c = matrix.getAt(i + 1, j + 1);
                    //Complex r = Complex.valueOf(d);
                    work.setAt(i, j, c);
                } else {
                    work.setAt(i, j, Complex.zero());
                }
            }
        }
        /*        System.out.println("begin pre order");
        System.out.println(work);*/
        boolean ok = _preOrder();/*
        System.out.println("end preorder");
        System.out.println(work);*/
        return ok;
    }

    public boolean _preOrder() {
        /*        if (factored != null) {
        return true;
        }*/
//        DenseMatrix<N> matrix = this;//copy();
        int startAt = 0;
        boolean anotherPassNeeded;
        boolean swapped = false;
        //reordered = true;
        int _size = work.getRowSize();
        do {
            anotherPassNeeded = false;
            swapped = false;
            for (int j = startAt; j < _size; j++) {
                int l = 0;
                //Looking for lone twins
                if (work.getDiag(j).isZero()) {
                    int twins = 0;
                    //Begin counting twins
                    loop:
                    for (int i = 0; i < _size; i++) {
                        Complex elem1 = work.getAt(i, j);
                        if (elem1.abs() == 1) {
                            for (int k = 0; k < _size && k != j; k++) {
                                Complex elem2 = work.getAt(k, j);
                                if (elem2 != null && elem2.abs() == 1) {
                                    l = k;
                                    if (++twins >= 2) {
                                        break loop;
                                    }
                                }
                            }
                        }
                    }
                    //End counting twins
                    if (twins == 1) {
                        work.swapCols(l, j);
                        colSwaps[l] = j;
                        colSwaps[j] = l;
                        swapped = true;
                    } else if ((twins > 1) && !anotherPassNeeded) {
                        anotherPassNeeded = true;
                        startAt = j;
                    }
                }
            }
            if (anotherPassNeeded) {
                for (int j = startAt; !swapped && j < _size; j++) {
                    int l = 0;
                    if (work.getDiag(j).isZero()) {
                        int twins = 0;
                        //Begin counting twins
                        loop:
                        for (int i = 0; i < _size; i++) {
                            Complex elem1 = work.getAt(i, j);
                            if (elem1.abs() == 1) {
                                for (int k = 0; k < _size && k != j; k++) {
                                    Complex elem2 = work.getAt(k, j);
                                    if (elem2 != null && elem2.abs() == 1) {
                                        l = k;
                                        if (++twins >= 2) {
                                            break loop;
                                        }
                                    }
                                }
                            }
                        }
                        //End counting twins
                        work.swapCols(l, j);
                        colSwaps[l] = j;
                        colSwaps[j] = l;
                        startAt = j;
                        swapped = true;
                    }
                }
            }
        } while (anotherPassNeeded);
        return true;
    }

    public void order() {
        int[] pivots = work.getPivots();
        for (int j = 0; j < work.getColSize(); j++) {
            int index = j;
            double largest = work.getAt(j, j).abs();
            for (int i = j + 1; i < work.getRowSize(); i++) {
                double mag = work.getAt(i, j).abs();
                if (mag > largest) {
                    index = i;
                    largest = mag;
                }
            }
            if (index != j) {
                work.swapRows(j, index);
            }
        }
        ordered = true;
    }

    public Matrix orderAndFactor(double relThreshold,
            double absThreshold, boolean diagPivoting) throws MatrixException {
        int reorderingRequired = 0;
        if (ordered && work.isFactored()) {
            return work;
        }
        if (relThreshold <= 0.0 || relThreshold > 1.0) {
            this.relThreshold = relThreshold;
        }

        if (absThreshold < 0.0) {
            this.absThreshold = absThreshold;
        }
        Matrix<Complex> lu = work.copy();
        int[] pivots = lu.getPivots();
        for (int p = 0; p < work.getColSize(); p++) {
            int pivot = p;
            double largest = lu.getAt(p, p).abs();
            for (int k = p + 1; k < work.getRowSize(); k++) {
                double mag = lu.getAt(k, p).abs();
                if (mag > largest) {
                    pivot = k;
                    largest = mag;
                }
            }
            if (pivot != p) {
                lu.swapRows(p, pivot);
            }

            if (lu.findLargestInCol(p) * relThreshold < lu.getDiag(p).abs()) {
                lu.rowColElimination(p, p);
            } else {
                reorderingRequired = p;
                System.out.println("Reordering required on column " + p);
                break;
            }
        }
        if (reorderingRequired == 0) {
            ordered = true;
//            factored = true;
        }
        return lu;
    }
}
