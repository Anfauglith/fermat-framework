package com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.ChatMessageStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.enums.DistributionStatus;
import com.bitdubai.fermat_cht_api.layer.network_service.chat.interfaces.ChatMetadata;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Gabriel Araujo on 06/01/16.
 */
public class ChatMetadataTransactionRecord implements ChatMetadata{

    private UUID transactionId;

    private String transactionHash;

    private UUID chatId;

    private UUID objectId;

    private PlatformComponentType localActorType;

    private String localActorPublicKey;

    private PlatformComponentType remoteActorType;

    private String remoteActorPublicKey;

    private String chatName;

    private ChatMessageStatus chatMessageStatus;

    private MessageStatus messageStatus;

    private Timestamp date;

    private UUID messageId;

    private String message;

    private DistributionStatus distributionStatus;

    /**
     * Represent the value of processed
     */
    private String processed;

    /**
     * Represent the value of PROCESSED
     */
    public final static String PROCESSED = "Y";

    /**
     * Represent the value of NO_PROCESSED
     */
    public final static String NO_PROCESSED = "N";

    /**
     * Get the Processed
     * @return String
     */
    public String getProcessed() {
        return processed;
    }

    /**
     * Set the Processed
     * @param processed
     */
    public void setProcessed(String processed) {
        this.processed = processed;
    }

    /**
     *
     * @return
     */
    public String getTransactionHash() {
        return transactionHash;
    }

    /**
     *
     * @param transactionHash
     */
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    /**
     *
     * @return
     */
    public UUID getTransactionId() {
        return transactionId;
    }

    /**
     *
     * @param transactionId
     */
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    /**
     *
     * @return
     */
    @Override
    public UUID getChatId() {
        return chatId;
    }

    /**
     *
     * @param chatId
     */
    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    /**
     *
     * @return
     */
    @Override
    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    /**
     *
     * @param messageStatus
     */
    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     *
     * @return
     */
    @Override
    public DistributionStatus getDistributionStatus() {
        return distributionStatus;
    }

    /**
     *
     * @param distributionStatus
     */
    public void setDistributionStatus(DistributionStatus distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    @Override
    public String toString() {
        return "ChatMetadataTransactionRecord{" +
                "chatId=" + chatId +
                ", objectId=" + objectId +
                ", localActorType='" + localActorType + '\'' +
                ", localActorPublicKey='" + localActorPublicKey + '\'' +
                ", remoteActorType='" + remoteActorType + '\'' +
                ", remoteActorPublicKey='" + remoteActorPublicKey + '\'' +
                ", chatName='" + chatName + '\'' +
                ", chatMessageStatus=" + chatMessageStatus +
                ", date=" + date +
                ", messageId=" + messageId +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     *
     * @return
     */
    @Override
    public PlatformComponentType getLocalActorType() {
        return localActorType;
    }

    /**
     *
     * @param localActorType
     */
    public void setLocalActorType(PlatformComponentType localActorType) {
        this.localActorType = localActorType;
    }

    /**
     *
     * @return
     */
    @Override
    public String getLocalActorPublicKey() {
        return localActorPublicKey;
    }

    /**
     *
     * @param localActorPublicKey
     */
    public void setLocalActorPublicKey(String localActorPublicKey) {
        this.localActorPublicKey = localActorPublicKey;
    }

    /**
     *
     * @return
     */
    @Override
    public PlatformComponentType getRemoteActorType() {
        return remoteActorType;
    }

    /**
     *
     * @param remoteActorType
     */
    public void setRemoteActorType(PlatformComponentType remoteActorType) {
        this.remoteActorType = remoteActorType;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRemoteActorPublicKey() {
        return remoteActorPublicKey;
    }

    /**
     *
     * @param remoteActorPublicKey
     */
    public void setRemoteActorPublicKey(String remoteActorPublicKey) {
        this.remoteActorPublicKey = remoteActorPublicKey;
    }

    /**
     *
     * @return
     */
    @Override
    public String getChatName() {
        return chatName;
    }

    /**
     *
     * @param chatName
     */
    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    /**
     *
     * @return
     */
    @Override
    public ChatMessageStatus getChatMessageStatus() {
        return chatMessageStatus;
    }

    /**
     *
     * @param chatMessageStatus
     */
    public void setChatMessageStatus(ChatMessageStatus chatMessageStatus) {
        this.chatMessageStatus = chatMessageStatus;
    }

    /**
     *
     * @return
     */
    @Override
    public Timestamp getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     *
     * @return
     */
    @Override
    public UUID getObjectId() {
        return objectId;
    }

    /**
     *
     * @param objectId
     */
    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    /**
     *
     * @return
     */
    @Override
    public UUID getMessageId() {
        return messageId;
    }

    /**
     *
     * @param messageId
     */
    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }
}
