package es.us.lsi.hermes.util;

import es.us.lsi.hermes.SimulatorStatus;
import es.us.lsi.hermes.SimulatorStatusWrapper;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVUtils {

    private static final Logger LOG = Logger.getLogger(CSVUtils.class.getName());

    private static void exportToCSV(CsvPreference csvPreference, boolean ignoreHeaders, File file, String[] headers, String[] fields, CellProcessor[] cellProcessors, List<?> itemList) {
        ICsvBeanWriter beanWriter = null;

        try {
            beanWriter = new CsvBeanWriter(new FileWriter(file), csvPreference);

            if (!ignoreHeaders) {
                beanWriter.writeHeader(headers != null ? headers : fields);
            }

            for (final Object element : itemList) {
                beanWriter.write(element, fields, cellProcessors);
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "exportToCSV() - Error exporting to CSV: " + file.getName(), ex);

        } finally {
            try {
                if (beanWriter != null) {
                    beanWriter.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "exportToCSV() - Error closing the writer", ex);
            }
        }
    }

    public static void saveProcessedSimulatorStatusFiles(List<SimulatorStatusWrapper> simulatorStatusWrapperList, String tag) {
        for (SimulatorStatusWrapper simulatorStatusWrapper : simulatorStatusWrapperList) {
            File newSimulatorStatusFile = new File(tag + "_" + simulatorStatusWrapper.getFileName());
            exportToCSV(CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE, false, newSimulatorStatusFile, SimulatorStatus.HEADERS2, SimulatorStatus.FIELDS2, SimulatorStatus.CELL_PROCESSORS2, simulatorStatusWrapper.getSimulatorStatusList());
        }
    }

    public static List<SimulatorStatusWrapper> loadSimulatorStatusFilesFromFolder(File folder, CsvPreference csvPreference) {
        LOG.log(Level.INFO, "loadSimulatorStatusFilesFromFolder() - Load simulator status files from path: {0}", folder.getAbsolutePath());
        File[] filesInFolder = folder.listFiles();
        List<SimulatorStatusWrapper> simulatorStatusWrapperList = new ArrayList<>();

        int csvCounter = 0;
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                if (file.getName().contains(".csv")) {
                    simulatorStatusWrapperList.add(loadSimulatorStatusFile(file, csvPreference));
                    csvCounter++;
                }
            }
        }
        LOG.log(Level.INFO, "loadSimulatorStatusFilesFromFolder() - {0} simulator status files has been loaded", csvCounter);

        return simulatorStatusWrapperList;
    }

    private static SimulatorStatusWrapper loadSimulatorStatusFile(File file, CsvPreference csvPreference) {
        List<SimulatorStatus> result = new ArrayList<>();
        ICsvBeanReader beanReader = null;
        Reader mainReader = null;

        try {
            mainReader = new FileReader(file);
            beanReader = new CsvBeanReader(mainReader, csvPreference);

            final String[] header = beanReader.getHeader(true);

            SimulatorStatus lineTemp;
            while ((lineTemp = beanReader.read(SimulatorStatus.class, header, SimulatorStatus.CELL_PROCESSORS)) != null) {
                result.add(lineTemp);
            }

        } catch (IOException | NullPointerException ex) {
            LOG.log(Level.SEVERE, "loadSimulatorStatusFile() - Error loading simulator status file", ex);
        } finally {
            try {
                if (beanReader != null) {
                    beanReader.close();
                }
                if (mainReader != null) {
                    mainReader.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "loadSimulatorStatusFile() - Error closing the reader", ex);
            }
        }

        return new SimulatorStatusWrapper(file.getName(), result);
    }
}
