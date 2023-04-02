package cartoland.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.awt.Color;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 2.0
 * @author Alex Cai
 */
public class QuestionForumHandle
{
	private static final Emoji resolved = Emoji.fromCustom("resolved", 1081082902785314921L, false);
	private static final String resolvedFormat = resolved.getFormatted();
	private static final Emoji reminder_ribbon = Emoji.fromUnicode("🎗️");
	private static final MessageEmbed startEmbed = new EmbedBuilder()
			.setTitle("**-=發問指南=-**", "https://discord.com/channels/886936474723950603/1079081061658673253/1079081061658673253")
			.setDescription("""
							-=發問指南=-
														
							• 請清楚說明你想做什麼，並想要什麼結果。
							• 請提及你正在使用的Minecraft版本，以及是否正在使用任何模組。
							• 討論完成後，使用 `:resolved:` %s 表情符號關閉貼文。
														
							-=Guidelines=-
							       
							• Ask your question straight and clearly, tell us what you are trying to do.
							• Mention which Minecraft version you are using and any mods.
							• Remember to use `:resolved:` %s to close the post after resolved.
							""".formatted(resolvedFormat, resolvedFormat))
			.setColor(new Color(133, 201, 103, 255)) //創聯的綠色
			.build();
	private static final List<Long> idledForumPosts;

	private QuestionForumHandle()
	{
		throw new AssertionError(IDAndEntities.YOU_SHALL_NOT_ACCESS);
	}

	static
	{
		Object list = FileHandle.deserializeObject(FileHandle.IDLED_FORUM_CHANNELS_LIST_FILE_NAME);

		//https://stackoverflow.com/questions/41778276/casting-from-object-to-arraylist
		if (list instanceof ArrayList)
			idledForumPosts = ((List<?>) list).stream().map(element -> (Long)element).collect(Collectors.toList());
		else
			idledForumPosts = new ArrayList<>();

	}

	public static void serializeIdlesList()
	{
		FileHandle.serializeObject(idledForumPosts);
	}

	public static void createForumPost(ThreadChannel forumPost)
	{
		forumPost.sendMessageEmbeds(startEmbed).queue(); //傳送發問指南

		List<ForumTag> tags = new ArrayList<>(forumPost.getAppliedTags());
		tags.remove(IDAndEntities.resolvedForumTag); //避免使用者自己加resolved
		if (tags.contains(IDAndEntities.unresolvedForumTag)) //如果使用者有加unresolved
		{
			forumPost.getManager().setAppliedTags(tags).queue(); //直接送出
			return;
		}

		if (tags.size() == 5) //不可以超過5個tag
			tags.remove(4); //移除最後一個 空出位置給unresolved
		tags.add(IDAndEntities.unresolvedForumTag);
		forumPost.getManager().setAppliedTags(tags).queue();
	}

	public static boolean typedResolved(Object withReaction)
	{
		if (withReaction instanceof Message message)
			return message.getContentRaw().equals(resolvedFormat);
		else if (withReaction instanceof MessageReaction reaction)
			return reaction.getEmoji().equals(resolved);
		else
			return false;
	}

	public static void archiveForumPost(ThreadChannel forumPost, Message eventMessage)
	{
		eventMessage.addReaction(resolved).queue(); //機器人會在訊息上加:resolved:
		List<ForumTag> tags = new ArrayList<>(forumPost.getAppliedTags());
		tags.remove(IDAndEntities.unresolvedForumTag); //移除unresolved
		tags.add(IDAndEntities.resolvedForumTag); //新增resolved
		idledForumPosts.remove(forumPost.getIdLong());

		//移除🎗️ 並關閉貼文
		forumPost.getIterableHistory().reverse().limit(1).queue(messages ->
		{
			if (messages.size() > 0)
			{
				Message message = messages.get(0);
				if (message.getReactions().stream().anyMatch(reaction -> reaction.getEmoji().equals(reminder_ribbon)))
					message.removeReaction(reminder_ribbon).queue();
			}

			forumPost.getManager().setAppliedTags(tags).setArchived(true).queue(); //關閉貼文

		});
	}

	public static void idleForumPost(ThreadChannel forumPost)
	{
		if (forumPost.isArchived() || forumPost.isLocked())
			return;

		forumPost.retrieveMessageById(forumPost.getLatestMessageIdLong()).queue(lastMessage ->
		{
			User author = lastMessage.getAuthor();
			if (author.isBot() || author.isSystem())
				return;

			if (Duration.between(lastMessage.getTimeCreated(), OffsetDateTime.now()).toHours() < 24L)
				return;

			String mentionOwner = "<@" + forumPost.getOwnerIdLong() + ">";
			forumPost.sendMessage(mentionOwner + "，你的問題解決了嗎？如果已經解決了，記得使用`:resolved:` "+ resolvedFormat +" 表情符號關閉貼文。\n" +
										  "如果還沒解決，可以嘗試在問題中加入更多資訊。\n" +
										  mentionOwner + ", did your question got a solution? If it did, remember to close this post using `:resolved:` "+ resolvedFormat +" emoji.\n" +
										  "If it didn't, try offer more information of question.").queue();

			idledForumPosts.add(forumPost.getIdLong());

			//增加🎗️
			forumPost.getIterableHistory().reverse().limit(1).queue(messages ->
			{
				if (messages.size() > 0)
					messages.get(0).addReaction(reminder_ribbon).queue();
			});
		});
	}
}