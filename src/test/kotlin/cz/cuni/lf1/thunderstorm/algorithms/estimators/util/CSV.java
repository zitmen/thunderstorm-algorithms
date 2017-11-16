package cz.cuni.lf1.thunderstorm.algorithms.estimators.util;

import cz.cuni.lf1.thunderstorm.datastructures.Molecule;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static cz.cuni.lf1.thunderstorm.datastructures.extensions.MoleculeKt.createMoleculeDetectionNano;

public class CSV {

    private static List<String[]> readCsv(String fname) {
        List<String[]> lines = new ArrayList<>();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(new File(fname), "UTF-8");
            while (it.hasNext()) lines.add(it.nextLine().split(","));
        } catch (IOException ignored) {
        } finally {
            if (it != null) LineIterator.closeQuietly(it);
        }
        return lines;
    }

    public static Vector<Molecule> csv2psf(String fname, int start_row, int start_col) throws IOException, InvalidObjectException, Exception {
        List<String[]> lines = CSV.readCsv(fname);

        if(lines.size() < 1) throw new InvalidObjectException("CSV data have to be in a full square/rectangle matrix!");
        if(lines.get(0).length < 1) throw new InvalidObjectException("CSV data have to be in a full square/rectangle matrix!");

        Vector<Molecule> loc = new Vector<>();
        for(int r = start_row, rm = lines.size(); r < rm; r++) {
            loc.add(createMoleculeDetectionNano(
                    Double.parseDouble(lines.get(r)[start_col+0]),    // x
                    Double.parseDouble(lines.get(r)[start_col+1])));  // y
        }
        return loc;
    }
}
