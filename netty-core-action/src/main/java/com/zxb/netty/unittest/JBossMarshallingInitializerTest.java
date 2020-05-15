package com.zxb.netty.unittest;

import com.zxb.java.pojo.User;
import com.zxb.netty.codec.serialization.jboss.JBossMarshallingInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.junit.Test;

/**
 * {@link JBossMarshallingInitializer} 测试用例
 *
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class JBossMarshallingInitializerTest {

    @Test
    public void testJBossMarshallingInitializer() {
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider marshallerProvider = new DefaultMarshallerProvider(marshallerFactory, configuration);

        UnmarshallerProvider unmarshallerProvider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);

        EmbeddedChannel channel = new EmbeddedChannel(new JBossMarshallingInitializer(marshallerProvider, unmarshallerProvider));

        User user = new User();
        user.setId(1);
        user.setName("zxb");

        channel.writeInbound(user);
    }
}
