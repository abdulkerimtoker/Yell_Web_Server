package yell.server.xmpp;

import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONObject;
import yell.server.gcm.GcmUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.filter.StanzaExtensionFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

import static org.jivesoftware.smack.ConnectionConfiguration.SecurityMode.ifpossible;

/**
 * Created by abdulkerim on 05.04.2016.
 */
public class XmppService {

    private static XMPPTCPConnectionConfiguration configuration;
    private static XMPPTCPConnection connection;

    private static boolean initialized = false;

    private static void init() {
        ProviderManager.addExtensionProvider(GcmExtension.GCM_ELEMENT_NAME, GcmExtension.GCM_NAMESPACE,
                new ExtensionElementProvider<GcmExtension>() {
                    public GcmExtension parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
                        return new GcmExtension(parser.nextText());
                    }
                });

        configuration = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(GcmUtils.GCM_XMPP_SERVER)
                .setHost(GcmUtils.GCM_XMPP_SERVER)
                .setPort(GcmUtils.GCM_XMPP_SERVER_PORT)
                .setSocketFactory(SSLSocketFactory.getDefault())
                .setSecurityMode(ifpossible)
                .setCompressionEnabled(false)
                .setSendPresence(false)
                .setConnectTimeout(10000)
                .build();

        initialized = true;
    }

    public static XMPPTCPConnection establishConnection() {
        if (!initialized) {
            init();
        }

        connection = new XMPPTCPConnection(configuration);

        connection.addConnectionListener(new XmppConnectionListener());
        connection.addAsyncStanzaListener(new XmppStanzaListener(), new StanzaExtensionFilter(GcmExtension.GCM_ELEMENT_NAME,
                GcmExtension.GCM_NAMESPACE));

        return connection;
    }

    public static XMPPTCPConnection getConnection() {
        return connection;
    }

    public static JSONObject createMessageJson(String to, String id, JSONObject data, Number lifeTime) {
        JSONObject json = new JSONObject();

        json.put("to", to);
        json.put("message_id", id);
        json.put("data", data);

        if (lifeTime != null)
            json.put("time_to_live", lifeTime);

        return json;
    }

    public static Stanza createPacketFromJson(final JSONObject json) {
        return new Stanza() {
            String jsonString = json.toString();

            public CharSequence toXML() {
                return String.format("<message><%s xmlns=\"%s\">%s</%s></message>",
                        GcmExtension.GCM_ELEMENT_NAME,
                        GcmExtension.GCM_NAMESPACE,
                        StringUtils.escapeForXML(jsonString),
                        GcmExtension.GCM_ELEMENT_NAME);
            }
        };
    }
}
