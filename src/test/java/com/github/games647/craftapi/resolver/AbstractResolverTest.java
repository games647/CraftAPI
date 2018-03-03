package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.UUIDAdapter;
import com.github.games647.craftapi.model.skin.SkinModel;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.github.games647.craftapi.model.skin.SkinPropertyTest;
import com.github.games647.craftapi.model.skin.Texture;
import com.github.games647.craftapi.model.skin.Texture.TextureType;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class AbstractResolverTest {

    private static final String SLIM_SIGNATURE = "71345E00F68523B8A2915E4BD70F074140E539F896AE77B4519DC2867F87008233283ECA1FCDE68" +
            "084B3985401D24DE502F4A9631D43946165AD560AA8B5AC3CEAF4033F47026609F89C2AEB9E98462C1D62A980281" +
            "4BADEEA15D198A3B4264908DF0C7B13A6CFE49C98577BE7F5B06D8CF783AD21937E641E916518FC5571724CF3EF1" +
            "B9959777D44B8A135181056C5BF23AE7C11557A4120982DB12FB912AE4A403BA053F85E5B9898A0E35FC90B65F9C" +
            "1BDDE28EFC3A30C29620FC429CD7AD8F2348BFF672970614E6EA5EF385DDADC7F37CBD08E0DF27503BA1FB506FD0" +
            "D0B21EBC2A3CF244257B6B38D07598A6C51BC7A18B2F01BFDE5094BE13CBC823B12DF36381A1218E9C462005542B" +
            "2965BB0C5BA2CB8D0D752C19A32547ACD8E89A8DD60975B328780257A6DD4D218EE1430FB4163149EE1BFBAD79F9" +
            "B281DFC33BCCF25D4A852059AF305AE86E521E0D6FDC98A4130C359D1DD199E541BAC4AAE4BAFD730D46BABAAFD1" +
            "7D8797E43F15B7E5507F9DCC66E64CCD6E04E2C49ADEFCC422ACB3087A3BCE0C504FCA50125A64C3FCEE2191BA13" +
            "727D23BC49FE5F5322F9E811BF13C6558FB4B8DA882EAFEF153C6E3450CE11D5852A943F943E0A4489FF21591F11" +
            "7AC92A9B9F817281AD9F5D25BEE8D1960562A8B5FAE7A510923673766A7FC11A56FD7734B2449A12D0E6D826241E" +
            "A452962D435D3DF6D95E24100";
    private static final String STEVE_SIGNATURE = "2537D54AB77E8250C73B68253E6B0A75E4CE5CF807C3A9819EF15B640EF94C1ABBB10A083285A3C" +
            "4E96F1FBBCF90FC9390CB1BE55B7813A8EC53BDE18B51436146A81AFCE64C7CE1375B1C5E5B9A831D63B327BA8DB" +
            "031EB81CAFB4230E7FF27C105135543FAB6DC4B0BED8766F21E36861CE83A9DEC7008C1C7711C58C355B31701F31" +
            "3DF70B437509396F9C4C1B6CAF85C75E79160D67294FCF6286925D62FD78433008B9697FF5134DE8260E5A4EC25A" +
            "3DCC6D9FF3F624840084F20C8964E07E589C1FD7E56FDC1513B809CC00ACBB2B3B590D9135D75F5F05F54B6E51B2" +
            "9278803BADC10135935AA3FFA13405092926A4F18CCB39DA00F251B78FC87C44B8BB16007D70B1296178672B5D7A" +
            "70266D1A34D19560EFB97AC1DB2DC6C746699B58101E67B1D17A95E85158B595621517E671E4D8709F9066E85E38" +
            "661607022F315C57E6120BFFCFD0237618306554650FC9B4F171E46383B5280383BD9A7FB56CBBD30FB0BCFC6F24" +
            "469A8B44D2BDFD69BC71FDAC31B3423FA80F4A519AA359C8B99B20EFE7918A1D46D4B8A5270B4E685845787F8FBB" +
            "E0943022193F12ED8D3C19A291BC3F4140C024E93B480926BAB2313FA9A331807C9DE66EDE2ECEEAB9320D576236" +
            "B3A84CF9660F27D09C1D4CFD19928895CB854C0DE433F82DF56D498F7120CBC69258177B8A4021A56CBD5466C85C" +
            "9D01D7104521C1BDDBAA33CA0";
    
    private AbstractResolver resolver;

    @Before
    public void setUp() throws Exception {
        resolver = new AbstractResolver() {};
    }

    @Test
    public void decodeSkinSteve() throws Exception {
        SkinProperty property = new SkinProperty(SkinPropertyTest.STEVE_VALUE, SkinPropertyTest.STEVE_SIGNATURE);
        SkinModel skin = resolver.decodeSkin(property);

        assertThat(skin.getSignature(), is(hexStringToByteArray(STEVE_SIGNATURE)));
        
        assertThat(skin.getTimestamp(), is(Instant.ofEpochMilli(1517052435668L)));
        assertThat(skin.getOwnerId(), is(UUIDAdapter.parseId("0aaa2c13922a411bb6559b8c08404695")));
        assertThat(skin.getOwnerName(), is("games647"));

        Texture skinTexture = skin.getTexture(TextureType.SKIN).get();
        assertThat(skinTexture.getShortUrl(), is("a2e6a3f8caea7913ab48237beea6d6a1a6f76936e3b71af4c7a08bb61c7870"));
        assertThat(skinTexture.getMetadata().isPresent(), is(false));
    }

    @Test
    public void decodeSkinSlim() throws Exception {
        SkinProperty property = new SkinProperty(SkinPropertyTest.SLIM_VALUE, SkinPropertyTest.SLIM_SIGNATURE);
        SkinModel skin = resolver.decodeSkin(property);

        assertThat(skin.getSignature(), is(hexStringToByteArray(SLIM_SIGNATURE)));
        
        assertThat(skin.getTimestamp(), is(Instant.ofEpochMilli(1519552798232L)));
        assertThat(skin.getOwnerId(), is(UUIDAdapter.parseId("78c3a4e837e448189df8f9ce61c5efcc")));
        assertThat(skin.getOwnerName(), is("F0ggyMonst3r"));

        Texture skinTexture = skin.getTexture(TextureType.SKIN).get();
        assertThat(skinTexture.getShortUrl(), is("52847ba3eb656e7ac69f2af9cec58d4ec2f5a2ea7e18968c97907e87efa9cc4"));
        assertThat(skinTexture.getMetadata().get().getModel(), is("slim"));
    }

    @Test
    public void encodeSkinSteve() throws Exception {
        SkinModel skin = new SkinModel(Instant.ofEpochMilli(1517052435668L),
                UUIDAdapter.parseId("0aaa2c13922a411bb6559b8c08404695"),
                "games647", false,
                "a2e6a3f8caea7913ab48237beea6d6a1a6f76936e3b71af4c7a08bb61c7870", "");
        SkinProperty property = resolver.encodeSkin(skin, hexStringToByteArray(STEVE_SIGNATURE));

        assertThat(property.getValue(), is(SkinPropertyTest.STEVE_VALUE));
        assertThat(property.getSignature(), is(SkinPropertyTest.STEVE_SIGNATURE));
    }

    @Test
    public void encodeSkinSlim() throws Exception {
        SkinModel skin = new SkinModel(Instant.ofEpochMilli(1519552798232L),
                UUIDAdapter.parseId("78c3a4e837e448189df8f9ce61c5efcc"),
                "F0ggyMonst3r", true,
                "52847ba3eb656e7ac69f2af9cec58d4ec2f5a2ea7e18968c97907e87efa9cc4", "");
        SkinProperty property = resolver.encodeSkin(skin, hexStringToByteArray(SLIM_SIGNATURE));

        assertThat(property.getValue(), is(SkinPropertyTest.SLIM_VALUE));
        assertThat(property.getSignature(), is(SkinPropertyTest.SLIM_SIGNATURE));
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }

        return data;
    }
}
