package link;

import org.json.simple.JSONObject;

public class Message {

    private final static String DESTINATION_KEY = "destination";
    private final static String SOURCE_KEY = "source";
    private final static String DATA_KEY = "data";

    private long mDestinationId;
    private long mSourceId;
    private String mData;

    public Message(JSONObject jsonObject) {
        this(
                (long) jsonObject.get(DESTINATION_KEY),
                (long) jsonObject.get(SOURCE_KEY),
                (String) jsonObject.get(DATA_KEY)
        );
    }

    public Message(long destinationId, long sourceId, String data) {
        mDestinationId = destinationId;
        mSourceId = sourceId;
        mData = data;
    }

    public JSONObject getJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put(DESTINATION_KEY, mDestinationId);
        resultJson.put(SOURCE_KEY, mSourceId);
        resultJson.put(DATA_KEY, mData);
        return resultJson;
    }

    @Override
    public String toString() {
        return "Message {\n"
                + "\t" + DESTINATION_KEY + ": " + mDestinationId + "\n"
                + "\t" + SOURCE_KEY + ": " + mSourceId + "\n"
                + "\t" + DATA_KEY + ": \"" + mData + "\"\n"
                + "}";
    }

    public long getDestinationId() {
        return mDestinationId;
    }

    public void setDestinationId(long destinationId) {
        mDestinationId = destinationId;
    }

    public long getSourceId() {
        return mSourceId;
    }

    public void setSourceId(long sourceId) {
        mSourceId = sourceId;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }
}
