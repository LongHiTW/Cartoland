package cartoland.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2.0
 * @author Alex Cai
 */
public class IntroduceHandle
{
	private IntroduceHandle()
	{
		throw new AssertionError(IDAndEntities.YOU_SHALL_NOT_ACCESS);
	}

	private static final String INTRODUCTION_FILE_NAME = "serialize/introduction.ser";

	@SuppressWarnings("unchecked")
	private static final Map<Long, String> introduction = (FileHandle.deserialize(INTRODUCTION_FILE_NAME) instanceof HashMap map) ? map : new HashMap<>();

	static
	{
		FileHandle.registerSerialize(INTRODUCTION_FILE_NAME, introduction);
	}

	public static void updateIntroduction(long userID, String content)
	{
		introduction.put(userID, content);
	}

	public static void deleteIntroduction(long userID)
	{
		introduction.remove(userID);
	}

	public static String getIntroduction(long userID)
	{
		return introduction.get(userID);
	}
}