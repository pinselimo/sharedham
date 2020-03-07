package plakolb.sharedham.msg;

/**
 * Created by pinselimo on 07/04/16.
 */
public interface Message<T>
{
	MsgType getMessageType();
	T       getContents();

	enum MsgType
	{
		STATE("state"),
		ARTISTS("artists"),
		ALBUMS("albums"),
		TRACKS("tracks");

		public final String key;

		MsgType(String key)
		{
			this.key = key;
		}
	}

	abstract class HamMessage<E> implements Message<E>
	{
		private final MsgType type;
		private final E contents;

		public HamMessage(MsgType type,E contents)
		{
			this.type = type;
			this.contents = contents;
		}

		@Override
		public MsgType getMessageType()
		{
			return this.type;
		}

		@Override
		public E getContents()
		{
			return this.contents;
		}
	}


	class StateMessage extends HamMessage<HamState>
	{
		public StateMessage(HamState state)
		{
			super(MsgType.STATE,state);
		}
	}
	class ArtistsMessage extends HamMessage<String[]>
	{
		public ArtistsMessage(String[] artists)
		{
			super(MsgType.ARTISTS,artists);
		}
	}
	class AlbumsMessage extends HamMessage<String[]>
	{
		public AlbumsMessage(String[] albums)
		{
			super(MsgType.ALBUMS,albums);
		}
	}
	class TracksMessage extends HamMessage<String[]>
	{
		public TracksMessage(String[] tracks)
		{
			super(MsgType.TRACKS,tracks);
		}
	}
}