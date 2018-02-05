package yell.server.xmpp;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by abdulkerim on 04.04.2016.
 */
public class GcmExtension extends DefaultExtensionElement {
    /**
     * Creates a new generic stanza(/packet) extension.
     *
     * @param elementName the name of the element of the XML sub-document.
     * @param namespace   the namespace of the element.
     */
    private String json;

    public GcmExtension(String json) {
        super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public CharSequence toXML() {
        return String.format("<%s xmlns=\"%s\">%s</&s>",
                GCM_ELEMENT_NAME,
                GCM_NAMESPACE,
                StringUtils.escapeForXML(json),
                GCM_ELEMENT_NAME);
    }

    public static final String GCM_ELEMENT_NAME = "gcm";
    public static final String GCM_NAMESPACE = "google:mobile:data";
}
