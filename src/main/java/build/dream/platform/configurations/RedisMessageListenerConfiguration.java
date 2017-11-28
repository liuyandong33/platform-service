package build.dream.platform.configurations;

import build.dream.common.utils.ConfigurationUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.listeners.ElemeCallbackMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;

@Configuration
public class RedisMessageListenerConfiguration {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    @Autowired
    private ElemeCallbackMessageListener elemeCallbackMessageListener;

    @Bean(destroyMethod = "destroy")
    public RedisMessageListenerContainer redisMessageListenerContainer() throws IOException {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(jedisConnectionFactory);
        String elemeCallbackMessageChannelTopic = ConfigurationUtils.getConfiguration(Constants.ELEME_CALLBACK_MESSAGE_CHANNEL_TOPIC) + "_" + ConfigurationUtils.getConfiguration(Constants.PARTITION_CODE);
        redisMessageListenerContainer.addMessageListener(elemeCallbackMessageListener, new ChannelTopic(elemeCallbackMessageChannelTopic));
        return redisMessageListenerContainer;
    }
}
