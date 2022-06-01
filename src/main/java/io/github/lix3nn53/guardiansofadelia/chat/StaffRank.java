package io.github.lix3nn53.guardiansofadelia.chat;


import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterChatTag;

public enum StaffRank {
    OWNER,
    ADMIN,
    DEVELOPER,
    BUILDER,
    SUPPORT,
    YOUTUBER,
    TRAINEE;

    public ChatPalette getChatPalette() {
        switch (this) {
            case OWNER:
                return ChatPalette.RED_DARK;
            case ADMIN:
                return ChatPalette.PURPLE_LIGHT;
            case DEVELOPER:
                return ChatPalette.GOLD;
            case BUILDER:
                return ChatPalette.BLUE;
            case SUPPORT:
                return ChatPalette.BLUE_LIGHT;
            case YOUTUBER:
                return ChatPalette.RED;
            case TRAINEE:
                return ChatPalette.GREEN_DARK;
        }
        return ChatPalette.GRAY;
    }

    public CustomCharacter getCustomCharacter() {
        switch (this) {
            case OWNER:
                return CustomCharacterChatTag.ADMIN;
            case ADMIN:
                return CustomCharacterChatTag.ADMIN;
            case DEVELOPER:
                return CustomCharacterChatTag.ADMIN;
            case BUILDER:
                return CustomCharacterChatTag.ADMIN;
            case SUPPORT:
                return CustomCharacterChatTag.ADMIN;
            case YOUTUBER:
                return CustomCharacterChatTag.ADMIN;
            case TRAINEE:
                return CustomCharacterChatTag.ADMIN;
        }

        return null;
    }

    public boolean canBuild() {
        switch (this) {
            case OWNER, BUILDER, ADMIN -> {
                return true;
            }
        }

        return false;
    }
}
