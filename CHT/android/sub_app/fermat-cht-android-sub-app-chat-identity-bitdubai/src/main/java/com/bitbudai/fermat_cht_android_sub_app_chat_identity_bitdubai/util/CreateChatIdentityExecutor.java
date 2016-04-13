package com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util;

import com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.sessions.ChatIdentitySession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_cht_api.layer.identity.exceptions.CantCreateNewChatIdentityException;
import com.bitdubai.fermat_cht_api.layer.identity.interfaces.ChatIdentity;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.identity.ChatIdentityModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

/**
 * Execute the method of the module manager to create a broker identity
 * <p/>
 * Created by nelson on 14/10/15.
 */
public class CreateChatIdentityExecutor {
    public static final int EXCEPTION_THROWN = 3;
    public static final int SUCCESS = 1;
    public static final int INVALID_ENTRY_DATA = 2;
    public static final int MISSING_IMAGE = 4;

    private byte[] imageInBytes;
    private String identityName;

    private ChatIdentityModuleManager moduleManager;
    private ErrorManager errorManager;
    private ChatIdentity identity;

    public CreateChatIdentityExecutor(byte[] imageInBytes, String identityName) {
        this.imageInBytes = imageInBytes;
        this.identityName = identityName;
    }

    public CreateChatIdentityExecutor(ChatIdentityModuleManager moduleManager, ErrorManager errorManager, byte[] imageInBytes, String identityName) {
        this(imageInBytes, identityName);

        this.moduleManager = moduleManager;
        this.errorManager = errorManager;
        identity = null;
    }

    public CreateChatIdentityExecutor(FermatSession session, String identityName, byte[] imageInBytes) {
        this(imageInBytes, identityName);
        identity = null;

        if (session != null) {
            ChatIdentitySession subAppSession = (ChatIdentitySession) session;
            this.moduleManager = (ChatIdentityModuleManager) subAppSession.getModuleManager();
            this.errorManager = subAppSession.getErrorManager();
        }
    }

    public int execute() {

        if (imageIsInvalid())
            return MISSING_IMAGE;

        if (entryDataIsInvalid())
            return INVALID_ENTRY_DATA;

        try {
            moduleManager.createNewIdentityChat(identityName, imageInBytes);

        } catch (CantCreateNewChatIdentityException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public ChatIdentity getIdentity() {
        return identity;
    }

    private boolean imageIsInvalid() {
        if (imageInBytes == null) return true;
        return imageInBytes.length == 0;
    }

    private boolean entryDataIsInvalid() {
        if (moduleManager == null) return true;
        if (imageInBytes == null) return true;
        if (imageInBytes.length == 0) return true;
        if (identityName == null) return true;
        return identityName.isEmpty();
    }
}
