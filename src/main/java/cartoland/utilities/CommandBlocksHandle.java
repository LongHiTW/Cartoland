package cartoland.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@code CommandBlocksHandle} is a utility class that handles command blocks of users. Command blocks is a
 * feature that whatever a user say in some specific channels, the user will gain command blocks as a kind of
 * reward point. This class open a static field {@link #commandBlocksMap} for {@link JsonHandle}, which is where all the
 * command blocks are stored, hence this class can be seen as an extension of {@code JsonHandle}. Can not be
 * instantiated.
 *
 * @since 1.5
 * @see JsonHandle
 * @author Alex Cai
 */
public class CommandBlocksHandle
{
	private CommandBlocksHandle()
	{
		throw new AssertionError(IDAndEntities.YOU_SHALL_NOT_ACCESS);
	}

	public static boolean changed = true;
	private static final String COMMAND_BLOCKS_FILE_NAME = "command_blocks.ser";
	private static final Map<Long, Long> commandBlocksMap;

	static
	{
		//會有unchecked assignment的警告 but I did it anyway
		commandBlocksMap = (FileHandle.deserialize(COMMAND_BLOCKS_FILE_NAME) instanceof Map map) ? map : new HashMap<>();
	}

	/**
	 * Add command blocks to the user that has userID as ID. This method calls
	 * {@link Algorithm#safeAdd(long, long)} in order to add without overflow.
	 *
	 * @param userID The ID of the user that needs to add command blocks.
	 * @param add The amount of command blocks that are going to add on this user.
	 */
	public static void add(long userID, long add)
	{
		changed = true;
		Long nowHave = commandBlocksMap.get(userID);
		if (nowHave == null)
			nowHave = 0L;
		commandBlocksMap.put(userID, Algorithm.safeAdd(nowHave, add));
	}

	public static void set(long userID, long value)
	{
		changed = true;
		commandBlocksMap.put(userID, value);
	}

	public static long get(long userID)
	{
		return commandBlocksMap.computeIfAbsent(userID, nowHave -> 0L);
	}

	public static int size()
	{
		return commandBlocksMap.size();
	}

	public static Set<Long> getKeySet()
	{
		return commandBlocksMap.keySet();
	}

	public static void serializeCommandBlocksMap()
	{
		FileHandle.serialize(COMMAND_BLOCKS_FILE_NAME, commandBlocksMap);
	}
}