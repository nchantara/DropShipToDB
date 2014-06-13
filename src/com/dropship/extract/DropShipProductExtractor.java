package com.dropship.extract;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simpleorm.dataset.SRecordMeta;
import simpleorm.sessionjdbc.SSessionJdbc;
import simpleorm.utils.SLog;
import au.com.bytecode.opencsv.CSVReader;

import com.lokivog.dropship.koleimports.KoleImportProductDAO;
import com.lokivog.dropship.koleimports.KoleManager;

public class DropShipProductExtractor {

	private final Logger logger = LoggerFactory.getLogger(DropShipProductExtractor.class);

	public static final SRecordMeta<?>[] TABLES = { KoleImportProductDAO.KOLE_PRODUCT };

	public static void main(String... args) {
		final String FILENAME = "/git/DropShipToDB/resources/product_feed_truncated.csv";
		DropShipProductExtractor extractor = new DropShipProductExtractor();
		KoleManager km = new KoleManager(KoleManager.class.getSimpleName());
		SLog.getSessionlessLogger().setLevel(0);
		boolean createTables = false;
		km.initDBConnection();
		if (createTables) {
			km.dropTables(TABLES);
			km.createTables(TABLES);
		}

		extractor.processProductsCSV(FILENAME, km);

		km.shutdownDB();
	}

	/**
	 * Processes the passed in CSV file to load into a database.
	 * 
	 * @param filename the filename of the csv file to process
	 */
	public void processProductsCSV(String filename, KoleManager km) {
		SSessionJdbc ses = km.getSession();
		boolean success = false;
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(filename));
			List<String[]> rows = reader.readAll();

			logger.info("There are " + rows.size() + " rows in the csv file.");
			boolean hasSkippedHeaderRow = false;
			ses.begin();
			for (String[] row : rows) {
				if (isHeaderRow(row)) {
					hasSkippedHeaderRow = true;
					continue;
				}
				if (hasSkippedHeaderRow) {
					String id = row[0];
					KoleImportProductDAO koleProductRow = ses.findOrCreate(KoleImportProductDAO.KOLE_PRODUCT, id);
					if (koleProductRow.isNewRow()) {
						logger.info("add item");
					} else if (!row[1].equalsIgnoreCase(koleProductRow.getString("MD5"))) {
						logger.info("update item");
					} else {
						logger.info("no change");
					}
				}
			}
			success = true;
		} catch (FileNotFoundException e) {
			logger.error("File " + filename + " was not found");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (success) {
				ses.commit();
			} else {
				ses.rollback();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Checks if row represents a header row in the file by checking if the first value is equal to "id"
	 * 
	 * @param row a String array of the row in the file to check
	 * @return true if the first value in the row is equal to "id" otherwise false
	 */
	private boolean isHeaderRow(String[] row) {
		boolean isHeaderRow = false;
		if (row != null && row.length > 0) {
			if (row[0].equalsIgnoreCase("id")) {
				isHeaderRow = true;
			}
		}
		return isHeaderRow;
	}
}
