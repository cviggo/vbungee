package org.cviggo.managers;

import net.md_5.bungee.api.config.ServerInfo;
import org.cviggo.configlibrary.Config;
import org.cviggo.configs.ChatConfig;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrefixSuffixManager {

    public static ArrayList<Affix> affixes = new ArrayList<>();

    public static void loadPrefixes() {
        Config chat = ChatConfig.c;
        chat.getString( "Prefix.Groups.Default", "&5[Member]" );
        List<String> grouplist = chat.getSubNodes( "Prefix.Groups" );

        for ( String data : grouplist ) {
            affixes.add( new Affix( true, data, chat.getString( "Prefix.Groups." + data, null ) ) );
        }
    }

    public static void loadSuffixes() {
        Config chat = ChatConfig.c;
        chat.getString( "Suffix.Groups.Default", "&4" );
        List<String> grouplist = chat.getSubNodes( "Suffix.Groups" );
        for ( String data : grouplist ) {
            affixes.add( new Affix( false, data, chat.getString( "Suffix.Groups." + data, null ) ) );
        }
    }


    public static void sendPrefixAndSuffixToServer( ServerInfo s ) throws IOException {
        for ( Affix a : affixes ) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream( b );
            out.writeUTF( "PrefixesAndSuffixes" );
            out.writeBoolean( a.type );
            out.writeUTF( a.group );
            out.writeUTF( a.affix );
            ChatManager.sendPluginMessageTaskChat( s, b );
        }
    }

}
