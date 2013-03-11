/*
 * java
 *
 * Created on April 5, 2006, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice;

import java.util.Properties;

import org.pmedv.blackboard.spice.sim.spice.EnumConsts.INTEG;


/**
 *
 * @author Kristopher T. Beck
 */
public class EnvVars {

    private INTEG integMethod = INTEG.TRAPEZOIDAL;
    private double vT;
    private double temp = 300.15;
    private double nomTemp = 300.15;
    private boolean bypass;
    private double absTol = 1e-12;
    private double pivotAbsTol = 1e-13;
    private double pivotRelTol = 1e-3;
    private double relTol = 1e-3;
    private double chgTol = 1e-14;
    private double voltTol = 1e-6;
    private double lteRelTol = 1e-3;
    private double lteAbsTol = 1e-6;
    private double gMin = 1e-12;
    private double gShunt;
    private double trTol=7;
    private double diagGMin = 0.0001;
    private int numSrcSteps = 1;
    private int numGMinSteps = 1;
    private double gMinFactor = 10;
    private double defaultMosM = 1;
    private double defaultMosL = 1e-4;
    private double defaultMosW = 1e-4;
    private double defaultMosAD;
    private double defaultMosAS;
    private boolean hadNodeSet;
    private boolean noOpIter;
    private boolean tryToCompact;
    private boolean badMos3;
    private boolean keepOpInfo;
    private boolean copyNodeSets;
    private boolean nodeDamping;
    private boolean fixLimit;
    private double absDv = 0.5;// Abs limit for iter-iter voltage change
    private double relDv = 2;// Rel limit for iter-iter voltage change
    private int dcMaxIter = 100;
    private int dcTrcvMaxIter = 50000;
    private int tranMaxIter = 10;
    private boolean convLimitEnabled;
    private boolean rShuntEnabled;
    private boolean xspice;

    public boolean isXspice() {
        return xspice;
    }

    public void setXspice(boolean xspice) {
        this.xspice = xspice;
        if (xspice) {
            trTol = 1;
        } else {
            trTol = 7;
        }
    }
    public static final String INTEG_METHOD = "integMethod";
    public static final String VT = "vT";
    public static final String TEMP = "Temp";
    public static final String NOM_TEMP = "NomTemp";
    public static final String BYPASS = "Bypass";
    public static final String ABS_TOL = "AbsTol";
    public static final String PIVOT_ABS_TOL = "PivotAbsTol";
    public static final String PIVOT_REL_TOL = "PivotRelTol";
    public static final String REL_TOL = "RelTol";
    public static final String CHG_TOL = "ChgTol";
    public static final String VOLT_TOL = "VoltTol";
    public static final String LTE_REL_TOL = "LteRelTol";
    public static final String LTE_ABS_TOL = "LteAbsTol";
    public static final String GMIN = "GMin";
    public static final String GSHUNT = "GShunt";
    public static final String TR_TOL = "TrTol";
    public static final String DIAG_GMIN = "DiagGMin";
    public static final String NUM_SRC_STEPS = "NumSrcSteps";
    public static final String NUM_GMIN_STEPS = "NumGMinSteps";
    public static final String GMIN_FACTOR = "GMinFactor";
    public static final String DEFAULT_MOS_M = "DefaultMosM";
    public static final String DEFAULT_MOS_L = "DefaultMosL";
    public static final String DEFAULT_MOS_W = "DefaultMosW";
    public static final String DEFAULT_M0S_AD = "DefaultMosAD";
    public static final String DEFAULT_MOS_AS = "DefaultMosAS";
    public static final String HAD_NODE_SET = "HadNodeSet";
    public static final String NO_OP_ITER = "NoOpIter";
    public static final String TRY_TO_COMPACT = "TryToCompact";
    public static final String BAD_MOS3 = "BadMos3";
    public static final String KEEP_OP_INFO = "KeepOpInfo";
    public static final String COPY_NODE_SET = "CopyNodeSets";
    public static final String NODE_DAMPING = "NodeDamping";
    public static final String FIX_LIMIT = "FixLimit";
    public static final String ABS_DV = "AbsDv";// abs limit for iter-iter voltage change
    public static final String REL_DV = "RelDv";// Rel limit for iter-iter voltage change
    public static final String DC_MAX_ITER = "DcMaxIter";
    public static final String DC_TRCV_MAX_ITER = "DcTrcvMaxIter";
    public static final String TRAN_MAX_ITER = "TranMaxIter";
    public static final String CONV_LINIT_ENABLED = "convLimitEnabled";
    public static final String RSHUNT_ENABLED = "rShuntEnabled";

