package plakolb.sharedham.msg;

/**
 * Created by pinselimo on 07/04/16.
 */

public class HamState
{
	public final PlaybackState state;
	public final String track;
	public final int currentTime;
	public final int remainingTime;
	public final boolean repeating;
	public final boolean shuffling;
	public final String[] tracklist;

	public HamState(PlaybackState state, String track, int currentTime,
	                int remainingTime, boolean repeating, boolean shuffling,
	                String[] tracklist)
	{
		this.state = state;
		this.track = track;
		this.currentTime = currentTime;
		this.remainingTime = remainingTime;
		this.repeating = repeating;
		this.shuffling = shuffling;
		this.tracklist = tracklist;
	}

	public enum PlaybackState
	{
		PLAYING,
		STOPPED,
		PAUSED,
		ENDED;

	static public PlaybackState parseState(String state)
	{
		switch (state)
		{
			case "Playing":
				return PlaybackState.PLAYING;
			case "Paused":
				return PlaybackState.PAUSED;
			case "Stopped":
				return PlaybackState.STOPPED;
			default:
				return PlaybackState.ENDED;
		}
	}

	}
}