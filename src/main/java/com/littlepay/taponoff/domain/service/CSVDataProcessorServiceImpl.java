package com.littlepay.taponoff.domain.service;

import com.littlepay.taponoff.domain.model.Tap;
import com.littlepay.taponoff.domain.model.TapType;
import com.littlepay.taponoff.domain.model.Trip;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVDataProcessorServiceImpl implements CSVDataProcessorService {
    private static final String DELIMITER = "\\s*,\\s*";
    private static final String OUTPUT_FILENAME = "trips.csv";
    private final SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public List<Tap> extractDataFrom(String filePath) {
        List<Tap> taps = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("ID")) {
                    String[] tapData = line.split(DELIMITER);
                    taps.add(new Tap(
                            sf.parse(tapData[1].strip()),
                            TapType.valueOf(tapData[2]),
                            tapData[3],
                            tapData[4],
                            tapData[5],
                            tapData[6]));
                }
            }
        } catch (IOException | ParseException exception) {
            System.err.println(exception.getMessage());
        }
        return taps;
    }

    @Override
    public void createCSVFrom(List<Trip> trips) {
        try {
            FileWriter out = new FileWriter(OUTPUT_FILENAME);
            try (BufferedWriter writer = new BufferedWriter(out)) {
                Trip trip = trips.get(0);
                writer.write(trip.getClass().getDeclaredFields()[0].getName());
                for (int i = 1; i < trip.getClass().getDeclaredFields().length; i++) {
                    writer.write("," + trip.getClass().getDeclaredFields()[i].getName());
                }
                writer.newLine();

                for (Trip obj : trips) {
                    writer.write(obj.toString());
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
