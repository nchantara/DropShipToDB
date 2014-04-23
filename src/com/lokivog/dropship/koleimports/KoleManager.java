package com.lokivog.dropship.koleimports;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simpleorm.dataset.SFieldMeta;
import simpleorm.dataset.SRecordInstance;
import simpleorm.dataset.SRecordMeta;
import simpleorm.sessionjdbc.SSessionJdbc;
import simpleorm.utils.SLog;

import com.lokivog.dropship.Constants;
import com.lokivog.mws.dao.DAOManager;

public class KoleManager extends DAOManager {

	final static Logger logger = LoggerFactory.getLogger(KoleManager.class);

	public static final SRecordMeta<?>[] TABLES = { KoleImportProductDAO.KOLE_PRODUCT };

	public KoleManager(String pSessionName) {
		super(pSessionName);
	}

	public static void main(String[] args) {
		KoleManager km = new KoleManager(KoleManager.class.getSimpleName());

		try {
			SLog.getSessionlessLogger().setLevel(0);
			boolean createTables = false;
			km.initDBConnection();
			if (createTables) {
				km.dropTables(TABLES);
				km.createTables(TABLES);
			}
			km.createKoleProduct();
		} catch (Exception e) {
			logger.info("Exception", e);
		} finally {
			km.shutdownDB();
		}
	}

	public void createKoleProduct() {
		boolean success = false;
		SSessionJdbc ses = getSession();

		try {

			List<String> lines = FileUtils.readLines(new File("input/products.csv"));
			if (lines != null && !lines.isEmpty()) {
				logger.info("KoleManager.createKoleProduct: processing: {} products", lines.size());
				String headerLine = lines.remove(0);
				// upper case the headings
				String[] headings = headerLine.split(",");
				int headCount = 0;
				for (String field : headings) {
					headings[headCount] = field.toUpperCase();
					logger.info("Heading Column: {}, {}", headCount, headings[headCount]);
					headCount++;
				}
				Date now = new Date();
				int newProducts = 0;
				int updatedProducts = 0;
				ses.begin();
				for (String line : lines) {
					String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					String id = fields[0];
					KoleImportProductDAO koleProductRow = ses.findOrCreate(KoleImportProductDAO.KOLE_PRODUCT, id);
					boolean isNewRow = koleProductRow.isNewRow();
					if (isNewRow) {
						koleProductRow.setDate(KoleImportProductDAO.CREATION_DATE, now);
						koleProductRow.setInt(KoleImportProductDAO.DAO_VERSION, 1);
						newProducts++;
					}
					int count = 0;
					boolean isUpdated = false;
					for (String fieldValue : fields) {
						String column = headings[count];
						fieldValue = fieldValue.replaceFirst("^\"", "");
						fieldValue = fieldValue.replaceFirst("\"$", "");
						if (column.equalsIgnoreCase("id")) {
							// do nothing, don't check id value
						} else if (column.equals(Constants.MD5)) {
							// do not perform audit on MD5 field
							koleProductRow.setObject(koleProductRow.getMeta().getField(column), fieldValue);
						} else {
							if (auditDAOChanges(koleProductRow.isNewRow(), id, fieldValue, column, koleProductRow)) {
								if (!isNewRow) {
									logger.debug("setting column: {}, with value: {}", column, fieldValue);
									updatedProducts++;
								}
								koleProductRow.setObject(koleProductRow.getMeta().getField(column), fieldValue);
								isUpdated = true;
							}
						}
						count++;
					}
					if (isUpdated) {
						if (!isNewRow) {
							int version = koleProductRow.getInt(KoleImportProductDAO.DAO_VERSION);
							koleProductRow.setInt(KoleImportProductDAO.DAO_VERSION, version + 1);
						}
						koleProductRow.setDate(KoleImportProductDAO.LAST_UPDATED, now);
					}
				}
				logger.info("Processed new products: {}, Updated products: {}, total size: {}", newProducts,
						updatedProducts, lines.size());
			}
			success = true;
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			if (ses != null) {
				if (success) {
					ses.commit();
				} else {
					ses.rollback();
				}
			}
		}
	}

	@Override
	public boolean auditDAOChanges(boolean pIsNewRow, String pId, Object pValue, String pName,
			SRecordInstance pProductRow) {
		boolean hasChanged = false;
		if (pIsNewRow) {
			hasChanged = true;
			return hasChanged;
		}
		hasChanged = super.auditDAOChanges(pIsNewRow, pId, pValue, pName, pProductRow);
		if (hasChanged) {
			String nameUpper = pName.toUpperCase();
			// if (nameUpper.equals(Constants.MD5)) {
			// return hasChanged;
			// }
			SFieldMeta daoField = pProductRow.getMeta().getField(nameUpper);
			if (daoField == null) {
				logger.info("Field: {} does not exist in Table", pName);
				return hasChanged;
			}
			Object daoValue = pProductRow.getObject(daoField);
			logger.info("LOGTYPE: {}, Field: {}, was updated, old/new: ({} != {}), for product DAO_ID: {}",
					"KOLE_UPDATE", pName, daoValue, pValue, pId);
		}
		return hasChanged;
	}

	public void createDAOColumns() {
		try {
			List<String> lines = FileUtils.readLines(new File("input/products.csv"));
			String headings = lines.get(0);
			String[] fields = headings.split(",");
			for (String field : fields) {
				String varUpper = field.toUpperCase();
				String var = String.format(
						"public static final SFieldString %s = new SFieldString(KOLE_PRODUCT, \"%s\", 40);", varUpper,
						varUpper);
				System.out.println(var);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
