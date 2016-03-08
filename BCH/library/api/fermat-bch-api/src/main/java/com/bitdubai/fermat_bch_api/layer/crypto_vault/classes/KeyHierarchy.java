package com.bitdubai.fermat_bch_api.layer.crypto_vault.classes;

import com.bitdubai.fermat_bch_api.layer.crypto_vault.classes.HierarchyAccount.HierarchyAccount;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.interfaces.CryptoVaultDao;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 3/8/16.
 */
public abstract  class KeyHierarchy extends DeterministicHierarchy {
    /**
     * Super constructor
     * @param rootKey
     */
    public KeyHierarchy(DeterministicKey rootKey) {
        super(rootKey);
    }

    /**
     * gets the DAO object to access the database
     * @return
     */
    public abstract CryptoVaultDao getCryptoVaultDao();

    /**
     * gets the private key of the corresponding public Key
     * @param publicKey
     * @return
     */
    public ECKey getPrivateKey (byte[] publicKey){
        /**
         * I will try to get the position from the database just to save resources and avoid deriving the keys
         */
        int keyPosition;
        try {
            keyPosition = getCryptoVaultDao().getPublicKeyPosition(publicKey.toString());
        } catch (CantExecuteDatabaseOperationException e) {
            keyPosition = 0;
        }

        /**
         * If I didn't get the key position, then I will derive it
         */
        if (keyPosition != 0) {
            try {
                for (HierarchyAccount hierarchyAccount : getCryptoVaultDao().getHierarchyAccounts()) {
                    for (ECKey privateKey : getDerivedPrivateKeys(hierarchyAccount)){
                        // If there is a match with the pub key, I will the private key
                        if (privateKey.getPubKey().equals(publicKey))
                            return privateKey;
                    }
                }
            } catch (CantExecuteDatabaseOperationException e) {
                //null if there was a database error
                return null;
            }
        }

        /**
         * If I didn't get it I will return null.
         */
        return null;
    }

    /**
     * gets the Deterministic Hierarchy for the specified account
     * @param hierarchyAccount
     * @return
     */
    public abstract DeterministicHierarchy getKeyHierarchyFromAccount(HierarchyAccount hierarchyAccount);

    /**
     * Gets all the already derived keys from the hierarchy on this account.
     * @param hierarchyAccount
     * @return
     */
    public List<ECKey> getDerivedPrivateKeys(HierarchyAccount hierarchyAccount){
        DeterministicHierarchy keyHierarchy = getKeyHierarchyFromAccount(hierarchyAccount);
        List<ECKey> childKeys = new ArrayList<>();

        /**
         * I will get how many keys are already generated for this account.
         */
        int generatedKeys;
        try {
            generatedKeys  = this.getCryptoVaultDao().getCurrentGeneratedKeys(hierarchyAccount.getId());
        } catch (CantExecuteDatabaseOperationException e) {
            generatedKeys = 200;
        }
        for (int i = 0; i < generatedKeys; i++) {
            // I derive the key at position i
            DeterministicKey derivedKey = keyHierarchy.deriveChild(keyHierarchy.getRootKey().getPath(), true, false, new ChildNumber(i, false));
            // I add this key to the ECKey list
            childKeys.add(ECKey.fromPrivate(derivedKey.getPrivKey()));
        }

        return childKeys;
    }
}
