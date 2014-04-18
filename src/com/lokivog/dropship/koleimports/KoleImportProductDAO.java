package com.lokivog.dropship.koleimports;

import simpleorm.dataset.SFieldDate;
import simpleorm.dataset.SFieldDouble;
import simpleorm.dataset.SFieldFlags;
import simpleorm.dataset.SFieldInteger;
import simpleorm.dataset.SFieldString;
import simpleorm.dataset.SFieldTimestamp;
import simpleorm.dataset.SRecordInstance;
import simpleorm.dataset.SRecordMeta;

import com.lokivog.dropship.Constants;

/**
 * The Class AmazonProductDAO is the data access object to the amazon_product table.
 */
public class KoleImportProductDAO extends SRecordInstance {
	private static final long serialVersionUID = 1L;

	public static final SRecordMeta<KoleImportProductDAO> KOLE_PRODUCT = new SRecordMeta(KoleImportProductDAO.class,
			Constants.TABLE_KOLE_IMPORTS);

	// public static final SFieldString ID = new SFieldString(PRODUCT, "ID", 10, SPRIMARY_KEY);
	// public static final SFieldLong ID = (SFieldLong) new SFieldLong(PRODUCT, "ID", SFieldFlags.SPRIMARY_KEY)
	// .setGeneratorMode(SSELECT_MAX, "product_seq"); // sequence tested too.

	// field length set based on koleimport api at http://support.koleimports.com/kb/dropship-api-documentation/api-products
	public static final SFieldString ID = new SFieldString(KOLE_PRODUCT, "ID", 5, SFieldFlags.SPRIMARY_KEY);
	public static final SFieldString MD5 = new SFieldString(KOLE_PRODUCT, "MD5", 40);
	public static final SFieldString TITLE = new SFieldString(KOLE_PRODUCT, "TITLE", 150);
	public static final SFieldString DESCRIPTION = new SFieldString(KOLE_PRODUCT, "DESCRIPTION", 2555);
	public static final SFieldString TYPE = new SFieldString(KOLE_PRODUCT, "TYPE", 50);
	public static final SFieldString BRAND = new SFieldString(KOLE_PRODUCT, "BRAND", 50);
	public static final SFieldString COLORS = new SFieldString(KOLE_PRODUCT, "COLORS", 250);
	public static final SFieldString MATERIALS = new SFieldString(KOLE_PRODUCT, "MATERIALS", 250);
	public static final SFieldString ATTRIBUTES = new SFieldString(KOLE_PRODUCT, "ATTRIBUTES", 250);
	public static final SFieldString UPC = new SFieldString(KOLE_PRODUCT, "UPC", 20);
	public static final SFieldInteger INVENTORY = new SFieldInteger(KOLE_PRODUCT, "INVENTORY");
	public static final SFieldInteger IS_CLOSEOUT = new SFieldInteger(KOLE_PRODUCT, "IS_CLOSEOUT");
	public static final SFieldInteger CATEGORY_ID = new SFieldInteger(KOLE_PRODUCT, "CATEGORY_ID");
	public static final SFieldString CATEGORY = new SFieldString(KOLE_PRODUCT, "CATEGORY", 150);
	public static final SFieldInteger SUBCATEGORY_ID = new SFieldInteger(KOLE_PRODUCT, "SUBCATEGORY_ID");
	public static final SFieldString SUBCATEGORY = new SFieldString(KOLE_PRODUCT, "SUBCATEGORY", 150);
	public static final SFieldString IMAGE_SMALL = new SFieldString(KOLE_PRODUCT, "IMAGE_SMALL", 510);
	public static final SFieldString IMAGE_MEDIUM = new SFieldString(KOLE_PRODUCT, "IMAGE_MEDIUM", 510);
	public static final SFieldString IMAGE_LARGE = new SFieldString(KOLE_PRODUCT, "IMAGE_LARGE", 510);
	public static final SFieldDouble ITEM_WEIGHT = new SFieldDouble(KOLE_PRODUCT, "ITEM_WEIGHT");
	public static final SFieldDate MODIFIED = new SFieldDate(KOLE_PRODUCT, "MODIFIED");
	public static final SFieldInteger TIER_PACK_1 = new SFieldInteger(KOLE_PRODUCT, "TIER_PACK_1");
	public static final SFieldDouble TIER_PRICE_1 = new SFieldDouble(KOLE_PRODUCT, "TIER_PRICE_1");
	public static final SFieldInteger TIER_PACK_2 = new SFieldInteger(KOLE_PRODUCT, "TIER_PACK_2");
	public static final SFieldDouble TIER_PRICE_2 = new SFieldDouble(KOLE_PRODUCT, "TIER_PRICE_2");
	public static final SFieldInteger TIER_PACK_3 = new SFieldInteger(KOLE_PRODUCT, "TIER_PACK_3");
	public static final SFieldDouble TIER_PRICE_3 = new SFieldDouble(KOLE_PRODUCT, "TIER_PRICE_3");
	public static final SFieldInteger TIER_PACK_4 = new SFieldInteger(KOLE_PRODUCT, "TIER_PACK_4");
	public static final SFieldDouble TIER_PRICE_4 = new SFieldDouble(KOLE_PRODUCT, "TIER_PRICE_4");
	public static final SFieldInteger CASE_PACK = new SFieldInteger(KOLE_PRODUCT, "CASE_PACK");
	public static final SFieldString CASE_PACK_PRICE = new SFieldString(KOLE_PRODUCT, "CASE_PACK_PRICE", 40);
	public static final SFieldInteger ON_SALE = new SFieldInteger(KOLE_PRODUCT, "ON_SALE");
	public static final SFieldInteger SALE_BEGINS = new SFieldInteger(KOLE_PRODUCT, "SALE_BEGINS");
	public static final SFieldInteger SALE_ENDS = new SFieldInteger(KOLE_PRODUCT, "SALE_ENDS");
	public static final SFieldTimestamp CREATION_DATE = new SFieldTimestamp(KOLE_PRODUCT, "CREATION_DATE");
	public static final SFieldTimestamp LAST_UPDATED = new SFieldTimestamp(KOLE_PRODUCT, "LAST_UPDATED");
	public static final SFieldInteger DAO_VERSION = new SFieldInteger(KOLE_PRODUCT, "DAO_VERSION");

	public @Override()
	SRecordMeta<KoleImportProductDAO> getMeta() {
		return KOLE_PRODUCT;
	};
}
