package es.us.lsi.hermes;

import java.util.List;

public class SimulatorStatusWrapper {

    private String fileName;
    private List<SimulatorStatus> simulatorStatusList;

    public SimulatorStatusWrapper(String fileName, List<SimulatorStatus> simulatorStatusList) {
        this.fileName = fileName;
        this.simulatorStatusList = simulatorStatusList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<SimulatorStatus> getSimulatorStatusList() {
        return simulatorStatusList;
    }

    public void setSimulatorStatusList(List<SimulatorStatus> simulatorStatusList) {
        this.simulatorStatusList = simulatorStatusList;
    }

    public long getLowestTimestamp() {
        if (!simulatorStatusList.isEmpty()) {
            long lowestTimestamp = simulatorStatusList.get(0).getTimestamp();

            for (SimulatorStatus ss : simulatorStatusList) {
                lowestTimestamp = (ss.getTimestamp() < lowestTimestamp) ? ss.getTimestamp() : lowestTimestamp;
            }

            return lowestTimestamp;
        } else {
            return -1L;
        }
    }
}
