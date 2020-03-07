package plakolb.sharedham.cmd;

import org.json.JSONException;
import org.json.JSONObject;

import plakolb.sharedham.TrackDiscoveryData;

/**
 * Created by pinselimo on 04/04/16.
 */
public abstract class Command
{
//region ABSTRACT CLASS
	private final String ARTIST = "artist";
	private final String ALBUM = "album";
	private final String TRACK ="track";
	private final String COMMAND = "cmd";
	private final String VALUE = "num";
	private final String TOGGLE = "bool";

	//TODO this is state and therefore bad
	private final HamCmd cmd;
	private JSONObject jsonObject = new JSONObject();

	protected Command(HamCmd cmd)
	{
		//Example:
		//String s = "{\"cmd\":1,\"track\":null,\"album\":\"From Nowhere (Single)\",\"artist\":\"Dan Croll\"}\"";
		this.cmd = cmd;
		try
		{
			jsonObject = jsonObject.put(COMMAND,cmd.cmdNum);

		}
		catch (JSONException ignored)
		{ }
	}

	public HamCmd getCmd()
	{
		return this.cmd;
	}

	public byte[] serialize() throws JSONException
	{
		return (jsonObject.toString()).getBytes();
	}

	private void processTData(TrackDiscoveryData data)
			throws JSONException
	{
		switch (cmd)
		{
			case PLAY:
			case ADD:
				jsonObject = jsonObject.put(TRACK,data.trackname);
			case GET_TRACKLIST:
				jsonObject = jsonObject.put(ALBUM,data.album);
			case GET_ALBUMS:
				jsonObject = jsonObject.put(ARTIST,data.artist);
				break;
			default:
				throw new IllegalArgumentException("Command does not match HamCmd");
		}
	}

	private void addNum(int secs) throws JSONException
	{
		jsonObject = jsonObject.put(VALUE,secs);
	}
	private void addBool(boolean b) throws JSONException
	{
		jsonObject = jsonObject.put(TOGGLE,b);
	}
	//endregion
//region CLASSES

	public static class RefreshLibCommand extends Command
	{
		public RefreshLibCommand()
		{
			super(HamCmd.REFRESH_LIB);
		}
	}
	public static class GetStateCommand extends Command
	{
		public GetStateCommand()
		{
			super(HamCmd.GET_STATE);
		}
	}
	public static class PauseCommand extends Command
	{
		public PauseCommand()
		{
			super(HamCmd.TOGGLE_PAUSE);
		}
	}
	public static class GetArtistsCommand extends Command
	{
		public GetArtistsCommand()
		{
			super(HamCmd.GET_ARTISTS);
		}
	}
	public static class StopCommand extends Command
	{
		public StopCommand()
		{
			super(HamCmd.STOP);
		}
	}
	public static class QuitCommand extends Command
	{
		public QuitCommand()
		{
			super(HamCmd.QUIT);
		}
	}
	public static class PlayCommand extends Command
	{
		public PlayCommand(TrackDiscoveryData data)
		{
			super(HamCmd.PLAY);
			try
			{
				super.processTData(data);
			}
			catch (JSONException ignored)
			{}
		}
	}
	public static class AddCommand extends Command
	{
		public AddCommand(TrackDiscoveryData data)
		{
			super(HamCmd.ADD);
			try
			{
				super.processTData(data);
			}
			catch (JSONException ignored)
			{}
		}
	}
	public static class GetTracksCommand extends Command
	{
		public GetTracksCommand(TrackDiscoveryData data)
		{
			super(HamCmd.GET_TRACKLIST);
			try
			{
				super.processTData(data);
			}
			catch (JSONException ignored)
			{}
		}
	}
	public static class GetAlbumsCommand extends Command
	{
		public GetAlbumsCommand(TrackDiscoveryData data)
		{
			super(HamCmd.GET_ALBUMS);
			try
			{
				super.processTData(data);
			}
			catch (JSONException ignored)
			{}
		}
	}
	public static class SetGainCommand extends Command
	{
		public SetGainCommand(byte gain)
		{
			super(HamCmd.GAIN);
			try
			{
				if (gain > 100)
				{
					super.addNum(100);
				}
				else if (gain < 0)
				{
					super.addNum(0);
				}
				else
				{
					super.addNum(gain);
				}
			}
			catch (JSONException ignored)
			{}
		}
	}
	public static class JumpCommand extends Command
	{
		public JumpCommand(int secs, boolean fwd)
		{
			super(HamCmd.JUMP);
			try
			{
				super.addNum(secs);
				super.addBool(fwd);
			}
			catch (JSONException ignored)
			{}
		}
	}
	public static class ToggleRepeatCommand extends Command
	{
		public ToggleRepeatCommand(boolean repeat)
		{
			super(HamCmd.TOGGLE_REPEAT);
			try
			{
				super.addBool(repeat);
			}
			catch (JSONException ignored)
			{}
		}
	}

	public static class ToggleShuffleCommand extends Command
	{
		public ToggleShuffleCommand(boolean shuffle)
		{
			super(HamCmd.TOGGLE_SHUFFLE);
			try
			{
				super.addBool(shuffle);
			}
			catch (JSONException ignored)
			{}
		}
	}
	//endregion
}