    /**
     * Creates a new instance of EnvVars
     */
    public EnvVars() {
    }

    public INTEG getIntegMethod() {
        return integMethod;
    }

    public void setIntegMethod(INTEG integMethod) {
        this.integMethod = integMethod;
    }

    /*
    public EnvVars(EnvVars def) {
    copyEnv(def);
    }
     */
    public void setFixLimit(boolean fixLimit) {
        this.fixLimit = fixLimit;
    }

    public boolean isFixLimit() {
        return fixLimit;
    }

    public void setRelTol(double relTol) {
        this.relTol = relTol;
    }

    public void setVT(double vT) {
        this.vT = vT;
    }

    public double getVT() {
        return vT;
    }

    public int getDcMaxIter() {
        return dcMaxIter;
    }

    public void setDcMaxIter(int dcMaxIter) {
        this.dcMaxIter = dcMaxIter;
    }

    public int getDcTrcvMaxIter() {
        return dcTrcvMaxIter;
    }

    public void setDcTrcvMaxIter(int dcTrcvMaxIter) {
        this.dcTrcvMaxIter = dcTrcvMaxIter;
    }

    public int getTranMaxIter() {
        return tranMaxIter;
    }

    public void setTranMaxIter(int tranMaxIter) {
        this.tranMaxIter = tranMaxIter;
    }

    public double getAbsDv() {
        return absDv;
    }

    public void setAbsDv(double absDv) {
        this.absDv = absDv;
    }

    public double getAbsTol() {
        return absTol;
    }

    public void setAbsTol(double absTol) {
        this.absTol = absTol;
    }

