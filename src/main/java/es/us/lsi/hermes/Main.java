package es.us.lsi.hermes;

import es.us.lsi.hermes.util.CSVUtils;
import es.us.lsi.hermes.util.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                LOG.log(Level.INFO, "main() - Lowest time = {0}", Constants.sdf.format(lowestTimestamp));
//                Collections.sort(simulatorStatusWrapper.getSimulatorStatusList(), new Comparator<SimulatorStatus>() {
//                    @Override
//                    public int compare(SimulatorStatus o1, SimulatorStatus o2) {
//                        return (int) (o1.getTimestamp() - (o2.getTimestamp()));
//                    }
//                });
                SimulatorStatus pss = simulatorStatusWrapper.getSimulatorStatusList().get(0);
                pss.setSimulationSecond((int) (pss.getTimestamp() - lowestTimestamp) / 1000);
                List<SimulatorStatus> needed = new ArrayList();
                for (int i = 1; i < simulatorStatusWrapper.getSimulatorStatusList().size(); i++) {
                    SimulatorStatus ss = simulatorStatusWrapper.getSimulatorStatusList().get(i);
                    ss.setSimulationSecond((int) ((pss.getTimestamp() - lowestTimestamp) / 1000));
                    int secondsDiff = (int) ((ss.getTimestamp() - pss.getTimestamp()) / 1000);
                    if (secondsDiff > 0) {
                        for (int j = 1; j < secondsDiff; j++) {
                            try {
                                SimulatorStatus nss = (SimulatorStatus) pss.clone();
                                nss.setSimulationSecond(nss.getSimulationSecond() + j);
                                needed.add(nss);
                            } catch (CloneNotSupportedException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    pss = ss;
                }

                System.out.println(simulatorStatusWrapper.getSimulatorStatusList().size());
                simulatorStatusWrapper.getSimulatorStatusList().addAll(needed);
//                Collections.sort(simulatorStatusWrapper.getSimulatorStatusList(), new Comparator<SimulatorStatus>() {
//                    @Override
//                    public int compare(SimulatorStatus o1, SimulatorStatus o2) {
//                        return (int) (o1.getTimestamp() - (o2.getTimestamp()));
//                    }
//                });
                System.out.println(simulatorStatusWrapper.getSimulatorStatusList().size());

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
}
