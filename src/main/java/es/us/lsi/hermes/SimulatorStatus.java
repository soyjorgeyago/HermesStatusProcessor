package es.us.lsi.hermes;

import es.us.lsi.hermes.util.Constants;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase con el estado de la simulación en cada segundo.
 */
public class SimulatorStatus implements Serializable, Cloneable {

    private String pcKey;
    private long timestamp;
    private String time;
    private int generated;
    private int sent;
    private int ok;
    private int notOk;
    private int errors;
    private int recovered;
    private int pending;
    private int runningThreads;
    private long currentDriversDelay;
    private int activeDrivers;
    private int pausedDrivers;
    
    private int simulationSecond;

    public SimulatorStatus() {
        this(null, System.currentTimeMillis(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public SimulatorStatus(String pcKey, long timestamp, int generated, int sent, int ok, int notOk, int errors, int recovered, int pending, int runningThreads, long currentDriversDelay, int activeDrivers, int pausedDrivers) {
        this.pcKey = pcKey;
        this.timestamp = timestamp;
        this.time = Constants.sdf.format(new Date(timestamp));
        this.generated = generated;
        this.sent = sent;
        this.ok = ok;
        this.notOk = notOk;
        this.errors = errors;
        this.recovered = recovered;
        this.pending = pending;
        this.runningThreads = runningThreads;
        this.currentDriversDelay = currentDriversDelay;
        this.activeDrivers = activeDrivers;
        this.pausedDrivers = pausedDrivers;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTime(String formattedDateTime) {
        this.time = formattedDateTime;
    }

    public void setGenerated(int generated) {
        this.generated = generated;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public void setNotOk(int notOk) {
        this.notOk = notOk;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public void setRunningThreads(int runningThreads) {
        this.runningThreads = runningThreads;
    }

    public void setCurrentDriversDelay(long currentDriversDelay) {
        this.currentDriversDelay = currentDriversDelay;
    }

    public void setActiveDrivers(int activeDrivers) {
        this.activeDrivers = activeDrivers;
    }

    public void setPausedDrivers(int pausedDrivers) {
        this.pausedDrivers = pausedDrivers;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        return time;
    }

    public int getGenerated() {
        return generated;
    }

    public int getSent() {
        return sent;
    }

    public int getOk() {
        return ok;
    }

    public int getNotOk() {
        return notOk;
    }

    public int getErrors() {
        return errors;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getPending() {
        return pending;
    }

    public int getRunningThreads() {
        return runningThreads;
    }

    public long getCurrentDriversDelay() {
        return currentDriversDelay;
    }

    public int getActiveDrivers() {
        return activeDrivers;
    }

    public int getPausedDrivers() {
        return pausedDrivers;
    }

    public String getPcKey() {
        return pcKey;
    }

    public void setPcKey(String pcKey) {
        this.pcKey = pcKey;
    }

    public int getSimulationSecond() {
        return simulationSecond;
    }

    public void setSimulationSecond(int simulationSecond) {
        this.simulationSecond = simulationSecond;
    }
    

    // ------------------------- CSV IMP/EXP -------------------------
    public final static CellProcessor[] CELL_PROCESSORS = new CellProcessor[]{
        new StrNotNullOrEmpty(),
        new ParseLong(),
        new StrNotNullOrEmpty(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseLong(),
        new ParseInt(),
        new ParseInt()};
    
    public final static CellProcessor[] CELL_PROCESSORS2 = new CellProcessor[]{
        new StrNotNullOrEmpty(),
        new ParseLong(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseInt(),
        new ParseLong(),
        new ParseInt(),
        new ParseInt()};

    public final static String[] FIELDS = new String[]{
        "pcKey",
        "timestamp",
        "formattedDateTime",
        "generated",
        "sent",
        "ok",
        "notOk",
        "errors",
        "recovered",
        "pending",
        "runningThreads",
        "currentDriversDelay",
        "activeDrivers",
        "pausedDrivers"};
    
    public final static String[] FIELDS2 = new String[]{
        "pcKey",
        "timestamp",
        "simulationSecond",
        "generated",
        "sent",
        "ok",
        "notOk",
        "errors",
        "recovered",
        "pending",
        "runningThreads",
        "currentDriversDelay",
        "activeDrivers",
        "pausedDrivers"};

    public final static String[] HEADERS = new String[]{
        "PcKey",
        "Timestamp",
        "Time",
        "Generated",
        "Sent",
        "Ok",
        "NotOk",
        "Errors",
        "Recovered",
        "Pending",
        "RunningThreads",
        "CurrentDriversDelay",
        "ActiveDrivers",
        "PausedDrivers"};
    
    public final static String[] HEADERS2 = new String[]{
        "PcKey",
        "Timestamp",
        "SimulationSecond",
        "Generated",
        "Sent",
        "Ok",
        "NotOk",
        "Errors",
        "Recovered",
        "Pending",
        "RunningThreads",
        "CurrentDriversDelay",
        "ActiveDrivers",
        "PausedDrivers"};

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException thrown " + e);
            throw new CloneNotSupportedException();
        }
    }
}
