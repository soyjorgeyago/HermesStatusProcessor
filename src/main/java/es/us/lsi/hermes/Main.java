package es.us.lsi.hermes;

import es.us.lsi.hermes.util.CSVUtils;
import es.us.lsi.hermes.util.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.supercsv.prefs.CsvPreference;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.log(Level.INFO, "main() - Loading simulator status files...");
        List<SimulatorStatusWrapper> simulatorStatusWrapperList = CSVUtils.loadSimulatorStatusFilesFromFolder(new File("."), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        if (!simulatorStatusWrapperList.isEmpty()) {
            for (SimulatorStatusWrapper simulatorStatusWrapper : simulatorStatusWrapperList) {
                LOG.log(Level.INFO, "main() - Processing file: {0}", simulatorStatusWrapper.getFileName());
                long lowestTimestamp = simulatorStatusWrapper.getLowestTimestamp();
                long highestTimestamp = simulatorStatusWrapper.getHighestTimestamp();
                LOG.log(Level.INFO, "main() - Lowest time = {0}", Constants.sdf.format(lowestTimestamp));
                LOG.log(Level.INFO, "main() - Highest time = {0}", Constants.sdf.format(highestTimestamp));

                Set<String> pcKeys = getPcKeys(simulatorStatusWrapper);
                List<SimulatorStatus> neededSimulatorStatus = new ArrayList();

                for (String pcKey : pcKeys) {
                    LOG.log(Level.INFO, "main() - Processing PcKey = {0}", pcKey);

                    SimulatorStatus pss = null;

                    for (int i = 0; i < simulatorStatusWrapper.getSimulatorStatusList().size(); i++) {
                        SimulatorStatus ss = simulatorStatusWrapper.getSimulatorStatusList().get(i);
                        if (!ss.getPcKey().equals(pcKey)) {
                            continue;
                        }
                        if (pss == null) {
                            ss.setSimulationSecond((int) ((ss.getTimestamp() - lowestTimestamp) / 1000));
                            pss = ss;
                            LOG.log(Level.INFO, "main() - Started = {0}", Constants.sdf.format(pss.getTimestamp()));
                            continue;
                        }

                        int secondsDiff = (int) ((ss.getTimestamp() - pss.getTimestamp()) / 1000);

                        if (secondsDiff > 0) {
                            for (int j = 1; j <= secondsDiff; j++) {
                                try {
                                    SimulatorStatus nss = (SimulatorStatus) pss.clone();
                                    nss.setSimulationSecond(nss.getSimulationSecond() + j);
                                    neededSimulatorStatus.add(nss);
                                } catch (CloneNotSupportedException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        ss.setSimulationSecond((int) ((ss.getTimestamp() - lowestTimestamp) / 1000));
                        pss = ss;
                    }
                }

                simulatorStatusWrapper.getSimulatorStatusList().addAll(neededSimulatorStatus);
//                Collections.sort(simulatorStatusWrapper.getSimulatorStatusList(), new Comparator<SimulatorStatus>() {
//                    @Override
//                    public int compare(SimulatorStatus o1, SimulatorStatus o2) {
//                        return (int) (o1.getTimestamp() - (o2.getTimestamp()));
//                    }
//                });
            }

            CSVUtils.saveProcessedSimulatorStatusFiles(simulatorStatusWrapperList, "inserted");
            for (SimulatorStatusWrapper simulatorStatusWrapper : simulatorStatusWrapperList) {
                HashMap<String, SimulatorStatus> registered = new HashMap();
                for (SimulatorStatus ss : simulatorStatusWrapper.getSimulatorStatusList()) {
                    String key = ss.getPcKey() + ss.getSimulationSecond();
                    SimulatorStatus rss = registered.get(key);
                    if (rss != null) {
                        if (ss.getGenerated() > rss.getGenerated()) {
                            registered.put(key, ss);
                        }
                    } else {
                        registered.put(key, ss);
                    }
                }
                simulatorStatusWrapper.setSimulatorStatusList(new ArrayList(registered.values()));
            }
            CSVUtils.saveProcessedSimulatorStatusFiles(simulatorStatusWrapperList, "clean");
        } else {
            LOG.log(Level.INFO, "main() - No simulator status files found.");
        }
    }

    private static Set<String> getPcKeys(SimulatorStatusWrapper simulatorStatusWrapper) {
        Set<String> pcKeys = new HashSet<>();

        for (SimulatorStatus ss : simulatorStatusWrapper.getSimulatorStatusList()) {
            pcKeys.add(ss.getPcKey());
        }

        return pcKeys;
    }

}