    public boolean isBypass() {
        return bypass;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public double getChgTol() {
        return chgTol;
    }

    public void setChgTol(double chgTol) {
        this.chgTol = chgTol;
    }

    public double getDefaultMosAD() {
        return defaultMosAD;
    }

    public void setDefaultMosAD(double defaultMosAD) {
        this.defaultMosAD = defaultMosAD;
    }

    public double getDefaultMosAS() {
        return defaultMosAS;
    }

    public void setDefaultMosAS(double defaultMosAS) {
        this.defaultMosAS = defaultMosAS;
    }

    public double getDefaultMosL() {
        return defaultMosL;
    }

    public void setDefaultMosL(double defaultMosL) {
        this.defaultMosL = defaultMosL;
    }

    public double getDefaultMosM() {
        return defaultMosM;
    }

    public void setDefaultMosM(double defaultMosM) {
        this.defaultMosM = defaultMosM;
    }

    public double getDefaultMosW() {
        return defaultMosW;
    }

    public void setDefaultMosW(double defaultMosW) {
        this.defaultMosW = defaultMosW;
    }

    public double getDiagGMin() {
        return diagGMin;
    }

    public void setDiagGMin(double diagGMin) {
        this.diagGMin = diagGMin;
    }

    public double getGMin() {
        return gMin;
    }

    public void setGMin(double gMin) {
        this.gMin = gMin;
    }

    public double getGShunt() {
        return gShunt;
    }

    public void setGShunt(double gShunt) {
        this.gShunt = gShunt;
    }

    public double getGMinFactor() {
        return gMinFactor;
    }

    public void setGMinFactor(double gMinFactor) {
        this.gMinFactor = gMinFactor;
    }

    public double getLteAbsTol() {
        return lteAbsTol;
    }

    public void setLteAbsTol(double lteAbsTol) {
        this.lteAbsTol = lteAbsTol;
    }

    public double getLteRelTol() {
        return lteRelTol;
    }

    public void setLteRelTol(double lteRelTol) {
        this.lteRelTol = lteRelTol;
    }

    public int getNumGMinSteps() {
        return numGMinSteps;
    }

    public void setNumGMinSteps(int numGMinSteps) {
        this.numGMinSteps = numGMinSteps;
    }

    public int getNumSrcSteps() {
        return numSrcSteps;
    }

    public void setNumSrcSteps(int numSrcSteps) {
        this.numSrcSteps = numSrcSteps;
    }

    public double getPivotAbsTol() {
        return pivotAbsTol;
    }

    public void setPivotAbsTol(double pivotAbsTol) {
        this.pivotAbsTol = pivotAbsTol;
    }

    public double getPivotRelTol() {
        return pivotRelTol;
    }

    public void setPivotRelTol(double pivotRelTol) {
        this.pivotRelTol = pivotRelTol;
    }

    public double getRelDv() {
        return relDv;
    }

    public void setRelDv(double relDv) {
        this.relDv = relDv;
    }

    public double getRelTol() {
        return relTol;
    }

    public double getTrTol() {
        return trTol;
    }

    public void setTrTol(double trTol) {
        this.trTol = trTol;
    }

    public double getVoltTol() {
        return voltTol;
    }

    public void setVoltTol(double voltTol) {
        this.voltTol = voltTol;
    }

    public boolean isBadMos3() {
        return badMos3;
    }

    public void setBadMos3(boolean badMos3) {
        this.badMos3 = badMos3;
    }

    public boolean isKeepOpInfo() {
        return keepOpInfo;
    }

    public void setKeepOpInfo(boolean keepOpInfo) {
        this.keepOpInfo = keepOpInfo;
    }

    public boolean isCopyNodeSets() {
        return copyNodeSets;
    }

    public void setCopyNodeSets(boolean copyNodeSets) {
        this.copyNodeSets = copyNodeSets;
    }

    public boolean isHadNodeSet() {
        return hadNodeSet;
    }

    public void setHadNodeSet(boolean hadNodeSet) {
        this.hadNodeSet = hadNodeSet;
    }

    public boolean isNoOpIter() {
        return noOpIter;
    }

    public void setNoOpIter(boolean noOpIter) {
        this.noOpIter = noOpIter;
    }

    public boolean isNodeDamping() {
        return nodeDamping;
    }

    public void setNodeDamping(boolean nodeDamping) {
        this.nodeDamping = nodeDamping;
    }

    public boolean isTryToCompact() {
        return tryToCompact;
    }

    public void setTryToCompact(boolean tryToCompact) {
        this.tryToCompact = tryToCompact;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getNomTemp() {
        return nomTemp;
    }

    public void setNomTemp(double nomTemp) {
        this.nomTemp = nomTemp;
    }

    public void setConvLimitEnabled(boolean convLimitEnabled) {
        this.convLimitEnabled = convLimitEnabled;
    }

    public boolean isConvLimitEnabled() {
        return convLimitEnabled;
    }

    public boolean isRShuntEnabled() {
        return rShuntEnabled;
    }

    public void setRShuntEnabled(boolean rShuntEnabled) {
        this.rShuntEnabled = rShuntEnabled;
    }

    public void setProperties(Properties props) {
        integMethod = INTEG.valueOf(props.getProperty(INTEG_METHOD, "TRAPEZOIDAL"));
        vT = Double.valueOf(props.getProperty(VT, "0"));
        temp = Double.valueOf(props.getProperty(TEMP, "300.15"));
        nomTemp = Double.valueOf(props.getProperty(NOM_TEMP, "300.15"));
        bypass = Boolean.valueOf(props.getProperty(BYPASS, "false"));
        absTol = Double.valueOf(props.getProperty(ABS_TOL, "1e-12"));
        pivotAbsTol = Double.valueOf(props.getProperty(PIVOT_ABS_TOL, "1e-13"));
        pivotRelTol = Double.valueOf(props.getProperty(PIVOT_REL_TOL, "1e-3"));
        relTol = Double.valueOf(props.getProperty(REL_TOL, "1e-3"));
        chgTol = Double.valueOf(props.getProperty(CHG_TOL, "1e-14"));
        voltTol = Double.valueOf(props.getProperty(VOLT_TOL, "1e-6"));
        lteRelTol = Double.valueOf(props.getProperty(LTE_REL_TOL, "1e-3"));
        lteAbsTol = Double.valueOf(props.getProperty(LTE_ABS_TOL, "1e-6"));
        gMin = Double.valueOf(props.getProperty(GMIN, "1e-12"));
        gShunt = Double.valueOf(props.getProperty(GSHUNT, "0"));
        trTol = Double.valueOf(props.getProperty(TR_TOL, "7"));//??? if xspice = 1
        diagGMin = Double.valueOf(props.getProperty(DIAG_GMIN, "0"));
        numSrcSteps = Integer.valueOf(props.getProperty(NUM_SRC_STEPS, "1"));
        numGMinSteps = Integer.valueOf(props.getProperty(NUM_GMIN_STEPS, "1"));
        gMinFactor = Double.valueOf(props.getProperty(GMIN_FACTOR, "10"));
        defaultMosM = Double.valueOf(props.getProperty(DEFAULT_MOS_M, "1"));
        defaultMosL = Double.valueOf(props.getProperty(DEFAULT_MOS_L, "1e-4"));
        defaultMosW = Double.valueOf(props.getProperty(DEFAULT_MOS_W, "1e-4"));
        defaultMosAD = Double.valueOf(props.getProperty(DEFAULT_M0S_AD, "0"));
        defaultMosAS = Double.valueOf(props.getProperty(DEFAULT_MOS_AS, "0"));
        hadNodeSet = Boolean.valueOf(props.getProperty(HAD_NODE_SET, "false"));
        noOpIter = Boolean.valueOf(props.getProperty(NO_OP_ITER, "false"));
        tryToCompact = Boolean.valueOf(props.getProperty(TRY_TO_COMPACT, "false"));
        badMos3 = Boolean.valueOf(props.getProperty(BAD_MOS3, "false"));
        keepOpInfo = Boolean.valueOf(props.getProperty(KEEP_OP_INFO, "false"));
        copyNodeSets = Boolean.valueOf(props.getProperty(COPY_NODE_SET, "false"));
        nodeDamping = Boolean.valueOf(props.getProperty(NODE_DAMPING, "false"));
        fixLimit = Boolean.valueOf(props.getProperty(FIX_LIMIT, "false"));
        absDv = Double.valueOf(props.getProperty(ABS_DV, "0.5"));		/* abs limit for iter-iter voltage change */
        relDv = Double.valueOf(props.getProperty(REL_DV, "2"));		/* rel limit for iter-iter voltage change */
        dcMaxIter = Integer.valueOf(props.getProperty(DC_MAX_ITER, "500"));
        dcTrcvMaxIter = Integer.valueOf(props.getProperty(DC_TRCV_MAX_ITER, "500"));
        tranMaxIter = Integer.valueOf(props.getProperty(TRAN_MAX_ITER, "10"));
        convLimitEnabled = Boolean.valueOf(props.getProperty(CONV_LINIT_ENABLED, "false"));
        rShuntEnabled = Boolean.valueOf(props.getProperty(RSHUNT_ENABLED, "false"));
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.put(INTEG_METHOD, integMethod);
        props.put(VT, vT);
        props.put(TEMP, temp);
        props.put(NOM_TEMP, nomTemp);
        props.put(BYPASS, bypass);
        props.put(ABS_TOL, absTol);
        props.put(PIVOT_ABS_TOL, pivotAbsTol);
        props.put(PIVOT_REL_TOL, pivotRelTol);
        props.put(REL_TOL, relTol);
        props.put(CHG_TOL, chgTol);
        props.put(VOLT_TOL, voltTol);
        props.put(LTE_REL_TOL, lteRelTol);
        props.put(LTE_ABS_TOL, lteAbsTol);
        props.put(GMIN, gMin);
        props.put(GSHUNT, gShunt);
        props.put(TR_TOL, trTol);//??? if xspice = 1
        props.put(DIAG_GMIN, diagGMin);
        props.put(NUM_SRC_STEPS, numSrcSteps);
        props.put(NUM_GMIN_STEPS, numGMinSteps);
        props.put(GMIN_FACTOR, gMinFactor);
        props.put(DEFAULT_MOS_M, defaultMosM);
        props.put(DEFAULT_MOS_L, defaultMosL);
        props.put(DEFAULT_MOS_W, defaultMosW);
        props.put(DEFAULT_M0S_AD, defaultMosAD);
        props.put(DEFAULT_MOS_AS, defaultMosAS);
        props.put(HAD_NODE_SET, hadNodeSet);
        props.put(NO_OP_ITER, noOpIter);
        props.put(TRY_TO_COMPACT, tryToCompact);
        props.put(BAD_MOS3, badMos3);
        props.put(KEEP_OP_INFO, keepOpInfo);
        props.put(COPY_NODE_SET, copyNodeSets);
        props.put(NODE_DAMPING, nodeDamping);
        props.put(FIX_LIMIT, fixLimit);
        props.put(ABS_DV, absDv);
        props.put(REL_DV, relDv);
        props.put(DC_MAX_ITER, dcMaxIter);
        props.put(DC_TRCV_MAX_ITER, dcTrcvMaxIter);
        props.put(TRAN_MAX_ITER, tranMaxIter);
        props.put(CONV_LINIT_ENABLED, convLimitEnabled);
        props.put(RSHUNT_ENABLED, rShuntEnabled);
        return props;
        /*      temp = Double.valueOf(def.getProperty("", ""));
        nomTemp = def.getNomTemp();
        maxOrder = def.getMaxOrder();
        integMethod = def.getIntegMethod();
        bypass = def.isBypass();
        dcMaxIter = def.getDcMaxIter();
        dcTrcvMaxIter = def.getDcTrcvMaxIter();
        tranMaxIter = def.getTranMaxIter();
        numSrcSteps = def.getNumSrcSteps();
        numGMinSteps = def.getNumGMinSteps();
        gMinFactor = def.getGMinFactor();
        absTol = def.getAbsTol();
        pivotAbsTol = def.getPivotAbsTol();
        pivotRelTol = def.getPivotRelTol();
        relTol = def.getRelTol();
        chgTol = def.getChgTol();
        voltTol = def.getVoltTol();
        gMin = def.getGMin();
        gShunt = def.getGShunt();
        trTol = def.getTrTol();
        defaultMosM = def.getDefaultMosM();
        defaultMosL = def.getDefaultMosL();
        defaultMosW = def.getDefaultMosW();
        defaultMosAD = def.getDefaultMosAD();
        defaultMosAS = def.getDefaultMosAS();
        noOpIter = def.isNoOpIter();
        tryToCompact = def.isTryToCompact();
        badMos3 = def.isBadMos3();
        keepOpInfo = def.isKeepOpInfo();
        copyNodeSets = def.isCopyNodeSets();
        nodeDamping = def.isNodeDamping();
        absDv = def.getAbsDv();
        relDv = def.getRelDv();
        lteRelTol = def.getLteRelTol();
        lteAbsTol = def.getLteAbsTol();
         */
    }
}
