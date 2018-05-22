package classes;

import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public final class FontAwesome {
    private static final GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    public static Glyph get(String name, double size){
        Glyph rGlyph = fontAwesome.create(name);
        rGlyph.setFontSize(size);
        return rGlyph;
    }
}
