package plakolb.sharedham.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import plakolb.sharedham.HamClient;

/**
 * Created by pinselimo on 07/04/16.
 */
public class MessageHandler implements Runnable
{
	private final InputStream inputStream;
	private final HamClient.OnMessageListener messageListener;

	public MessageHandler(InputStream inputStream, HamClient.OnMessageListener messageListener)
	{
		this.inputStream     = inputStream;
		this.messageListener = messageListener;
	}

	@Override
	public void run()
	{
		try
		{
			InputStreamReader isr = new InputStreamReader(inputStream,"UTF-8");
			char[] buffer  = new char[1024];
			int buffCount  = 0;
			String message = "";

			while (true)
			{
				buffCount = isr.read(buffer);
				message  += String.valueOf(buffer, 0, buffCount);

				message = parseMessage(message);

				Thread.sleep(10l,0);
			}

		} catch (IOException ignored)
		{

		} catch (InterruptedException ignored)
		{

		}

	}

	/** this function takes a raw string as input and
	 *  parses out messages handed to the messageListener
	 *  interface and returns the remainder of the input
	 */
	public String parseMessage(String m)
	{
		String[] msgs = getJObject(m);
		if (msgs.length > 1)
		{
			try
			{
				JSONObject object = (JSONObject) new JSONTokener(msgs[1]).nextValue();
				String msg = object.getString("msg");

				switch (msg)
				{
					case "state":

						HamState.PlaybackState state
									 = HamState.PlaybackState.parseState(object.getString("state"));
						String track = object.getString("track");
						int ctime    = object.getInt("ctime");
						int rtime    = object.getInt("rtime");
						boolean repeat  = object.getBoolean("repeat");
						boolean shuffle = object.getBoolean("shuffle");
						String[] tracks = fromJSONArray(object.getJSONArray("tracks"));

						messageListener.onMessage(
								new Message.StateMessage(
								new HamState(state,track,ctime,rtime,repeat,shuffle,tracks)
								)
						);
						break;

					//from here on just array are added as message content
					case "artists":
						messageListener.onMessage(
								new Message.ArtistsMessage(
										fromJSONArray(object.getJSONArray("artists"))
								)
						);
						break;
					case "albums":
						messageListener.onMessage(
								new Message.AlbumsMessage(
										fromJSONArray(object.getJSONArray("albums"))
								)
						);
						break;
					case "tracks":
					case "playlist": //TODO remove playlist completely from messages
						messageListener.onMessage(
								new Message.TracksMessage(
										fromJSONArray(object.getJSONArray("tracks"))
								)
						);
						break;
				}
				return msgs[0];
			}
			catch (JSONException ignored)
			{
				return m;
			}
		}
		else
		{
			return m;
		}
	}

	private String[] getJObject(String m)
	{
		int i = 0;
		int inObj = 0;
		int objStart = 0;
		while (i < m.length())
		{
			char c = m.charAt(i++);
			if (c == '{') //look out for start of an object, and mark position
			{
				++inObj;

				if(objStart == 0)
				{
					objStart = i-1;
				}
			}
			if (c == '}') //if we have ended our first object we inObj == 0, now we can extract it
			{
				if(--inObj == 0)
				{
					return new String[]{m.substring(i),m.substring(objStart,i)};
				}
			}
		}
		return new String[]{m};
	}

	private String[] fromJSONArray(JSONArray array)
	{
		String[] returnVal = new String[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			try
			{
				returnVal[i] = array.getString(i);
			}
			catch (JSONException ignored)
			{
				returnVal[i] = "";
			}
		}
		return returnVal;
	}
}
