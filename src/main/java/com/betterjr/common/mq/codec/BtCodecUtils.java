package com.betterjr.common.mq.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.alibaba.rocketmq.common.message.Message;
import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.codec.BtObjectOutput;
import com.betterjr.common.mq.message.BtMessage;
import com.betterjr.common.utils.Collections3;

/**
 * 
 * @author liuwl
 *
 */
public final class BtCodecUtils {

    /**
     * @param anBtMessage
     * @return
     * @throws IOException
     */
    public final static Message wrap(final BtMessage anBtMessage) throws IOException {
        final BtCodecType codecType = anBtMessage.getCodecType();
        final BtCodec codec = BtCodecFactory.getCodec(codecType);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final BtObjectOutput objectOutput = codec.serialize(output);
            objectOutput.writeObject(anBtMessage);
            objectOutput.flushBuffer();
            
            final byte[] body = output.toByteArray();
            final byte[] buf = new byte[body.length + 1];
            buf[0] = codecType.getCodecType();
            System.arraycopy(body, 0, buf, 1, body.length);

            final Message message = new Message(anBtMessage.getTopic(), buf);
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
        assert anMessage != null;
        final byte[] body = anMessage.getBody();
        assert body != null;
        final byte[] buf = new byte[body.length - 1];
        System.arraycopy(body, 1, buf, 0, body.length - 1);
        BtCodec codec = BtCodecFactory.getCodec(body[0]);
        BtObjectInput objectInput = codec.deserialize(new ByteArrayInputStream(buf));
        return objectInput.readObject();
    }
}
