package funkcionalisElemek;

/**
 * Egy útszakasz ({@link Ut}) fajtája. A normál szakaszokon a járművek a szokásos
 * módon ütközhetnek; a hídon és az alagútban viszont nem, mert a járművek egymás
 * felett (híd), illetve egymás alatt (alagút) haladnak el — két jármű nem tud
 * összecsúszni rajtuk.
 */
public enum SzakaszTipus {
    /** Sima útszakasz – normál ütközéskezeléssel. */
    NORMAL,
    /** Híd – a járművek egymás felett haladnak el, nincs ütközés. */
    HID,
    /** Alagút – a járművek egymás alatt haladnak el, nincs ütközés. */
    ALAGUT
}
