package com.bitdubai.fermat_api.layer.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by mati on 2016.02.04..
 */
public enum FermatApps  implements FermatEnum {


    /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    BITCOIN_REFERENCE_WALLET                         ("BRW"),
    INTRA_WALLET_USER_IDENTITY                    ("IWUI"),
    INTRA_WALLET_USER_COMMUNITY              ("IWUC");
            ;

    private final String code;

    FermatApps(final String code) {
        this.code = code;
    }

    public static FermatApps getByCode(String code) throws InvalidParameterException {

        switch (code) {

            case "BRW":  return BITCOIN_REFERENCE_WALLET;
            case "IWUI":  return INTRA_WALLET_USER_IDENTITY;
            case "IWUC":   return INTRA_WALLET_USER_COMMUNITY;
            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "The received code is not valid for the Platforms enum"
                );
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }

}
