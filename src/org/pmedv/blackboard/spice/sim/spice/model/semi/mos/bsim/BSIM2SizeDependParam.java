/*
 * bsim2SizeDependParam.java
 *
 * Created on Nov 10, 2007, 5:05:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice.model.semi.mos.bsim;

/**
 *
 * @author Kristopher T. Beck
 */
public class BSIM2SizeDependParam {
    
    double Width;
    double Length;
/* flat band voltage at given L and W */
    double vfb;
/* surface potential at strong inversion */
    double phi;
/* bulk effect coefficient 1             */
    double k1;
/* bulk effect coefficient 2             */
    double k2;
/* drain induced barrier lowering        */
    double eta0;
/* Vbs dependence of eta                 */
    double etaB;
/* Beta at Vds = 0 and Vgs = vth         */
    double beta0;
/* Vbs dependence of beta0               */
    double beta0B;
/* Beta at Vds = Vdd and Vgs = vth           */
    double betas0;
/* Vbs dependence of betas               */
    double betasB;
/* Vds dependence of Beta in tanh term   */
    double beta20;
/* Vbs dependence of beta2               */
    double beta2B;
/* Vgs dependence of beta2               */
    double beta2G;
/* Vds dependence of Beta in linear term */
    double beta30;
/* Vbs dependence of beta3               */
    double beta3B;
/* Vgs dependence of beta3               */
    double beta3G;
/* Vds dependence of Beta in quadra term */
    double beta40;
/* Vbs dependence of beta4               */
    double beta4B;
/* Vgs dependence of beta4               */
    double beta4G;
/* Linear Vgs dependence of Mobility     */
    double ua0;
/* Vbs dependence of ua                  */
    double uaB;
/* Quadratic Vgs dependence of Mobility  */
    double ub0;
/* Vbs dependence of ub                  */
    double ubB;
/* Drift Velocity Saturation due to Vds  */
    double u10;
/* Vbs dependence of u1                  */
    double u1B;
/* Vds dependence of u1                  */
    double u1D;
/* Subthreshold slope at Vds = 0, Vbs = 0    */
    double n0;
/* Vbs dependence of n                   */
    double nB;
/* Vds dependence of n                   */
    double nD;
/* vth offset at Vds = 0, Vbs = 0            */
    double vof0;
/* Vbs dependence of vof                 */
    double vofB;
/* Vds dependence of vof                 */
    double vofD;
/* Pre - factor in hot - electron effects    */
    double ai0;
/* Vbs dependence of ai                  */
    double aiB;
/* Exp - factor in hot - electron effects    */
    double bi0;
/* Vbs dependence of bi                  */
    double biB;
/* Upper bound of cubic spline function  */
    double vghigh;
/* Lower bound of cubic spline function  */
    double vglow;
/* Gate Drain Overlap Capacitance     */
    double gDoverlapCap;
/* Gate Source Overlap Capacitance    */
    double gSoverlapCap;
/* Gate Bulk Overlap Capacitance      */
    double gBoverlapCap;
    double SqrtPhi;
    double Phis3;
    double CoxWL;
    double One_Third_CoxWL;
    double Two_Third_CoxWL;
    double Arg;
    double vt0;
    
    public BSIM2SizeDependParam pNext;
   
    public BSIM2SizeDependParam() {
    }
    
}
