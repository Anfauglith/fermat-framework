package com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_seller.developer.bitdubai.version_1.structure.database;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 9/02/16.
 */
public class AssetSellerDatabaseConstants {
    public static final String ASSET_SELLER_DATABASE = "assetSellerDatabase";
    /**
     * Asset seller database table definition.
     */
    public static final String ASSET_SELLER_TABLE_NAME = "asset_seller";

    public static final String ASSET_SELLER_ENTRY_ID_COLUMN_NAME = "entry_id";
    public static final String ASSET_SELLER_GENESIS_TRANSACTION_COLUMN_NAME = "genesisTx";
    public static final String ASSET_SELLER_METADATA_ID_COLUMN_NAME = "metadataId";
    public static final String ASSET_SELLER_BUYER_PUBLICKEY_COLUMN_NAME = "buyerPublicKey";
    public static final String ASSET_SELLER_SELL_STATUS_COLUMN_NAME = "sellStatus";
    public static final String ASSET_SELLER_TIMESTAMP_COLUMN_NAME = "timeStamp";

    public static final String ASSET_SELLER_FIRST_KEY_COLUMN = ASSET_SELLER_ENTRY_ID_COLUMN_NAME;

    /**
     * Events recorded database table definition.
     */
    public static final String ASSET_SELLER_EVENTS_RECORDED_TABLE_NAME = "seller_events_recorded";

    public static final String ASSET_SELLER_EVENTS_RECORDED_ID_COLUMN_NAME = "event_id";
    public static final String ASSET_SELLER_EVENTS_RECORDED_EVENT_COLUMN_NAME = "event";
    public static final String ASSET_SELLER_EVENTS_RECORDED_SOURCE_COLUMN_NAME = "source";
    public static final String ASSET_SELLER_EVENTS_RECORDED_STATUS_COLUMN_NAME = "status";
    public static final String ASSET_SELLER_EVENTS_RECORDED_TIMESTAMP_COLUMN_NAME = "timestamp";

    public static final String ASSET_SELLER_EVENTS_RECORDED_TABLE_FIRST_KEY_COLUMN = ASSET_SELLER_EVENTS_RECORDED_ID_COLUMN_NAME;
}
