package cartoland.messages;

import cartoland.buttons.IButton;
import cartoland.utilities.IDs;
import cartoland.utilities.TimerHandle;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

/**
 * {@code ShowcaseMessage} is a listener that triggers when a user types anything in text channels (excluding thread
 * channels) in showcase category. This class is in an array in {@link cartoland.events.MessageEvent}.
 *
 * @since 2.0
 * @author Alex Cai
 */
public class ShowcaseMessage implements IMessage
{
	private final Button archiveButton = Button.success(IButton.ARCHIVE_THREAD, "Archive Thread")
			.withEmoji(Emoji.fromUnicode("📁"));
	private final Button deleteButton = Button.danger(IButton.DELETE_THREAD, "Delete Thread")
			.withEmoji(Emoji.fromUnicode("🗑️"));
	private final Button renameButton = Button.primary(IButton.RENAME_THREAD, "Edit Title")
			.withEmoji(Emoji.fromUnicode("✏️"));

	@Override
	public boolean messageCondition(MessageReceivedEvent event)
	{
		if (event.isFromThread()) //是在討論串內
			return false; //不應通過
		Category category = event.getMessage().getCategory(); //嘗試獲取類別
		return category != null && category.getIdLong() == IDs.SHOWCASE_CATEGORY_ID;
	}

	@Override
	public void messageProcess(MessageReceivedEvent event)
	{
		String name = event.getAuthor().getEffectiveName();
		event.getMessage().createThreadChannel(name + '(' + TimerHandle.getDateString() + ')').queue(threadChannel ->
			threadChannel.sendMessage("Thread automatically created by " + name + " in " + event.getChannel().getAsMention())
					.addActionRow(archiveButton, deleteButton, renameButton).queue(message -> message.pin().queue()));
	}
}