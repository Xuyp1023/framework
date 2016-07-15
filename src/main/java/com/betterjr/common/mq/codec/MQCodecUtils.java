package com.betterjr.common.mq.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.alibaba.rocketmq.common.message.Message;
import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.mq.message.MQMessage;

/**
 * 
 * @author liuwl
 *
 */
public final class MQCodecUtils {

    /**
     * @param anMQMessage
     * @return
     * @throws IOException
     */
    public final static Message wrap(final MQMessage anMQMessage) throws IOException {
        final MQCodecType codecType = anMQMessage.getCodecType();
        final BtCodec codec = MQCodecFactory.getCodec(codecType);
        if (codec == null) {
            throw new BytterException("序列化方式指定错误！");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final BtObjectOutput objectOutput = codec.serialize(outputStream);
            objectOutput.writeObject(anMQMessage);
            objectOutput.flushBuffer();

            final byte[] body = outputStream.toByteArray();
            final byte[] buf = new byte[body.length + 1];
            buf[0] = codecType.getCodecType();
            System.arraycopy(body, 0, buf, 1, body.length);

            final Message message = new Message(anMQMessage.getTopic(), buf);
            return message;
        }
    }

    /**
     * @param anMessage
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public final static Object unwrap(final Message anMessage) throws ClassNotFoundException, IOException {
        if (anMessage == null) {
            return null;
        }
        final byte[] body = anMessage.getBody();
        final byte[] buf = new byte[body.length - 1];
        System.arraycopy(body, 1, buf, 0, body.length - 1);
        BtCodec codec = MQCodecFactory.getCodec(body[0]);
        if (codec == null) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(buf)) {
            BtObjectInput objectInput = codec.deserialize(inputStream);
            return objectInput.readObject();
        }

    }
}
