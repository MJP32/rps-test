import static org.junit.Assert.*;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.game.GameDetails;


public class SerializationTests
{
    ObjectEncoder enc = new ObjectEncoder();
    ObjectDecoder dec = new ObjectDecoder(ClassResolvers.cacheDisabled(null));
    
    @Test
    public void test()
    {
        GameListResponse gameListResponse = new GameListResponse(UUID.randomUUID(), new ArrayList<GameDetails>());
        
        
    }
}
