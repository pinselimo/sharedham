package plakolb.sharedham.cmd;

/**
 * Created by pinselimo on 04/04/16.
 */
enum HamCmd
{
	REFRESH_LIB   (0x0),
	PLAY          (0x1),
	STOP          (0x2),
	TOGGLE_PAUSE  (0x3),
	GET_ARTISTS   (0x4),
	GET_ALBUMS    (0x5),
	GET_TRACKLIST (0x6),
	UPLOAD        (0x7), //TODO implement upload functionality
	GAIN          (0x8),
	JUMP          (0x9),
	TOGGLE_REPEAT (0xA),
	TOGGLE_SHUFFLE(0xB),
	ADD           (0xC),
	GET_STATE     (0xD),
	QUIT          (0xE);//not implemented

	public final int cmdNum;

	HamCmd(int cmdNum)
	{
		this.cmdNum = cmdNum;
	}
}
