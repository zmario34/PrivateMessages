package tech.zmario.privatemessages.velocity.commands.subcommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import tech.zmario.privatemessages.velocity.PrivateMessagesVelocity;
import tech.zmario.privatemessages.velocity.commands.interfaces.SubCommand;
import tech.zmario.privatemessages.velocity.enums.MessagesConfiguration;
import tech.zmario.privatemessages.velocity.enums.SettingsConfiguration;

import java.util.List;

@RequiredArgsConstructor
public class ListSubCommand implements SubCommand {

    private final PrivateMessagesVelocity plugin;

    @Override
    public void execute(CommandSource sender, String[] args) {
        final Player player = (Player) sender;
        final List<String> ignoredLists = plugin.getStorage().getGamePlayers().get(player.getUniqueId()).getIgnoredPlayers();

        int page = 0;

        if (args.length == 2) {
            try {

                page = Integer.parseInt(args[1]) - 1;

                if (page < 0) {
                    player.sendMessage(MessagesConfiguration.IGNORE_LIST_NOT_A_NUMBER.getString());
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(MessagesConfiguration.IGNORE_LIST_NOT_A_NUMBER.getString());
                return;
            }
        }

        if (ignoredLists.isEmpty()) {
            player.sendMessage(MessagesConfiguration.IGNORE_LIST_EMPTY.getString());
            return;
        }

        int start = page * SettingsConfiguration.COMMAND_IGNORE_LIST_MAX_SIZE.getInt();
        int end = start + SettingsConfiguration.COMMAND_IGNORE_LIST_MAX_SIZE.getInt();

        if (end > ignoredLists.size()) {
            end = ignoredLists.size();
        }

        int maxPage = ignoredLists.size() / SettingsConfiguration.COMMAND_IGNORE_LIST_MAX_SIZE.getInt() + 1;

        if (page >= maxPage) {
            player.sendMessage(MessagesConfiguration.IGNORE_LIST_PAGE_NOT_EXIST.getString("%page%:" + (page + 1), "%max-page%:" + maxPage));
            return;
        }

        player.sendMessage(MessagesConfiguration.IGNORE_LIST_HEADER.getString("%page%:" + (page + 1), "%max-page%:" + maxPage));

        for (int i = start; i < end; i++) {
            player.sendMessage(MessagesConfiguration.IGNORE_LIST_LINE.getString("%index%:" + (i + 1), "%player%:" + ignoredLists.get(i)));
        }

        player.sendMessage(MessagesConfiguration.IGNORE_LIST_FOOTER.getString("%page%:" + (page + 1), "%max-page%:" + maxPage));
    }

    @Override
    public String getPermission() {
        return SettingsConfiguration.COMMAND_IGNORE_LIST_PERMISSION.getString();
    }
}
